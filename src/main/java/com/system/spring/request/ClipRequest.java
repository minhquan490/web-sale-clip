package com.system.spring.request;

public class ClipRequest {

	private String clipName;
	private String price;
	private String[] categories;
	private boolean isEnable;

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

	public boolean isEnable() {
		return isEnable;
	}
}
