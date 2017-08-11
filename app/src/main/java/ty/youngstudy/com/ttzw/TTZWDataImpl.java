package ty.youngstudy.com.ttzw;

import android.util.Log;

import org.htmlparser.util.ParserException;

import ty.youngstudy.com.bean.Novels;
import ty.youngstudy.com.reader.DataInterface;

/**
 * Created by edz on 2017/8/11.
 */

public class TTZWDataImpl implements DataInterface{

    private static final String TAG = "ttzw";
    @Override
    public Novels getSortKindNovels(String url) throws ParserException {
        Log.i(TAG,"getSortKindNovels");
        return TTZWUtil.getTTZWSortKindNovels(url);
    }

    @Override
    public DataInterface select(String url) {
        if (url.contains(TAG)) {
            return this;
        }
        return null;
    }
}
