package com.mycompany.springmarket;

import com.mycompany.springmarket.entity.RoleEntity;
import com.mycompany.springmarket.models.ERole;
import com.mycompany.springmarket.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements ApplicationRunner {

    @Autowired
    RoleRepository roleRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (roleRepository.findByName(ERole.ROLE_USER).isEmpty()) {
            roleRepository.save(new RoleEntity(ERole.ROLE_USER));
        }
        if (roleRepository.findByName(ERole.ROLE_MODERATOR).isEmpty()) {
            roleRepository.save(new RoleEntity(ERole.ROLE_MODERATOR));
        }
        if (roleRepository.findByName(ERole.ROLE_ADMIN).isEmpty()) {
            roleRepository.save(new RoleEntity(ERole.ROLE_ADMIN));
        }
    }
}