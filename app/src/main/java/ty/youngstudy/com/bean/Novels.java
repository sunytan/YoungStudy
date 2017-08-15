package ty.youngstudy.com.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Novels implements Parcelable {

	private ArrayList<Novel> novels;

	private String kindName;
	private boolean isok;
	
	/**the url for more result*/
	private String nextUrl;
	
	private String currentUrl;
	
	
	public Novels() {
		isok = true;
	}
	
	
	
	public boolean isIsok() {
		return isok;
	}



	public void setIsok(boolean isok) {
		this.isok = isok;
	}

	public void setKindName(String kindName) {
		this.kindName = kindName;
	}

	public String getKindName(){
		return kindName;
	}

	public Novels(ArrayList<Novel> novels) {
		this.novels = novels;
	}

	public ArrayList<Novel> getNovels() {
		return novels;
	}

	public void setNovels(ArrayList<Novel> novels) {
		this.novels = novels;
	}

	public String getNextUrl() {
		return nextUrl;
	}

	public void setNextUrl(String nextUrl) {
		this.nextUrl = nextUrl;
	}

	public String getCurrentUrl() {
		return currentUrl;
	}

	public void setCurrentUrl(String currentUrl) {
		this.currentUrl = currentUrl;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeTypedList(novels);
		dest.writeString(nextUrl);
	} 
	
	public static final Parcelable.Creator<Novels> CREATOR = new Creator<Novels>() {
		
		@Override
		public Novels[] newArray(int size) {
			// TODO Auto-generated method stub
			return new Novels[size];
		}
		
		@Override
		public Novels createFromParcel(Parcel source) {
			Novels novel = new Novels();
			novel.novels = new ArrayList<Novel>();
			source.readTypedList(novel.novels, Novel.CREATOR);
			novel.nextUrl = source.readString();
			return novel;
		}
	};
	
}
