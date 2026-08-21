package com.example.bookservice.controller;

import com.example.bookservice.dto.BookDto;
import com.example.bookservice.dto.StandardResponse;
import com.example.bookservice.exceptions.ResourceExistsException;
import com.example.bookservice.exceptions.ResourceNotFoundException;
import com.example.bookservice.service.BookService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
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

    /**
     * Retrieves a paginated list of books.
     * <p>
     * You can use the following query parameters to fetch and sort the data:
     * <ul>
     *   <li><b>page</b>: The page number to fetch (0-indexed, default is usually 0). Example: {@code ?page=0}</li>
     *   <li><b>size</b>: The number of records per page (default is usually 20). Example: {@code ?size=10}</li>
     *   <li><b>sort</b>: The field to sort by and the direction (asc/desc).
     *       You can use multiple sort parameters. Example: {@code ?sort=title,asc&sort=author,desc}</li>
     * </ul>
     * <br>
     * <b>Sample URLs to test:</b>
     * <ul>
     *   <li>{@code http://localhost:8081/books} (Default fetch)</li>
     *   <li>{@code http://localhost:8081/books?page=1&size=5} (Fetch 2nd page with 5 records per page)</li>
     *   <li>{@code http://localhost:8081/books?sort=title,asc} (Sort all books by title ascending)</li>
     *   <li>{@code http://localhost:8081/books?page=0&size=10&sort=unitPrice,desc} (Fetch first 10 books
     *      sorted by price descending)</li>
     * </ul>
     * <br>
     * Note: While Pageable handles pagination and sorting, field-based filtering (e.g., searching by author)
     * is not natively supported by Pageable alone and would require additional request parameters.
     * </p>
     *
     * @param pageable the pagination and sorting information injected by Spring.
     * @return a {@link ResponseEntity} containing a {@link StandardResponse} with the paginated books.
     */
    @GetMapping
    public ResponseEntity<StandardResponse> fetchAllBooks(Pageable pageable) {
        log.info("Received request to fetch all books");
        var books = service.findAll(pageable);
//        if (books.isEmpty()) {
//            log.debug("No books found in the database");
//            var response = new StandardResponse("No Books", HttpStatus.NO_CONTENT, "");
//            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
//        }
        var response = new StandardResponse("Books fetched", HttpStatus.OK,
                books.isEmpty() ? "No Books Found" : books);
        return ResponseEntity.status(HttpStatus.OK).body(response);
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
        var response = new StandardResponse("Book details", HttpStatus.OK, service.mapToDto(optionalBook));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @GetMapping("/{id}/stock")
    public ResponseEntity<StandardResponse> checkStock(@PathVariable Long id, @RequestParam int quantity) {
        log.info("Checking stock for book id: {} and quantity: {}", id, quantity);
        boolean exists = service.checkIfStockExists(id, quantity);
        var response = new StandardResponse(
                exists ? "Stock available" : "Stock not available",
                HttpStatus.OK,
                exists
        );
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/deduct")
    public ResponseEntity<StandardResponse> deductStock(@PathVariable Long id, @RequestParam int quantity) {
        log.info("Deducting stock for book id: {} and quantity: {}", id, quantity);
        // TODO: Create a deductStock method
        boolean exists = service.deductStock(id, quantity);
        var response = new StandardResponse(
                exists ? "Stock available" : "Stock not available",
                HttpStatus.OK,
                exists
        );
        return ResponseEntity.ok(response);
    }
}
