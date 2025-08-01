package com.example.firstApi.dto;

import lombok.Data;

@Data
public class PaystackRequestDTO {
    private int amount;
    private String plan;
    private String cycle;
    private String userId;
    private String packageId;
    private String callbackUrl;
}
