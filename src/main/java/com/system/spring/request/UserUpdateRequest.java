package com.system.spring.request;

public class UserUpdateRequest {

	private String firstName;
	private String lastName;
	private String gender;
	private String birthDate;

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

	public String getBirthDate() {
		return birthDate;
	}
}
