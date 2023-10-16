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

	String executeCreateOperation(Map<String, Object> user, Module module) throws SQLException;

	String executeDeleteOperation(Map<String, Object> user, Module moduleName);

	List<Map<String, Object>> readData(String fieldName, Object value, Module module);

	String checkExistingUser(Map<String, Object> user, Module moduleName) throws SQLException;

	String updateUser(Map<String, Object> user, Module module) throws SQLException;
	
	Void addToHist(Map<String, Object> user, Module module,String actionMesaage);

}
