package com.example.crudDemo.repository;

import java.util.Map;

public interface UserRepository {

	void saveWithMap(Map<String, Object> params);

	void updateWithMap(Map<String, Object> params);

}
