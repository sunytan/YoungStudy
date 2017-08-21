package ty.youngstudy.com.mvp;

import ty.youngstudy.com.bean.Novel;
import ty.youngstudy.com.reader.DownLoadMessage;

/**
 * Created by edz on 2017/8/10.
 */

public class ViewEventMessage {
    private String eventType;
    DownLoadMessage downLoadMessage;

    public Novel novel;


    public void setNovel(Novel novel) {
        this.novel = novel;
    }

    public Novel getNovel() {
        return novel;
    }

    public void setDownLoadMessage(DownLoadMessage downLoadMessage){
        this.downLoadMessage = downLoadMessage;
    }

    public DownLoadMessage getDownLoadMessage() {
        return downLoadMessage;
    }

    public String getEventType(){
        return eventType;
    }

    public void setEventType(String eventType){
        this.eventType = eventType;
    }
}
