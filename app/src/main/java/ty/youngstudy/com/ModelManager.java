package ty.youngstudy.com;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by edz on 2017/8/8.
 */

public class ModelManager {

    private final static String TAG = "ModelManager";
    private Set<BaseModel> allModel = new HashSet<BaseModel>();

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
            if (model.getName().equals(name)){
                return model;
            }
        }
        return null;
    }
}
