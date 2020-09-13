package com.young.study.reader;

import java.util.List;

import com.young.study.bean.Novel;

public class NovelDetail {

	private Novel novel;
	private List<Chapter> chapters;
	private String chapterUrl;
	
	
	public String getChapterUrl() {
		return chapterUrl;
	}
	public void setChapterUrl(String chapterUrl) {
		this.chapterUrl = chapterUrl;
	}
	public Novel getNovel() {
		return novel;
	}
	public void setNovel(Novel novel) {
		this.novel = novel;
	}
	public List<Chapter> getChapters() {
		return chapters;
	}
	public void setChapters(List<Chapter> chapters) {
		this.chapters = chapters;
	}
	
	
}
