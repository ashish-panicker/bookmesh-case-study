package com.example.bookorderservice.dto;

public record ApiErrorResponse(
        boolean success,
        String timestamp,
        int status,
        String error,
        String message,
        String path
) {
    public static ApiErrorResponse of(int status, String error, String message, String path) {
        return new ApiErrorResponse(
                false,
                java.time.OffsetDateTime.now().toString(),
                status,
                error,
                message,
                path
        );
    }
}
