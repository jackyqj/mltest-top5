package com.jacky.backend.testtop5list.security;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import com.jacky.backend.testtop5list.entity.Account;

/**
 * @author Jacky Zhang
 * The configuration class for web security
 */
@Configuration
@EnableWebSecurity
@EnableScheduling
@EnableAsync
@EnableGlobalMethodSecurity(prePostEnabled = true)
class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final static Logger LOGGER = Logger.getLogger(WebSecurityConfiguration.class.getName());

    @Autowired
    private SpringDataJpaUserDetailsService userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(this.userDetailsService).passwordEncoder(Account.PASSWORD_ENCODER);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        LOGGER.fine("configuring the http login settings...");
        http
                .authorizeRequests()
                .antMatchers("/resources/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .defaultSuccessUrl("/index.html", true)
                .permitAll()
                .and()
                .httpBasic()
                .and().csrf().disable()
                .logout()
                .logoutSuccessUrl("/login")
                .permitAll();
    }

}