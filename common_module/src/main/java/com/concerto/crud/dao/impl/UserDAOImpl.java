package com.concerto.crud.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
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
import com.concerto.crud.service.JsonToJavaConverter;

/**
 * Copyright (C) Concerto Software and Systems (P) LTD | All Rights Reserved
 * 
 * @File : com.concerto.crud.dao.impl.UserDAOImpl.java
 * @Author : Suyog Kedar
 * @AddedDate : October 03, 2023 12:30:40 PM
 * @ModifiedBy : Tejas Kute
 * @ModifiedDate : Oct 19, 2023 12:58:12 PM
 * @Purpose : DAO Implementation
 * @Version : 1.0
 */

@Repository
public class UserDAOImpl implements UserDAO {

	private static final Logger logger = LoggerFactory.getLogger(UserDAOImpl.class);

	private static final String MASTER_TABLE_SUFFIX = "_MASTER";
	private static final String TEMP_TABLE_SUFFIX = "_TEMP";

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
	public Map<String, Object> executeCreateOperation(Map<String, Object> user, Module module) {
		Map<String, Object> response = new HashMap<>();

		try {
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
			setClause.append("Status");

			String sqlInsert = "INSERT INTO " + tableName + TEMP_TABLE_SUFFIX + " (" + setClause + ") VALUES ("
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
				response.put("status", "success");
				response.put("message", "Data Inserted Successfully");
			} catch (Exception e) {
				logger.error("Please check the format or URL", e);
				response.put("message", "An error occurred during data insertion");
			}
		} catch (JsonConversionException e) {
			logger.error("Error during JSON conversion", e);
			response.put("status", "error");
			response.put("message", "An error occurred during JSON conversion");
		}

		return response;
	}

	@Override
	public List<Map<String, Object>> executeReadOperation(String fieldName, Object value, Module module) {
		String tableName = module.getModuleName();
		List<Field> fields = module.getFields();

		String columns = fields.stream().map(Field::getName).collect(Collectors.joining(", "));

		String sql = "SELECT " + columns + " FROM " + tableName + MASTER_TABLE_SUFFIX + " WHERE " + fieldName + " = ?";

		try {
			return jdbcTemplate.queryForList(sql, value);
		} catch (DataAccessException e) {
			logger.error("Please check the format ot URL", e);
		}
		return Collections.emptyList();

	}

	@Override
	public Map<String, Object> checkExistingUser(Map<String, Object> user, Module module) throws SQLException {

		List<String> primaryfields = new ArrayList<>();
		StringBuilder setClause = new StringBuilder();
		List<Object> primaryKeyValues = new ArrayList<>();
		String tableName = module.getModuleName();
		Map<String, Object> message = null;

		primaryfields = JsonToJavaConverter.getPrimaryfields();
		for (int i = 0; i < primaryfields.size(); i++) {
			setClause.append(primaryfields.get(i));
			setClause.append(" = ? AND ");
			primaryKeyValues.add(user.get(primaryfields.get(i)));
		}

		// Remove the trailing "AND" if it exists
		final int AND_LENGTH = 5; // Length of " AND "
		if (setClause.length() >= AND_LENGTH) {
			setClause.setLength(setClause.length() - AND_LENGTH);
		}

		try (Connection connection = dataSource.getConnection()) {
			message = executeCheckExistingUserQuery(connection, tableName, setClause.toString(), primaryKeyValues);
		} catch (SQLException e) {

			logger.error("Error while checking check Existing Data in the the" + module.getModuleName(), e);
		}

		return message;
	}

	private Map<String, Object> executeCheckExistingUserQuery(Connection connection, String tableName, String setClause,
			List<Object> primaryKeyValues) throws SQLException {

		final String SQL_FORMAT = "SELECT * FROM %s%s WHERE %s";
		Map<String, Object> message = null;

		// Check TEMP table
		String sql = String.format(SQL_FORMAT, tableName, TEMP_TABLE_SUFFIX, setClause);
		Map<String, Object> messageParams = new HashMap<>();
		messageParams.put("message", "Pending for Approval.");
		message = executeQueryAndSetMessage(connection, primaryKeyValues, sql, messageParams);

		// message = executeQueryAndSetMessage(connection, primaryKeyValues, sql,

		// If user not found in TEMP table, check MASTER table
		if (message == null || message.isEmpty()) {
			sql = String.format(SQL_FORMAT, tableName, MASTER_TABLE_SUFFIX, setClause);
			messageParams.put("message", "User already Present.");
			message = executeQueryAndSetMessage(connection, primaryKeyValues, sql, messageParams);
			return message;
		}

		return message;
	}

	private Map<String, Object> executeQueryAndSetMessage(Connection connection, List<Object> primaryKeyValues,
			String sql, Map<String, Object> failureMessage) throws SQLException {
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
		return null;
	}

	@Override
	public Map<String, Object> executeUpdateOperation(Map<String, Object> input, Module module) throws SQLException {
		if (dataSource == null) {
			throw new JsonConversionException("DataSource cannot be null");
		}

		Map<String, Object> response = new HashMap<>();
		Map<String, Object> messageParams = new HashMap<>();

		List<Field> fields = module.getFields();
		StringBuilder whereClause = new StringBuilder();
		List<Object> primaryKeyValues = new ArrayList<>();
		String tableName = module.getModuleName();
		StringBuilder setClause = new StringBuilder();
		List<Object> setValues = new ArrayList<>();

		for (Field field : fields) {

			if (field.isPrimaryKey()) {
				whereClause.append(field.getName());
				whereClause.append(" = ? AND ");
				primaryKeyValues.add(input.get(field.getName()));

			} else {
				setClause.append(field.getName()).append("= ?,");
				setValues.add(input.get(field.getName()));
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

			messageParams.put("message", "User already Present.");
			response = executeQueryAndSetMessage(connection, primaryKeyValues, sql, messageParams);

			if (!response.isEmpty()) {
				int val = 0;
				String updateQueryFormat = "UPDATE %s%s SET %s WHERE %s";

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
						response.put("message", "Data updated successfully");

						addToHist(input, module, "Update");
					} else {
						response.put("status", "failure");
						response.put("message", "Update failed");
					}
				}

			} else {
				response.put("status", "failure");
				response.put("message", "Data Is not Present in Master Table");
			}
		} catch (Exception e) {
			logger.error("Error while Updating the" + module.getModuleName() + " data", e);
			response.put("status", "error");
			response.put("message", "An error occurred during the update");
		}

		return response;
	}

	@Override
	public String executeDeleteOperation(Map<String, Object> input, Module module) {
		String tableName = module.getModuleName();
		List<Field> fields = module.getFields();

		// Create a map to store deleted data
		Map<String, Object> deletedData = new HashMap<>();

		// Construct the WHERE clause
		StringBuilder whereClause = new StringBuilder();
		List<Object> parameterValues = new ArrayList<>();
		primaryKeyLoop(fields, input, whereClause, parameterValues);

		// Check if the WHERE clause is not empty
		if (whereClause.length() == 0) {
			return "No criteria provided for deletion.";
		}

		try (Connection connection = dataSource.getConnection()) {
			connection.setAutoCommit(false);

			// Construct a SELECT statement to check the existence of data
			String selectSql = "SELECT * FROM [" + tableName + MASTER_TABLE_SUFFIX + "] WHERE "
					+ whereClause.toString();

			try (PreparedStatement selectStatement = connection.prepareStatement(selectSql)) {
				for (int i = 0; i < parameterValues.size(); i++) {
					selectStatement.setObject(i + 1, parameterValues.get(i));
				}

				// Execute the SELECT statement to fetch existing data
				ResultSet resultSet = selectStatement.executeQuery();

				if (resultSet.next()) {
					// Data exists, proceed with deletion
					String deleteSql = "DELETE FROM [" + tableName + MASTER_TABLE_SUFFIX + "] WHERE "
							+ whereClause.toString();

					try (PreparedStatement deleteStatement = connection.prepareStatement(deleteSql)) {
						for (int i = 0; i < parameterValues.size(); i++) {
							deleteStatement.setObject(i + 1, parameterValues.get(i));
						}

						int rowsDeleted = deleteStatement.executeUpdate();
						if (rowsDeleted > 0) {
							// Capture the deleted data and store it in the map
							for (Field field : fields) {
								String fieldName = field.getName();
								Object value = resultSet.getObject(fieldName);
								deletedData.put(fieldName, value);
							}

							// Add deleted data to historical record
							addToHist(deletedData, module, "Delete");

							// Commit the transaction
							connection.commit();

							return "Data deleted successfully";
						}
					}
				}
			} catch (Exception e) {
				// Handle exceptions and log errors
				connection.rollback();
				logger.error("Error while Deleting the " + module.getModuleName() + " data", e);
			}
		} catch (Exception e) {
			logger.error("Error while setting up database connection", e);
		}

		return "User Not found with Given Details";
	}

	private void primaryKeyLoop(List<Field> fields, Map<String, Object> input, StringBuilder whereClause,
			List<Object> parameterValues) {
		for (Field field : fields) {
			if (field.isPrimaryKey()) {
				String fieldName = field.getName();
				Object value = input.get(fieldName);

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
	public Void addToHist(Map<String, Object> input, Module module, String actionMesaage) {

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
				Object value = input.get(field.getName());
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

	@Override
	public List<Object> search(String fieldName, String value, Module module) {
		List<Object> results = new ArrayList<>();
		final String SQL_QUERY = "SELECT * FROM %s where %s=?";
		String sql = String.format(SQL_QUERY, module.getModuleName() + MASTER_TABLE_SUFFIX, fieldName);

		try (Connection connection = dataSource.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setString(1, value);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				ResultSetMetaData metaData = resultSet.getMetaData();
				int columnCount = metaData.getColumnCount();

				while (resultSet.next()) {
					List<Object> rowData = new ArrayList<>();
					for (int i = 1; i <= columnCount; i++) {
						Object columnData = resultSet.getObject(i);
						rowData.add(columnData);
					}
					results.add(rowData);
				}
			}

		} catch (SQLException e) {
			// Handle any exceptions that may occur during execution.
			e.printStackTrace();
		}
		return results;
	}
}
