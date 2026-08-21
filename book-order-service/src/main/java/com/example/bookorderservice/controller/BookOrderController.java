package com.example.bookorderservice.controller;

import com.example.bookorderservice.dto.BookOrderRequest;
import com.example.bookorderservice.dto.BookOrderResponse;
import com.example.bookorderservice.service.BookOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class BookOrderController {

    private final BookOrderService bookOrderService;

    @PostMapping
    public ResponseEntity<BookOrderResponse> createOrder(@Valid @RequestBody BookOrderRequest request) {
        BookOrderResponse response = bookOrderService.createOrder(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookOrderResponse> getOrderById(@PathVariable Long id) {
        BookOrderResponse response = bookOrderService.getOrderById(id);
        return ResponseEntity.ok(response);
    }
}
