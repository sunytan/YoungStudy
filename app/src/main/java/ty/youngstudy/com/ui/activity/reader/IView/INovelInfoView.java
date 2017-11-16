package ty.youngstudy.com.ui.activity.reader.IView;

import ty.youngstudy.com.bean.Novel;

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
