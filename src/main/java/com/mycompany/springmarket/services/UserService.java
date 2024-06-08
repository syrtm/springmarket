package com.mycompany.springmarket.services;

import com.mycompany.springmarket.entity.UserEntity;
import com.mycompany.springmarket.repository.OrderRepository;
import com.mycompany.springmarket.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Autowired
    private OrderRepository orderRepository;

    public UserEntity saveUser(UserEntity user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public Optional<UserEntity> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void deleteUser(UserEntity user) {
        userRepository.delete(user);
    }

    @Transactional
    public void deleteUserAndOrders(String username) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Error: User not found!"));

        // First, delete the user's orders
        orderRepository.deleteByUserId(user.getId());

        // Then, delete the user
        userRepository.delete(user);
    }
}
