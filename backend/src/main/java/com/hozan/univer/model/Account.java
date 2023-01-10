package com.hozan.univer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@Entity
public class Account  extends  AbstractEntity implements UserDetails {

    public interface SignUpForm{}
    public interface SignInForm{}
    public interface ValidateName{}
    public interface ValidateUsername{}
    public interface ValidatePassword{}
    public interface ValidateEmail{}
    public interface ValidateProfession{}
    public interface ValidateAge{}
    public interface ValidateTeachAt{}
    public interface ValidateStudyAt{}
    public interface ValidateDescription{}

    @NotBlank(message = "Blank fields empty are forbidden", groups = {SignUpForm.class, ValidateName.class})
    @Size(min = 3, max = 50,message = "length requirements of name:  min = 3, max = 50 ",
            groups = {SignUpForm.class, ValidateName.class})
    private String name;

    @NotBlank(message = "Blank fields or empty are forbidden", groups = {SignInForm.class, SignUpForm.class, ValidateUsername.class})
    @Size(min = 3, max = 50, message = "length requirements of username:  min = 3, max = 50 ",
            groups = {SignUpForm.class,SignInForm.class, ValidateUsername.class})
    private String username;

    @NotBlank(message = "Blank fields or empty are forbidden",groups = {SignInForm.class, SignUpForm.class, ValidatePassword.class})
    @Size(min = 3, max = 50, message = "length requirements of password: min = 3, max = 50 ",
            groups = {SignUpForm.class,SignInForm.class, ValidatePassword.class})
    private String password;

    @NotBlank(message = "Blank fields or empty are forbidden", groups = {SignUpForm.class, ValidateEmail.class})
    @Size(min = 3, max = 50, message = "length requirements of email: min = 3, max = 50 ",
            groups = {SignUpForm.class, ValidateEmail.class})
    @Pattern(regexp = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"" +
            "(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")" +
            "@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]" +
            "?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-" +
            "\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])"
    , groups = {SignUpForm.class, ValidateEmail.class})
    private String email;

    @NotBlank(message = "Blank or empty fields are forbidden", groups = ValidateProfession.class)
    @Size(min = 3, max = 50, message = "length requirements of profession: min = 3, max = 50 ",
            groups = {ValidateProfession.class})
    private String profession;

    @NotBlank(message = "Blank or empty fields are forbidden", groups = ValidateAge.class)
    @Pattern(regexp="^(0|[1-9][0-9]*)$", groups = ValidateAge.class)
    private String age;

    @NotBlank(message = "Blank or empty fields are forbidden", groups = ValidateTeachAt.class)
    @Size(min = 3, max = 50, message = "length requirements of 'Teach at: min = 3, max = 50 ",
            groups = { ValidateTeachAt.class})
    private String  teachAt;

    @NotBlank(message = "Blank or empty fields are forbidden", groups = ValidateStudyAt.class)
    @Size(min = 3, max = 50, message = "length requirements of 'Study at': min = 3, max = 50 ",
            groups = { ValidateTeachAt.class})
    private String  studyAt;

    @Lob
    @Column(length = 1000)
    @NotBlank(message = "Blank fields or empty are forbidden", groups = {ValidateDescription.class})
    @Size(min = 10, max = 1000, message = "length requirements of  description:  min = 10, max = 1000 ", groups = {ValidateDescription.class})
    private String description;



    @OneToMany (mappedBy = "owner",  orphanRemoval = true)
    @JsonIgnore
    @LazyCollection(LazyCollectionOption.FALSE)
    private Collection<Group> ownGroups = new ArrayList<>();

    @ManyToMany(mappedBy = "followers")
    @LazyCollection(LazyCollectionOption.FALSE)
    @JsonIgnore
    private Collection<Group> followingGroups = new ArrayList<>();

    @ManyToMany(mappedBy = "accounts",fetch = FetchType.EAGER)
    private Collection<Role> roles = new ArrayList<>();

    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    @ManyToOne
    private File avatar;

    private boolean enabled = true;
    private boolean credentialsExpired = false;
    private boolean expired = false;
    private boolean locked = false;
    

    public Account() { }

    public Account(String name, String email, String username, String password, Date creationDate) {
        this.name = name;
        this.email = email;
        this.username = username;
        this.password = password;
        this.creationDate = creationDate;
    }

    public Account( Long id, String username, String password, Date creationDate) {
        setId(id);
        this.username = username;
        this.password = password;
        this.creationDate = creationDate;
    }


    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<>();
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public boolean isCredentialsExpired() { return credentialsExpired; }
    public void setCredentialsExpired(boolean credentialsExpired) { this.credentialsExpired = credentialsExpired; }
    public boolean isExpired() { return expired; }
    public void setExpired(boolean expired) { this.expired = expired; }
    public boolean isLocked() { return locked; }
    public void setLocked(boolean locked) { this.locked = locked; }
    public Date getCreationDate() { return creationDate; }
    public void setCreationDate(Date creationDate) { this.creationDate = creationDate; }

    //Roles
    public Collection<Role> getRoles() { return roles; }

    public void setRoles(Collection<Role> roles) {
        for(Role r: roles){
            addRole(r);
        }
    }

    public void addRole(Role role) {
        if(this.roles.contains(role)){
            return;
        }
        this.roles.add(role);
        role.addAccount(this);
    }
    public void removeRole(Role role){
        if(!this.roles.contains(role)){
            return;
        }
        this.roles.remove(role);
        role.removeAccount(this);
    }


    // Own groups
    public Collection<Group> getOwnGroups() { return ownGroups; }

    public void setOwnGroups(Collection<Group> groups){
        for(Group g:groups){
            addOwnGroup(g);
        }
    }

    public void addOwnGroup(Group ownGroup)
    {
        if(this.ownGroups.contains(ownGroup)) {
            return;
        }

       this.ownGroups.add(ownGroup);
        ownGroup.setOwner(this);
    }

    public void removeOwnGroup(Group ownGroup){
       if(!this.ownGroups.contains(ownGroup)) {
           return;
       }
       this.ownGroups.remove(ownGroup);
       ownGroup.setOwner(null);
    }

    //Following groups
    public Collection<Group> getFollowingGroups() {
        return followingGroups;
    }

    public void setFollowingGroups(Collection<Group> groups){

        for(Group g: groups){
            startFollowingGroup(g);
        }

    }
    public void startFollowingGroup(Group group)
    {
        if(followingGroups.contains(group)){
            return;
        }

        this.followingGroups.add(group);
        group.addFollower(this);
    }

    public void stopFollowingGroup(Group group){

        if(!followingGroups.contains(group)){
            return;
        }
        followingGroups.remove(group);
         group.removeFollower(this);
    }

    public File getAvatar() {
        return avatar;
    }

    public void setAvatar(File avatar) {
        this.avatar = avatar;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getTeachAt() {
        return teachAt;
    }

    public void setTeachAt(String teachAt) {
        this.teachAt = teachAt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStudyAt() {
        return studyAt;
    }

    public void setStudyAt(String studyAt) {
        this.studyAt = studyAt;
    }
}