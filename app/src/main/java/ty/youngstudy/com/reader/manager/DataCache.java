package ty.youngstudy.com.reader.manager;

import android.util.Log;

import java.util.List;

import ty.youngstudy.com.bean.ShelftBook;

public class DataCache {

	
	private static DataCache mCache;
	
	private static final String TAG = "DataCache";
	
	private List<ShelftBook> mShelftBooks;
	
	public synchronized static DataCache instance() {
		if(mCache == null)
			mCache = new DataCache();
		return mCache;
	}
	
	public List<ShelftBook> getCacheShelft() {
		Log.v(TAG, "getCacheShelft");
		return mShelftBooks;
	}
	
	public void setCacheShelft(List<ShelftBook> list) {
		mShelftBooks = list;
	}
	
	public int size() {
		if(mShelftBooks == null) {
			return 0;
		}
		return mShelftBooks.size();
	}
	
	public void removeBook(int bookid) {
		if(mShelftBooks != null) {
			for(ShelftBook book : mShelftBooks) {
				if(bookid == book.getId()) {
					mShelftBooks.remove(book);
					return;
				}
			}
		}
	}
}
