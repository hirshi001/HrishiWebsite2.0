package com.hirshi001.springbootmicroservices.localrepo.libraries;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.hirshi001.springbootmicroservices.localrepo.AbstractLocalRepo;
import lombok.extern.slf4j.Slf4j;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class LibraryRepositoryImpl extends AbstractLocalRepo<Library> implements LibraryRepository {

    private final Map<String, Library> librariesByName;
    private final Map<Integer, Library> librariesById;
    private final List<String> libraryNames;
    private final List<Library> libraries;


    public LibraryRepositoryImpl() {
        librariesByName = new ConcurrentHashMap<>();
        librariesById = new ConcurrentHashMap<>();
        libraryNames = new ArrayList<>();
        libraries = new ArrayList<>();
    }


    @Override
    public List<Library> searchFor(String name, List<Library> result) {
        checkReload();
        if (result == null) {
            result = new ArrayList<>();
        }
        name = name.toLowerCase();

        for (String libName : libraryNames) {
            if (libName.contains(name)) {
                result.add(librariesByName.get(libName));
            }
        }
        return result;
    }

    @Override
    public Library getByName(String name) {
        checkReload();
        return librariesByName.get(name);
    }

    @Override
    public Library getById(int id) {
        checkReload();
        return librariesById.get(id);
    }

    @Override
    public void reset() {
        librariesByName.clear();
        librariesById.clear();
        libraryNames.clear();
        libraries.clear();
    }

    @Override
    protected void loadStuff() throws FileNotFoundException {
        Path repoPath = getRepoPath();
        log.info("Loading libraries from: " + repoPath);
        Gson gson = new Gson();

        JsonArray jsonArray = gson.fromJson(new FileReader(repoPath.toFile()), JsonArray.class);
        for (JsonElement lib : jsonArray) {
            Library library = gson.fromJson(lib, Library.class);

            librariesByName.put(library.getName(), library);
            librariesById.put(library.getId(), library);
            libraryNames.add(library.getName().toLowerCase());
            libraries.add(library);
        }
    }

    @Override
    protected Collection<Library> getStuff() {
        return libraries;
    }

}
