package com.hirshi001.springbootmicroservices.localrepo.projects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDisplay {


    public ProjectDisplay(Project project) {
        setName(project.getName());
        setId(project.getId());
        setImage(project.getImage());
    }

    private int id;
    private String name;
    private String image;

}
