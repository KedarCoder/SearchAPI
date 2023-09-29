package com.example.crudDemo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.crudDemo.bean.User;
import com.example.crudDemo.repository.UserRepository;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@GetMapping("/info")
	public List<User> getUsers() {
		return userRepository.findAll();
	}

	@GetMapping("/{id}")
	public User getUserById(@PathVariable Long id) {
		return userRepository.findById(id);
	}

	@PostMapping("/addUser")
	public void addUser(@RequestBody User user) {
		userRepository.save(user);
	}

	@PutMapping("/{id}")
	public void updateUser(@PathVariable Long id, @RequestBody User user) {
		user.setId(id);
		userRepository.update(user);
	}

	@DeleteMapping("/{id}")
	public void deleteUser(@PathVariable Long id) {
		userRepository.delete(id);
	}

	@GetMapping("/getByMap/{id}")
	public User getUserByIdWithMap(@PathVariable Long id) {
		Map<String, Object> params = new HashMap<>();
		params.put("id", id);
		return userRepository.findByIdWithMap(params);
	}

	@PostMapping("/add")
	public void addUserWithMap(@RequestBody Map<String, String> request) {
		String name = request.get("name");
		Map<String, Object> params = new HashMap<>();
		params.put("name", name);
		userRepository.saveWithMap(request);
	}

}