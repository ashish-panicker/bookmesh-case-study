package com.example.bookorderservice.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "order_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "bookOrder")
@EqualsAndHashCode(exclude = "bookOrder")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private BookOrder bookOrder;

    private Long bookId;

    private Integer quantity;

    private Double price;
}
