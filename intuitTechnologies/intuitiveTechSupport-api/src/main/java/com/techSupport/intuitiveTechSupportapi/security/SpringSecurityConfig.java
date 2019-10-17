package com.techSupport.intuitiveTechSupportapi.security;

import com.techSupport.intuitiveTechSupportapi.appConstants.Constants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value(Constants.securityUSERusername)
    private String USER_username;

    @Value(Constants.securityUSERpaassword)
    private String USER_password;

    @Value(Constants.securityADMINusername)
    private String ADMIN_username;

    @Value(Constants.securityADMINpassword)
    private String ADMIN_password;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.inMemoryAuthentication()
                .withUser(USER_username).password("{noop}"+USER_password).roles("USER")
                .and()
                .withUser(ADMIN_username).password("{noop}"+ADMIN_password).roles("USER", "ADMIN");

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                //HTTP Basic authentication
                .httpBasic()
                .and()
                .authorizeRequests()
                .antMatchers("/techsupport/customer/**").hasRole("USER")
                .antMatchers("/techsupport/slots/**").hasRole("USER")
                .antMatchers( "/techsupport/**").hasRole("ADMIN")
                .and()
                .csrf().disable()
                .formLogin().disable();
    }
}
