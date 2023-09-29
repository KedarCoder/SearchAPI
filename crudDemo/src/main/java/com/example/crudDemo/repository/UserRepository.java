package com.example.crudDemo.repository;

import java.util.List;
import java.util.Map;

import com.example.crudDemo.bean.User;

public interface UserRepository {
	
	void save(User user);

    User findById(Long id);

    List<User> findAll();

    void update(User user);

    void delete(Long id);

    void saveWithMap(Map<String, String> params);

    User findByIdWithMap(Map<String, Object> params);
    
}
