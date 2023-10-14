package com.example.springbootmicroservices.projects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProjectController {

    @Autowired
    ProjectsRepository projectsRepository;

    @Autowired
    LibraryRepository libraryRepository;

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

    @GetMapping("/libraries.json")
    public List<Library> libraries(@RequestParam(value = "search", required = false) String searchString) {
        if (searchString == null)
            return libraryRepository.findAll();
        else
            return libraryRepository.findProjectsByNameContainingIgnoreCase(searchString);
    }

    private ProjectDisplay mapToProjectDisplay(Project project) {
        ProjectDisplay projectDisplay = new ProjectDisplay();
        projectDisplay.setName(project.getName());
        projectDisplay.setId(project.getId());
        projectDisplay.setImage(project.getImage());
        return projectDisplay;
    }

}
