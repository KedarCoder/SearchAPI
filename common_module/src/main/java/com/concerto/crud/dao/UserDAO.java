package com.concerto.crud.dao;

import java.sql.SQLException;
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

	 Map<String, Object> executeCreateOperation(Map<String, Object> user, Module module) throws SQLException;
	
	Map<String, Object> executeUpdateOperation(Map<String, Object> user, Module module) throws SQLException;
	
	List<Map<String, Object>> executeReadOperation(String fieldName, Object value, Module module);

	String executeDeleteOperation(Map<String, Object> user, Module moduleName);

	Map<String, Object> checkExistingUser(Map<String, Object> user, Module moduleName) throws SQLException;

	
	
	Void addToHist(Map<String, Object> user, Module module,String actionMesaage);
	
	List<Object> search(String fieldName, String value, Module module);

}
