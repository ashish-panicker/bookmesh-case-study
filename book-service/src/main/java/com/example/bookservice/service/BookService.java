package com.example.bookservice.service;

import com.example.bookservice.dto.BookDto;
import com.example.bookservice.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface BookService {

    Book create(Book book);

    Optional<Book> findById(Long id);

    /**
     * @deprecated Use {@link #findAll(Pageable)} instead for better performance and pagination support.
     */
    @Deprecated
    List<Book> findAll();

    Page<Book> findAll(Pageable pageable);

    Book mapToEntity(BookDto dto);

    BookDto mapToDto(Book book);

    Optional<Book> findByAuthorAndTitle(String author, String title);

    boolean checkIfStockExists(Long bookId, int stock);


    boolean deductStock(Long id, int quantity);
    
    boolean checkBulkStockExists(List<com.example.bookservice.dto.StockCheckRequest> requests);
}
