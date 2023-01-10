package com.hozan.univer.model;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.*;

@Entity
public class Lesson extends AbstractEntity{

    public interface LessonForm{};
    public interface ValidateBody{};
    public interface ValidateShortDescription{};
    public interface ValidateTitle{};
    public interface ValidateOrderNumber{};




    @NotBlank(message = "Blank fields or empty are forbidden", groups = {ValidateTitle.class, LessonForm.class})
    @Size(min = 3, max = 100, message = "length requirements of short description: min = 3, max = 100 ", groups = {ValidateTitle.class, LessonForm.class})
    private String title;

    @Positive(message = "Your age must be positive integer", groups = {ValidateOrderNumber.class,LessonForm.class})
    private int orderNumber;


    @Lob
    @Column(length = 50000)
    @NotBlank( message = "Blank fields or empty are forbidden", groups = {ValidateBody.class,LessonForm.class})
    @Size(min = 10, max = 50000,message = "length requirements  min = 3, max = 50000 ", groups = {ValidateBody.class, LessonForm.class})
    private String body;

    @Lob
    @Column(length = 1000)
    @NotBlank( message = "Blank fields or empty are forbidden", groups = {ValidateShortDescription.class,LessonForm.class})
    @Size(min = 3, max = 1000, message = "length requirements of short description: min = 3, max = 1000 ", groups = {ValidateShortDescription.class, LessonForm.class})
    private String shortDescription;


    @ManyToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<File> files;

    @ManyToOne
    private File video;

    private Date created = new Date();
    public Lesson(){}

    public void setFiles(List<File> files) {
        this.files = files;
    }
    public Collection<File> getFiles(){
       return this.files;
    }
    public void addFile(File file){
        this.files.add(file);
    }

    public Optional<File> getFileById(Long id){
        for (File f: files) {
           if(f.getId() == id){
               return Optional.of(f);
           }
        }
        return Optional.empty();
    }

    public void removeFileById(Long id){
        for (Iterator<File> iter = files.iterator(); iter.hasNext(); ) {
            File f = iter.next();
            if (f.getId() == id) {
                iter.remove();
            }
        }
        return;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getBody() {
        return body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Date getCreated() {
        return created;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public File getVideo() {
        return video;
    }

    public void setVideo(File video) {
        this.video = video;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
