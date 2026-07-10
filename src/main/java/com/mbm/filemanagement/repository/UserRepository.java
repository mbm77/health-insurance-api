package com.mbm.filemanagement.repository;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.mbm.filemanagement.model.User;

@Repository
public class UserRepository {

	private final Map<String, User> users = new HashMap<>();

	public User save(User user) {

		users.put(user.getUsername(), user);
		return user;
	}

	public User findByUsername(String username) {

		return users.get(username);
	}

	public boolean existsByUsername(String username) {

		return users.containsKey(username);
	}
	
	

}