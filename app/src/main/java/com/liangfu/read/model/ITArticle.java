package com.liangfu.read.model;

/**
 * @author: LF
 * @date: 2016/11/8 18:01
 */
public class ITArticle {

    /**
     * 标题
     */
    private String title;

    /**
     * 概要
     */
    private String summary;

    /**
     * 新闻url
     */
    private String url;

    /**
     * 图片地址
     */
    private String imageUrl;

    /**
     * 发布时间
     */
    private String postTime;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPostTime() {
        return postTime;
    }

    public void setPostTime(String postTime) {
        this.postTime = postTime;
    }
}
