package com.example.firstApi.controllers;

import com.example.firstApi.dto.CashPaymentRequestDTO;
import com.example.firstApi.dto.PaystackRequestDTO;
import com.example.firstApi.model.Subscription;
import com.example.firstApi.repository.SubscriptionRepository;
import com.example.firstApi.service.CashPaymentService;
import com.example.firstApi.service.PaystackService;
import com.example.firstApi.service.UserEmailService;
import com.fasterxml.jackson.databind.JsonNode;

import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaystackService paystackService;
    private final SubscriptionRepository subscriptionRepository;
    private final CashPaymentService cashPaymentService;

    @Resource(name = "userEmailService") // you’ll need a service to fetch user email & source_id
    private UserEmailService userEmailService;

    @PostMapping("/checkout/admin")
    public ResponseEntity<?> checkoutAdmin(@RequestBody PaystackRequestDTO request) {
        try {
            Map<String, String> user = userEmailService.getEmailAndSourceByUserId(request.getUserId());
            String email = user.get("email");
            String sourceId = user.get("sourceId");

            Map<String, Object> result = paystackService.initializePayment(request, email, sourceId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                    "status", false,
                    "message", "Payment initialization failed",
                    "error", e.getMessage()));
        }
    }

    @PostMapping("/cashpayment/{userId}")
    public ResponseEntity<?> cashPayment(@PathVariable String userId, @RequestBody CashPaymentRequestDTO request) {
        try {
            cashPaymentService.createCashPayment(
                    userId,
                    request.getAmount(),
                    request.getCycle(),
                    request.getPackageId(),
                    request.getPlan());
            return ResponseEntity.ok().body(
                    new ApiResponse(true, "Payment and subscriber created successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                    new ApiResponse(false, e.getMessage()));
        }
    }

    record ApiResponse(boolean status, String message) {
    }

    @PutMapping("/verify/{reference}")
    public ResponseEntity<?> verifyPayment(@PathVariable String reference) {
        try {
            JsonNode result = paystackService.verifyPayment(reference);

            boolean status = result.get("status").asBoolean();
            JsonNode data = result.get("data");

            if (!status || data == null || !data.get("status").asText().equals("success")) {
                return ResponseEntity.badRequest().body(Map.of(
                        "status", false,
                        "message", "Payment verification failed",
                        "data", data
                ));
            }

            // Mark subscription as verified
            Optional<Subscription> subscriptionOpt = subscriptionRepository.findBySubscriptionId(reference);
            if (subscriptionOpt.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                        "status", false,
                        "message", "No subscription found for reference"
                ));
            }

            Subscription subscription = subscriptionOpt.get();
            subscription.setVerified("True");
            subscriptionRepository.save(subscription);

            return ResponseEntity.ok(Map.of(
                    "status", true,
                    "type", "payment.create.success",
                    "message", "Payment verified successfully"
            ));

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", false,
                    "type", "payment.verify.error",
                    "message", e.getMessage()
            ));
        }
    }
}
