package com.concerto.validator.autoDeployment.init;


import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;



/**
 * Copyright (C) Concerto Software and Systems (P) LTD | All Rights Reserved
 * AppInitializer.java
 * @File : com.concerto.validator.autoDeployment.init.AppInitializer.java
 * @Author : Suyog Kedar
 * @AddedDate : Feb 05, 2024 17:30:40 PM
 * @Purpose : Initializes and Load data on application.prpo startup using the
 * @Version : 1.0
 */

@Component
public class AppInitializer implements CommandLineRunner {

	
	private static final Logger logger = LoggerFactory.getLogger(AppInitializer.class);

	public static Properties props = new Properties();


	@Override
	public void run(String... args) throws Exception {
		try {
			props.load(new FileInputStream(new File("D:\\Validator\\Projects\\AutoDeploymentPlugin\\Config\\Configuration\\config.properties")));
			
		} catch (Exception e) {
			logger.error("Error while run AppInitializer", e);
		}
	}

	public static Properties getProps() {
		return props;
	}

	public static void setProps(Properties props) {
		AppInitializer.props = props;
	}
	

}