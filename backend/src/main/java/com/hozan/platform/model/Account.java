package com.hozan.platform.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
import java.util.Collections;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Account  extends  AbstractEntity implements UserDetails {

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

    @JsonIgnore
    private boolean enabled = true;
    @JsonIgnore
    private boolean credentialsExpired = false;
    @JsonIgnore
    private boolean expired = false;
    @JsonIgnore
    private boolean locked = false;
    
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

    public void startFollowingGroup(Group group)
    {
        if(this.followingGroups.contains(group)){
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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.EMPTY_LIST;
    }

    @Override
    public boolean isAccountNonExpired() {
        return !this.expired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !this.locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !this.credentialsExpired;
    }

    @Override
    public boolean isEnabled (){
        return this.enabled;
    }
    @Override
    public String getUsername(){
        return this.username;
    }
    @Override
    public String getPassword(){
        return this.password;
    }


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
}
