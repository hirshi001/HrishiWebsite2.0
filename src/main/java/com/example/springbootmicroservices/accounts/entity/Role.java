package com.example.springbootmicroservices.accounts.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="roles")
public class Role {

    public static final String
            ROLE_USER = "ROLE_USER",
            ROLE_PREMIUM = "ROLE_PREMIUM",
            ROLE_MODERATOR = "ROLE_MODERATOR",
            ROLE_ADMIN = "ROLE_ADMIN";

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToMany(mappedBy = "roles")
    private List<User> users;


    public void setId(Long id) {
        this.id = id;
    }

}
