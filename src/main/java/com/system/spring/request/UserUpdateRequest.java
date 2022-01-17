package com.system.spring.request;

import java.sql.Date;

public class UserUpdateRequest {
	private String firstName;
	private String lastName;
	private String gender;
	private Date birthDate;

	public UserUpdateRequest() {
		super();
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getGender() {
		return gender;
	}

	public Date getBirthDate() {
		return birthDate;
	}
}
