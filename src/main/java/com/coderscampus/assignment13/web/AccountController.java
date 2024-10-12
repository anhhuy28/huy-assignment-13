package com.coderscampus.assignment13.web;

import com.coderscampus.assignment13.domain.Account;
import com.coderscampus.assignment13.domain.User;
import com.coderscampus.assignment13.service.AccountService;
import com.coderscampus.assignment13.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AccountController {

    @Autowired
    private UserService userService;

    @Autowired
    private AccountService accountService;

    @PostMapping("/users/{userId}/accounts")
    public String createNewAccount(@PathVariable Long userId) {
        User user = userService.findById(userId);
        if (user != null) {
            // Create a new account and return its ID
            Account newAccount = accountService.createNewAccountForUser(user);
            // Redirect to the account editing page to allow user to set account name
            return "redirect:/users/" + userId + "/accounts/" + newAccount.getAccountId() + "/edit";
        }
        return "redirect:/users/" + userId;  // Fallback to user details page if something goes wrong
    }

    @GetMapping("/users/{userId}/accounts/{accountId}/edit")
    public String editAccountName(@PathVariable Long userId, @PathVariable Long accountId, Model model) {
        User user = userService.findById(userId);
        Account account = accountService.findById(accountId);

        if (user != null && account != null) {
            model.addAttribute("user", user);       // Adding user object to the model
            model.addAttribute("account", account); // Adding account object to the model
            return "new-account";  // Display the form for setting the account name
        } else {
            return "error";  // Handle case where user or account is not found
        }
    }

    // Handle form submission to update the account name
    @PostMapping("/users/{userId}/accounts/{accountId}/edit")
    public String updateAccountName(@PathVariable Long userId, @PathVariable Long accountId, Account account) {
        Account existingAccount = accountService.findById(accountId);

        if (existingAccount != null) {
            existingAccount.setAccountName(account.getAccountName());
            accountService.save(existingAccount);
        }

        return "redirect:/users/" + userId;
    }
}