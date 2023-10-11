
package com.concerto.crud.config.bean;


/**
 * Copyright (C) Concerto Software and Systems (P) LTD | All Rights Reserved
 *
 * @File : com.concerto.jdbctemplate.bean.Field.java
 * @Author : Suyog Kedar
 * @AddedDate : Octo 03, 2023 12:30:40 PM
 * @ModifiedBy :
 * @ModifiedDate :
 * @Purpose :
 * @Version : 1.0
 */
public class Field {

	private String name;
	
	private String type;
	
	private Validation validation;
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Validation getValidation() {
		return validation;
	}

	public void setValidation(Validation validation) {
		this.validation = validation;
	}

	@Override
	public String toString() {
		return "Field [name=" + name + ", type=" + type + ", validation=" + validation + "]";
	}
	
	

}