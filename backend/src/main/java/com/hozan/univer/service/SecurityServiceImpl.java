//package com.hozan.univer.service;
//
//import com.hozan.univer.model.Account;
//import com.hozan.univer.security.UserDetailsServiceImpl;
//import org.modelmapper.ModelMapper;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Service;
//
//import java.security.Principal;
//
//@Service
//public class SecurityServiceImpl implements SecurityService {
//    @Autowired
//    private AuthenticationProvider authenticationProvider;
//
//    @Autowired
//    private UserDetailsServiceImpl userDetailsService;
//
//    @Autowired
//    private ModelMapper modelMapper;
//
//    @Autowired
//    private AccountService accountService;
//
//    private static final Logger logger = LoggerFactory.getLogger(SecurityServiceImpl.class);
//
//    @Override
//    public String getLoggedInUsername() {
//        Object userDetails = SecurityContextHolder.getContext().getAuthentication().getDetails();
//        if (userDetails instanceof UserDetails) {
//            return ((UserDetails) userDetails).getUsername();
//        }
//return null; }
//
//    @Override
//    public Account getLoggedInUser() {
//        logger.debug("< getLoggedInUser");
//
//        String username = SecurityContextHolder.getContext().getAuthentication().getName();
//        Account account = accountService.getByUsername(username);
//
//
//
//        logger.debug("> getLoggedInUser");
//        return account;
//    }
//
//    @Override
//    public void autologin(String username, String password) {
//        logger.debug(String.format("> autologin %s ",username));
//        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
//
//        authenticationProvider.authenticate(usernamePasswordAuthenticationToken);
//
//        if (usernamePasswordAuthenticationToken.isAuthenticated()) {
//            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
//            logger.debug(String.format("Auto login %s successfully!", username));
//        }
//        logger.debug("< autologin ");
//    }
//}
