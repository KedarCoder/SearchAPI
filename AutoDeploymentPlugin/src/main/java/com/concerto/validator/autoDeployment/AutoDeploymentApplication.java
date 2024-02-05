package com.concerto.validator.autoDeployment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.concerto.validator.autoDeployment.service.ReadZip;
import com.google.gson.Gson;
import com.google.gson.JsonObject;



@SpringBootApplication
public class AutoDeploymentApplication {
	
	@Autowired
	ReadZip readZip;

	public static void main(String[] commandLineArgs) {
	
		SpringApplication.run(AutoDeploymentApplication.class, commandLineArgs);
		System.out.println("Hello Jar");
		
		System.out.println("Project Started");
	  	Gson gson = new Gson();
        // Assuming the JSON string is the first argument
	  	 JsonObject jsonObject = null;
	  	if(commandLineArgs.length > 0) {
	  		String jsonString = commandLineArgs[0];
	        jsonObject = gson.fromJson(jsonString, JsonObject.class);
	  	}
        
        // Now you can use jsonObject as needed
        System.out.println(jsonObject);
		
	}
	
//	@Bean
//	public void AppInitializer() throws InterruptedException {
//
//		readZip.zipProcess();
//	}	

}
