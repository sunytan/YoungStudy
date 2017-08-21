package ty.youngstudy.com.reader;

import org.htmlparser.util.ParserException;

import ty.youngstudy.com.bean.Novels;

/**
 * Created by edz on 2017/8/10.
 */

public interface DataInterface {

    public Novels getSortKindNovels(String url) throws ParserException;

    public DataInterface select(String url);

    public NovelDetail getNovelDetail(String url) throws ParserException;
}
