package ty.youngstudy.com.reader;

import android.os.AsyncTask;
import android.util.Log;

import org.htmlparser.util.ParserException;

import java.util.ArrayList;

import io.reactivex.subjects.PublishSubject;
import ty.youngstudy.com.bean.Novel;
import ty.youngstudy.com.bean.Novels;
import ty.youngstudy.com.ttzw.SourceSelector;

/**
 * Created by edz on 2017/8/10.
 */

public class DataQueryManager implements DataInterface{

    public DataQueryManager(){}

    public NovelDetail getNovelDetail(String url) throws ParserException{
        DataInterface df = SourceSelector.selectDateSource(url);
        if (df != null)
            return df.getNovelDetail(url);
        return null;
    }

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

    public void loadNovelFromUrl(final PublishSubject subject,final String[] url,final String[] kind){
        Log.i("tanyang","loadNovelFromUrl");
        new AsyncTask<Void,Void,ArrayList<Novels>>(){
            @Override
            protected void onPostExecute(ArrayList<Novels> novelses) {
                super.onPostExecute(novelses);
                NovelTotleInfo.getInstance().setNovels(novelses);
                Log.i("tanyang","novel totle size = "+NovelTotleInfo.getInstance().getNovels().size());
                subject.onComplete();
                NovelTotleInfo.getInstance().setUpdate(false);
            }

            @Override
            protected ArrayList<Novels> doInBackground(Void... voids) {
                ArrayList<Novels> data = new ArrayList<Novels>();
                NovelTotleInfo.getInstance().setUpdate(true);
                int i = 0;
                //出错重试三次
                while (i < 3) {
                    try {
                        // 后台获取小说的种类
                        for (int j = 0; j < url.length; j++) {
                            Novels novels = new Novels();
                            novels = getSortKindNovels(url[j]);
                            // 分类无小说时，不显示分类
                            if (novels.getNovels() == null || novels.getNovels().size() <= 0) continue;
                            novels.setCurrentUrl(url[j]);
                            novels.setKindName(kind[j]);
                            subject.onNext(novels);
                            //Log.i("tanyang","novel name = "+novels.getNovels().get(0).getName());
                            data.add(novels);

                        }
                        break;
                    } catch (ParserException e) {
                        e.printStackTrace();
                        i++;
                    }
                }
                return data;
            }
        }.execute();
    }

    public void loadDataFromUrl(final PublishSubject subject,final String url){

        new AsyncTask<Void,Void,Novel>(){
            @Override
            protected Novel doInBackground(Void... voids) {
                return null;
            }

            @Override
            protected void onPostExecute(Novel novel) {
                super.onPostExecute(novel);
            }
        }.execute();
    }

}
