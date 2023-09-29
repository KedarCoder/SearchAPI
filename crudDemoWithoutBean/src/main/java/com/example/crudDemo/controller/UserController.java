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

import com.example.crudDemo.repository.UserRepository;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserRepository userRepository;

//	@PostMapping("/addUser")
//	public void addUser(@RequestBody Map<String, Object> userMap) {
//		userRepository.saveWithMap(userMap);
//	}

	
	@PostMapping("/addUser")
	public void addUser(@RequestBody Map<String, Object> userParams) {
	    // Extract parameters from the map
	    String name = (String) userParams.get("name");
	    String city = (String) userParams.get("city");
	    String ageString = (String) userParams.get("age");
	    String gender = (String) userParams.get("gender");

	    // Process the parameters as needed and save to the repository
	    Map<String, Object> params = new HashMap<>();
	    params.put("name", name);
	    params.put("city", city);
	    params.put("age", ageString);
	    params.put("gender", gender);

	    userRepository.saveWithMap(params);
	}

	@PutMapping("/{id}")
	public void updateUser(@PathVariable Long id, @RequestBody Map<String, Object> userMap) {
		userMap.put("id", id);
		userRepository.updateWithMap(userMap);
	}

}