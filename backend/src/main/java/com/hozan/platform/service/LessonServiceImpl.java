package com.hozan.platform.service;

import com.hozan.platform.model.File;
import com.hozan.platform.model.Lesson;
import com.hozan.platform.repository.LessonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import javax.persistence.NoResultException;
import java.util.Collection;
import java.util.Optional;

@Service
@Transactional(propagation =  Propagation.SUPPORTS, readOnly=true)
public class LessonServiceImpl implements LessonService{

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private  LessonRepository lessonRepo;

    @Autowired
    private FileService fileService;
    @Autowired
    private FileContentService fileContentService;



    @Override
    public Optional<Lesson> getById(Long id) {
        logger.info("<> getById id:{}");
        return lessonRepo.findById(id);
    }


    @Override
    public Collection<Lesson> getAll() {
        logger.info("<> getAll");
        return lessonRepo.findAll();
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void remove(Long id) {
        logger.info("< remove id:{}",id);

         Optional<Lesson> lesson = lessonRepo.findById(id);

         if(lesson.isPresent()) {
             if(!lesson.get().getFiles().isEmpty()) {
                 for (File f : lesson.get().getFiles()) {
                     fileService.remove(f.getId());
                     fileContentService.deleteContent(f);
                 }
             }

             lessonRepo.delete(lesson.get());
         }
        logger.info("> remove id:{}", id);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public Optional<Lesson> save(Lesson lesson) {
        logger.info("< remove");

        if (lesson.getId() != null) {
            logger.error(
                    "Attempted to create a lesson, but id attribute was not null.");
            throw new EntityExistsException(
                    "The id attribute must be null to persist a new model.");
        }

        Optional<Lesson> newLesson = Optional.of(this.lessonRepo.save(lesson));
        logger.info("> remove");
        return newLesson;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public Optional<Lesson> update(Lesson lesson) {
        logger.info("< update id:{} ", lesson.getId());

        Optional<Lesson> lessonToUpdate = this.lessonRepo.findById(lesson.getId());
        if(!lessonToUpdate.isPresent()){
            logger.error( "Attempted to update a file, but the model does not exist.");
            throw new NoResultException("Requested model not found.");
        }

        lessonToUpdate = Optional.of(lessonRepo.save(lesson));

        logger.info("> update id:{} ", lesson.getId());
        return lessonToUpdate;
    }
}
