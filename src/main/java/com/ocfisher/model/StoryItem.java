package com.ocfisher.model;

import java.sql.Timestamp;

public class StoryItem {

	private long id;
	
	private String snapshot;
	
	private String  title;
	
	private String baseInfo;
	
	private String detailInfo;
	
	private Timestamp create_time;

	public StoryItem() {}
	
	public StoryItem(long id, String snapshot, String title, String baseInfo, String detailInfo,
			Timestamp create_time) {
		this.id = id;
		this.snapshot = snapshot;
		this.title = title;
		this.baseInfo = baseInfo;
		this.detailInfo = detailInfo;
		this.create_time = create_time;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getSnapshot() {
		return snapshot;
	}

	public void setSnapshot(String snapshot) {
		this.snapshot = snapshot;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBaseInfo() {
		return baseInfo;
	}

	public void setBaseInfo(String baseInfo) {
		this.baseInfo = baseInfo;
	}

	public String getDetailInfo() {
		return detailInfo;
	}

	public void setDetailInfo(String detailInfo) {
		this.detailInfo = detailInfo;
	}

	public Timestamp getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Timestamp create_time) {
		this.create_time = create_time;
	}
	
}
