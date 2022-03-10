package com.system.spring.request;

public class ClipRequest {

	private String clipName;
	private String price;
	private String[] categories;
	private String isEnable;

	public ClipRequest() {
		super();
	}

	public String getClipName() {
		return clipName;
	}

	public String getPrice() {
		return price;
	}

	public String[] getCategories() {
		return categories;
	}

	public String isEnable() {
		return isEnable;
	}
}
