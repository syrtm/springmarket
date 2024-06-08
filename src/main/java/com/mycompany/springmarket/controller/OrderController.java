package com.mycompany.springmarket.controller;

import com.mycompany.springmarket.payload.response.OrderResponse;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.mycompany.springmarket.entity.OrderEntity;
import com.mycompany.springmarket.entity.UserEntity;
import com.mycompany.springmarket.services.OrderService;
import com.mycompany.springmarket.services.UserService;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @PostMapping("/add")
    public ResponseEntity<?> createOrder(@RequestBody OrderEntity order, Principal principal) {
        System.out.println("test");
        UserEntity user = userService.findByUsername(principal.getName()).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().body("User not found");
        }
        order.setUser(user);
        orderService.saveOrder(order);
        return ResponseEntity.ok("Order created successfully");
    }

    // Cancel order endpoint
    // Implement according to your business logic

    /*@GetMapping("/getOrder")
    public List<OrderEntity> getUserOrders(Principal principal) {
        List<OrderEntity> oe= new ArrayList<>();
        try{
            System.out.println(principal.getName());
            UserEntity user = userService.findByUsername(principal.getName()).orElse(null);
             oe = orderService.getOrdersByUser(user);
            return oe;
        }catch (Exception e){
            e.printStackTrace();
        }
            return oe;
    } */ @GetMapping("/getOrder")
    public OrderResponse getUserOrders(Principal principal) {
        OrderEntity oe= new OrderEntity();
        OrderResponse or = new OrderResponse();
        try{
            System.out.println(principal.getName());
            UserEntity user = userService.findByUsername(principal.getName()).orElse(null);
             oe = orderService.getOrder(user);
             or.setId(oe.getId());
             or.setAddress(oe.getAddress());
             or.setProductName(oe.getProductName());
             or.setProductPrice(oe.getProduct().getPrice());
             or.setProductDescription(oe.getProduct().getDescription());
             or.setCreditCardNumber(oe.getCreditCardNumber());
            return or;
        }catch (Exception e){
            e.printStackTrace();
        }
            return or;
    }


    @PostMapping("/cancel")
    public ResponseEntity<String> cancelOrder(Principal principal) {
        // Oturum açmış kullanıcının kimliğini al
        System.out.println(principal.getName());
        UserEntity currentUser = userService.findByUsername(principal.getName()).orElse(null);

        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
        }
        OrderEntity order = orderService.getOrder(currentUser);

        if (order == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found for the user");
        }
        // Siparişin iptal edilip edilmediğini kontrol et
        if (order.isCancelled()) {
            return ResponseEntity.badRequest().body("Order is already cancelled");
        }

        // Siparişi iptal et
        orderService.cancelOrder(order);
        return ResponseEntity.ok("Order cancelled successfully");
    }

}
