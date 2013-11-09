package com.webgis.application;

public class Parameter {
	private Integer Class_id;
	private Integer parameter_id;
	private String category;
	
	
	public  Parameter(String cat,Integer class_id,Integer parameter_id) 
	{this.category=cat;
	this.Class_id=class_id;
	this.parameter_id=parameter_id;
		
	}
	
	public String toString()
	{
		StringBuilder r= new StringBuilder("Parametro :");
		r.append(this.category);
		r.append("(");
		r.append(this.Class_id);
		r.append(",");
		r.append(this.parameter_id);
		r.append(")");
		return r.toString();
	}
	public Integer getClass_id() {
		return Class_id;
	}


	public void setClass_id(Integer class_id) {
		Class_id = class_id;
	}


	public Integer getParameter_id() {
		return parameter_id;
	}


	public void setParameter_id(Integer parameter_id) {
		this.parameter_id = parameter_id;
	}


	public String getCategory() {
		return category;
	}


	public void setCategory(String category) {
		this.category = category;
	}
	
}
