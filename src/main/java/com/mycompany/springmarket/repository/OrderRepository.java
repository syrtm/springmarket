package com.mycompany.springmarket.repository;

import com.mycompany.springmarket.entity.OrderEntity;
import com.mycompany.springmarket.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    List<OrderEntity> findByUser(UserEntity user);

    OrderEntity findByUserAndCancelledFalse(UserEntity user);

    void deleteByUserId(Long userId);

}
