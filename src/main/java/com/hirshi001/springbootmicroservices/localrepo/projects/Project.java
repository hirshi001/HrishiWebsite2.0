package com.hirshi001.springbootmicroservices.localrepo.projects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Project {


    private int id;
    private String name;
    private String url;
    private String game_embed;
    private String image;
    private String repository_link;
    private String description;

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", gameEmbed='" + game_embed + '\'' +
                ", image='" + image + '\'' +
                ", repositoryLink='" + repository_link + '\'' +
                '}';
    }
}
