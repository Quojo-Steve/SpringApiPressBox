package com.example.firstApi.dto;

import lombok.Data;

@Data
public class CashPaymentRequestDTO {
    private double amount;
    private String cycle;
    private String packageId;
    private String plan;
}
