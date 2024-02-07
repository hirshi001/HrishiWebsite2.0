package com.hirshi001.springbootmicroservices.localrepo.libraries;

import com.hirshi001.springbootmicroservices.localrepo.LocalRepo;

import java.util.List;

public interface LibraryRepository extends LocalRepo<Library> {

    List<Library> searchFor(String name, List<Library> result);

    Library getByName(String name);

    Library getById(int id);

}
