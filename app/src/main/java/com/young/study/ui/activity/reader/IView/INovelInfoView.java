package com.young.study.ui.activity.reader.IView;

import com.young.study.bean.Novel;

public interface INovelInfoView {

	
	void showLoading();
	void hideLoading();
	
	int getDownloadState();
	boolean isInbookShelf(int bookid);
	void onLoadFail();
	
	void startRead();
	void removeBookShelt(int id);
	void addBookShelft(int id);
	
	void showDownloadComplete();
	void showDownloadBook();
	void showDownloadPause();
	void showDownloadProgress(String p);
	
	void showChapters();
	
	void showNovelInfo(Novel novel);
}
