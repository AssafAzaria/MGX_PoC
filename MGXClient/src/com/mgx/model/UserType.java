package com.mgx.model;

public enum UserType {
	ADMIN("Admin"), 
	EMERGENCY_USER("Emergencu User"),
	SUPER_USER("Super User"),
	HEAD_TECHNOLOGIST("Head Technologoist"),
	TECHNOLOGIST("Technologist");
	
	private String name;
	
	private UserType(String name)
	{
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
