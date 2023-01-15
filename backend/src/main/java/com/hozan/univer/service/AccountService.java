package com.hozan.univer.service;

import com.hozan.univer.model.Account;

import java.util.Collection;
import java.util.Optional;

public interface AccountService {

    Optional<Collection<Account>> getAll();
    Optional<Account>       getById(Long id);
    Optional<Account>       create(Account account);
    Optional<Account>       update(Account account);
    void        remove(Long id);
    void        evictCache();

    Optional<Account>       getByUsername(String usernme);
    Optional<Account>       getByEmail(String usernme);
}
