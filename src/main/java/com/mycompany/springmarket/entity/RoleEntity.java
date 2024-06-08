package com.mycompany.springmarket.entity;

import com.mycompany.springmarket.models.ERole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor  // Bu anotasyon varsayılan yapıcı metodu ekler
@Entity
@Table(name = "roles")
public class RoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ERole name;

    public RoleEntity(Integer id) {
        this.id = id;
    }

    public RoleEntity(ERole name) {
        this.name = name;
    }
}
