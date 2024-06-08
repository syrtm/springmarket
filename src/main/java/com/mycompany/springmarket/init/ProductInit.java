package com.mycompany.springmarket.init;

import com.mycompany.springmarket.entity.ProductEntity;
import com.mycompany.springmarket.repository.ProductRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.util.ArrayList;
import java.util.List;

@Component
public class ProductInit {

    @Autowired
    private ProductRepository productRepository;

    @PostConstruct
    public void init() {
        // Örnek ürünleri oluştur ve veritabanına ekle
        List<ProductEntity> products = createSampleProducts();
        productRepository.saveAll(products);
    }

    private List<ProductEntity> createSampleProducts() {
        List<ProductEntity> products = new ArrayList<>();

        for (int i = 1; i <= 25; i++) {
            ProductEntity product = new ProductEntity();
            product.setName("Ürün " + i);
            double price = Math.round(Math.random() * 1000); // Rastgele fiyat oluştur ve tam sayıya yuvarla
            product.setPrice(price);
            product.setDescription("Bu ürünün açıklaması");

            // Ürünü listeye ekle
            products.add(product);
        }

        return products;
    }
}
