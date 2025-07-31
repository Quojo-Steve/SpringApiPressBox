package com.example.firstApi.dto;

public class LoginResponse {
    public boolean status;
    public Object data;
    public String message;
    public String type;

    public LoginResponse(boolean status, Object data, String message, String type) {
        this.status = status;
        this.data = data;
        this.message = message;
        this.type = type;
    }
}
