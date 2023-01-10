package com.hozan.univer.service;

import com.hozan.univer.model.File;
import com.hozan.univer.repository.FileContentStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Optional;

@Service
public class FileContentServiceImpl implements FileContentService{

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private FileContentStore fileContentStore;

    @Override
    public Optional<InputStream> getFileContent(File file) {
        logger.info(" <> getFileContent name:{}",file.getName());

        return Optional.of(fileContentStore.getContent(file));
    }

    @Override
    public void saveFileContent(File file, InputStream inputStream) {
        logger.info(" <> saveFileContent name:{}",file.getName());
        fileContentStore.setContent(file,inputStream);
    }

    @Override
    public void deleteContent(File file) {
        logger.info(" <> delete name:{}",file.getName());
       fileContentStore.unsetContent(file);
    }
}
