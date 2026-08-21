package com.example.bookorderservice.repository;

import com.example.bookorderservice.model.BookOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookOrderRepository extends JpaRepository<BookOrder, Long> {
}
