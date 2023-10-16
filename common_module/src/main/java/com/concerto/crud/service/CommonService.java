
package com.concerto.crud.service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.concerto.crud.config.bean.Module;
import com.concerto.crud.config.exception.JsonConversionException;
import com.concerto.crud.dao.UserDAO;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class CommonService {

	@Autowired
	UserDAO userDAO;

	@Autowired
	ValidationService validationService;

	public String userCreate(Map<String, Object> user, String moduleName) {

		String message = "";
		try {
			Module module = JsonToJavaConverter.moduleData(moduleName);

			Map<String, String> validation = validationService.checkValidation(module, user);
			if (validation.isEmpty()) {

				message = userDAO.checkExistingUser(user, module);
				if (message.isEmpty()) {
					message = userDAO.executeCreateOperation(user, module);
				}

			} else {
				ObjectMapper obj = new ObjectMapper();
				return obj.writeValueAsString(validation);
			}
		} catch (IOException | SQLException e) {
			return "Error occurred during JSON mapping.";
		}
		return message;
	}

	public String userUpdate(Map<String, Object> user, String moduleName) {
		String message = "";
		try {

			Module module = JsonToJavaConverter.moduleData(moduleName);
			Map<String, String> validation = validationService.checkValidation(module, user);
			if (validation.isEmpty()) {
				message = userDAO.updateUser(user, module);
			}
		} catch (Exception e) {

			throw new JsonConversionException("Please check the input data");

		}
		return message;
	}

	public List<Map<String, Object>> jsonDynamicRead(String fieldName, Object value, String moduleName) {
		try {
			Module module = JsonToJavaConverter.moduleData(moduleName);
			if (module == null) {
				throw new JsonConversionException("Module not found.");
			}

			return userDAO.readData(fieldName, value, module);
		} catch (Exception e) {
			throw new JsonConversionException("Module not found.");

		}
	}

	public String userDelete(Map<String, Object> user, String moduleName) {
		try {
			Module module = JsonToJavaConverter.moduleData(moduleName);
			return userDAO.executeDeleteOperation(user, module);
		} catch (Exception e) {
			return "Error occurred during delete operation.";
		}
	}
}
