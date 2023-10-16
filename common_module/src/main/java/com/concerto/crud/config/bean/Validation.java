package com.concerto.crud.config.bean;

/**
 * Copyright (C) Concerto Software and Systems (P) LTD | All Rights Reserved
 *
 * @File : com.concerto.jdbctemplate.bean.Validation.java
 * @Author : Suyog Kedar
 * @AddedDate : Octo 03, 2023 12:30:40 PM
 * @ModifiedBy :
 * @ModifiedDate :
 * @Purpose :
 * @Version : 1.0
 */

public class Validation {

	private String pattern;

	private int maxLength;

	private int minimum;

	private int maximum;

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public int getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}

	public int getMinimum() {
		return minimum;
	}

	public void setMinimum(int minimum) {
		this.minimum = minimum;
	}

	public int getMaximum() {
		return maximum;
	}

	public void setMaximum(int maximum) {
		this.maximum = maximum;
	}

	@Override
	public String toString() {
		return "Validation [pattern=" + pattern + ", maxLength=" + maxLength + ", minimum=" + minimum + ", maximum="
				+ maximum + "]";
	}

}
