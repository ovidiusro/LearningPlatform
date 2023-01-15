package com.hozan.platform.service;


import com.hozan.platform.model.File;

import java.util.Collection;
import java.util.Optional;

public interface FileService {

    Optional<File> getByName(String name);

    Collection<File> getAll();
    Optional<File>       getById(Long id);
    Optional<File>       create(File file);
    Optional<File>       update(File file);
    void        remove(Long id);
}
