package com.hozan.platform.security;


import com.hozan.platform.model.Account;

import java.util.Optional;

public interface UserAuthenticationService {

    String login(String username, String password);
    Optional<Account> findByToken(String token);
    void logout(Account user);
}
