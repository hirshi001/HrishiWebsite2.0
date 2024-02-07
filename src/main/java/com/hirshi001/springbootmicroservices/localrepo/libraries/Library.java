package com.hirshi001.springbootmicroservices.localrepo.libraries;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Library {

    private String name;
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
