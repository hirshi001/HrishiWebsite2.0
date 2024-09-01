package com.hirshi001.springbootmicroservices.localrepo.libraries;

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
    private String defaultImage;
    private String description;

    @Override
    public String toString() {
        return "Library{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", url='" + url + '\'' +
                ", default_image='" + defaultImage + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
