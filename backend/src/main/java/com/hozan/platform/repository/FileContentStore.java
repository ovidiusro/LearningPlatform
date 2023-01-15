package com.hozan.platform.repository;

import com.hozan.platform.model.File;
import org.springframework.content.commons.repository.ContentStore;
import org.springframework.content.commons.search.Searchable;
import org.springframework.content.rest.StoreRestResource;
import org.springframework.stereotype.Component;

@StoreRestResource
@Component
public interface FileContentStore extends ContentStore<File, String>, Searchable<String> {
}
