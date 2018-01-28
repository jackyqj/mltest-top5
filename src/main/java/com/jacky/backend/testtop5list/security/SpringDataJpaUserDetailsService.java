package com.jacky.backend.testtop5list.security;

import com.jacky.backend.testtop5list.dao.AccountRepository;
import com.jacky.backend.testtop5list.entity.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * @author Jacky Zhang
 */
@Component
public class SpringDataJpaUserDetailsService implements UserDetailsService {

    private final AccountRepository repository;

    @Autowired
    public SpringDataJpaUserDetailsService(AccountRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Account usr = repository.findByUsername(username).get();
        return new User(
                usr.username, usr.password, true, true, true, true,
                AuthorityUtils.createAuthorityList("USER", "write"));
    }

}
