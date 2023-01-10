package com.hozan.univer.service;

import com.hozan.univer.model.Group;
import com.hozan.univer.model.Lesson;
import com.hozan.univer.repository.GroupRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import javax.persistence.NoResultException;
import java.util.Collection;
import java.util.Optional;

@Service
@Transactional(propagation =  Propagation.SUPPORTS, readOnly=true)
public class GroupServiceImpl implements GroupService{


    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private GroupRepository groupRepo;
    private LessonService lessonService;

    @Autowired
    private FileContentService fileContentService;
    @Autowired
    private FileService fileService;

    @Autowired
    public GroupServiceImpl(GroupRepository groupRepo, LessonService lessonService) {
        this.groupRepo = groupRepo;
        this.lessonService = lessonService;
    }


    @Override
    public Collection<Group> getAll() {
        logger.info("< getAll()");

        Collection<Group> groups = this.groupRepo.findAll();

        logger.info("> getAll()");

        return groups;
    }


    @Override
    @Cacheable(value = "groupCache",unless = "#id != null")
    public Optional<Group> getById(Long id) {
        logger.info("< getById id:{}", id);

        Optional<Group> group = this.groupRepo.findGroupById(id);

        logger.info("> getById id:{}", id);

        return group;
    }

    @Override
    @Transactional(propagation=Propagation.REQUIRED,readOnly = false)
    @CachePut(value = "groupCache", unless = "#result.id != null ", key="#result.id")
    public Optional<Group> create(Group group) {
        logger.info("< create()");

        if (group.getId() != null) {
            logger.error(
                    "Attempted to create a group, but id attribute was not null.");
            throw new EntityExistsException(
                    "The id attribute must be null to persist a new model.");
        }

        Optional<Group> newGroup = Optional.of(this.groupRepo.save(group));


        logger.info("> create ");

        return  newGroup;
    }

    @Override
    @Transactional(propagation=Propagation.REQUIRED,readOnly = false)
    @CachePut(value = "groupCache", unless = "#result.id != null ", key="#group.id")
    public Optional<Group> update(Group group) {
        logger.info("< update id:{} ", group.getId());

        Optional<Group> groupToUpdate= groupRepo.findGroupById(group.getId());

        if(!groupToUpdate.isPresent()) {
            logger.error(
                    "Attempted to update a group, but the object does not exist.");
            throw new NoResultException("Requested group not found.");
        }

         groupToUpdate = Optional.of(this.groupRepo.save(group));

        logger.info("> update id:{}",group.getId());
        return groupToUpdate;
    }


    @Override
    @Transactional(propagation=Propagation.REQUIRED,readOnly = false)
    @CacheEvict(value = "groupCache",key = "#id")
    public void remove(Long id) {
        logger.info("< remove id:{} ", id);

        Optional<Group> group = groupRepo.findGroupById(id);
        if(!group.isPresent()) {
            return;
        }

        for (Lesson l :group.get().getLessons()) {
           lessonService.remove(l.getId());
        }
        if(group.get().getBanner() != null) {
            fileContentService.deleteContent(group.get().getBanner());
            fileService.getById(group.get().getBanner().getId());
        }

        if(group.get().getCover() != null) {
            fileContentService.deleteContent(group.get().getCover());
            fileService.getById(group.get().getCover().getId());
        }


        this.groupRepo.deleteById(id);

        logger.info("> remove id:{} ", id);

    }

    @Override
    @Cacheable(value="groupCache",unless = "#name != null")
    public Optional<Group> getByName(String name) {
        logger.info("< getByName name:{}",name);

        Optional<Group> group = groupRepo.findByName(name);

        logger.info("> getByName name:{}",name);

        return group;
    }

    @Override
    @CacheEvict(value="groupCache", allEntries = true)
    public void evictCache() {
        logger.info("< evict cache");
        logger.info("> evict cache");
    }
}
