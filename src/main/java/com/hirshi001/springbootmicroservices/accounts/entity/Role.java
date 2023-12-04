package com.hirshi001.springbootmicroservices.accounts.entity;

import com.google.common.collect.Sets;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="roles")
public class Role implements GrantedAuthority {

    @Override
    public String getAuthority() {
        return name;
    }

    public static final String
            USER = "ROLE_USER",
            PREMIUM = "ROLE_PREMIUM",
            MODERATOR = "ROLE_MODERATOR",
            ADMIN = "ROLE_ADMIN";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<User> users = Sets.newHashSet();


    @Override
    public String toString() {
        return "Role{" +
                "name='" + getAuthority() + '\'' +
                '}';
    }
}
