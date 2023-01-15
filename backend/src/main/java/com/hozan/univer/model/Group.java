package com.hozan.univer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Entity
@Table(name = "Groupp")
@Indexed
@Getter
@Setter
@NoArgsConstructor
public class  Group extends  AbstractEntity{

    public interface GroupForm{}
    public interface ValidateName{}
    public interface ValidateShortDescription{}
    public interface ValidateLongDescription{}

    @ManyToOne
    private Account owner;

    @ManyToMany
    @JsonIgnore
    @LazyCollection(LazyCollectionOption.FALSE)
    private Collection<Account> followers = new ArrayList<>();

    @OneToMany
    @JsonIgnore
    @LazyCollection(LazyCollectionOption.FALSE)
    private Collection<Lesson> lessons = new ArrayList<>();

    @NotBlank(message = "Blank fields or empty are forbidden", groups = {GroupForm.class, ValidateName.class})
    @Size(min = 3, max = 50, message = "length requirements  of name: min = 3, max = 50 ",
            groups = {GroupForm.class, ValidateName.class})
    @Field(store = Store.NO)
    private String name;

    @NotBlank
    @NotBlank(message = "Blank fields or empty are forbidden", groups = {GroupForm.class,
            ValidateShortDescription.class})
    @Size(min = 3, max = 244, message = "length requirements of short description: min = 3, max = 244 ",
            groups = {GroupForm.class, ValidateShortDescription.class})
    @Field(store = Store.NO)
    private String shortDescription;

    @Lob
    @Column(length = 15000)
    @NotBlank(message = "Blank fields or empty are forbidden",
            groups = {GroupForm.class, ValidateLongDescription.class})
    @Size(min = 10, max = 15000, message = "length requirements of long description:  min = 10, max = 15000 ",
            groups = {GroupForm.class, ValidateLongDescription.class})
    @Field(store = Store.NO)
    private String longDescription;

    @ManyToOne
    private File banner;

    @ManyToOne
    private File cover;

    public Group(String name, String shortDescription) {
        this.name = name;
        this.shortDescription = shortDescription;
    }

    public void setOwner(Account owner) {
        if(this.owner != null){
            return;
        }
        this.owner = owner;
        owner.addOwnGroup(this);
    }

    public void removeOwner()
    {
        if(this.owner == null ){
            return;
        }
        owner.removeOwnGroup(this);
        owner = null;
    }

    public void addFollower(Account account)
    {
        if(followers.contains(account)) {
            return;
        }

        this.followers.add(account);
        account.startFollowingGroup(this);
    }

    public void removeFollower(Account account)
    {
        if(!followers.contains(account)){
            return;
        }
        followers.remove(account);
        account.stopFollowingGroup(this);
    }

    public void addLesson(Lesson lesson){
        this.lessons.add(lesson);
    }
    public Optional<Lesson> getLessonById(Long id){
        for (Lesson l: this.lessons ) {
            if(l.getId().equals(id)) {
                return Optional.of(l);
            }
        }
        return Optional.empty();
    }
    public void removeLessonById(Long id){
        lessons.removeIf(l -> l.getId().equals(id));
    }

}
