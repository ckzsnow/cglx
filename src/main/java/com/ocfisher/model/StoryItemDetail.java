package com.ocfisher.model;

import java.sql.Timestamp;

public class StoryItemDetail {

	private long id;
	
	private long storyItemId;
	
	private String title;
	
	private String snapshot;
	
	private String studentName;
	
	private String studentBackground;
	
	private String enrollMajor;
	
	private String enrollElse;
	
	private String applyAdvantage;
	
	private String apply_difficulty;
	
	private String detailsInfo;
	
	private Timestamp createTime;
	
	public StoryItemDetail(){}

	public StoryItemDetail(long id, long storyItemId, String title, String snapshot, String studentName,
			String studentBackground, String enrollMajor, String enrollElse, String applyAdvantage,
			String apply_difficulty, String detailsInfo, Timestamp createTime) {
		super();
		this.id = id;
		this.storyItemId = storyItemId;
		this.title = title;
		this.snapshot = snapshot;
		this.studentName = studentName;
		this.studentBackground = studentBackground;
		this.enrollMajor = enrollMajor;
		this.enrollElse = enrollElse;
		this.applyAdvantage = applyAdvantage;
		this.apply_difficulty = apply_difficulty;
		this.detailsInfo = detailsInfo;
		this.createTime = createTime;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getStoryItemId() {
		return storyItemId;
	}

	public void setStoryItemId(long storyItemId) {
		this.storyItemId = storyItemId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSnapshot() {
		return snapshot;
	}

	public void setSnapshot(String snapshot) {
		this.snapshot = snapshot;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public String getStudentBackground() {
		return studentBackground;
	}

	public void setStudentBackground(String studentBackground) {
		this.studentBackground = studentBackground;
	}

	public String getEnrollMajor() {
		return enrollMajor;
	}

	public void setEnrollMajor(String enrollMajor) {
		this.enrollMajor = enrollMajor;
	}

	public String getEnrollElse() {
		return enrollElse;
	}

	public void setEnrollElse(String enrollElse) {
		this.enrollElse = enrollElse;
	}

	public String getApplyAdvantage() {
		return applyAdvantage;
	}

	public void setApplyAdvantage(String applyAdvantage) {
		this.applyAdvantage = applyAdvantage;
	}

	public String getApply_difficulty() {
		return apply_difficulty;
	}

	public void setApply_difficulty(String apply_difficulty) {
		this.apply_difficulty = apply_difficulty;
	}

	public String getDetailsInfo() {
		return detailsInfo;
	}

	public void setDetailsInfo(String detailsInfo) {
		this.detailsInfo = detailsInfo;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	
}
