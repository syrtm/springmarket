package com.mycompany.springmarket.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "orders")
@ToString
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;

    @Column(nullable = false)
    private LocalDateTime orderDate;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String creditCardNumber;

    private boolean cancelled;

    @Transient
    public String getProductName() {
        return this.product.getName();
    }

}
