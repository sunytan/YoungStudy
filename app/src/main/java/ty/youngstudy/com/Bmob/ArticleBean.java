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
     <body><p><img src="http://bmob-cdn-14937.b0.upaiyun.com/2017/11/06/b9c108ed40f48284803f31371d1f1599.jpg" data-filename="image name" style="width: 778px;"><br></p>
     </body>
     </html>
     * createdAt : 2017-11-06 15:21:53
     * objectId : opClDDDH
     * splash : {"__type":"File","cdn":"upyun","filename":"splash3.jpg","url":"http://bmob-cdn-14937.b0.upaiyun.com/2017/11/06/92836080401cdb1a80eb1ad1b727b4e2.jpg"}
     * title : splash3
     * updatedAt : 2017-11-06 17:38:48
     * url : http://bmob-cdn-14937.b0.upaiyun.com/2017/11/06/c649692e4069ac6480b68a4526506006.html
     */

    private String content;
    private String createdAt;
    private String objectId;
    private SplashBean splash;
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

    public SplashBean getSplash() {
        return splash;
    }

    public void setSplash(SplashBean splash) {
        this.splash = splash;
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

    public static class SplashBean {
        /**
         * __type : File
         * cdn : upyun
         * filename : splash3.jpg
         * url : http://bmob-cdn-14937.b0.upaiyun.com/2017/11/06/92836080401cdb1a80eb1ad1b727b4e2.jpg
         */

        private String __type;
        private String cdn;
        private String filename;
        private String url;

        public String get__type() {
            return __type;
        }

        public void set__type(String __type) {
            this.__type = __type;
        }

        public String getCdn() {
            return cdn;
        }

        public void setCdn(String cdn) {
            this.cdn = cdn;
        }

        public String getFilename() {
            return filename;
        }

        public void setFilename(String filename) {
            this.filename = filename;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
