package com.young.study.util;

import android.text.TextUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.young.study.bean.ShelftBook;

public class SortUtil {

	
	public static void sort(List<ShelftBook> novels) {
		Collections.sort(novels, c1);
	}
	
	 static Comparator<ShelftBook> c1 = new Comparator<ShelftBook>() {

		@Override
		public int compare(ShelftBook o1, ShelftBook o2) {
			if(TextUtils.isEmpty(o2.getReadTime())) {
				if(TextUtils.isEmpty(o1.getReadTime()))
					return 0;
				return -1;
			}
			if(TextUtils.isEmpty(o1.getReadTime())) {
				return 1;
			}
			return o2.getReadTime().compareTo(o1.getReadTime());
		}
	};
	
	 static  Comparator<ShelftBook> c2 = new Comparator<ShelftBook>() {

		@Override
		public int compare(ShelftBook o1, ShelftBook o2) {
			return o1.name.compareTo(o2.name);
		}
	};
}
