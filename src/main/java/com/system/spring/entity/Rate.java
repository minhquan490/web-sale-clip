package com.system.spring.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "Rate")
public class Rate implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "rate_id")
	private long id;

	@Column(name = "value", length = 6)
	private int value;

	@Column(name = "description")
	private String description;

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "rates")
	private Set<Clip> clips = new HashSet<>();

	public Rate() {
		super();
	}

	public Rate(int value, String description) {
		super();
		this.value = value;
		this.description = description;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<Clip> getClips() {
		return clips;
	}

	public void setClips(Set<Clip> clips) {
		this.clips = clips;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

}
