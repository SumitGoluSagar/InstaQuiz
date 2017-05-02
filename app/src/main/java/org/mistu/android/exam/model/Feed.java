package org.mistu.android.exam.model;

/**
 * Created by kedee on 26/4/17.
 *
 */

public class Feed {
    private String title;
    private String text;
    private FeedFormat feedFormat;
    private String resourceUrl;
    private String subject;
    private String chapter;
    private String topic;
    private FeedType feedType;
    private String tag1;
    private String tag2;
    private String tag3;

    public Feed() {

    }

    public Feed(String title, String text, FeedFormat feedFormat, String resourceUrl, String subject) {
        this.title = title;
        this.text = text;
        this.feedFormat = feedFormat;
        this.resourceUrl = resourceUrl;
        this.subject = subject;
    }

    public Feed(String title, String text, FeedFormat feedFormat, String subject) {
        this.title = title;
        this.text = text;
        this.feedFormat = feedFormat;
        this.subject = subject;
    }

    public String getResourceUrl() {
        return resourceUrl;
    }

    public void setResourceUrl(String resourceUrl) {
        this.resourceUrl = resourceUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public FeedFormat getFeedFormat() {
        return feedFormat;
    }

    public void setFeedFormat(FeedFormat feedFormat) {
        this.feedFormat = feedFormat;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getChapter() {
        return chapter;
    }

    public void setChapter(String chapter) {
        this.chapter = chapter;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public FeedType getFeedType() {
        return feedType;
    }

    public void setFeedType(FeedType feedType) {
        this.feedType = feedType;
    }

    public String getTag1() {
        return tag1;
    }

    public void setTag1(String tag1) {
        this.tag1 = tag1;
    }

    public String getTag2() {
        return tag2;
    }

    public void setTag2(String tag2) {
        this.tag2 = tag2;
    }

    public String getTag3() {
        return tag3;
    }

    public void setTag3(String tag3) {
        this.tag3 = tag3;
    }
}
