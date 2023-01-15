package com.hozan.platform.service;

import com.hozan.platform.model.Group;

import java.util.Collection;
import java.util.Optional;

public interface GroupService {

    Collection<Group>  getAll();
    Optional<Group> getById(Long id);
    Optional<Group> create(Group group);
    Optional<Group> update(Group group);
    void  remove(Long id);
    void  evictCache();

    Optional<Group> getByName(String name);
}
