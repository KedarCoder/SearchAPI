package com.concerto.crud.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.concerto.crud.repository.UserRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	public void addUsers(List<Map<String, Object>> usersList) {
		for (Map<String, Object> userParams : usersList) {
			String name = (String) userParams.get("name");
			String city = (String) userParams.get("city");
			Integer age = (Integer) userParams.get("age");
			String gender = (String) userParams.get("gender");

			if (name == null || city == null || age == null || gender == null) {
				System.err.println("Invalid request. Missing required parameters.");
				continue;
			}

			Map<String, Object> params = new HashMap<>();
			params.put("name", name);
			params.put("city", city);
			params.put("age", age);
			params.put("gender", gender);
			userRepository.saveWithMap(params);
		}
	}

	public void updateUser(Long id, Map<String, Object> userMap) {
		userMap.put("id", id);
		userRepository.updateWithMap(userMap);
	}
}
