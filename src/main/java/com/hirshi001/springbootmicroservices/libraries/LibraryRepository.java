package com.hirshi001.springbootmicroservices.libraries;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LibraryRepository extends JpaRepository<Library, Integer> {

    List<Library> findProjectsByNameContainingIgnoreCase(@Param("name") String name);

    Library findByName(@Param("name") String name);

    Library findProjectByNameEqualsIgnoreCase(String name);

    Library getById(@Param("id") int id);
}
