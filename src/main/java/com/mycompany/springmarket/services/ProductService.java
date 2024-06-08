package com.mycompany.springmarket.services;

import com.mycompany.springmarket.entity.ProductEntity;
import com.mycompany.springmarket.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<ProductEntity> getAllProducts() {
        return productRepository.findAll();
    }

    public ProductEntity getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }
}
