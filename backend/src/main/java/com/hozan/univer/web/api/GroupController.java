package com.hozan.univer.web.api;

import com.hozan.univer.dto.HibernateSearchService;
import com.hozan.univer.model.Account;
import com.hozan.univer.model.File;
import com.hozan.univer.model.Group;
import com.hozan.univer.model.Lesson;
import com.hozan.univer.service.*;
import jdk.nashorn.internal.runtime.regexp.joni.exception.InternalException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.*;
import java.io.InputStream;
import java.util.*;

import static com.google.common.collect.Lists.newArrayList;

@RestController
@RequestMapping(value = "/api/group")
@CrossOrigin(origins = "http://localhost:4200")
@ComponentScan
public class GroupController extends BaseController{

    private final GroupService groupService;
    private final AccountService accountService;
    private final LessonService lessonService;
    private final FileService fileService;
    private final FileContentService fileContentService;
    private final HibernateSearchService hibernateSearchService;

    @Autowired
    public GroupController(GroupService groupService,
                           AccountService accountService,
                           LessonService lessonService,
                           FileService fileService,
                           HibernateSearchService hibernateSearchService,
                           FileContentService fileContentService) {

        this.groupService = groupService;
        this.accountService = accountService;
        this.lessonService = lessonService;
        this.fileService = fileService;
        this.fileContentService = fileContentService;
        this.hibernateSearchService = hibernateSearchService;
    }


    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<Collection<Group>> getSearchedGroup(@RequestParam("searchTerm") String searchTerm)  {
        logger.info("< getSearchedGroup ");

        Collection<Group> groups = hibernateSearchService.fuzzySearch(searchTerm);

        logger.info("> getSearchedGroup ");
        return new  ResponseEntity<>(groups, HttpStatus.OK);
}

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<Collection<Group>> getAllGroup(){
        logger.info("< getAll ");

        Collection<Group> groups = groupService.getAll();
        if(groups.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        logger.info("> getAll ");
        return new  ResponseEntity<>(groups, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/owner")
    public ResponseEntity<Account> getOwner(@PathVariable(value = "id") Long id){
        logger.info("< getOwner") ;

        Optional<Group> group = groupService.getById(id);

        if(!group.isPresent()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        logger.info("> getOwner") ;
        return new ResponseEntity<>(group.get().getOwner(),HttpStatus.OK);
    }
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<Group> getById(@PathVariable(value = "id") long id){
        logger.info("< getById id:{}", id);

        Optional<Group> group = groupService.getById(id);

        if(!group.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        logger.info("> getById id:{}", id);
        return new  ResponseEntity<>(group.get(),HttpStatus.OK);
    }


    @GetMapping(value = "/{groupId}/lesson/all")
    public ResponseEntity<?> getAllLesson(@PathVariable("groupId")Long groupId){
        logger.info("< getAllLessons groupId :{},",groupId);

        Optional<Group> group = this.groupService.getById(groupId);


        if(!group.isPresent()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<Lesson> lessons = newArrayList(group.get().getLessons());
        lessons.sort(Comparator.comparing(Lesson::getOrderNumber));


        logger.info("> getAllLessons groupId :{},",groupId);
        return new ResponseEntity<>(lessons, HttpStatus.OK);

    }

    @GetMapping(value = "/{groupId}/lesson/{idLesson}")
    public ResponseEntity<?> getAllLesson(@PathVariable("groupId")Long groupId, @PathVariable("idLesson")Long idLesson){
        logger.info("< getAllLessons groupId :{},",groupId);

        Optional<Group> group = this.groupService.getById(groupId);

        if(!group.isPresent()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Collection<Lesson> lessons = group.get().getLessons();

        for (Lesson l: lessons ) {
            if(l.getId() == idLesson) {
                return new ResponseEntity<>(l, HttpStatus.OK);
            }
        }


        logger.info("> getAllLessons groupId :{},",groupId);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @PostMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<Group> updateGroup(@RequestBody Group bodyGroup, @PathVariable(value = "id") Long id) {
        logger.info("< updateGroup ", id);

        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();


        Optional<Group> group = groupService.getById(id);
        if(!group.isPresent()){
            return new  ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if(!bodyGroup.getName().isEmpty()){
            Set<ConstraintViolation<Group>> constraintViolations = validator.validate(bodyGroup, Group.ValidateName.class);
            if(constraintViolations.isEmpty()){
                group.get().setName(bodyGroup.getName());
            }
            else {
                throw new ConstraintViolationException(constraintViolations);
            }
        }
        if(!bodyGroup.getShortDescription().isEmpty()){
            Set<ConstraintViolation<Group>> constraintViolations = validator.validate(bodyGroup, Group.ValidateShortDescription.class);
            if(constraintViolations.isEmpty()){
                group.get().setShortDescription(bodyGroup.getShortDescription());
            }
            else {
                throw new ConstraintViolationException(constraintViolations);
            }
        }
        if(!bodyGroup.getLongDescription().isEmpty()){
            Set<ConstraintViolation<Group>> constraintViolations = validator.validate(bodyGroup, Group.ValidateLongDescription.class);
            if(constraintViolations.isEmpty()){
                group.get().setLongDescription(bodyGroup.getLongDescription());
            }
            else {
                throw new ConstraintViolationException(constraintViolations);
            }
        }

        Optional<Group> updatedGroup = groupService.update(group.get());

        logger.info("> updateGroup", id);
        return new  ResponseEntity<Group>(updatedGroup.get(),HttpStatus.OK);
    }


    @GetMapping(value = "/{groupId}/cover", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getCover(@PathVariable("groupId")Long groupId){
        logger.info("< getCove  groupId:{}",groupId);

        Optional<Group> group = groupService.getById(groupId);

        if (!group.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Optional<File> file = fileService.getById(group.get().getCover().getId());
        if (!file.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Optional<InputStream> inputStream = fileContentService.getFileContent(file.get());
        InputStreamResource inputStreamResource = new InputStreamResource(inputStream.get());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentLength(file.get().getContentLength());
        headers.set("Content-Type", file.get().getMimeType());
        logger.info("> getCover groupId:{}",groupId);
        return new ResponseEntity<Object>(inputStreamResource, headers, HttpStatus.OK);
    }

    @PostMapping(value = "/cover")
    public ResponseEntity<Group> setCover(@RequestParam("groupId")Long groupId, @RequestParam("fileId")Long fileId){
        logger.info("< setCover  groupId:{} fileId id:{}",groupId, fileId);


        Optional<Group> group = groupService.getById(groupId);
        Optional<File> cover = fileService.getById(fileId);


        if(!group.isPresent()){
            throw new EntityNotFoundException("Cover with this ID don't exist.");
        }
        if(!cover.isPresent()){
            throw new EntityNotFoundException("File with this ID don't exist.");
        }

        if(group.get().getCover() != null){
            fileContentService.deleteContent(group.get().getCover());
            fileService.remove(group.get().getCover().getId());
        }

        group.get().setCover(cover.get());


        groupService.update(group.get());

        logger.info("> setCover groupId:{} fileId:{}",groupId, fileId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @GetMapping(value = "/{groupId}/banner",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getBanner(@PathVariable("groupId")Long groupId){
        logger.info("< getBanner  groupId:{}",groupId);

        Optional<Group> group = groupService.getById(groupId);

        if (!group.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Optional<File> file = fileService.getById(group.get().getBanner().getId());
        if (!file.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }


        Optional<InputStream> inputStream = fileContentService.getFileContent(file.get());
        InputStreamResource inputStreamResource = new InputStreamResource(inputStream.get());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentLength(file.get().getContentLength());
        headers.set("Content-Type", file.get().getMimeType());
        logger.info("> getBanner  groupId:{}",groupId);
        return new ResponseEntity<Object>(inputStreamResource, headers, HttpStatus.OK);
    }

    @PostMapping(value = "/banner")
    public ResponseEntity<Group> setBanner(@RequestParam("groupId")Long groupId, @RequestParam("fileId")Long fileId){
        logger.info("< setBanner  groupId:{} fileId id:{}", groupId, fileId);


        Optional<Group> group = groupService.getById(groupId);
        Optional<File> banner = fileService.getById(fileId);

        if(!group.isPresent()){
            throw new EntityNotFoundException("Banner with this ID don't exist.");
        }
        if(!banner.isPresent()){
            throw new EntityNotFoundException("File with this ID don't exist.");
        }

        if(group.get().getBanner() != null){
            fileContentService.deleteContent(group.get().getBanner());
            fileService.remove(group.get().getBanner().getId());
        }
        group.get().setBanner(banner.get());


        groupService.update(group.get());

        logger.info("> setBanner groupId:{} fileId:{}",groupId, fileId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

    @PostMapping(value = "/{groupId}/lesson/{lessonId}")
    public ResponseEntity<?> addLesson(@PathVariable("groupId")Long groupId, @PathVariable("lessonId") Long lessonId){
        logger.info("< addLesson groupId :{}, lessonId",groupId,lessonId);
        Optional<Lesson> lesson = this.lessonService.getById(lessonId);
        Optional<Group> group = this.groupService.getById(groupId);

        if(!lesson.isPresent() || !group.isPresent()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        group.get().addLesson(lesson.get());
        this.groupService.update(group.get());

        logger.info("> addLesson groupId :{}, lessonId",groupId,lessonId);
        return new ResponseEntity<>(HttpStatus.OK);

    }

    @DeleteMapping(value = "/{groupId}/lesson/{lessonId}")
    public ResponseEntity<?> removeLesson(@PathVariable("groupId")Long groupId, @PathVariable("lessonId") Long lessonId){
        logger.info("< removeLesson groupId :{}, lessonId",groupId,lessonId);

        Optional<Lesson> lesson = this.lessonService.getById(lessonId);
        Optional<Group> group = this.groupService.getById(groupId);

        if(!lesson.isPresent() || !group.isPresent()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        group.get().removeLessonById(lessonId);
        this.groupService.update(group.get());
        this.lessonService.remove(lessonId);

        logger.info("> removeLesson groupId :{}, lessonId",groupId,lessonId);
        return new ResponseEntity<>(HttpStatus.OK);

    }


    @GetMapping(value = "/{id}/followers")
    public ResponseEntity<Collection<Account>> getFollowers(@PathVariable(value = "id") Long id){
        logger.info("< getFollowers") ;

        Optional<Group> group = groupService.getById(id);

        if(!group.isPresent()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        logger.info("> getFollowers") ;
        return new ResponseEntity<>(group.get().getFollowers(),HttpStatus.OK);
    }



    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Group> createGroup(@RequestBody @Validated(Group.GroupForm.class) Group group){
        logger.info("< createGroup ");

        Optional<Group> createdGroup= groupService.create(group);

        if(!createdGroup.isPresent()){
            throw new InternalException("Group was not created");
        }

        logger.info("> createGroup");
        return new ResponseEntity<>(createdGroup.get(),HttpStatus.CREATED);
    }

//    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
//            produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<Group> updateGroup(@RequestBody @Validated(Group.GroupForm.class) Group bodyGroup)
//    {
//        logger.info("< updateGroup id:{}", bodyGroup.getId());
//
//        Optional<Group> updatedGroup = groupService.update(bodyGroup);
//
//        if(!updatedGroup.isPresent()) {
//            throw  new InternalException("Group was not updated");
//        }
//
//        logger.info("> updateGourp id:{}", bodyGroup.getId());
//        return new ResponseEntity<>(bodyGroup,HttpStatus.OK);
//    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Group> deleteGroup(@PathVariable("id") Long id){
        logger.info("< deleteGroup id:{} ", id);

        Optional<Group> group = groupService.getById(id);

        if(!group.isPresent()){
            throw new EntityNotFoundException("Group with this ID don't exist.");
        }

        List<Account> followers = new ArrayList<>(group.get().getFollowers());

        for (Account follower : followers) {
            follower.stopFollowingGroup(group.get());
            accountService.update(follower);
        }

        group.get().removeOwner();

        groupService.update(group.get());
        groupService.remove(id);

        logger.info("> deleteGroup id:{}", id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

    @PostMapping(value = "/{groupId}/owner/{accountId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Group> setOwnerOfGroup(@PathVariable("groupId")Long groupId, @PathVariable("accountId")Long accountId){
        logger.info("< setOwnerOfGroup idGroup:{} , idAccount :{}",groupId, accountId);

        Optional<Account> account = accountService.getById(accountId);

        if(!account.isPresent()){
            throw new EntityNotFoundException("Account with this ID don't exist.");
        }

        Optional<Group> group = groupService.getById(groupId);

        if(!group.isPresent()){
            throw new EntityNotFoundException("Group with this ID don't exist.");
        }

        group.get().setOwner(account.get());

        groupService.update(group.get());
        accountService.update(account.get());

        logger.info("> setOwnerOfGroup idGroup:{} , idAccount :{}",groupId, accountId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping(value = "/{groupId}/follower/{followerId}")
    public ResponseEntity<Group> addFollower(@PathVariable("groupId")Long groupId, @PathVariable("followerId")Long followerId){
        logger.info("< addFollower id:{} in group id:{}",followerId,groupId);

        Optional<Account> loggedInUser = accountService.getById(followerId);

        if(!loggedInUser.isPresent()){
            throw new EntityNotFoundException("User with this ID don't exist.");
        }

        Optional<Group> group = groupService.getById(groupId);

        if(!group.isPresent()){
            throw new EntityNotFoundException("Group with this ID don't exist.");
        }

        group.get().addFollower(loggedInUser.get());


        groupService.update(group.get());
        accountService.update(loggedInUser.get());

        logger.info("> addFollower id:{} in group id:{}",followerId,groupId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

    @DeleteMapping(value = "/{groupId}/follower/{followerId}")
    public ResponseEntity<Group> removeFollower(@PathVariable("groupId")Long groupId, @PathVariable("followerId")Long followerId){
        logger.info("< removeFollower id:{} in group id:{}",followerId,groupId);

        Optional<Account> user = accountService.getById(followerId);
        Optional<Group> group = groupService.getById(groupId);

        if(!user.isPresent()){
            throw new EntityNotFoundException("User with this id don't exist");
        }

        if(!group.isPresent()){
            throw new EntityNotFoundException("Group with this id don't exist");
        }

        group.get().removeFollower(user.get());

        groupService.update(group.get());
        accountService.update(user.get());

        logger.info("> removeFollower id:{} in group id:{}",followerId,groupId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}