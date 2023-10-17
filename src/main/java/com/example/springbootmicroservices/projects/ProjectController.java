package com.example.springbootmicroservices.projects;

import com.example.springbootmicroservices.SpringbootMicroservicesApplication;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ProjectController {

    private static final Logger LOG = LoggerFactory.getLogger(ProjectController.class);


    @Autowired
    ProjectsRepository projectsRepository;

    @Autowired
    LibraryRepository libraryRepository;

    Map<String, byte[]> libraryScreenshots = new HashMap<>();


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

    @Scheduled(fixedRate = 1000 * 60 * 60, initialDelay = 5000)
    public void updateLibraryScreenshots() {
        libraryScreenshots.clear();
        System.out.println("Updating library screenshots");

        WebDriver driver = SpringbootMicroservicesApplication.driver;
        for(Library library:libraryRepository.findAll()){
            try {
                driver.get(library.getUrl());
                Thread.sleep(1000);

                byte[] screenshot = (((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES));

                libraryScreenshots.put(library.getName(), screenshot);
                System.out.println("Updated screenshot for library: " + library.getName());
            } catch (Exception e) {
                LOG.error("Error getting screenshot for library: " + library.getName(), e);
            }

        }
    }



    @GetMapping(
            value="/libScreenshot",
            produces= MediaType.IMAGE_PNG_VALUE
    )
    public @ResponseBody byte[] getLibraryScreenshot(@RequestParam(value="name") String name) {
        return libraryScreenshots.get(name);
    }

    private ProjectDisplay mapToProjectDisplay(Project project) {
        ProjectDisplay projectDisplay = new ProjectDisplay();
        projectDisplay.setName(project.getName());
        projectDisplay.setId(project.getId());
        projectDisplay.setImage(project.getImage());
        return projectDisplay;
    }

}
