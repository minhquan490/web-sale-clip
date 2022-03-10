package com.system.spring.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@SuppressWarnings("serial")
@Entity
@Table(name = "Clip")
public class Clip implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "clip_id")
	private long id;

	@Column(name = "clip_name")
	private String name;

	@Column(name = "clip_link")
	private String link;

	@Column(name = "price")
	private long price;

	@Column(name = "enabled", columnDefinition = "TINYINT")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	private boolean isEnabled;

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "clip_rate", joinColumns = @JoinColumn(name = "clip_id"), inverseJoinColumns = @JoinColumn(name = "rate_id"))
	private Set<Rate> rates = new HashSet<>();

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "clip_category", joinColumns = @JoinColumn(name = "clip_id"), inverseJoinColumns = @JoinColumn(name = "category_id"))
	private Set<Category> categories = new HashSet<>();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "clipsHaveBeenPurchased")
	private Set<User> usersPurchased = new HashSet<>();

	public Clip() {
		super();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public Set<Category> getCategories() {
		return categories;
	}

	public void setCategories(Set<Category> categories) {
		this.categories = categories;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public long getPrice() {
		return price;
	}

	public void setPrice(long price) {
		this.price = price;
	}

	public Set<User> getUsersPurchased() {
		return usersPurchased;
	}

	public void setUsersPurchased(Set<User> usersPurchased) {
		this.usersPurchased = usersPurchased;
	}

	public boolean isEnabled() {
		return isEnabled;
	}

	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	public Set<Rate> getRates() {
		return rates;
	}

	public void setRates(Set<Rate> rates) {
		this.rates = rates;
	}
}
