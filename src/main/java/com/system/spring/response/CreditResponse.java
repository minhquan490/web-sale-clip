package com.system.spring.response;

public class CreditResponse {

	private String creditNumber;

	public CreditResponse(String creditNumber) {
		super();
		this.creditNumber = creditNumber;
	}

	public String getCreditNumber() {
		return creditNumber;
	}
}
