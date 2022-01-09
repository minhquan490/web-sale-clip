package com.system.spring.request;

import java.util.HashSet;
import java.util.Set;

public class UserVo {

	private String username;

	private String password;

	private String email;

	private boolean isEnabled;

	private boolean isPremium;

	private Set<String> roles = new HashSet<String>();

	public UserVo() {
		super();
	}

	public UserVo(String username, String email, Set<String> roles, boolean isEnabled, boolean isPremium) {
		super();
		this.username = username;
		this.email = email;
		this.roles = roles;
		this.isEnabled = isEnabled;
		this.isPremium = isPremium;
	}

	public UserVo(String username, String email, Set<String> roles) {
		super();
		this.username = username;
		this.email = email;
		this.roles = roles;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}

	public String getEmail() {
		return email;
	}

	public String getUsername() {
		return username;
	}

	public boolean isEnabled() {
		return isEnabled;
	}

	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	public String getPassword() {
		return password;
	}

	public Set<String> getRoles() {
		return roles;
	}

	public boolean isPremium() {
		return isPremium;
	}

	public void setPremium(boolean isPremium) {
		this.isPremium = isPremium;
	}
}
