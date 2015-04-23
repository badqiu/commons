package com.github.rapid.common.jdbc.sqlgenerator.support;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

public class RssInfo {
	
	private int rssId;
	private String rssTitle;
	private String rssContent;
	
	@Id
	public int getRssId() {
		return rssId;
	}
	public void setRssId(int rssId) {
		this.rssId = rssId;
	}
	
	public String getRssTitle() {
		return rssTitle;
	}
	public void setRssTitle(String rssTitle) {
		this.rssTitle = rssTitle;
	}
	
	public String getRssContent() {
		return rssContent;
	}
	public void setRssContent(String rssContent) {
		this.rssContent = rssContent;
	}

	
}
