package com.hozan.platform.service;

import com.hozan.platform.model.File;

import java.io.InputStream;
import java.util.Optional;

public interface FileContentService {

    Optional<InputStream> getFileContent(File file);
    void saveFileContent(File file, InputStream inputStream);
    void deleteContent(File file);
}
