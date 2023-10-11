package com.concerto.crud.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.concerto.crud.config.bean.Field;
import com.concerto.crud.config.bean.Module;
import com.concerto.crud.config.exception.JsonConversionException;
import com.concerto.crud.dao.UserDAO;

/**
 * Copyright (C) Concerto Software and Systems (P) LTD | All Rights Reserved
 * 
 * @File : com.concerto.crud.dao.impl.UserDAOImpl.java
 * @Author : Suyog Kedar
 * @AddedDate : October 03, 2023 12:30:40 PM
 * @ModifiedBy :
 * @ModifiedDate :
 * @Purpose : DAO Implementation
 * @Version : 1.0
 */
@Repository
public class UserDAOImpl implements UserDAO {

	private static final Logger logger = LoggerFactory.getLogger(UserDAOImpl.class);

	@Autowired
	JdbcTemplate jdbcTemplate;

	DataSource dataSource;

	public UserDAOImpl() {
		super();
	}

	@Autowired
	public UserDAOImpl(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public void executeCreateOperation(Map<String, Object> user, Module module) throws Exception {
		if (dataSource == null) {
			throw new JsonConversionException("Datasource not be null");
		}

		String tableName = module.getModuleName();
		List<Field> fields = module.getFields();

		StringBuilder setClause = new StringBuilder();

		for (int i = 0; i < fields.size(); i++) {
			Field field = fields.get(i);
			setClause.append(field.getName());
			setClause.append(",");
		}
		setClause.deleteCharAt(setClause.length() - 1);


		String sql = "INSERT INTO " + tableName + " (" + setClause + ") VALUES ("
				+ String.join(", ", Collections.nCopies(fields.size(), "?")) + ")";

		try (Connection connection = dataSource.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

			int paramIndex = 1;
			for (Field field : fields) {
				Object value = user.get(field.getName());
				if (value instanceof String) {
					preparedStatement.setString(paramIndex, (String) value);
				} else if (value instanceof Integer) {
					preparedStatement.setInt(paramIndex, (Integer) value);
				} // Add other supported types as needed

				paramIndex++;
			}

			preparedStatement.executeUpdate();
		} catch (DataAccessException e) {
			logger.error("Please check the format ot URL", e);
		}
	}

	@Override
	public void executeDeleteOperation(int userId, String moduleName) {
		String sql = "DELETE FROM [" + moduleName + "] WHERE userId=?";

		jdbcTemplate.update(sql, userId);
	}

	@Override
	public List<Map<String, Object>> readData(String fieldName, Object value, Module module) {
		String tableName = module.getModuleName();
		List<Field> fields = module.getFields();

		String columns = fields.stream().map(Field::getName).collect(Collectors.joining(", "));

		String sql = "SELECT " + columns + " FROM " + tableName + " WHERE " + fieldName + " = ?";

		try {
			return jdbcTemplate.queryForList(sql, value);
		} catch (DataAccessException e) {
			throw new JsonConversionException("Error while executing read operation", e);
		}
		
	}

	@Override
	public void updateUser(Map<String, Object> user, Module module, int userId) throws Exception {
		if (dataSource == null) {
			throw new JsonConversionException("Datasource not be null");
		}

		String tableName = module.getModuleName();
		List<Field> fields = module.getFields();

		StringBuilder setClause = new StringBuilder();

		for (int i = 0; i < fields.size(); i++) {
			Field field = fields.get(i);
			setClause.append(field.getName()).append(" = ?");
			setClause.append(",");
		}
		setClause.deleteCharAt(setClause.length() - 1);

		String sql = "UPDATE " + tableName + " SET " + setClause + " WHERE UserId = ?";

		try (Connection connection = dataSource.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

			int paramIndex = 1;
			for (Field field : fields) {
				Object value = user.get(field.getName());
				if (value instanceof String) {
					preparedStatement.setString(paramIndex, (String) value);
				} else if (value instanceof Integer) {
					preparedStatement.setInt(paramIndex, (Integer) value);
				} // Add other supported types as needed

				paramIndex++;
			}
			preparedStatement.setInt(paramIndex, userId);

			preparedStatement.executeUpdate();
		} catch (DataAccessException e) {
			logger.error("An error occurred during the update process.", e);
		}
	}

}
