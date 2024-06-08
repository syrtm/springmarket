package com.mycompany.springmarket.repository;

import com.mycompany.springmarket.entity.RoleEntity;
import com.mycompany.springmarket.models.ERole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    Optional<RoleEntity> findByName(ERole name);
}
