package ty.youngstudy.com.Bmob;

/**
 * Created by edz on 2017/11/6.
 */

public class ArticleBean {

    /**
     * content : <!DOCTYPE html>
     <html lang="en">
     <head>
     <meta charset="UTF-8" />
     <title>素材预览</title>
     </head>
     <body><p><img src="http://bmob-cdn-14937.b0.upaiyun.com/2017/11/06/f8d09c3c400998c980fd95fd2edabc79.jpg" data-filename="image name" style="width: 778px;"><br></p>
     </body>
     </html>
     * createdAt : 2017-11-06 14:29:03
     * objectId : kFlqSSSU
     * title : splash1
     * updatedAt : 2017-11-06 14:29:03
     * url : http://bmob-cdn-14937.b0.upaiyun.com/2017/11/06/0c41875e402eff5780ea05071ab27ea0.html
     */

    private String content;
    private String createdAt;
    private String objectId;
    private String title;
    private String updatedAt;
    private String url;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
