package com.hirshi001.springbootmicroservices.localrepo.projects;

import com.hirshi001.springbootmicroservices.localrepo.LocalRepo;

import java.util.List;

public interface ProjectRepository extends LocalRepo<Project> {

    List<Project> searchFor(String name, List<Project> result);

    Project getByName(String name);

    Project getById(int id);

}
