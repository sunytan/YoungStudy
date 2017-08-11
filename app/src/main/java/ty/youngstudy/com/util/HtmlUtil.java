package ty.youngstudy.com.util;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.util.NodeList;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by edz on 2017/8/10.
 */

public class HtmlUtil {

    public static String readHtml(String url){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        try {
            Response response = client.newCall(request).execute();
            String content = response.body().string();
            return content;
        }catch (IOException e){

        }
        return null;
    }

    public static NodeList getAllTagNodeChildren(Node node) {
        NodeList nl = node.getChildren();
        return nl.extractAllNodesThatMatch(new NodeFilter() {

            public boolean accept(Node node) {
                if(node instanceof TagNode)
                    return true;
                return false;
            }
        }, true);
    }

}
