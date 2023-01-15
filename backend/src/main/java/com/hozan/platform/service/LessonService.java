package com.hozan.platform.service;


import com.hozan.platform.model.Lesson;

import java.util.Collection;
import java.util.Optional;

public interface LessonService {

    Optional<Lesson> getById(Long id);
    Collection<Lesson> getAll();
    void remove(Long id);
    Optional<Lesson> save(Lesson lesson);
    Optional<Lesson> update(Lesson lesson);

}
