
package com.concerto.crud.service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.concerto.crud.config.bean.Module;
import com.concerto.crud.config.exception.JsonConversionException;
import com.concerto.crud.constant.AppConstant;
import com.concerto.crud.dao.UserDAO;
import com.concerto.crud.dao.impl.UserDAOImpl;


/**
 * Copyright (C) Concerto Software and Systems (P) LTD | All Rights Reserved
 * 
 * @File : com.concerto.crud.service.CommonService.java
 * @Author : Suyog Kedar
 * @AddedDate : October 03, 2023 12:30:40 PM
 * @ModifiedBy : Tejas Kute
 * @ModifiedDate : Oct 19, 2023 11:35:12 AM
 * @Purpose : 
 * @Version : 1.0
 */

@Service
public class CommonService {

	static final Logger logger = LoggerFactory.getLogger(UserDAOImpl.class);

	@Autowired
	UserDAO userDAO;

	@Autowired
	ValidationService validationService;

	public Map<String, Object> create(Map<String, Object> requestBody, String moduleName)
			throws SQLException {

		Map<String, String> validation = null;
		Map<String, Object> result = new HashMap<>();
		Module module = JsonToJavaConverter.moduleData(moduleName);

		validation = validationService.checkValidation(module, requestBody);
		if (validation.isEmpty()) {
			result = userDAO.checkExistingUser(requestBody, module);
			if (result == null || result.isEmpty()) {
				result = userDAO.executeCreateOperation(requestBody, module);
				return result;
			}
		} else if (!validation.isEmpty()) {
			// Populate your validation with data
			Map<String, Object> objectMap = new HashMap<>();
			for (Map.Entry<String, String> entry : validation.entrySet()) {
				objectMap.put(entry.getKey(), (Object) entry.getValue());
			}
			return objectMap;
		}
		return result;
	}

	public Map<String, Object> update(Map<String, Object> requestBody, String moduleName) throws SQLException {

		Map<String, Object> result = new HashMap<>();
		Module module = JsonToJavaConverter.moduleData(moduleName);
		Map<String, String> validation = validationService.checkValidation(module, requestBody);
		if (validation.isEmpty()) {
			result = userDAO.executeUpdateOperation(requestBody, module);
		}

		return result;
	}

	public List<Map<String, Object>> read(String fieldName, Object value, String moduleName) {

		Module module = JsonToJavaConverter.moduleData(moduleName);
		if (module == null) {	
			throw new JsonConversionException(AppConstant.COMMON_MODULE_NOT_FOUND);
		}

		return userDAO.executeReadOperation(fieldName, value, module);

	}

	public String delete(Map<String, Object> requestBody, String moduleName) {

		Module module = JsonToJavaConverter.moduleData(moduleName);
		return userDAO.executeDeleteOperation(requestBody, module);

	}
	
	public List<Object> search( String filterType,  String value,
			 String moduleName) {
		try {
			Module module = JsonToJavaConverter.moduleData(moduleName);
			if (module == null) {
				throw new JsonConversionException(AppConstant.COMMON_MODULE_NOT_FOUND);
			}

			return userDAO.search(filterType, value, module);
		} catch (Exception e) {
			logger.error("Error while json Read ", e);

			throw new JsonConversionException(AppConstant.COMMON_MODULE_NOT_FOUND);

		}
	}
}
