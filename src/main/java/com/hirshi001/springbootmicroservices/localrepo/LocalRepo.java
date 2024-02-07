package com.hirshi001.springbootmicroservices.localrepo;

import java.util.List;

public interface LocalRepo <T> extends Loader, Iterable<T>{

    List<T> getAll(List<T> result);

    void reset();

}
