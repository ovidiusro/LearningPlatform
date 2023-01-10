package com.hozan.univer.repository;

import com.hozan.univer.model.File;
import org.springframework.content.commons.repository.ContentStore;
import org.springframework.content.commons.search.Searchable;
import org.springframework.content.rest.StoreRestResource;
import org.springframework.stereotype.Component;

@StoreRestResource
@Component
public interface FileContentStore extends ContentStore<File, String>, Searchable<String> {
}
