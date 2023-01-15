package com.hozan.univer.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.search.annotations.Field;
import org.springframework.content.commons.annotations.ContentId;
import org.springframework.content.commons.annotations.ContentLength;
import org.springframework.content.commons.annotations.MimeType;

import javax.persistence.Entity;
import java.util.Date;

@Entity
@Getter
@Setter
public class File extends AbstractEntity {

    private String name;

    private final Date created = new Date();

    private String summary;

    @ContentId
    private String contentId;
    @ContentLength
    private long contentLength;
    @MimeType
    private String mimeType = "text/plain";

}
