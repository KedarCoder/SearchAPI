package com.concerto.crud.dao;

import com.concerto.crud.config.bean.CrudInterface;
import com.concerto.crud.config.bean.User;


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

	void createUser(CrudInterface clazz);

	void deleteUser(int userId);

	void updateUser(CrudInterface user , int  id);

	User getUserByName(String name); // New method to get user by name

}
