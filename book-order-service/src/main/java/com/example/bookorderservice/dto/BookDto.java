package com.example.bookorderservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record BookDto(
        Long id,
        String title,
        String author,
        int stock,
        @JsonProperty("unit_price") double unitPrice
) {}
