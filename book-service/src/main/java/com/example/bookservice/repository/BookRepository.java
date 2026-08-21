package com.example.bookservice.repository;

import com.example.bookservice.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    // Custom JPA Query Method
    boolean existsByIdAndStockGreaterThanEqual(Long id, int stock);

    Optional<Book> findByAuthorAndTitle(String author, String title);
}
