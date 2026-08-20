package com.example.bookservice.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "books")
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@Builder
public class Book {

    @Id @GeneratedValue
    private Long id;

    @Column(nullable = false, length = 120)
    private String title;

    @Column(nullable = false, length = 100)
    private String author;

    @Column(nullable = false)
    private int stock;

    @Column(nullable = false)
    private double unitPrice;

}
