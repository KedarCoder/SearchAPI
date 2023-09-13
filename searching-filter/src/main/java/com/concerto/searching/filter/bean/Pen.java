package com.concerto.searching.filter.bean;

public class Pen {
	
	private Long id;
	private String name;
	private String brand;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	
	public Pen(Long id, String name, String brand) {
	
		this.id = id;
		this.name = name;
		this.brand = brand;
	}
	
	@Override
	public String toString() {
		return "Pen [id=" + id + ", name=" + name + ", brand=" + brand + "]";
	}
	
	
	

}
