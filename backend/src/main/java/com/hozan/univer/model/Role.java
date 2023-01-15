package com.hozan.univer.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Role  extends AbstractEntity{

    private String code;
    private String label;

    @ManyToMany
    @JsonIgnore
    private Collection<Account> accounts = new ArrayList<>();

    public Role(String code, String label) {
        this.code = code;
        this.label = label;
    }

    public void addAccount(Account account)
    {
       if(this.accounts.contains(account)) {
           return;
       }

       this.accounts.add(account);
       account.addRole(this);
    }

    public void removeAccount(Account account){
        if(!this.accounts.contains(account)){
            return;
        }
        accounts.remove(account);
        account.removeRole(this);
    }

}