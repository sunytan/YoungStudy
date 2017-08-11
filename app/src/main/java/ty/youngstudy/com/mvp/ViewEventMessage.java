package ty.youngstudy.com.mvp;

import ty.youngstudy.com.reader.DownLoadMessage;

/**
 * Created by edz on 2017/8/10.
 */

public class ViewEventMessage {
    private String eventType;
    DownLoadMessage downLoadMessage;


















    public void setDownLoadMessage(DownLoadMessage downLoadMessage){
        this.downLoadMessage = downLoadMessage;
    }

    public String getEventType(){
        return eventType;
    }

    public void setEventType(String eventType){
        this.eventType = eventType;
    }
}
