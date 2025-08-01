package com.example.firstApi.service;

import com.example.firstApi.dto.PaystackRequestDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import okhttp3.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaystackService {

    @Value("${paystack.secret.key}")
    private String secretKey;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final OkHttpClient client = new OkHttpClient();
    private final RestTemplate restTemplate = new RestTemplate();

    private static final String PAYSTACK_INITIALIZE_URL = "https://api.paystack.co/transaction/initialize";
    private static final String PAYSTACK_VERIFY_URL = "https://api.paystack.co/transaction/verify/";

    public Map<String, Object> initializePayment(PaystackRequestDTO request, String email, String sourceId) throws IOException {
        String reference = UUID.randomUUID().toString();
        int amountInKobo = request.getAmount() * 100;

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("plan", request.getPlan());
        metadata.put("cycle", request.getCycle());
        metadata.put("user_id", request.getUserId());
        metadata.put("source_id", sourceId);
        metadata.put("package_id", request.getPackageId());

        Map<String, Object> payload = new HashMap<>();
        payload.put("email", email);
        payload.put("amount", amountInKobo);
        payload.put("reference", reference);
        payload.put("callback_url", request.getCallbackUrl());
        payload.put("metadata", metadata);

        // Use OkHttp's media type
        okhttp3.MediaType JSON = okhttp3.MediaType.get("application/json; charset=utf-8");

        RequestBody body = RequestBody.create(objectMapper.writeValueAsString(payload), JSON);

        Request httpRequest = new Request.Builder()
                .url(PAYSTACK_INITIALIZE_URL)
                .post(body)
                .addHeader("Authorization", "Bearer " + secretKey)
                .addHeader("Content-Type", "application/json")
                .build();

        try (Response response = client.newCall(httpRequest).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected response code " + response.code());
            }

            return objectMapper.readValue(response.body().string(), Map.class);
        }
    }

    public JsonNode verifyPayment(String reference) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(secretKey.replace("Bearer ", "")); // only token
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                PAYSTACK_VERIFY_URL + reference,
                HttpMethod.GET,
                entity,
                String.class
        );

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new Exception("Paystack verification failed with status: " + response.getStatusCode());
        }

        return objectMapper.readTree(response.getBody());
    }
}
