package ty.youngstudy.com.ui.activity.reader.IView;

import java.util.List;

import ty.youngstudy.com.bean.Novel;

public interface IBookShelftView {

	void showLoading();
	void hideLoading();
	
	
	List<Novel> getNeedUpdateNovels();
	
}
