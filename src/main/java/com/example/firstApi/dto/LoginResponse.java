package com.example.firstApi.dto;

import com.example.firstApi.model.User;
import com.example.firstApi.model.Source;

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
