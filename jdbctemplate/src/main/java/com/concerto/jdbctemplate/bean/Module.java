package com.concerto.jdbctemplate.bean;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Copyright (C) Concerto Software and Systems (P) LTD | All Rights Reserved
 *
 * @File : com.concerto.jdbctemplate.bean.Module.java
 * @Author : Suyog Kedar
 * @AddedDate : Octo 03, 2023 12:30:40 PM
 * @ModifiedBy :
 * @ModifiedDate :
 * @Purpose :
 * @Version : 1.0
 */
public class Module {
	
	@JsonProperty("ModuleName")
	private String moduleName;
	
	@JsonProperty("Fields")
	private List<Field> fields;
	

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public List<Field> getFields() {
		return fields;
	}

	public void setFields(List<Field> fields) {
		this.fields = fields;
	}

	@Override
	public String toString() {
		return "Module [moduleName=" + moduleName + ", fields=" + fields + "]";
	}

}