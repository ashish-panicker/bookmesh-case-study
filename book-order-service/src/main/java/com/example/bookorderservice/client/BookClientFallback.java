package com.example.bookorderservice.client;

import com.example.bookorderservice.dto.BookDto;
import com.example.bookorderservice.dto.StandardResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class BookClientFallback implements BookClient {

    @Override
    public ResponseEntity<StandardResponse<BookDto>> getBookById(Long id) {
        // Fallback response when book-service is down or fails
        BookDto fallbackBook = new BookDto(id, "Unknown Title (Fallback)", "Unknown Author", 0, 0.0);
        StandardResponse<BookDto> response = new StandardResponse<>(
                "Fallback triggered: Book service is unavailable",
                HttpStatus.SERVICE_UNAVAILABLE,
                fallbackBook
        );
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }

    @Override
    public ResponseEntity<StandardResponse<Boolean>> checkBulkStock(java.util.List<com.example.bookorderservice.dto.StockCheckRequest> requests) {
        StandardResponse<Boolean> response = new StandardResponse<>(
                "Fallback triggered: Book service is unavailable",
                HttpStatus.SERVICE_UNAVAILABLE,
                false
        );
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }

    @Override
    public ResponseEntity<StandardResponse<Boolean>> deductStock(Long id, int quantity) {
        StandardResponse<Boolean> response = new StandardResponse<>(
                "Fallback triggered: Book service is unavailable",
                HttpStatus.SERVICE_UNAVAILABLE,
                false
        );
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }
}
