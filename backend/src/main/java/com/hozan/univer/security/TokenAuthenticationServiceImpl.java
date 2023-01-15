package com.hozan.univer.security;

import com.hozan.univer.model.Account;
import com.hozan.univer.model.Role;
import com.hozan.univer.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class TokenAuthenticationServiceImpl implements UserAuthenticationService{

    @Autowired
    TokenService tokenService;

    @Autowired
    AccountService accountService;

    @Autowired
    private PasswordEncoder passwordEncoder;



    @Override
    public String login(final String username, final String password) {


        Optional<Account> userOp = accountService.getByUsername(username);

        if(!userOp.isPresent()){
            throw new BadCredentialsException("Account with this username is not found.");
        }
        Account user = userOp.get();

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Invalid credentials.");
        }


        Map<String,String > attributes = new HashMap <String, String>();
        StringBuilder roles = new StringBuilder();
        for(Role r: user.getRoles()){
            roles.append(r.getCode()).append(" ");
        }
        attributes.put( "id" , String.valueOf(user.getId()));
        attributes.put("username", user.getUsername());
        attributes.put("roles", roles.toString());


        String token = tokenService.expiring(attributes);
        tokenService.saveToken(user.getId(), token);

        return token;

    }

    @Override
    public Optional<Account> findByToken(final String token) {
        Map<String, String> claims = tokenService.verify(token);

        if(tokenService.containToken(Long.valueOf(claims.get("id")))) {
            return Optional.of(accountService.getByUsername(claims.get("username")).get());
        }
       return Optional.empty();
    }

    @Override
    public void logout(Account user) {
       tokenService.deleteToken(user.getId());
    }

}
