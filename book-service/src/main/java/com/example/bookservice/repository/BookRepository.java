package com.example.bookservice.repository;

import com.example.bookservice.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    /**
     * Check if the Book has the requested units available
     */
    // boolean checkIfStockExists();

    Optional<Book> findByAuthorAndTitle(String author, String title);
}
