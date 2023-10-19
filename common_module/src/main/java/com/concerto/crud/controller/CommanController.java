package com.concerto.crud.controller;

import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import com.concerto.crud.constant.AppConstant;
import com.concerto.crud.dao.UserDAO;
import com.concerto.crud.service.CommonService;
import com.concerto.crud.service.JsonToJavaConverter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright (C) Concerto Software and Systems (P) LTD | All Rights Reserved
 * 
 * @File : com.concerto.crud.dao.impl.UserDAOImpl.java
 * @Author : Suyog Kedar
 * @AddedDate : October 03, 2023 12:30:40 PM
 * @ModifiedBy : Tejas Kute
 * @ModifiedDate : Oct 19, 2023 12:35:12 PM
 * @Purpose : Controller
 * @Version : 1.0
 */
@RestController
@RequestMapping("/commonModule")
public class CommanController {

	private static final Logger logger = LoggerFactory.getLogger(CommanController.class);

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	UserDAO userDAO;

	@Autowired
	CommonService commonService;

	// create
	@PostMapping("create/{moduleName}")
	public ResponseEntity<Map<String, Object>> create(@RequestBody Map<String, Object> requestBody,
			@PathVariable String moduleName) {
		if (JsonToJavaConverter.getModuleMap().containsKey(moduleName)) {
			Map<String, Object> message = null;
			try {
				message = commonService.create(requestBody, moduleName);
			} catch (Exception e) {
				logger.error("Error found while Saving data ", e.getMessage());
			}
			return new ResponseEntity<>(message, HttpStatus.OK);
		} else {
			Map<String, Object> errorResponse = new HashMap<>();
			errorResponse.put("error", AppConstant.COMMON_MODULE_NOT_FOUND);
			return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
		}
	}

	@PutMapping("/update/{moduleName}")
	public Map<String, Object> update(@RequestBody Map<String, Object> requestBody, @PathVariable String moduleName) {
		Map<String, Object> message = null;
		if (JsonToJavaConverter.getModuleMap().containsKey(moduleName)) {
			try {
				message = commonService.update(requestBody, moduleName);
			} catch (Exception e) {
				logger.error("An error occurred during the update process.", e);
			}
			return message;
		}
		return message;
	}

	// read
	// http://localhost:8080/users/readData/City/Mumbai?moduleName=Student
	@GetMapping("/readData/{moduleName}/{fieldName}/{value}")
	public List<Map<String, Object>> readData(@PathVariable String fieldName, @PathVariable Object value,
			@PathVariable String moduleName) {
		return commonService.read(fieldName, value, moduleName);
	}

	// Delete
	@DeleteMapping("/delete/{moduleName}")
	public ResponseEntity<Map<String, Object>> delete(@RequestBody Map<String, Object> input,
			@PathVariable String moduleName) {
		if (JsonToJavaConverter.getModuleMap().containsKey(moduleName)) {
			String result = commonService.delete(input, moduleName);

			Map<String, Object> successResponse = new HashMap<>();
			successResponse.put("message", result);
			return new ResponseEntity<>(successResponse, HttpStatus.OK);

		} else {

			Map<String, Object> errorResponse = new HashMap<>();
			errorResponse.put("error", "Please check the URL.");
			return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/search/{moduleName}/{filterType}/{value}")
	public List<Object> search(@PathVariable String filterType, @PathVariable String value,
			@PathVariable String moduleName) {
		return commonService.search(filterType, value, moduleName);
	}

}
