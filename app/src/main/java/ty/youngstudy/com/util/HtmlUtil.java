package ty.youngstudy.com.util;

import android.text.TextUtils;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.util.NodeList;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import ty.youngstudy.com.ttzw.TagAttrFilter;


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

    public static String parseContent(Node node) {
        String content = "";
        int size = node.getChildren().size();
        for (int i = 0; i < size; i++) {
            // System.out.println(node.getChildren().elementAt(i).getClass().getName());
            Node cn = node.getChildren().elementAt(i);
            if (cn instanceof TextNode) {
                content += cn.toPlainTextString().replace("&nbsp;", " ");
//					System.out.println(cn.toPlainTextString());
            } else if (cn instanceof TagNode) {
                TagNode tn = (TagNode) cn;
                if ("br".equalsIgnoreCase(tn.getTagName())) {
                    content += "\n";
                }
                // System.out.println(tn.getTagName());
            }
        }
        return content;
    }

    public static TagNode getFirstNodeByAttr(Node parent,String tag,String ...attr) {
        NodeList nl = parent.getChildren().extractAllNodesThatMatch(new TagAttrFilter(tag, attr), true);
        if(nl.size() > 0)
            return (TagNode) nl.elementAt(0);
        return null;

    }

    /**get the first node that match the gived tag*/
    public static String getFirstNodeAttr(Node parent,String tag,String attr) {
        NodeList nl = parent.getChildren();
        int size = nl.size();
        if(nl.size() == 0) {
            return null;
        }
        for(int i=0;i<size;i++) {
            Node node =  nl.elementAt(i);
            if(node instanceof TagNode) {
                TagNode tnode = (TagNode) node;
                if(tnode.getTagName().equalsIgnoreCase(tag)) {
                    return tnode.getAttribute(attr);
                }
            }
        }
        return null;
    }

    public static boolean hasChild(Node node) {
        NodeList children = node.getChildren();
        return children != null && children.size() > 0;
    }

    public static String trim(String text) {
        if(TextUtils.isEmpty(text)) {
            return "";
        }
        return text.trim().replace("&nbsp;", " ");
    }

    public static TagNode getFirstTagNode(Node parent,String tag) {
        NodeList nl = parent.getChildren();
        int size = nl.size();
        if(nl.size() == 0) {
            return null;
        }
        for(int i=0;i<size;i++) {
            Node node =  nl.elementAt(i);
            if(node instanceof TagNode) {
                TagNode tnode = (TagNode) node;
                if(tnode.getTagName().equalsIgnoreCase(tag)) {
                    return tnode;
                }
            }
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
