package com.example.firstApi.controllers;

import com.example.firstApi.dto.LoginRequest;
import com.example.firstApi.dto.LoginResponse;
import com.example.firstApi.dto.RegisterRequest;
import com.example.firstApi.model.User;
import com.example.firstApi.model.Source;
import com.example.firstApi.model.SourceSocial;
import com.example.firstApi.repository.SourceSocialRepository;
import com.example.firstApi.repository.UserRepository;
import com.example.firstApi.repository.SourceRepository;
import com.example.firstApi.util.ImageUtil;
import com.example.firstApi.util.JwtUtil;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final SourceSocialRepository sourceSocialRepository;

    private final UserRepository userRepository;
    private final SourceRepository sourceRepository;

    public AuthController(UserRepository userRepository, SourceRepository sourceRepository, SourceSocialRepository sourceSocialRepository) {
        this.userRepository = userRepository;
        this.sourceRepository = sourceRepository;
        this.sourceSocialRepository = sourceSocialRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
        Map<String, Object> organisation = req.organisation;
        Map<String, Object> contact = req.contact;
        String logo = req.logo;

        try {
            // Extract organisation fields
            String name = (String) organisation.get("name");
            String industry = (String) organisation.get("industry");
            String country = (String) organisation.get("country");
            String region = (String) organisation.get("region");
            String email = (String) organisation.get("email");
            String telephone = (String) organisation.get("telephone");
            String website = (String) organisation.get("website");
            String language = (String) organisation.get("language");
            String description = (String) organisation.get("description");
            String orgIso = (String) organisation.get("iso_code");
            String orgDial = (String) organisation.get("dial_code");

            // Extract contact fields
            String firstName = (String) contact.get("first_name");
            String lastName = (String) contact.get("last_name");
            String userEmail = (String) contact.get("email");
            String userPhone = (String) contact.get("telephone");
            String userIso = (String) contact.get("iso_code");
            String userDial = (String) contact.get("dial_code");
            String password = (String) contact.get("password");

            // Generate IDs
            String userId = UUID.randomUUID().toString();
            String sourceId = UUID.randomUUID().toString();

            // Hash password
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

            // Save user
            User user = new User();
            user.setUserId(userId);
            user.setSourceId(sourceId);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEmail(userEmail);
            user.setTelephone(userPhone);
            user.setIsoCode(userIso);
            user.setDialCode(userDial);
            user.setPassword(hashedPassword);
            user.setStatus(User.Status.enabled);
            user.setRole(User.Role.user);

            userRepository.save(user);

            // Save logo
            String logoUrl = null;
            if (logo != null && !logo.isEmpty()) {
                logoUrl = ImageUtil.saveImageToFile(logo, "logos");
            }

            // Save organisation (source)
            Source source = new Source();
            source.setSourceId(sourceId);
            source.setName(name);
            source.setIndustry(industry);
            source.setCountry(country);
            source.setRegion(region);
            source.setEmail(email);
            source.setTelephone(telephone);
            source.setWebsite(website);
            source.setLanguage(language);
            source.setDescription(description);
            source.setIsoCode(orgIso);
            source.setDialCode(orgDial);
            source.setLogo(logoUrl);
            source.setStatus(Source.Status.unverified);

            sourceRepository.save(source);

            // Save to source_socials table manually (use JdbcTemplate or native query if
            // needed)
            SourceSocial social = new SourceSocial();
            social.setSourceId(sourceId);
            sourceSocialRepository.save(social);

            // Create JWT
            Map<String, Object> claims = Map.of("user_id", userId, "source_id", sourceId);
            String token = JwtUtil.generateToken(claims, 86400000); // 1 day

            // Response
            Map<String, Object> data = Map.of(
                    "user_id", userId,
                    "source_id", sourceId,
                    "user", contact,
                    "source", organisation,
                    "token", token);

            return ResponseEntity.ok(Map.of(
                    "status", true,
                    "message", "Account created successfully",
                    "data", data,
                    "type", "auth.source.create.success"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                    "status", false,
                    "message", e.getMessage(),
                    "type", "auth.source.create.failed"));
        }
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest req) {
        var userOpt = userRepository.findByEmail(req.email);
        if (userOpt.isEmpty()) {
            return new LoginResponse(false, null, "Email or password is incorrect", "auth.user.email.notfound");
        }

        User user = userOpt.get();

        if ("disabled".equals(user.getStatus())) {
            return new LoginResponse(false, null, "This account has been deactivated", "auth.admin.login.failed");
        }

        boolean verified = BCrypt.checkpw(req.password, user.getPassword());
        if (!verified) {
            return new LoginResponse(false, null, "The email or password is incorrect", "auth.user.login.failed");
        }

        Source source = sourceRepository.findBySourceId(user.getSourceId()).orElse(null);
        if (source == null) {
            return new LoginResponse(false, null, "Source not found", "auth.user.source.notfound");
        }

        Map<String, Object> claims = Map.of(
                "user_id", user.getUserId(),
                "source_id", user.getSourceId());
        long expiration = req.rememberMe ? 7 * 24 * 60 * 60 * 1000L : 24 * 60 * 60 * 1000L;
        String token = JwtUtil.generateToken(claims, expiration);

        // Clean sensitive data
        user.setPassword(null);

        Map<String, Object> data = new HashMap<>();
        data.put("user_id", user.getUserId());
        data.put("source_id", user.getSourceId());
        data.put("user", user);
        data.put("source", source);
        data.put("token", token);

        return new LoginResponse(true, data, "User logged in successfully", "auth.user.login.success");
    }
}
