package com.example.springbootmicroservices.projects;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "libraries")
public class Library {

    @Id
    private String name;
    private String url;
    private String description;

    @Override
    public String toString() {
        return "Project{" +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

}
