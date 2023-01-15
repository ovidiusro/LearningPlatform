package com.hozan.platform.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
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

    public Optional<File> getFileById(Long id){
        for (File f: files) {
           if(Objects.equals(f.getId(), id)){
               return Optional.of(f);
           }
        }
        return Optional.empty();
    }

    public void removeFileById(Long id){
        files.removeIf(f -> Objects.equals(f.getId(), id));
    }

    public void addFile(File file){
        this.files.add(file);
    }
}
