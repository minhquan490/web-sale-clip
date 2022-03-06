package com.system.spring.response;

import java.util.Set;

public class ClipResponse {

	private long id;
	private String name;
	private long price;
	private Set<String> categories;
	private String link;

	public ClipResponse() {
		super();
	}

	public ClipResponse(long id, String name, long price, Set<String> categories) {
		super();
		this.id = id;
		this.name = name;
		this.price = price;
		this.categories = categories;
	}

	public ClipResponse(long id, String name, long price, Set<String> categories, String link) {
		super();
		this.id = id;
		this.name = name;
		this.price = price;
		this.categories = categories;
		this.link = link;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public long getPrice() {
		return price;
	}

	public Set<String> getCategories() {
		return categories;
	}

	public String getLink() {
		return link;
	}
}
