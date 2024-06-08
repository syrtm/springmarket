package com.mycompany.springmarket.repository;

import com.mycompany.springmarket.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
}
