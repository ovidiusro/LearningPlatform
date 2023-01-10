package com.hozan.univer.model;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.Collection;

/**
 * The Role class is an model model object. A Role describes a privilege level
 * within the application. A Role is used to authorize an Account to access a
 * set of application resources.
 */

@Entity
public class Role  extends AbstractEntity{

    private String code;

    private String label;

    @ManyToMany
    @JsonIgnore
    private Collection<Account> accounts = new ArrayList<>();

    public Role() { }

    public Role(String code, String label) {
        this.code = code;
        this.label = label;
    }

    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getLabel() {
        return label;
    }
    public void setLabel(String label) {
        this.label = label;
    }

    //Accounts
    public Collection<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(Collection<Account> accounts) {
        for(Account a: accounts){
            addAccount(a);
        }
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
