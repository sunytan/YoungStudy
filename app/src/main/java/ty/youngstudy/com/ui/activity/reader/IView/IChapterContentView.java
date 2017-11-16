package ty.youngstudy.com.ui.activity.reader.IView;

public interface IChapterContentView {

	
	void showLoading();
	void hideLoading();
	
	void showChapterContent(int chapter);
	void onLoadFail(int chapter);
	
	void prepareNext(String path);
	void preparaPrev(String path);
}
