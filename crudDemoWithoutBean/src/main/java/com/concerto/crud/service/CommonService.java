package com.concerto.crud.service;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.concerto.crud.config.bean.CrudInterface;
import com.concerto.crud.dao.UserDAO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Copyright (C) Concerto Software and Systems (P) LTD | All Rights Reserved
 * 
 * @File : com.concerto.crud.service.CommonService.java
 * @Author : Suyog Kedar
 * @AddedDate : Octo 03, 2023 12:30:40 PM
 * @ModifiedBy :
 * @ModifiedDate :
 * @Purpose : Converting JSON to String
 * @Version : 1.0
 */

@Service
public class CommonService {

	private static final Logger logger = LoggerFactory.getLogger(CommonService.class);

	@Autowired
	UserDAO userDAO;

	public void jsonMapperCreate(Map<String, Object> user, String moduleName) {
		ObjectMapper objectMapper = new ObjectMapper();
		String json;

		try {
			json = objectMapper.writeValueAsString(user);

			CrudInterface clazz = (CrudInterface) JsonToJavaConverter.getModuleNameMap().get(moduleName);
			clazz = objectMapper.readValue(json, clazz.getClass());

			userDAO.createUser(clazz);

		} catch (JsonProcessingException e) {

			logger.error("An error occurred while processing JSON: {}", e.getMessage(), e);
		}

	}
	
	public void jsonMapperUpdate(Map<String, Object> user, String moduleName, int id) {
		ObjectMapper objectMapper = new ObjectMapper();
		String json;

		try {
			json = objectMapper.writeValueAsString(user);

			CrudInterface clazz = (CrudInterface) JsonToJavaConverter.getModuleNameMap().get(moduleName);
			clazz = objectMapper.readValue(json, clazz.getClass());

			userDAO.updateUser(clazz, id);

		} catch (JsonProcessingException e) {

			logger.error("An error occurred while processing JSON: {}", e.getMessage(), e);
		}

	}

}
