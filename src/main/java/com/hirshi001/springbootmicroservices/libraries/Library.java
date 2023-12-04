package com.hirshi001.springbootmicroservices.libraries;

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

    private String name;
    @Id
    private int id;
    private String url;
    private String description;

    @Override
    public String toString() {
        return "Project{" +
                ", name='" + name + '\'' +
                ", id=" + id +
                ", url='" + url + '\'' +
                '}';
    }

}
