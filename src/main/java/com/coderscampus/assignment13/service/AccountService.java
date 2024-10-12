package com.coderscampus.assignment13.service;

import com.coderscampus.assignment13.domain.Account;
import com.coderscampus.assignment13.domain.User;
import com.coderscampus.assignment13.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserService userService;

    public Account findById(Long accountId) {
        return accountRepository.findById(accountId).orElse(null);
    }

    public Account save(Account account) {
        return accountRepository.save(account);
    }

    public Account createNewAccountForUser(User user) {
        Account newAccount = new Account();
        newAccount.setAccountName("New Account");

        newAccount = accountRepository.save(newAccount);

        newAccount.getUsers().add(user);
        user.getAccounts().add(newAccount);

        userService.saveUser(user, false);

        return newAccount;
    }
}