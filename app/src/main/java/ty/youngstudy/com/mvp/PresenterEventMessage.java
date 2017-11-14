package ty.youngstudy.com.mvp;

import ty.youngstudy.com.reader.NovelDetail;
import ty.youngstudy.com.reader.NovelTotleInfo;

/**
 * Created by edz on 2017/8/10.
 */

public class PresenterEventMessage {

    private NovelTotleInfo novelTotleInfo;
    private NovelDetail novelDetail;

    public NovelDetail getNovelDetail() {
        return novelDetail;
    }

    public void setNovelDetail(NovelDetail novelDetail) {
        this.novelDetail = novelDetail;
    }

    public NovelTotleInfo getNovelTotleInfo() {
        return novelTotleInfo;
    }

    public void setNovelTotleInfo(NovelTotleInfo novelTotleInfo) {
        this.novelTotleInfo = novelTotleInfo;
    }
}
