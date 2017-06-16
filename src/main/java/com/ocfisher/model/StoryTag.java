package com.ocfisher.model;

import java.sql.Timestamp;

public class StoryTag {

	private long id;
	
	private String name;
	
	private Timestamp create_time;
	
	public StoryTag() {}

	public StoryTag(long id, String name, Timestamp create_time) {
		super();
		this.id = id;
		this.name = name;
		this.create_time = create_time;
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

	public Timestamp getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Timestamp create_time) {
		this.create_time = create_time;
	}
}
