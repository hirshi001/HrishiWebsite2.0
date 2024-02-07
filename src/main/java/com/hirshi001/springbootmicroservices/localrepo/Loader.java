package com.hirshi001.springbootmicroservices.localrepo;

import java.io.FileNotFoundException;
import java.nio.file.Path;

public interface Loader {


    void setRepoPath(Path path);

    Path getRepoPath();

    void load() throws FileNotFoundException;

    void reload() throws FileNotFoundException;

    void defaultReload(boolean defaultReload);

}
