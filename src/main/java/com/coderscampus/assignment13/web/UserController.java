package com.coderscampus.assignment13.web;

import com.coderscampus.assignment13.domain.User;
import com.coderscampus.assignment13.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping("/register")
	public String showRegistrationForm(Model model) {
		model.addAttribute("user", new User());
		return "register";
	}

	@PostMapping("/register")
	public String registerUser(User user) {
		userService.saveUser(user, true);
		return "redirect:/users";
	}

	@GetMapping("/users")
	public String getAllUsers(Model model) {
		model.addAttribute("users", userService.findAll());
		return "users";
	}

	@GetMapping("/users/{userId}")
	public String getUserDetails(@PathVariable Long userId, Model model) {
		User user = userService.findById(userId);
		if (user != null) {
			model.addAttribute("user", user);
			model.addAttribute("accounts", user.getAccounts());
			return "user-details";
		} else {
			return "error";
		}
	}

	@PostMapping("/users/{userId}")
	public String updateUser(@PathVariable Long userId, User updatedUser) {
		User existingUser = userService.findById(userId);

		if (updatedUser.getPassword() == null || updatedUser.getPassword().isEmpty()) {
			updatedUser.setPassword(existingUser.getPassword());
		}

		userService.saveUser(updatedUser, false);

		return "redirect:/users";
	}

	@PostMapping("/users/{userId}/delete")
	public String deleteUser(@PathVariable Long userId) {
		userService.delete(userId);
		return "redirect:/users";
	}
}