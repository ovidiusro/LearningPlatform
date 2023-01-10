package com.hozan.univer.web.api;

import com.hozan.univer.model.File;
import com.hozan.univer.model.Lesson;
import com.hozan.univer.service.FileContentService;
import com.hozan.univer.service.FileService;
import com.hozan.univer.service.LessonService;
import jdk.nashorn.internal.runtime.regexp.joni.exception.InternalException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.*;
import java.io.InputStream;
import java.util.*;

@RestController
@RequestMapping(value = "/api/lesson")
@CrossOrigin(origins = "http://localhost:4200")
@Transactional
public class LessonController extends BaseController{


    private final LessonService lessonService;
    private final FileService fileService;
    private final FileContentService fileContentService;

    @Autowired
    public LessonController(LessonService lessonService, FileService fileService, FileContentService fileContentService) {
        this.lessonService = lessonService;
        this.fileService = fileService;
        this.fileContentService = fileContentService;
    }

    @PostMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<Lesson> updateLesson(@RequestBody Lesson bodyLesson, @PathVariable(value = "id") Long id) {
        logger.info("< updateLesson ", id);

        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();


        Optional<Lesson> lesson = lessonService.getById(id);
        if(!lesson.isPresent()){
            return new  ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if(!bodyLesson.getTitle().isEmpty()){
            Set<ConstraintViolation<Lesson>> constraintViolations = validator.validate(bodyLesson, Lesson.ValidateTitle.class);
            if(constraintViolations.isEmpty()){
                lesson.get().setTitle(bodyLesson.getTitle());
            }
            else {
                throw new ConstraintViolationException(constraintViolations);
            }
        }
        if(bodyLesson.getOrderNumber() != 0){
            Set<ConstraintViolation<Lesson>> constraintViolations = validator.validate(bodyLesson, Lesson.ValidateOrderNumber.class);
            if(constraintViolations.isEmpty()){
                lesson.get().setOrderNumber(bodyLesson.getOrderNumber());
            }
            else {
                throw new ConstraintViolationException(constraintViolations);
            }
        }
        if(!bodyLesson.getShortDescription().isEmpty()){
            Set<ConstraintViolation<Lesson>> constraintViolations = validator.validate(bodyLesson, Lesson.ValidateShortDescription.class);
            if(constraintViolations.isEmpty()){
                lesson.get().setShortDescription(bodyLesson.getShortDescription());
            }
            else {
                throw new ConstraintViolationException(constraintViolations);
            }
        }
        if(!bodyLesson.getBody().isEmpty()){
            Set<ConstraintViolation<Lesson>> constraintViolations = validator.validate(bodyLesson, Lesson.ValidateBody.class);
            if(constraintViolations.isEmpty()){
                lesson.get().setBody(bodyLesson.getBody());
            }
            else {
                throw new ConstraintViolationException(constraintViolations);
            }
        }

        Optional<Lesson> updatedLesson = lessonService.update(lesson.get());

        logger.info("> updateLesson", id);
        return new  ResponseEntity<Lesson>(updatedLesson.get(),HttpStatus.OK);
    }

    @PutMapping( produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Lesson> updateLesson(@Validated(Lesson.LessonForm.class) @RequestBody Lesson bodyLesson) {
        logger.info("< updateLesson id:{}", bodyLesson.getId());

        Optional<Lesson> updatedLesson = lessonService.update(bodyLesson);

        if(!updatedLesson.isPresent()){
            throw new InternalException("Lesson was not updated.");
        }


        logger.info("> updateLesson id:{}", bodyLesson.getId());
        return new ResponseEntity<>(bodyLesson, HttpStatus.OK);
    }

    @GetMapping(value = "/{lessonId}/files")
    public ResponseEntity<?> getAllFiles(@PathVariable("lessonId")Long lessonId){
        logger.info("< getAllFilesFromLesson lessonId:{}", lessonId);

        Optional<Lesson> lesson = lessonService.getById(lessonId);

        if (!lesson.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Collection<File> files = lesson.get().getFiles();

        logger.info("> getAllFilesFromLesson lessonId:{}", lessonId);
        return new ResponseEntity<Object>(files, HttpStatus.OK);
    }

    @GetMapping(value = "/{lessonId}/file/{fileId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getFileFromLessonById(@PathVariable("lessonId")Long lessonId, @PathVariable("fileId") Long fileId){
        logger.info("< getFileFromLessonByID lessonId:{}, fileId", lessonId, fileId);

        Optional<Lesson> lesson = lessonService.getById(lessonId);

        if (!lesson.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Optional<File> file = lesson.get().getFileById(fileId);
        if (!file.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Optional<InputStream> inputStream = fileContentService.getFileContent(file.get());
        InputStreamResource inputStreamResource = new InputStreamResource(inputStream.get());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentLength(file.get().getContentLength());
        headers.set("Content-Type", file.get().getMimeType());
        logger.info("> getFieFromLessonById lessonId:{}, fileId", lessonId, fileId);
        return new ResponseEntity<Object>(inputStreamResource, headers, HttpStatus.OK);
    }

    @PostMapping(value = "/{lessonId}/file/{fileId}",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addFileToLesson(@PathVariable("lessonId")Long lessonId, @PathVariable("fileId")Long fileId){
        logger.info("< addFileToLesson lessonId:{}, fileId", lessonId, fileId);

        Optional<Lesson> lesson = lessonService.getById(lessonId);
        Optional<File> file = fileService.getById(fileId);

        if(!lesson.isPresent()){
            throw new EntityNotFoundException("Lesson with this ID don't exist.");
        }
        if(!file.isPresent()){
            throw new EntityNotFoundException("File with this ID don't exist.");
        }

        lesson.get().addFile(file.get());
        Optional<Lesson> newLesson =  lessonService.update(lesson.get());

        logger.info("> addFileToLesson lessonId:{}, fileId", lessonId, fileId);
        return new ResponseEntity<Object>(newLesson, HttpStatus.OK);
    }

    @GetMapping(value = "/{lessonId}/video", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getVideo(@PathVariable("lessonId")Long lessonId){
        logger.info("< getVideo :{}, lessonId", lessonId);

        Optional<Lesson> lesson = lessonService.getById(lessonId);

        if (!lesson.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }


        Optional<InputStream> inputStream = fileContentService.getFileContent(lesson.get().getVideo());
        InputStreamResource inputStreamResource = new InputStreamResource(inputStream.get());
        HttpHeaders headers = new HttpHeaders();

        headers.setContentLength(lesson.get().getVideo().getContentLength());
        headers.set("Content-Type", lesson.get().getVideo().getMimeType());
        logger.info("> getVideo :{}, lessonId", lessonId);
        return new ResponseEntity<Object>(inputStreamResource, headers, HttpStatus.OK);
    }

    @PostMapping(value = "/{lessonId}/video/{fileId}",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> setVideoToLesson(@PathVariable("lessonId")Long lessonId, @PathVariable("fileId")Long fileId){
        logger.info("< setVideoToLesson lessonId:{}, fileId", lessonId, fileId);

        Optional<Lesson> lesson = lessonService.getById(lessonId);
        Optional<File> file = fileService.getById(fileId);

        if(!lesson.isPresent()){
            throw new EntityNotFoundException("Lesson with this ID don't exist.");
        }
        if(!file.isPresent()){
            throw new EntityNotFoundException("File with this ID don't exist.");
        }


        if(lesson.get().getVideo() != null) {
            lesson.get().setVideo(null);
            fileContentService.deleteContent(file.get());
            fileService.remove(file.get().getId());
        }
        lesson.get().setVideo(file.get());

        Optional<Lesson> newLesson =  lessonService.update(lesson.get());

        logger.info("> setVideoTolesson lessonId:{}, fileId", lessonId, fileId);
        return new ResponseEntity<Object>(newLesson, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{lessonId}/file/{fileId}")
    public ResponseEntity<?> removeFileFromLesson(@PathVariable("lessonId")Long lessonId, @PathVariable("fileId")Long fileId){
        logger.info("< removeFileFromLesson lessonId:{}, fileId", lessonId, fileId);

        Optional<Lesson> lesson = lessonService.getById(lessonId);
        Optional<File> file = fileService.getById(fileId);

        if(!lesson.isPresent()){
            throw new EntityNotFoundException("Lesson with this ID don't exist.");
        }
        if(!file.isPresent()){
            throw new EntityNotFoundException("File with this ID don't exist.");
        }

        if(!lesson.get().getFiles().isEmpty()){
            lesson.get().removeFileById(fileId);
            fileContentService.deleteContent(file.get());
            fileService.remove(fileId);
            Optional<Lesson> newLesson =  lessonService.update(lesson.get());
            return new ResponseEntity<>(newLesson, HttpStatus.OK);
        }

        logger.info("> removeFileFromLesson lessonId:{}, fileId", lessonId, fileId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<Collection<Lesson>> getAllLessonsOrderedByOrderNumber(){
        logger.info("< getAll ");

        List<Lesson> lessons = new ArrayList(lessonService.getAll());
        lessons.sort(Comparator.comparing(Lesson::getOrderNumber));

        if(lessons.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        logger.info("> getAll ");
        return new  ResponseEntity<>(lessons, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<Lesson> getById(@PathVariable(value = "id") Long id){
        logger.info("< getById id:{}", id);

        Optional<Lesson> account = this.lessonService.getById(id);

        if(!account.isPresent()){
            return new  ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        logger.info("> getById id:{}", id);
        return new  ResponseEntity<Lesson>(account.get(),HttpStatus.OK);
    }


    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Lesson> createLesson(@RequestBody @Validated(Lesson.LessonForm.class) Lesson lesson){
        logger.info("< createLesson");

        Optional<Lesson> createdLesson = lessonService.save(lesson);

        if(!createdLesson.isPresent()){
            throw new InternalException("The lesson was not created.");
        }

        logger.info("> createLesson ");
        return new ResponseEntity<>(lesson, HttpStatus.CREATED);
    }


    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Lesson> deleteLesson(@PathVariable("id") Long id){
        logger.info("< deleteLesson id:{} ", id);

        lessonService.remove(id);

        logger.info("> deleteLesson id:{}", id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }
}
