package ty.youngstudy.com.ttzw;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ty.youngstudy.com.reader.DataInterface;

/**
 * Created by edz on 2017/8/11.
 */

public class SourceSelector {

    private static List<DataInterface> mDataInterface;

    private static DataInterface defaultInterface;

    public static void init(){
        Log.i("tanyang","init");
        mDataInterface = new ArrayList<DataInterface>();
        defaultInterface = new TTZWDataImpl();
        mDataInterface.add(defaultInterface);
    }

    public static DataInterface selectDateSource(String url){
        Log.i("tanyang",mDataInterface+"");
        for (DataInterface dataInterface : mDataInterface){
            if (dataInterface.select(url) != null) {
                return dataInterface;
            }
        }
        return null;
    }
}
