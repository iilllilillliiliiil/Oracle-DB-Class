package com.java.test;

import java.util.*;

public abstract class User {
	protected String id;
	protected String name;
	protected String password;

	public User(String id, String name, String password) {
		this.id = id;
		this.name = name;
		this.password = password;
	}

	public abstract void showMenu(Scanner sc, List<Course> courseList);
}