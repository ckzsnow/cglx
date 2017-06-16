package com.ocfisher.model;

import java.sql.Timestamp;

public class StoryItemTag {
	
	private long id;
	
	private long itemId;
	
	private long tagId;
	
	private Timestamp create_time;
	
	public StoryItemTag(){}

	public StoryItemTag(long id, long itemId, long tagId, Timestamp create_time) {
		super();
		this.id = id;
		this.itemId = itemId;
		this.tagId = tagId;
		this.create_time = create_time;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getItemId() {
		return itemId;
	}

	public void setItemId(long itemId) {
		this.itemId = itemId;
	}

	public long getTagId() {
		return tagId;
	}

	public void setTagId(long tagId) {
		this.tagId = tagId;
	}

	public Timestamp getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Timestamp create_time) {
		this.create_time = create_time;
	}	
}
