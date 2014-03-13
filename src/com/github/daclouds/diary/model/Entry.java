package com.github.daclouds.diary.model;

public class Entry {

	Long id;
	String title;
	String content;
	String lastUpdated;
	
	public Entry(Long id, String title, String content, String lastUpdated) {
		this.id = id;
		this.title = title;
		this.content = content;
		this.lastUpdated = lastUpdated;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(String lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	@Override
	public String toString() {
		return "Entry [id=" + id + ", title=" + title + ", content=" + content
				+ ", lastUpdated=" + lastUpdated + "]";
	}
	
}
