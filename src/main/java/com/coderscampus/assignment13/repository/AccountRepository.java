package com.coderscampus.assignment13.repository;

import com.coderscampus.assignment13.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}