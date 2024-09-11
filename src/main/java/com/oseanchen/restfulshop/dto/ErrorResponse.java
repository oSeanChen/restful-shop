package com.oseanchen.restfulshop.dto;

import lombok.Data;

import java.util.List;

@Data
public class ErrorResponse {
    private String error;
    private List<String> message;

    public ErrorResponse(String error, List<String> message) {
        this.error = error;
        this.message = message;
    }
}
