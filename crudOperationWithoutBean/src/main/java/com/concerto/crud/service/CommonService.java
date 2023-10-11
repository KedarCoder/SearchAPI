package com.concerto.crud.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.concerto.crud.config.bean.Module;
import com.concerto.crud.dao.UserDAO;

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

	@Autowired
	UserDAO userDAO;

	public void jsonMapperCreate(Map<String, Object> user, String moduleName) throws Exception {

		Module module = JsonToJavaConverter.moduleData(moduleName);
		userDAO.executeCreateOperation(user, module);

	}

	public void jsonMapperUpdate(Map<String, Object> user, String moduleName, int id) throws Exception {

		Module module = JsonToJavaConverter.moduleData(moduleName);

		userDAO.updateUser(user, module, id);

	}

	public List<Map<String, Object>> jsonDynamicRead(String fieldName, Object value, String moduleName)
			throws Exception {

		Module module = JsonToJavaConverter.moduleData(moduleName);
		if (module == null) {
			throw new Exception();
		}

		return userDAO.readData(fieldName, value, module);
	}

	public void jsonMapperDelete(int id, String moduleName) {
		userDAO.executeDeleteOperation(id, moduleName);

	}

}
