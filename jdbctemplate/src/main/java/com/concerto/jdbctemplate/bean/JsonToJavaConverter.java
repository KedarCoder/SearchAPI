package com.concerto.jdbctemplate.bean;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class JsonToJavaConverter {

	private static final Logger logger = LoggerFactory.getLogger(JsonToJavaConverter.class);

	public static void main(String[] args) {

		try {
			// Load the input.json file from the classpath
			InputStream inputStream = JsonToJavaConverter.class.getResourceAsStream("/input.json");

			ObjectMapper objectMapper = new ObjectMapper();

			// Read JSON data from the InputStream and convert to Module array
			Module[] modules = objectMapper.readValue(inputStream, Module[].class);

			for (Module module : modules) {

				logger.info("Module Name: {}", module.getModuleName());
				logger.info("Fields: {}", "");
				for (Field field : module.getFields()) {
					logger.info("  Name: {}", field.getName());
					logger.info("  Type: {}", field.getType());

					Validation validation = field.getValidation();
					logger.info("  Validation: {}", "");
					logger.info("    Pattern: {}", validation.getPattern());
					logger.info("    MaxLength: {}", validation.getMaxLength());
					logger.info("    Minimum: {}", validation.getMinimum());
					logger.info("    Maximum: {}", validation.getMaximum());
				}
				logger.info("--------------- {}", ""); // Empty line

			}

			inputStream.close();
		} catch (Exception e) {
			logger.error("An exception occurred:", e);
		}
	}
}
