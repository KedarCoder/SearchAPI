package com.example.crudDemo.repository;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.crudDemo.bean.User;

@Repository
public class JdbcUserRepository implements UserRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Override
	public void save(User user) {
		String sql = "INSERT INTO [user] (name) VALUES (?)";
		jdbcTemplate.update(sql, user.getName());
	}

	@Override
	public User findById(Long id) {
		String sql = "SELECT * FROM [user] WHERE id=?";
		return jdbcTemplate.queryForObject(sql, new Object[] { id }, new BeanPropertyRowMapper<>(User.class));
	}

	@Override
	public List<User> findAll() {
		String sql = "SELECT * FROM [user]";
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class));
	}

	@Override
	public void update(User user) {
		String sql = "UPDATE [user] SET name=? WHERE id=?";
		jdbcTemplate.update(sql, user.getName(), user.getId());
	}

	@Override
	public void delete(Long id) {
		String sql = "DELETE FROM [user] WHERE id=?";
		jdbcTemplate.update(sql, id);
	}

	@Override
	public void saveWithMap(Map<String, String> params) {
		String sql = "UPDATE [user] SET name=? WHERE id=?";
		jdbcTemplate.update(sql, params.get("name"), params.get("id"));
		System.out.println("Transaction Completed");
	}

	@Override
	public User findByIdWithMap(Map<String, Object> params) {
		String sql = "SELECT * FROM [user] WHERE id = :id";
		return namedParameterJdbcTemplate.queryForObject(sql, params, new BeanPropertyRowMapper<>(User.class));
	}

}
