package com.concerto.crud;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.concerto.crud.service.JsonToJavaConverter;

@Component
public class AppInitializer implements CommandLineRunner {

	/*Field*/	
	private final JsonToJavaConverter jsonToJavaConverter;

	/*Constructor*/
	@Autowired
	public AppInitializer(JsonToJavaConverter jsonToJavaConverter) {
		this.jsonToJavaConverter = jsonToJavaConverter;
	}

	/*CommandLineRunner run Method*/
	@Override
	public void run(String... args) throws Exception {
		jsonToJavaConverter.moduleMap();
		
	}

}
