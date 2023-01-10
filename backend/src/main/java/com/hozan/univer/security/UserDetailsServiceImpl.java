//package com.hozan.univer.security;
//
//import java.util.ArrayList;
//import java.util.Collection;
//
//import com.hozan.univer.model.Account;
//import com.hozan.univer.model.Role;
//import com.hozan.univer.service.AccountService;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
///**
// * A Spring Security UserDetailsService implementation which creates UserDetails
// * objects from the Account and Role entities.
// */
//@Service
//public class UserDetailsServiceImpl implements UserDetailsService {
//
//    private Logger logger = LoggerFactory.getLogger(this.getClass());
//
//    private final AccountService accountService;
//
//    @Autowired
//    public UserDetailsServiceImpl(AccountService accountService) {
//        this.accountService = accountService;
//    }
//
//    @Override
//    public UserDetails loadUserByUsername(String username)
//            throws UsernameNotFoundException {
//        logger.debug("> loadUserByUsername {}", username);
//
//        Account account = accountService.getByUsername(username);
//        if (account == null) {
//            // Not found...
//            throw new UsernameNotFoundException(
//                    "User " + username + " not found.");
//        }
//
//        if (account.getRoles() == null || account.getRoles().isEmpty()) {
//            // No Roles assigned to user...
//            throw new UsernameNotFoundException("User not authorized.");
//        }
//
//        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
//        for (Role role : account.getRoles()) {
//            grantedAuthorities.add(new SimpleGrantedAuthority(role.getCode()));
//        }
//
//        User userDetails = new User(account.getUsername(),
//                account.getPassword(), account.isEnabled(),
//                !account.isExpired(), !account.isCredentialsExpired(),
//                !account.isLocked(), grantedAuthorities);
//
//        logger.debug("< loadUserByUsername {}", username);
//        return userDetails;
//    }
//
//}
