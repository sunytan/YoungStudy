package ty.youngstudy.com.reader;

import org.htmlparser.util.ParserException;

import ty.youngstudy.com.bean.Novels;
import ty.youngstudy.com.ttzw.SourceSelector;

/**
 * Created by edz on 2017/8/10.
 */

public class DataQueryManager implements DataInterface{

    public DataQueryManager(){}

    @Override
    public Novels getSortKindNovels(String url) throws ParserException {
        DataInterface df = SourceSelector.selectDateSource(url);
        if (df != null)
            return df.getSortKindNovels(url);
        return null;
    }

    @Override
    public DataInterface select(String url) {
        return SourceSelector.selectDateSource(url);
    }

    static class Single{
        private static DataQueryManager instance = new DataQueryManager();
    }

    public static DataQueryManager instance() {
        return Single.instance;
    }

}
