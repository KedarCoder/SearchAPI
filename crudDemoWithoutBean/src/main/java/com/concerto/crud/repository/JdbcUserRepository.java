package com.concerto.crud.repository;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcUserRepository implements UserRepository {

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Override
	public void saveWithMap(Map<String, Object> params) {
	    String sql = "INSERT INTO [user] (name, city, age, gender) VALUES (:name, :city, :age, :gender)";
	    namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(params));
	    System.out.println("Transaction Completed");
	}


	@Override
	public void updateWithMap(Map<String, Object> params) {
		String sql = "UPDATE [user] SET name=:name WHERE id=:id";
		namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(params));
	}

}
