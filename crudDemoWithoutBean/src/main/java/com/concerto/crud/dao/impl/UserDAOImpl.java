package com.concerto.crud.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.concerto.crud.config.bean.CrudInterface;
import com.concerto.crud.config.bean.User;
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

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	public void createUser(CrudInterface user) {

		String sql = "INSERT INTO [user] (name, city, age, gender) VALUES (?, ?, ?, ?)";

		jdbcTemplate.update(connection -> {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			User customUser = (User) user;
			preparedStatement.setString(1, customUser.getName());
			preparedStatement.setString(2, customUser.getCity());
			preparedStatement.setInt(3, customUser.getAge());
			preparedStatement.setString(4, customUser.getGender());
			return preparedStatement;
		});
	}

	@Override
	public void deleteUser(int userId) {
		String sql = "DELETE FROM [user] WHERE userId=?";

		jdbcTemplate.update(sql, userId);
	}

	@SuppressWarnings("deprecation")
	@Override
	public User getUserByName(String name) {
		String sql = "SELECT * FROM [user] WHERE name=?";

		return jdbcTemplate.queryForObject(sql, new Object[] { name }, new UserRowMapper());
	}

	private static class UserRowMapper implements RowMapper<User> {
		@Override
		public User mapRow(ResultSet rs, int rowNum) throws SQLException {
			User user = new User();
			user.setName(rs.getString("name"));
			user.setCity(rs.getString("city"));
			user.setAge(rs.getInt("age"));
			user.setGender(rs.getString("gender"));
			return user;
		}

	}

	@Override
	public void updateUser(CrudInterface user, int id) {
		String sql = "UPDATE [user] SET name=?, city=?, age=?, gender=? WHERE UserId=?";

		jdbcTemplate.update(connection -> {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			User customUser = (User) user;
			preparedStatement.setString(1, customUser.getName());
			preparedStatement.setString(2, customUser.getCity());
			preparedStatement.setInt(3, customUser.getAge());
			preparedStatement.setString(4, customUser.getGender());
			preparedStatement.setLong(5, id);
			return preparedStatement;
		});
	}
}
