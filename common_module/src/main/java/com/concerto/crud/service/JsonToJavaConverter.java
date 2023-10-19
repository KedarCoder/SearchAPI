package com.concerto.crud.service;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.concerto.crud.config.bean.Field;
import com.concerto.crud.config.bean.Module;
import com.concerto.crud.config.exception.JsonConversionException;
import com.concerto.crud.dao.UserDAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

/**
 * Copyright (C) Concerto Software and Systems (P) LTD | All Rights Reserved
 * 
 * @File : com.concerto.jdbctemplate.bean.JsonToJavaConverter.java
 * @Author : Suyog Kedar
 * @AddedDate : Octo 03, 2023 12:30:40 PM
 * @ModifiedBy :
 * @ModifiedDate :
 * @Purpose : Converting JSON to String
 * @Version : 1.0
 */

@Configuration
public class JsonToJavaConverter {

	@Autowired
	ApplicationContext applicationContext;

	@Value("${input.json.file.path}")
	private String inputJsonFilePath;

	private static Map<String, Module> moduleMap = new HashMap<>();
	private static Map<String, Object> moduleNameMap = new HashMap<>();
	private static List<String> primaryfields = new ArrayList<>();
	private static final Logger logger = LoggerFactory.getLogger(JsonToJavaConverter.class);

	public static Module moduleData(String moduleName) {
		try {
			if (moduleMap.containsKey(moduleName)) {
				return moduleMap.get(moduleName);

			} else {
				logger.warn("Module not found for moduleName: {}", moduleName);
			}
		} catch (Exception e) {
			// Handle other exceptions
			logger.error("An exception occurred while retrieving module data: " + e.getMessage(), e);
			throw new JsonConversionException("Failed to retrieve module data", e);
		}
		return null;
	}

	public void moduleMap() {

		ObjectMapper objectMapper = new ObjectMapper();
		List<Field> fields=new ArrayList<>();
		try {
			// Load the input.json file from the classpath

			File file = new File(inputJsonFilePath);

			// Read JSON data from the InputStream and convert to Module array
			Module[] modules = objectMapper.readValue(file, Module[].class);

			// Create a HashMap to store modules using moduleName as key
			for (Module module : modules) {
				moduleMap.put(module.getModuleName(), module);
				 fields = module.getFields();
				for (Field field : fields) {
					if (field.isPrimaryKey()) {
						primaryfields.add(field.getName());
					}
				}
			}

		} catch (Exception e) {
			logger.error("An exception occurred:", e);
			throw new JsonConversionException("Failed to read and convert JSON to Java", e);
		}
	}

	public UserDAO getRunBean(String moduleName) {
		return (UserDAO) (applicationContext).getBean(moduleName + "DAOImpl");

	}

	public static Map<String, Object> getModuleNameMap() {
		return moduleNameMap;
	}

	public static void setModuleNameMap(Map<String, Object> moduleNameMap) {
		JsonToJavaConverter.moduleNameMap = moduleNameMap;
	}

	public static Map<String, Module> getModuleMap() {
		return moduleMap;
	}

	public static void setModuleMap(Map<String, Module> moduleMap) {
		JsonToJavaConverter.moduleMap = moduleMap;
	}

	public static List<String> getPrimaryfields() {
		return primaryfields;
	}

	public static void setPrimaryfields(List<String> primaryfields) {
		JsonToJavaConverter.primaryfields = primaryfields;
	}
}