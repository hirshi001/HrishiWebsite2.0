package com.hirshi001.springbootmicroservices.localrepo;

import lombok.extern.slf4j.Slf4j;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.util.*;
import java.util.function.Consumer;

@Slf4j
public abstract class AbstractLocalRepo<T> implements LocalRepo<T> {

    private Path repoPath;
    private FileTime lastModified;
    private boolean defaultReload = false;

    public AbstractLocalRepo() {
        super();
    }

    @Override
    public void setRepoPath(Path path) {
        this.repoPath = path;
    }

    @Override
    public Path getRepoPath() {
        return repoPath;
    }

    @Override
    public List<T> getAll(List<T> result) {
        if(result == null){
            result = new ArrayList<>();
        }
        result.addAll(getStuff());
        return result;
    }

    @Override
    public void load() throws FileNotFoundException {
        setLastModified();
        loadStuff();
    }

    @Override
    public void reload() throws FileNotFoundException {
        if (!checkAndSetLastModified()) {
            return;
        }
        reset();
        loadStuff();
    }

    @Override
    public void defaultReload(boolean defaultReload) {
        this.defaultReload = true;
    }

    protected boolean checkAndSetLastModified() {
        FileTime lastModified = null;
        try {
            lastModified = Files.getLastModifiedTime(repoPath);
        } catch (Exception e) {
            log.error("Error getting last modified time", e);
        }
        if (lastModified == null || lastModified.equals(this.lastModified)) {
            return false;
        }
        this.lastModified = lastModified;
        return true;
    }

    protected boolean setLastModified() {
        try {
            lastModified = Files.getLastModifiedTime(repoPath);
            return true;
        } catch (Exception e) {
            log.error("Error getting last modified time", e);
            return false;
        }
    }

    protected void checkReload() {
        if (defaultReload) {
            try {
                reload();
            } catch (FileNotFoundException e) {
                log.error("Error reloading repository: " + repoPath, e);
            }
        }
    }


    @Override
    public Iterator<T> iterator() {
        checkReload();
        return getStuff().iterator();
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        checkReload();
        getStuff().forEach(action);
    }

    @Override
    public Spliterator<T> spliterator() {
        checkReload();
        return getStuff().spliterator();
    }

    protected abstract void loadStuff() throws FileNotFoundException;
    protected abstract Collection<T> getStuff();

}
