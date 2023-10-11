package com.concerto.crud.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import com.concerto.crud.dao.UserDAO;
import com.concerto.crud.service.CommonService;
import com.concerto.crud.service.JsonToJavaConverter;

import java.util.List;
import java.util.Map;

/**
 * Copyright (C) Concerto Software and Systems (P) LTD | All Rights Reserved
 * 
 * @File : com.concerto.crud.dao.impl.UserDAOImpl.java
 * @Author : Suyog Kedar
 * @AddedDate : October 03, 2023 12:30:40 PM
 * @ModifiedBy :
 * @ModifiedDate : Bean controller
 * @Purpose : Controller
 * @Version : 1.0
 */
@RestController
@RequestMapping("/users")
public class UserController {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	UserDAO userDAO;

	@Autowired
	CommonService commonService;

	// Create
	@PostMapping("/Create/{moduleName}")
	public ResponseEntity<String> create(@RequestBody Map<String, Object> user, @PathVariable String moduleName) {
		if (JsonToJavaConverter.getModuleMap().containsKey(moduleName)) {
			try {
				commonService.jsonMapperCreate(user, moduleName);
			} catch (Exception e) {
				logger.error("Error found while Saveing data ", e);
			}
			return ResponseEntity.status(HttpStatus.CREATED).body("Created successfully.");
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module not found.");
		}
	}

	@PutMapping("/update/{moduleName}/{id}")
	public ResponseEntity<String> update(@RequestBody Map<String, Object> user, @PathVariable String moduleName,
			@PathVariable int id) {

		if (JsonToJavaConverter.getModuleMap().containsKey(moduleName)) {
			try {
				commonService.jsonMapperUpdate(user, moduleName, id);
			} catch (Exception e) {
				logger.error("An error occurred during the update process.", e);
			}
			return ResponseEntity.status(HttpStatus.CREATED).body("Updated successfully.");
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module not found.");
		}
	}

	// read
	// http://localhost:8080/users/readData/City/Mumbai?moduleName=Student
	@GetMapping("/readData/{moduleName}/{fieldName}/{value}")
	public List<Map<String, Object>> readData(@PathVariable String fieldName, @PathVariable Object value,
			@PathVariable String moduleName) throws Exception {
		return commonService.jsonDynamicRead(fieldName, value, moduleName);
	}

	// Delete
	@DeleteMapping("/delete/{moduleName}/{id}")
	public ResponseEntity<String> deleteUser(@PathVariable int id, @PathVariable String moduleName) {
		if (JsonToJavaConverter.getModuleMap().containsKey(moduleName)) {
			commonService.jsonMapperDelete(id, moduleName);
			return ResponseEntity.ok("Deleted successfully.");
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Please check the URL.");
		}
	}

}
