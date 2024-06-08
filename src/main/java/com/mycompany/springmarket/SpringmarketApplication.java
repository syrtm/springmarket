package com.mycompany.springmarket;

import com.mycompany.springmarket.init.ProductInit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringmarketApplication {

	public static void main(String[] args) {
		ProductInit productInit = new ProductInit();
		SpringApplication.run(SpringmarketApplication.class, args);
	}

}
