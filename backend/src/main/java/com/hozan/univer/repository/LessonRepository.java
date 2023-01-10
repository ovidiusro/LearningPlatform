package com.hozan.univer.repository;

import com.hozan.univer.model.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {

    Optional<Lesson> findById(Long id);
    Optional<Lesson> findByTitle(Long id);

}


