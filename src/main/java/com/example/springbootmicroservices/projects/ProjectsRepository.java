package com.example.springbootmicroservices.projects;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectsRepository extends JpaRepository<Project, Integer> {

    List<Project> findProjectsByNameContainingIgnoreCase(@Param("name") String name);

    Project findProjectByNameEqualsIgnoreCase(String name);

}
