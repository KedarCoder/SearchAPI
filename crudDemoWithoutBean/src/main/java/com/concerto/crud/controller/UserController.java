package com.concerto.crud.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import com.concerto.crud.config.bean.User;
import com.concerto.crud.dao.UserDAO;
import com.concerto.crud.service.CommonService;
import com.concerto.crud.service.JsonToJavaConverter;

import java.util.Map;

/**
 * Copyright (C) Concerto Software and Systems (P) LTD | All Rights Reserved
 * 
 * @File : com.concerto.crud.dao.impl.UserDAOImpl.java
 * @Author : Suyog Kedar
 * @AddedDate : October 03, 2023 12:30:40 PM
 * @ModifiedBy :
 * @ModifiedDate : Bean controller
 * @Purpose : Converting JSON to String
 * @Version : 1.0
 */
@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	UserDAO userDAO;

	@Autowired
	CommonService commonService;

	//Create
	@PostMapping("/Create/{moduleName}")
	public ResponseEntity<String> create(@RequestBody Map<String, Object> user, @PathVariable String moduleName) {
		if (JsonToJavaConverter.getModuleMap().containsKey(moduleName)) {
			commonService.jsonMapperCreate(user, moduleName);
			return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully.");
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module not found.");
		}
	}

	// Delete
	@DeleteMapping("/delete/{moduleName}/{userId}")
	public ResponseEntity<String> deleteUser(@PathVariable int userId, @PathVariable String moduleName) {
		if (JsonToJavaConverter.getModuleMap().containsKey(moduleName)) {
			userDAO.deleteUser(userId);
			return ResponseEntity.ok("Deleted successfully.");
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Please check the URL.");
		}
	}

	@GetMapping("/getByName/{moduleName}/{name}")
	public ResponseEntity<Object> getByName(@PathVariable String name, @PathVariable String moduleName) {
		User user = userDAO.getUserByName(name);
		if (user != null) {
			return ResponseEntity.ok(user);
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
		}
	}

	@PutMapping("/update/{moduleName}/{id}")
	public ResponseEntity<String> update(@RequestBody Map<String, Object> user, @PathVariable String moduleName,
			@PathVariable int id) {

		if (JsonToJavaConverter.getModuleMap().containsKey(moduleName)) {
			commonService.jsonMapperUpdate(user, moduleName, id);
			return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully.");
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module not found.");
		}
	}

}
