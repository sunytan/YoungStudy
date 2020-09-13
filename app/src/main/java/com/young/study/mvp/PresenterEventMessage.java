package com.young.study.mvp;

import com.young.study.reader.NovelDetail;
import com.young.study.reader.NovelTotleInfo;

/**
 * Created by edz on 2017/8/10.
 */

public class PresenterEventMessage {

    private NovelTotleInfo novelTotleInfo;
    private NovelDetail novelDetail;
    private ChapterEvent chapterEvent;

    public void setChapterEvent(ChapterEvent chapterEvent) {
        this.chapterEvent = chapterEvent;
    }

    public ChapterEvent getChapterEvent() {
        return chapterEvent;
    }

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

    public static class ChapterEvent {
        private int result;
        private String content;
        private int chapterId;

        public void setChapterId(int chapterId) {
            this.chapterId = chapterId;
        }

        public int getResult() {
            return result;
        }

        public void setResult(int result) {
            this.result = result;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getChapterId() {
            return chapterId;
        }
    }
}
