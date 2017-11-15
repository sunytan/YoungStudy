package ty.youngstudy.com.ttzw;

import android.text.TextUtils;
import android.util.Log;

import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.tags.Html;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.ParagraphTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import java.util.ArrayList;
import java.util.List;

import ty.youngstudy.com.bean.Novel;
import ty.youngstudy.com.bean.Novels;
import ty.youngstudy.com.reader.Chapter;
import ty.youngstudy.com.reader.NovelDetail;
import ty.youngstudy.com.util.HtmlUtil;


/**
 * Created by edz on 2017/8/11.
 */

public class TTZWUtil {

    private final static String TAG = "TTZWUtil";

    public static final String TTZW_BASE_URL = "http://m.ttzw.com/";

    private static final String RE_NEW = "更新：";
    private static final String LASTEST = "最新：";
    private static final String STATUS = "状态：";
    private static final String KIND = "类别：";
    private static final String AUTHOR = "作者：";


    public static List<Chapter> getNovelChapers(String baseUrl, String source, String tag) throws ParserException {
        List<Chapter> chapters = new ArrayList<Chapter>();
        Parser parser = new Parser(source);
        NodeList nodeList = parser
                .parse(new TagAttrFilter("DIV", "id", "chapterlist"));
        nodeList = nodeList.extractAllNodesThatMatch(new TagNameFilter("p"),
                true);
        int size = nodeList.size();
        for (int i = 0; i < size; i++) {
            TagNode p = (TagNode) nodeList.elementAt(i);
            String link = HtmlUtil.getFirstNodeAttr(p, "a", "href");
            if(link.startsWith("#"))
                continue;
            Chapter chapter = new Chapter();
            chapter.setUrl(baseUrl  + link);
            chapter.setTitle(p.toPlainTextString().trim());
            chapter.setSource(tag);
            chapters.add(chapter);
//			System.out.println(chapter);
        }
        return chapters;
    }


    public static NovelDetail getTTZWNovelDetail(String url) throws ParserException {
        Novel novel = new Novel();
        NovelDetail detail = new NovelDetail();
        detail.setNovel(novel);
        novel.setUrl(url);
        Parser parser = new Parser(url);
        NodeList nl = parser.parse(null);
        Node html = null;
        for (int i = 0; i < nl.size(); i++) {
            Node node = nl.elementAt(i);
            if (node instanceof Html) {
                html = node;
                break;
            }
        }
        NodeList spans = html.getChildren().extractAllNodesThatMatch(new TagAttrFilter("span", "class", "title"), true);
        if (spans.size() > 0) {
            TagNode span = (TagNode) spans.elementAt(0);
            novel.setName(span.toPlainTextString().trim());
        }
        Node synopsisArea = HtmlUtil.getFirstNodeByAttr(html, "div", "class","synopsisArea");
        if(synopsisArea == null) {
            synopsisArea = html;
        } else {
            TagNode chapterLink = HtmlUtil.getFirstNodeByAttr(synopsisArea, "p", "class","btn");
            String allLink = HtmlUtil.getFirstNodeAttr(chapterLink, "a", "href");
            detail.setChapterUrl(TTZW_BASE_URL + allLink);
        }
        NodeList nodes = synopsisArea.getChildren()
                .extractAllNodesThatMatch(new TagAttrFilter("div", "class", "synopsisArea_detail"), true);
        if (nodes.size() > 0) {
            nodes = nodes.elementAt(0).getChildren();
            for (int i = 0; i < nodes.size(); i++) {
                Node n = nodes.elementAt(i);
                if (n instanceof TagNode) {
                    TagNode tag = (TagNode) n;
                    if (tag instanceof ImageTag) {
                        novel.setThumb(tag.getAttribute("src"));
                    } else if (tag instanceof LinkTag) {
                        if (HtmlUtil.hasChild(tag)) {
                            System.out.println(tag.toHtml());
                            TagNode t = HtmlUtil.getFirstTagNode(tag, "p");
                            if (t != null) {
                                novel.setAuthor(t.toPlainTextString().replace(AUTHOR, "").trim());
                            }
                        }
                    } else if (tag instanceof ParagraphTag) {
                        String clas = tag.getAttribute("class");
                        if ("sort".equals(clas)) {
                            novel.setKind(tag.toPlainTextString().trim().replace(KIND, ""));
                        } else if ("author".equals(clas)) {
                            novel.setAuthor(tag.toPlainTextString().replace(AUTHOR, "").trim());
                        } else {
                            String planText = tag.toPlainTextString();
                            if (planText.contains(RE_NEW)) {
                                novel.setLastUpdateTime(planText.replace(RE_NEW, "").trim());
                            } else if (planText.contains(LASTEST)) {
                                TagNode a = HtmlUtil.getFirstTagNode(tag, "a");
                                Log.i(TAG,"最新 = "+a.toPlainTextString());
                                novel.setLastUpdateChapter(a.toPlainTextString().trim());
                                novel.setLastUpdateChapterUrl(TTZW_BASE_URL + a.getAttribute("href"));
                            }
                        }
                    }
                }
            }
        }
        TagNode revew = HtmlUtil.getFirstNodeByAttr(html, "p", "class","review");
        if(revew != null) {
            novel.setBrief(HtmlUtil.trim(revew.toPlainTextString()));
        }
        if(TextUtils.isEmpty(novel.getLastUpdateChapter())) {
            NodeList divs = html.getChildren().extractAllNodesThatMatch(new TagAttrFilter("div", "class","directoryArea"),true);
            if(divs.size() == 1) {
                Node div = divs.elementAt(0);
                Node p = HtmlUtil.getFirstTagNode(div, "p");
                if(p != null) {
                    TagNode a = HtmlUtil.getFirstTagNode(p, "a");
                    if(a != null) {
                        novel.setLastUpdateChapter(a.toPlainTextString().trim());
                        novel.setLastUpdateChapterUrl(TTZW_BASE_URL + a.getAttribute("href"));
                    }
                }
            }
        }
        if(TextUtils.isEmpty(detail.getChapterUrl())) {
            Log.e(TAG, "parser html fail,not get the chapter list,url = " +url);
            detail.setChapterUrl(url + "all.html");
        }
        novel.setChapterUrl(detail.getChapterUrl());
        return detail;
    }


    public static Novels getTTZWSortKindNovels(String url) throws ParserException{
        Log.i(TAG,"getTTZWSortKindNovels");
        Novels novels = new Novels();
        novels.setCurrentUrl(url);
        ArrayList<Novel> listnovel = new ArrayList<Novel>();
        Parser parser = new Parser(url);
        NodeList nodeList = parser.parse(new TagAttrFilter("div","class","hot_sale"));
        for (int i = 0; i < nodeList.size(); i++) {
            Node novelNode = nodeList.elementAt(i);
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
        Log.i(TAG,"novels size= "+novels.getNovels().size() + "novel = "+novels.getNovels().toString());
        return novels;
    }

//    public static Novel getTTZWOneKindNovel(String url)throws ParserException{
//        Novel novel = new Novel();
//        ArrayList<Novel> novelArrayList = new ArrayList<Novel>();
//        Parser parser = new Parser(url);
//
//
//        return novel;
//    }

    private static Novel parseNovelNode(String baseUrl,Node node) {
        Log.i(TAG,"parseNovelNode");
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
        Log.i(TAG,"novel = "+novel.toString());
        return novel;
    }



//    public static List<String> getNovelKind(String url) throws ParserException{
//        List<String> kingd
//    }
}
