package com.liangfu.read.model;

/**
 * @author: LF
 * @date: 2016/11/10 14:53
 */
public class Chapter implements Comparable<Chapter>{

    /**
     * 章节名
     */
    private String chapter;

    /**
     * 章节URL
     */
    private String chapterUrl;

    public String getChapter() {
        return chapter;
    }

    public void setChapter(String chapter) {
        this.chapter = chapter;
    }

    public String getChapterUrl() {
        return chapterUrl;
    }

    public void setChapterUrl(String chapterUrl) {
        this.chapterUrl = chapterUrl;
    }

    @Override
    public int compareTo(Chapter chapter) {
        return this.getChapter().compareTo(chapter.getChapter());
    }
}
