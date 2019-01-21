package com.liangfu.read.model;

/**
 * @author: LF
 * @date: 2016/11/10 10:48
 */
public class Book {

    /**
     * 标题
     */
    private String title;

    /**
     * 该书的URL
     */
    private String bookUrl;

    /**
     * 最新章节
     */
    private String newChapter;

    /**
     * 最新章节的URL
     */
    private String newChapterUrl;

    /**
     * 作者
     */
    private String author;

    /**
     * 作者书籍URL
     */
    private String authorUrl;

    /**
     * 更新时间
     */
    private String updTime;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNewChapter() {
        return newChapter;
    }

    public void setNewChapter(String newChapter) {
        this.newChapter = newChapter;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getUpdTime() {
        return updTime;
    }

    public void setUpdTime(String updTime) {
        this.updTime = updTime;
    }

    public String getBookUrl() {
        return bookUrl;
    }

    public void setBookUrl(String bookUrl) {
        this.bookUrl = bookUrl;
    }

    public String getNewChapterUrl() {
        return newChapterUrl;
    }

    public void setNewChapterUrl(String newChapterUrl) {
        this.newChapterUrl = newChapterUrl;
    }

    public String getAuthorUrl() {
        return authorUrl;
    }

    public void setAuthorUrl(String authorUrl) {
        this.authorUrl = authorUrl;
    }
}
