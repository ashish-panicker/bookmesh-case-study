package com.example.bookorderservice.service;

import com.example.bookorderservice.dto.BookOrderRequest;
import com.example.bookorderservice.dto.BookOrderResponse;

public interface BookOrderService {
    BookOrderResponse createOrder(BookOrderRequest request);
    BookOrderResponse getOrderById(Long id);
}
