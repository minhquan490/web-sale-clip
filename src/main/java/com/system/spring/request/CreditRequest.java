package com.system.spring.request;

public class CreditRequest {

	private String creditNumber;
	private String passCode;
	private String classification;
	private String expire;

	public CreditRequest() {
		super();
	}

	public String getCreditNumber() {
		return creditNumber;
	}

	public String getPassCode() {
		return passCode;
	}

	public String getClassification() {
		return classification;
	}

	public String getExpire() {
		return expire;
	}
}
