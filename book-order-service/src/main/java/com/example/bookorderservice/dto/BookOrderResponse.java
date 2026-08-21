package com.example.bookorderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookOrderResponse {

    private Long orderId;
    private List<OrderItemResponse> items;
    private Double totalAmount;
    private LocalDateTime orderDate;
    private String status;

}
