package com.example.bookorderservice.client;

import com.example.bookorderservice.dto.BookDto;
import com.example.bookorderservice.dto.StandardResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import com.example.bookorderservice.dto.StockCheckRequest;

@FeignClient(name = "book-service", url = "${book-service.url:http://localhost:8081}", fallback = BookClientFallback.class)
public interface BookClient {

    @GetMapping("/books/{id}")
    ResponseEntity<StandardResponse<BookDto>> getBookById(@PathVariable("id") Long id);

    @PostMapping("/books/stock/bulk-check")
    ResponseEntity<StandardResponse<Boolean>> checkBulkStock(@RequestBody List<StockCheckRequest> requests);

    @PutMapping("/books/{id}/deduct")
    ResponseEntity<StandardResponse<Boolean>> deductStock(@PathVariable("id") Long id, @RequestParam("quantity") int quantity);
}
