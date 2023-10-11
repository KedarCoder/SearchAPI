package com.concerto.crud.dao;

import java.util.List;
import java.util.Map;

import com.concerto.crud.config.bean.Module;

/**
 * Copyright (C) Concerto Software and Systems (P) LTD | All Rights Reserved
 * 
 * @File : com.concerto.crud.dao.impl.UserDAOImpl.java
 * @Author : Suyog Kedar
 * @AddedDate : October 03, 2023 12:30:40 PM
 * @ModifiedBy :
 * @ModifiedDate :
 * @Purpose : DAO
 * @Version : 1.0
 */
public interface UserDAO {

	void executeCreateOperation(Map<String, Object> user, Module module) throws Exception;

	void executeDeleteOperation(int userId, String moduleName);

	void updateUser(Map<String, Object> user, Module module, int id) throws Exception;

	List<Map<String, Object>> readData(String fieldName, Object value, Module module);


}
