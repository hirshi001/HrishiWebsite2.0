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
@Table(name = "projects")
public class Project {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String url;
    private String gameEmbed;
    private String image;
    private String repositoryLink;
    private String description;

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", gameEmbed='" + gameEmbed + '\'' +
                ", image='" + image + '\'' +
                ", repositoryLink='" + repositoryLink + '\'' +
                '}';
    }
}
