package com.concerto.crud.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import com.concerto.crud.config.bean.CrudInterface;
import com.concerto.crud.config.bean.User;
import com.concerto.crud.dao.UserDAO;
import com.concerto.crud.service.CommonService;
import com.concerto.crud.service.JsonToJavaConverter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Collections;
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
	
	private static final Logger logger = LoggerFactory.getLogger(JsonToJavaConverter.class);


	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	UserDAO userDAO;

	@Autowired
	CommonService commonService;

	@PostMapping("/getModuleData/{moduleName}") // DONE
	public void creatrUser(@RequestBody Map<String, Object> user, @PathVariable String moduleName) {

		if (JsonToJavaConverter.getModuleMap().containsKey(moduleName)) {

			commonService.jsonMapper(user, moduleName);

		} else {
			Collections.emptyList();
		}

	}

	// Delete
	@DeleteMapping("/delete/{moduleName}/{userId}") // Done
	public void deleteUser(@PathVariable int userId, @PathVariable String moduleName) {

		if (JsonToJavaConverter.getModuleMap().containsKey(moduleName)) {

			userDAO.deleteUser(userId);

		} else {
			Collections.emptyList();
		}

	}

	// http://localhost:8080/users/getUserByName/User/Gayatri Hande
	@GetMapping("/getUserByName/{moduleName}/{name}")
	public ResponseEntity<User> getUserByName(@PathVariable String name, @PathVariable String moduleName) {

		User user = userDAO.getUserByName(name);

		if (user != null) {
			return ResponseEntity.ok(user);
		} else {
			return ResponseEntity.notFound().build();
		}

	}

	@PutMapping("/updateUser/{moduleName}/{id}")
	public void updateUser(@RequestBody User user, @PathVariable String moduleName, @PathVariable int id) {

		if (JsonToJavaConverter.getModuleMap().containsKey(moduleName)) {

			ObjectMapper objectMapper = new ObjectMapper();
			String json;

			try {
				json = objectMapper.writeValueAsString(user);

				CrudInterface clazz = (CrudInterface) JsonToJavaConverter.getModuleNameMap().get(moduleName);
				clazz = objectMapper.readValue(json, clazz.getClass());

				userDAO.updateUser(clazz, id);
			} catch (JsonProcessingException e) {
				logger.warn("Module not found for moduleName: {}", e);
			}

		} else {
			Collections.emptyList();
		}

	}

}
