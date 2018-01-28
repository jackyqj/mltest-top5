package com.jacky.backend.testtop5list.dao;

import com.jacky.backend.testtop5list.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
/**
 * @author Jacky Zhang
 * Simple DAO class for account manangement
 */

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByUsername(String username);
}
