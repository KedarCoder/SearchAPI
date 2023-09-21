package com.concerto.searching.filter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
		System.out.println("Done");

	}
}

// {
// "filters": [
//
// {
// "fieldName": "brand",
// "operator": "ge",
// "value": "Cello"
//
// }
//
// ],
// "page": 0,
// "size": 2
// }
