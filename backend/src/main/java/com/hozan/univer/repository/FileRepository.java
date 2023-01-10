package com.hozan.univer.repository;

import com.hozan.univer.model.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;


@RepositoryRestResource(path="files", collectionResourceRel="files")
public interface FileRepository extends JpaRepository<File, Long> {

    Optional<File> findByName(String name);
    Optional<File> findById(Long id);

}
