package com.hozan.univer.service;


import com.hozan.univer.model.Lesson;

import java.util.Collection;
import java.util.Optional;

public interface LessonService {

    Optional<Lesson> getById(Long id);
    Collection<Lesson> getAll();
    void remove(Long id);
    Optional<Lesson> save(Lesson lesson);
    Optional<Lesson> update(Lesson lesson);

}
