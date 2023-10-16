package com.concerto.crud.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
	
	private static final String MASTER_TABLE_SUFFIX = "_MASTER";

	@Override
	public String executeCreateOperation(Map<String, Object> user, Module module) throws SQLException {
		if (dataSource == null) {
			throw new JsonConversionException("Datasource not be null");
		}
		String message = "";

		String tableName = module.getModuleName();
		List<Field> fields = module.getFields();

		StringBuilder setClause = new StringBuilder();

		for (int i = 0; i < fields.size(); i++) {
			Field field = fields.get(i);
			setClause.append(field.getName());
			setClause.append(",");
		}
		setClause.append("Status");

		String sqlInsert = "INSERT INTO " + tableName + "_MASTER" + " (" + setClause + ") VALUES ("
				+ String.join(", ", Collections.nCopies(fields.size() + 1, "?")) + ")";

		try (Connection connection = dataSource.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(sqlInsert)) {

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
			preparedStatement.setString(paramIndex, "Pending");
			preparedStatement.executeUpdate();
			message = " Data Inserted Successfully";
		} catch (Exception e) {
			logger.error("Please check the format ot URL", e);
		}
		return message;
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
	public String checkExistingUser(Map<String, Object> user, Module module) throws SQLException {
		List<Field> fields = module.getFields();
		StringBuilder setClause = new StringBuilder();
		List<Object> primaryKeyValues = new ArrayList<>();
		String tableName = module.getModuleName();
		String message = "";

		for (Field field : fields) {
			if (field.isPrimaryKey()) {
				setClause.append(field.getName());
				setClause.append(" = ? AND ");
				primaryKeyValues.add(user.get(field.getName()));
			}
		}

		// Remove the trailing "AND" if it exists
		final int AND_LENGTH = 5; // Length of " AND "
		if (setClause.length() >= AND_LENGTH) {
			setClause.setLength(setClause.length() - AND_LENGTH);
		}

		try (Connection connection = dataSource.getConnection()) {
			message = executeCheckExistingUserQuery(connection, tableName, setClause.toString(), primaryKeyValues);
		} catch (SQLException e) {

			logger.error("Error while checking check Existing Data of the the" +module.getModuleName(), e);
		}

		return message;
	}

	private String executeCheckExistingUserQuery(Connection connection, String tableName, String setClause,
			List<Object> primaryKeyValues) throws SQLException {
		final String TEMP_TABLE_SUFFIX = "_TEMP";
		final String SQL_FORMAT = "SELECT * FROM %s%s WHERE %s";
		String message = "";

		// Check TEMP table
		String sql = String.format(SQL_FORMAT, tableName, TEMP_TABLE_SUFFIX, setClause);
		message = executeQueryAndSetMessage(connection, primaryKeyValues, sql, "Pending for Approval.");

		// If user not found in TEMP table, check MASTER table
		if (message.isEmpty()) {
			sql = String.format(SQL_FORMAT, tableName, MASTER_TABLE_SUFFIX, setClause);
			message = executeQueryAndSetMessage(connection, primaryKeyValues, sql, "User already Present.");
		}

		return message;
	}

	private String executeQueryAndSetMessage(Connection connection, List<Object> primaryKeyValues, String sql,
			String failureMessage) throws SQLException {
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			for (int i = 0; i < primaryKeyValues.size(); i++) {
				preparedStatement.setObject(i + 1, primaryKeyValues.get(i));
			}

			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				if (resultSet.next()) {
					return failureMessage;
				}
			}
		}
		return "";
	}

	@Override
	public String updateUser(Map<String, Object> user, Module module) throws SQLException {
		if (dataSource == null) {
			throw new JsonConversionException("DataSource cannot be null");
		}

		List<Field> fields = module.getFields();
		StringBuilder whereClause = new StringBuilder();
		List<Object> primaryKeyValues = new ArrayList<>();
		String tableName = module.getModuleName();
		StringBuilder setClause = new StringBuilder();
		List<Object> setValues = new ArrayList<>();

		String message = "";

		for (Field field : fields) {

			if (field.isPrimaryKey()) {
				whereClause.append(field.getName());
				whereClause.append(" = ? AND ");
				primaryKeyValues.add(user.get(field.getName()));
			} else {
				setClause.append(field.getName()).append("= ?,");
				setValues.add(user.get(field.getName()));
			}
		}
		setClause.setLength(setClause.length() - 1);

		final int AND_LENGTH = 5; // Length of " AND "
		if (whereClause.length() >= AND_LENGTH) {
			whereClause.setLength(whereClause.length() - AND_LENGTH);
		}

		final String SQL_FORMAT = "SELECT * FROM %s%s WHERE %s";

		try (Connection connection = dataSource.getConnection()) {

			String sql = String.format(SQL_FORMAT, tableName, MASTER_TABLE_SUFFIX, whereClause);
			message = executeQueryAndSetMessage(connection, primaryKeyValues, sql, "User already Present.");

			if (!message.isEmpty()) {
				int val = 0;
				String updateQueryFormat  = "UPDATE %s%s SET %s WHERE %s";

				// SQL_UPDATE_FORMAT
				String sqlUpdate = String.format(updateQueryFormat, tableName, MASTER_TABLE_SUFFIX, setClause,
						whereClause);

				try (PreparedStatement preparedStatement = connection.prepareStatement(sqlUpdate)) {
					for (int i = 0; i < setValues.size(); i++) {
						preparedStatement.setObject(val + 1, setValues.get(i));
						val = val + 1;
					}
					for (int i = 0; i < primaryKeyValues.size(); i++) {
						preparedStatement.setObject(val + 1, primaryKeyValues.get(i));
						val = val + 1;
					}

					int rowsUpdated = preparedStatement.executeUpdate();
					if (rowsUpdated > 0) {
						message = "Data updated successfully";
						addToHist(user, module, "Update");

					} else {
						message = "Update failed";
					}

				}

			}
		} catch (Exception e) {
			logger.error("Error while Updating the" +module.getModuleName()+ " data", e);
		}
		return message;

	}

	@Override
	public String executeDeleteOperation(Map<String, Object> user, Module module) {
		String tableName = module.getModuleName();
		List<Field> fields = module.getFields();

		StringBuilder whereClause = new StringBuilder();
		List<Object> parameterValues = new ArrayList<>();
		String message = "";
		
		primaryKeyLoop(fields, user, whereClause, parameterValues);
		
		if (whereClause.length() > 0) {
			String sql = "DELETE FROM [" + tableName + MASTER_TABLE_SUFFIX + "] WHERE " + whereClause.toString();

			try (Connection connection = dataSource.getConnection();
					PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

				for (int i = 0; i < parameterValues.size(); i++) {
					preparedStatement.setObject(i + 1, parameterValues.get(i));
				}

				int rowsDeleted = preparedStatement.executeUpdate();
				if (rowsDeleted > 0) {
					message = "Data deleted successfully";
					addToHist(user, module, "Delete");
				} else {
					message = " Data Deletion failed";
				}

			} catch (Exception e) {
				logger.error("Error while Deleteing the" +module.getModuleName()+ " data", e);
			}
		}
		return message;

	}
	
	private void primaryKeyLoop(List<Field> fields, Map<String, Object> user, StringBuilder whereClause, List<Object> parameterValues) {
		for (Field field : fields) {
			if (field.isPrimaryKey()) {
				String fieldName = field.getName();
				Object value = user.get(fieldName);

				if (value != null) {
					if (whereClause.length() > 0) {
						whereClause.append(" AND ");
					}
					whereClause.append(fieldName).append("=?");
					parameterValues.add(value);
				}
			}
		}
	}

	@Override
	public Void addToHist(Map<String, Object> user, Module module, String actionMesaage) {

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
		setClause.append("Action");

		String query = "INSERT INTO " + tableName + "_HIST" + " (" + setClause + ") VALUES ("
				+ String.join(", ", Collections.nCopies(fields.size() + 1, "?")) + ")";

		try (Connection connection = dataSource.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query)) {

			int paramIndex = 1;
			for (Field field : fields) {
				Object value = user.get(field.getName());
				if (value instanceof String) {
					preparedStatement.setString(paramIndex, (String) value);
				} else if (value instanceof Integer) {
					preparedStatement.setInt(paramIndex, (Integer) value);
				}
				paramIndex++;
			}
			preparedStatement.setString(paramIndex, actionMesaage);
			preparedStatement.executeUpdate();
			logger.info("Data Inserted Successfully: Table = {}", tableName);

		} catch (Exception e) {
			logger.error("Please check the format ot URL", e);
		}
		return null;

	}

}
