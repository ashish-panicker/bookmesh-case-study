package com.example.bookservice.dto;

import org.springframework.http.HttpStatus;

public record StandardResponse(String message, HttpStatus status, Object data) {
}
