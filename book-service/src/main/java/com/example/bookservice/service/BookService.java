package com.example.bookservice.service;

import com.example.bookservice.dto.BookDto;
import com.example.bookservice.model.Book;

import java.util.List;
import java.util.Optional;

public interface BookService {

    /**
     * Create a new Book
     */
    Book create(Book book);

    Optional<Book> findById(Long id);

    List<Book> findAll();

    Book mapToEntity(BookDto dto);

    BookDto mapToDto(Book book);

    Optional<Book> findByAuthorAndTitle(String author, String title);


}
