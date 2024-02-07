package com.hirshi001.springbootmicroservices.localrepo.projects;

import com.hirshi001.springbootmicroservices.localrepo.AbstractLocalRepo;
import com.hirshi001.springbootmicroservices.localrepo.libraries.Library;
import com.nimbusds.jose.shaded.gson.Gson;
import com.nimbusds.jose.shaded.gson.JsonArray;
import com.nimbusds.jose.shaded.gson.JsonElement;
import lombok.extern.slf4j.Slf4j;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

@Slf4j
public class ProjectRepositoryImpl extends AbstractLocalRepo<Project> implements ProjectRepository {

    private final Map<String, Project> projectsByName;
    private final Map<Integer, Project> projectsById;
    private final List<String> projectNames;
    private final List<Project> projects;


    public ProjectRepositoryImpl() {
        projectsByName = new ConcurrentHashMap<>();
        projectsById = new ConcurrentHashMap<>();
        projectNames = new ArrayList<>();
        projects = new ArrayList<>();
    }


    @Override
    protected void loadStuff() throws FileNotFoundException {
        Path repoPath = getRepoPath();
        log.info("Loading projects from: " + repoPath);
        Gson gson = new Gson();

        JsonArray jsonArray = gson.fromJson(new FileReader(repoPath.toFile()), JsonArray.class);
        for (JsonElement lib : jsonArray) {
            Project project = gson.fromJson(lib, Project.class);
            projectsByName.put(project.getName().toLowerCase(), project);
            projectsById.put(project.getId(), project);
            projectNames.add(project.getName().toLowerCase());
            projects.add(project);
        }
    }

    @Override
    protected Collection<Project> getStuff() {
        return projects;
    }


    @Override
    public List<Project> searchFor(String name, List<Project> result) {
        checkReload();
        if (result == null) {
            result = new ArrayList<>();
        }
        name = name.toLowerCase();

        for (String projectName : projectNames) {
            if (projectName.contains(name)) {
                result.add(projectsByName.get(projectName));
            }
        }
        return result;
    }

    @Override
    public Project getByName(String name) {
        checkReload();
        return projectsByName.get(name.toLowerCase());
    }

    @Override
    public Project getById(int id) {
        checkReload();
        return projectsById.get(id);
    }

    @Override
    public void reset() {
        projectsByName.clear();
        projectsById.clear();
        projectNames.clear();
        projects.clear();
    }
}
