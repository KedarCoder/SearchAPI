package com.concerto.crud.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.concerto.crud.config.bean.Module;
import com.concerto.crud.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

//	public void add(Map<String, Object> userParams, String module) {
//		try {
//			// Use reflection to create an instance of the specified class
//			Class<?> clazz = Class.forName(module);
//			
//			Object beanObject = clazz.getDeclaredConstructor().newInstance();
//			
//			// Create an ObjectMapper
//			ObjectMapper objectMapper = new ObjectMapper();
//
//			// Convert the dataMap to a PersonBean object
//			beanObject = objectMapper.convertValue(userParams, clazz);
//			
//			
//		} catch (Exception e) {
//		}
//	}
//
//	public void updateUser(Long id, Map<String, Object> userMap) {
//		userMap.put("id", id);
//		userRepository.updateWithMap(userMap);
//	}
}
