package ty.youngstudy.com.ttzw;

import android.util.Log;

import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.ParagraphTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import java.util.ArrayList;
import java.util.List;

import ty.youngstudy.com.bean.Novel;
import ty.youngstudy.com.bean.Novels;
import ty.youngstudy.com.util.HtmlUtil;


/**
 * Created by edz on 2017/8/11.
 */

public class TTZWUtil {

    private final static String TAG = "TTZWUtil";

    public static final String TTZW_BASE_URL = "http://m.ttzw.com/";



    public static Novels getTTZWSortKindNovels(String url) throws ParserException{
        Log.i(TAG,"getTTZWSortKindNovels");
        Novels novels = new Novels();
        novels.setCurrentUrl(url);
        ArrayList<Novel> listnovel = new ArrayList<Novel>();
        Parser parser = new Parser(url);
        NodeList nodeList = parser.parse(new TagAttrFilter("div","class","hot_sale"));
        for (int i = 0; i < nodeList.size(); i++) {
            Node novelNode = nodeList.elementAt(i);
            //Log.i(TAG,novelNode+"");
            Novel novel1 = parseNovelNode(TTZW_BASE_URL,novelNode);
            listnovel.add(novel1);
        }
        parser.reset();
        NodeList nl = parser.extractAllNodesThatMatch(new TagAttrFilter("a","id","nextPage"));
        if (nl.size()>0) {
            TagNode tagNode = (TagNode) nl.elementAt(0);
            novels.setNextUrl(TTZW_BASE_URL + tagNode.getAttribute("href"));
        }
        novels.setNovels(listnovel);
        return novels;
    }

    private static Novel parseNovelNode(String baseUrl,Node node) {
        NodeList nl = HtmlUtil.getAllTagNodeChildren(node);
        Novel novel = new Novel();
        for(int i=0;i<nl.size();i++) {
            TagNode tag = (TagNode) nl.elementAt(i);
            if(tag.isEndTag()) {
                continue;
            }
            if(tag instanceof LinkTag) {
                novel.setUrl(baseUrl + tag.getAttribute("href"));
            } else if(tag instanceof ImageTag) {
                novel.setThumb(tag.getAttribute("data-original"));
            } else if(tag instanceof ParagraphTag) {
                String classAttr = tag.getAttribute("class");
                if("author".equals(classAttr)) {
                    String author = tag.toPlainTextString().trim();
                    Log.i(TAG,"author"+author);
                    novel.setAuthor(author.substring(author.indexOf("：") + 1));
                } else if("title".equals(classAttr)) {
                    novel.setName(tag.toPlainTextString().trim());
                } else if("review".equals(classAttr)) {
                    novel.setBrief(tag.toPlainTextString().trim().replace("&nbsp;", " "));
                }
            }
        }
        return novel;
    }

//    public static List<String> getNovelKind(String url) throws ParserException{
//        List<String> kingd
//    }
}
