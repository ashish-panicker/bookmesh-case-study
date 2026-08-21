package com.example.bookorderservice.service;

import com.example.bookorderservice.dto.BookOrderRequest;
import com.example.bookorderservice.dto.BookOrderResponse;
import com.example.bookorderservice.dto.OrderItemResponse;
import com.example.bookorderservice.exception.ResourceNotFoundException;
import com.example.bookorderservice.model.BookOrder;
import com.example.bookorderservice.model.OrderItem;
import com.example.bookorderservice.repository.BookOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.example.bookorderservice.client.BookClient;
import com.example.bookorderservice.dto.BookDto;
import com.example.bookorderservice.dto.StandardResponse;
import com.example.bookorderservice.dto.StockCheckRequest;
import org.springframework.http.ResponseEntity;

@Service
@RequiredArgsConstructor
public class BookOrderServiceImpl implements BookOrderService {

    private final BookOrderRepository bookOrderRepository;
    private final BookClient bookClient;

    @Override
    @Transactional
    public BookOrderResponse createOrder(BookOrderRequest request) {
        
        // 1. Bulk Stock Check
        List<StockCheckRequest> stockChecks = request.getItems().stream()
                .map(item -> new StockCheckRequest(item.getBookId(), item.getQuantity()))
                .collect(Collectors.toList());
        
        ResponseEntity<StandardResponse<Boolean>> stockResponse = bookClient.checkBulkStock(stockChecks);
        if (stockResponse == null || stockResponse.getBody() == null || Boolean.FALSE.equals(stockResponse.getBody().data())) {
            throw new RuntimeException("Stock is not available for one or more items or book-service is unavailable.");
        }

        // 2. Create Order
        BookOrder order = BookOrder.builder()
                .orderDate(LocalDateTime.now())
                .status("CREATED")
                .build();

        List<OrderItem> items = request.getItems().stream().map(itemRequest -> {
            ResponseEntity<StandardResponse<BookDto>> response = bookClient.getBookById(itemRequest.getBookId());
            double actualPrice = 0.0;
            
            if (response != null && response.getBody() != null && response.getBody().data() != null) {
                actualPrice = response.getBody().data().unitPrice();
            } else {
                throw new ResourceNotFoundException("Book not found or service unavailable for id: " + itemRequest.getBookId());
            }

            return OrderItem.builder()
                    .bookOrder(order)
                    .bookId(itemRequest.getBookId())
                    .quantity(itemRequest.getQuantity())
                    .price(actualPrice)
                    .build();
        }).collect(Collectors.toList());

        order.setItems(items);
        
        // Calculate total amount
        double totalAmount = items.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
        order.setTotalAmount(totalAmount);

        BookOrder savedOrder = bookOrderRepository.save(order);

        // 3. Deduct Stock for each item
        for (OrderItem item : savedOrder.getItems()) {
            ResponseEntity<StandardResponse<Boolean>> deductResponse = bookClient.deductStock(item.getBookId(), item.getQuantity());
            if (deductResponse == null || deductResponse.getBody() == null || Boolean.FALSE.equals(deductResponse.getBody().data())) {
                throw new RuntimeException("Failed to deduct stock for book id: " + item.getBookId());
            }
        }

        return mapToResponse(savedOrder);
    }

    @Override
    public BookOrderResponse getOrderById(Long id) {
        BookOrder order = bookOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
        return mapToResponse(order);
    }

    private BookOrderResponse mapToResponse(BookOrder order) {
        List<OrderItemResponse> itemResponses = order.getItems().stream()
                .map(item -> OrderItemResponse.builder()
                        .id(item.getId())
                        .bookId(item.getBookId())
                        .quantity(item.getQuantity())
                        .price(item.getPrice())
                        .build())
                .collect(Collectors.toList());

        return BookOrderResponse.builder()
                .orderId(order.getOrderId())
                .items(itemResponses)
                .totalAmount(order.getTotalAmount())
                .orderDate(order.getOrderDate())
                .status(order.getStatus())
                .build();
    }
}
