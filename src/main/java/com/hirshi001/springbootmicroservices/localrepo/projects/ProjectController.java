package com.hirshi001.springbootmicroservices.localrepo.projects;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping("/projects")
public class ProjectController {


    @Value("${app.base-folder}")
    private String baseFolder;

    @Value("${app.project-folder}")
    private String projectFolder;

    private final ProjectRepository projectRepository = new ProjectRepositoryImpl();


    @GetMapping("/projects.json")
    public List<Project> index(@RequestParam(value = "searchTerm", required = false) String searchTerm, @RequestParam(value = "id", required = false) Integer id) {
        if (searchTerm != null) {
            return List.of(projectRepository.getByName(searchTerm));
        } else if (id != null) {
            return List.of(projectRepository.getById(id));
        }
        return projectRepository.getAll(null);
    }

    @GetMapping("/projects_display.json")
    public List<ProjectDisplay> projectsDisplay(@RequestParam(value = "search", required = false) String searchString) {
        if (searchString == null)
            return projectRepository.getAll(null).stream().map(this::mapToProjectDisplay).toList();
        else
            return projectRepository.searchFor(searchString, null).stream().map(this::mapToProjectDisplay).toList();
    }

    private ProjectDisplay mapToProjectDisplay(Project project) {
        ProjectDisplay projectDisplay = new ProjectDisplay();
        projectDisplay.setName(project.getName());
        projectDisplay.setId(project.getId());
        projectDisplay.setImage(project.getImage());
        return projectDisplay;
    }


    @EventListener(ApplicationReadyEvent.class)
    public void onStartup() {
        try {
            loadProjects();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadProjects() throws FileNotFoundException {
        projectRepository.setRepoPath(Path.of(baseFolder, projectFolder, "projects.json"));
        projectRepository.defaultReload(true);
        projectRepository.load();
    }


}
