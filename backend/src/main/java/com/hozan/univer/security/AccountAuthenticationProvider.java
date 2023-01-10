//package com.hozan.univer.security;
//
//
//import com.hozan.univer.model.Account;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//
//import java.security.Principal;
//
///**
// * A Spring Security AuthenticationProvider which extends
// * <code>AbstractUserDetailsAuthenticationProvider</code>. This classes uses the
// * <code>AccountUserDetailsService</code> to retrieve a UserDetails instance.
// *
// * A PasswordEncoder compares the supplied authentication credentials to those
// * in the UserDetails.
// *
// */
//@Component
//public class AccountAuthenticationProvider
//        extends AbstractUserDetailsAuthenticationProvider {
//
//    private Logger logger = LoggerFactory.getLogger(this.getClass());
//
//    /**
//     * A Spring Security UserDetailsService implementation based upon the
//     * Account model model.
//     */
//    private UserDetailsServiceImpl userDetailsService;
//
//    /**
//     * A PasswordEncoder instance to hash clear test password values.
//     */
//    private PasswordEncoder passwordEncoder;
//
//    @Autowired
//    public void setUserDetailsService(UserDetailsServiceImpl userDetailsService) {
//        this.userDetailsService = userDetailsService;
//    }
//
//    @Autowired
//    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
//        this.passwordEncoder = passwordEncoder;
//    }
//
//    @Override
//    protected void additionalAuthenticationChecks(UserDetails userDetails,
//                                                  UsernamePasswordAuthenticationToken token)
//            throws AuthenticationException {
//        logger.debug("> additionalAuthenticationChecks");
//
//        if (token.getCredentials() == null
//                || userDetails.getPassword() == null) {
//            throw new BadCredentialsException("Credentials may not be null.");
//        }
//
//        if (!passwordEncoder.matches((String) token.getCredentials(),
//                userDetails.getPassword())) {
//            throw new BadCredentialsException("Invalid credentials.");
//        }
//
//        logger.debug("< additionalAuthenticationChecks");
//    }
//
//    @Override
//    protected UserDetails retrieveUser(String username,
//                                       UsernamePasswordAuthenticationToken token)
//            throws AuthenticationException {
//        logger.debug("> retrieveUser");
//
//        UserDetails userDetails = userDetailsService
//                .loadUserByUsername(username);
//
//        logger.debug("< retrieveUser");
//        return userDetails;
//    }
//
//}
//
