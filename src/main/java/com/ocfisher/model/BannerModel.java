package com.ocfisher.model;

import java.sql.Timestamp;

public class BannerModel {

	private long id;
	
	private String fileName;
	
	private long foreignKey;
	
	private String url;
	
	private String tag;
	
	private String title;
	
	private Timestamp create_time;
	
	public BannerModel(){}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public BannerModel(long id, String fileName, long foreignKey, String url, String tag, String title,
			Timestamp create_time) {
		super();
		this.id = id;
		this.fileName = fileName;
		this.foreignKey = foreignKey;
		this.url = url;
		this.tag = tag;
		this.title = title;
		this.create_time = create_time;
	}

	public BannerModel(long id, String fileName, long foreignKey, Timestamp create_time) {
		super();
		this.id = id;
		this.fileName = fileName;
		this.foreignKey = foreignKey;
		this.create_time = create_time;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public long getForeignKey() {
		return foreignKey;
	}

	public void setForeignKey(long foreignKey) {
		this.foreignKey = foreignKey;
	}

	public Timestamp getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Timestamp create_time) {
		this.create_time = create_time;
	}	
}
