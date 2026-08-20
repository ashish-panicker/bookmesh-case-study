package com.example.bookservice.controller;

import com.example.bookservice.dto.BookDto;
import com.example.bookservice.dto.StandardResponse;
import com.example.bookservice.exceptions.ResourceExistsException;
import com.example.bookservice.exceptions.ResourceNotFoundException;
import com.example.bookservice.service.BookService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService service;

    public BookController(BookService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<StandardResponse> createBook(@Valid @RequestBody BookDto request) {
        log.info("Received request to create book with title: {} and author: {}", request.title(), request.author());
        var bookExists = service.findByAuthorAndTitle(request.author().trim(), request.title().trim());
        if(bookExists.isPresent()){
         log.error("Book already exists: title={}, author={}", request.title(), request.author());
         throw new ResourceExistsException("""
                 Book with title: %s and author: %s is already present."""
                 .formatted(request.title(), request.author()));
        }
        var book = service.mapToEntity(request);
        var created = service.create(book);
        var response = new StandardResponse("Created Book", HttpStatus.CREATED, service.mapToDto(created));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<StandardResponse> fetchAllBooks() {
        log.info("Received request to fetch all books");
        var books = service.findAll();
        if (books.isEmpty()) {
            log.debug("No books found in the database");
            var response = new StandardResponse("No Books", HttpStatus.NO_CONTENT, "");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
        }
        var response = new StandardResponse("No Books", HttpStatus.NO_CONTENT, books);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StandardResponse> fetchAllBookById(@PathVariable Long id) {
        log.info("Received request to fetch book by id: {}", id);
        var optionalBook = service.findById(id).orElseThrow(() -> {
            log.error("Book not found with id: {}", id);
            return new ResourceNotFoundException("Cannot find with Id: " + id);
        });
//        if (optionalBook.isEmpty()) {
//            var response = new StandardResponse("No Book found", HttpStatus.NO_CONTENT, "");
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
//        }
        var response = new StandardResponse("No Books", HttpStatus.OK, service.mapToDto(optionalBook));
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }
}
