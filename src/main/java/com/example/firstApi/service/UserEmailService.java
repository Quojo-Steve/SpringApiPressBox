package com.example.firstApi.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service("userEmailService")
public class UserEmailService {

    private final JdbcTemplate jdbcTemplate;

    public UserEmailService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Map<String, String> getEmailAndSourceByUserId(String userId) {
        return jdbcTemplate.queryForObject(
            "SELECT email, source_id FROM users WHERE user_id = ?",
            (rs, rowNum) -> {
                Map<String, String> result = new HashMap<>();
                result.put("email", rs.getString("email"));
                result.put("sourceId", rs.getString("source_id"));
                return result;
            },
            userId
        );
    }
}
