package com.young.study.ui.activity.reader.IView;

import java.util.List;

import com.young.study.bean.Novel;

public interface IBookShelftView {

	void showLoading();
	void hideLoading();
	
	
	List<Novel> getNeedUpdateNovels();
	
}
