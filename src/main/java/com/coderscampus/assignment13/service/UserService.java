package com.coderscampus.assignment13.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coderscampus.assignment13.domain.Account;
import com.coderscampus.assignment13.domain.Address;
import com.coderscampus.assignment13.domain.User;
import com.coderscampus.assignment13.repository.AccountRepository;
import com.coderscampus.assignment13.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private AccountRepository accountRepo;

	public List<User> findByUsername(String username) {
		return userRepo.findByUsername(username);
	}

	public List<User> findByNameAndUsername(String name, String username) {
		return userRepo.findByNameAndUsername(name, username);
	}

	public List<User> findByCreatedDateBetween(LocalDate date1, LocalDate date2) {
		return userRepo.findByCreatedDateBetween(date1, date2);
	}

	public User findExactlyOneUserByUsername(String username) {
		List<User> users = userRepo.findExactlyOneUserByUsername(username);
		if (users.size() > 0) {
			return users.get(0);
		} else {
			return new User();
		}
	}

	public Set<User> findAll() {
		return userRepo.findAllUsersWithAccountsAndAddresses();
	}

	public User findById(Long userId) {
		Optional<User> userOpt = userRepo.findById(userId);
		return userOpt.orElse(new User());
	}

	public User saveUser(User user, boolean createDefaultAccounts) {

		User existingUser = userRepo.findById(user.getUserId()).orElseThrow(() -> new IllegalArgumentException("User not found"));

		existingUser.setName(user.getName());
		existingUser.setUsername(user.getUsername());

		if (user.getPassword() != null && !user.getPassword().isEmpty()) {
			existingUser.setPassword(user.getPassword());
		}

		Address existingAddress = existingUser.getAddress();
		Address newAddress = user.getAddress();

		if (existingAddress != null && newAddress != null) {
			existingAddress.setAddressLine1(newAddress.getAddressLine1());
			existingAddress.setAddressLine2(newAddress.getAddressLine2());
			existingAddress.setCity(newAddress.getCity());
			existingAddress.setRegion(newAddress.getRegion());
			existingAddress.setCountry(newAddress.getCountry());
			existingAddress.setZipCode(newAddress.getZipCode());
		} else if (existingAddress == null && newAddress != null) {
			existingUser.setAddress(newAddress);
			newAddress.setUser(existingUser);
		}

		if (existingUser.getUserId() == null && createDefaultAccounts) {
			Account checking = new Account();
			checking.setAccountName("Checking Account");
			checking.getUsers().add(existingUser);

			Account savings = new Account();
			savings.setAccountName("Savings Account");
			savings.getUsers().add(existingUser);

			existingUser.getAccounts().add(checking);
			existingUser.getAccounts().add(savings);
			accountRepo.save(checking);
			accountRepo.save(savings);
		}

		return userRepo.save(existingUser);
	}

	public void delete(Long userId) {
		userRepo.deleteById(userId);
	}

	public void createNewAccount(Long userId) {
		Optional<User> userOpt = userRepo.findById(userId);

		if (userOpt.isPresent()) {
			User user = userOpt.get();

			Account newAccount = new Account();
			newAccount.setAccountName("New Account");
			newAccount.getUsers().add(user);
			user.getAccounts().add(newAccount);

			accountRepo.save(newAccount);
			userRepo.save(user);
		}
	}
}
