//package com.hozan.univer.configuration;
//
//import com.hozan.univer.security.AccountAuthenticationProvider;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.annotation.Order;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.builders.WebSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.config.web.server.ServerHttpSecurity;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.security.web.server.SecurityWebFilterChain;
//import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter;
//import org.springframework.security.web.session.SessionManagementFilter;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
//
///**
// * The SecurityConfiguration class provides a centralized location for
// * application security configuration. This class bootstraps the Spring Security
// * components during application startup.
// *
// */
//@Configuration
//@EnableWebSecurity
//public class SecurityConfiguration {
//
//    /**
//     * The AccountAuthenticationProvider security component.
//     */
//    @Autowired
//    private AccountAuthenticationProvider accountAuthenticationProvider;
//
//
//    /**
//     * Supplies a PasswordEncoder instance to the Spring ApplicationContext. The
//     * PasswordEncoder is used by the AuthenticationProvider to perform one-way
//     * hash operations on passwords for credential comparison.
//     *
//     * @return A PasswordEncoder.
//     */
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    /**
//     * process authentication requests.
//     *
//     * @param auth An AuthenticationManagerBuilder instance used to construct
//     *        the AuthenticationProvider.
//     */
//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth) {
//        auth.authenticationProvider(accountAuthenticationProvider);
//    }
//
//    /**
//     * This inner class configures a WebSecurityConfigurerAdapter instance for
//     * the web service API context paths.
//     */
//    @Configuration
//    @Order(1)
//    public static class ApiWebSecurityConfigurerAdapter
//            extends WebSecurityConfigurerAdapter {
//
//        TokenAuthenticationProvider provider;
//
//
//
//        @Override
//        protected void configure(HttpSecurity http) throws Exception {
//
//            // @formatter:off
//           // http.csrf().disable()
//
//                    /*
//                    .addFilterBefore(corsFilter(), SessionManagementFilter.class) //adds your custom CorsFilter
//                    .csrf() .disable()
//                    .sessionManagement() .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                    .and()
//                    .antMatcher("/api/**")
//                    .authorizeRequests()
//                   // .anyRequest().permitAll()
//                    .anyRequest().hasRole("USER")
//                    .and()
//                    //.formLogin().loginPage("/sing-in")
//                    //.and()
//                    .httpBasic();
//                    */
//
//            // @formatter:on
//
//        }
//
//    }
//
//    /**
//     * This inner class configures a WebSecurityConfigurerAdapter instance for
//     * the Spring Actuator web service context paths.
//     */
//
//    @Configuration
//    @Order(2)
//    public static class ActuatorWebSecurityConfigurerAdapter
//            extends WebSecurityConfigurerAdapter {
//
//        @Override
//        protected void configure(HttpSecurity http) throws Exception {
//
//            // http.requestMatcher(EndpointRequest.toAnyEndpoint()).authorizeRequests()
//			//.anyRequest().permitAll()
//
//            // @formatter:off
//
//            http
//                    .antMatcher("/actuator/**")
//                    .authorizeRequests()
//                    .anyRequest().hasRole("SYSADMIN")
//                    .and()
//                    .httpBasic()
//                    .and()
//                    .sessionManagement()
//                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//
//            // @formatter:on
//
//        }
//
//    }
//
//}
package com.hozan.univer.configuration;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private static final RequestMatcher PUBLIC_URLS = new OrRequestMatcher(
            new AntPathRequestMatcher("/api/account/sign-in"),
            new AntPathRequestMatcher("/api/account/sign-up"),
            new AntPathRequestMatcher("/api/account/email/*"),
            new AntPathRequestMatcher("/api/account/username/*"),


            new AntPathRequestMatcher("/api/files/*"),
            new AntPathRequestMatcher("/api/files/**"),

            new AntPathRequestMatcher("/api/group/all"),
            new AntPathRequestMatcher("/api/group/*"),
            new AntPathRequestMatcher("/api/group/*/owner"),
            new AntPathRequestMatcher("/api/group/*/followers")
    );
    private static final RequestMatcher PROTECTED_URLS = new NegatedRequestMatcher(PUBLIC_URLS);

    @Autowired
    TokenAuthenticationProvider provider;


    @Override
    protected void configure(final AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(provider);
    }

    @Override
    public void configure(final WebSecurity web) {
        web.ignoring().requestMatchers(PUBLIC_URLS);
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {

        http.
                httpBasic()
                .and()
                .cors()
                .and()
                .sessionManagement().sessionCreationPolicy(STATELESS)
                .and()
                .authenticationProvider(provider)
                .addFilterBefore(restAuthenticationFilter(), AnonymousAuthenticationFilter.class)
                .csrf().disable()
                .logout().disable()
                .formLogin().disable();
    }




    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    TokenAuthenticationFilter restAuthenticationFilter() throws Exception {
        final TokenAuthenticationFilter filter = new TokenAuthenticationFilter(PROTECTED_URLS);
        filter.setAuthenticationManager(authenticationManager());
        filter.setAuthenticationSuccessHandler(successHandler());
        return filter;
    }

    @Bean
    SimpleUrlAuthenticationSuccessHandler successHandler() {
        final SimpleUrlAuthenticationSuccessHandler successHandler = new SimpleUrlAuthenticationSuccessHandler();
        successHandler.setRedirectStrategy(new NoRedirectStrategy());
        return successHandler;
    }


}