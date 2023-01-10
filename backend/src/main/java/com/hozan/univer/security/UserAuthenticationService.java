package com.hozan.univer.security;


import com.hozan.univer.model.Account;

import java.util.Optional;

public interface UserAuthenticationService {

    String login(String username, String password);
    Optional<Account> findByToken(String token);
    void logout(Account user);
}
