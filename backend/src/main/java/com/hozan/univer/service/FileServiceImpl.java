package com.hozan.univer.service;

import com.hozan.univer.model.File;
import com.hozan.univer.repository.FileRepository;
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
public class FileServiceImpl implements FileService{

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private FileRepository fileRepo;

    @Override
    public Optional<File> getByName(String name){
       return fileRepo.findByName(name);
    }

    @Override
    public Collection<File> getAll() {
       return fileRepo.findAll();
    }

    @Override
    public Optional<File> getById(Long id) {
        return fileRepo.findById(id);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public Optional<File> create(File file) {
        logger.info("< create");

        if (file.getId() != null) {
            logger.error(
                    "Attempted to create a file, but id attribute was not null.");
            throw new EntityExistsException(
                    "The id attribute must be null to persist a new model.");
        }

        Optional<File> newFile = Optional.of(this.fileRepo.save(file));


        logger.info("> create ");
        return newFile;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public Optional<File> update(File file) {
        logger.info("< update id:{} ", file.getId());

        Optional<File> fileToUpdate = fileRepo.findById(file.getId());
        if(!fileToUpdate.isPresent()){
            logger.error( "Attempted to update a file, but the model does not exist.");
            throw new NoResultException("Requested model not found.");
        }

        fileToUpdate = Optional.of(fileRepo.save(file));

        logger.info("> update id:{} ", file.getId());
        return fileToUpdate;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void     remove(Long id){
        fileRepo.deleteById(id);
    }
}
