package com.example.bookservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BookDto(
        Long id,

        @NotBlank
        String title, //title()

        @NotBlank
        String author, //author()

        @NotNull @Min(1)
        int stock,

        @JsonProperty("unit_price")
        double unitPrice
        ) {
}
