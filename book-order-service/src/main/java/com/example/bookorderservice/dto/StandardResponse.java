package com.example.bookorderservice.dto;

public record StandardResponse<T>(
        String message,
        Object status,
        T data
) {}
