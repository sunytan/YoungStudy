package ty.youngstudy.com;

import android.util.Log;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import ty.youngstudy.com.reader.ReaderModel;

/**
 * Created by edz on 2017/8/8.
 */

public class ModelManager {

    private final static String TAG = "ModelManager";
    private static Set<BaseModel> allModel = new HashSet<BaseModel>();

    public ModelManager(){

    }

    private static class SingleTon{
        private static ModelManager instance = new ModelManager();
    }

    public static ModelManager getInstance(){
        return SingleTon.instance;
    }

    public void add(BaseModel model){
        if (allModel.add(model)){

        }
    }

    public BaseModel getModel(String name){
        for (Iterator<BaseModel> it = allModel.iterator();it.hasNext();) {
            BaseModel model = it.next();
            Log.i("tanyang",model.getName());
            if (model.getName().equals(name)){
                return model;
            }
        }
        return new ReaderModel("NovelDetailActivity");
    }
}
