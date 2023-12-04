package com.hirshi001.springbootmicroservices.projects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projects")
public class ProjectController {



    @Autowired
    ProjectsRepository projectsRepository;


    @GetMapping("/projects.json")
    public List<Project> index(@RequestParam(value = "searchTerm", required = false) String searchTerm, @RequestParam(value = "id", required = false) Integer id) {
        if (searchTerm != null) {
            return projectsRepository.findProjectsByNameContainingIgnoreCase(searchTerm);
        } else if (id != null) {
            return projectsRepository.findById(id).stream().toList();
        }
        return projectsRepository.findAll();
    }

    @GetMapping("/projects_display.json")
    public List<ProjectDisplay> projectsDisplay(@RequestParam(value = "search", required = false) String searchString) {
        if (searchString == null)
            return projectsRepository.findAll().stream().map(this::mapToProjectDisplay).toList();
        else
            return projectsRepository.findProjectsByNameContainingIgnoreCase(searchString).stream().map(this::mapToProjectDisplay).toList();
    }

    private ProjectDisplay mapToProjectDisplay(Project project) {
        ProjectDisplay projectDisplay = new ProjectDisplay();
        projectDisplay.setName(project.getName());
        projectDisplay.setId(project.getId());
        projectDisplay.setImage(project.getImage());
        return projectDisplay;
    }

}
