package com.example.ae.yourhub;

/**
 * Created by A E on 22-Jun-17.
 */

public class NewsData {

    String id;
    String title;
    String link;
    String desc;
    String image;
    String title_news;
    NewsData(String id, String title_news,String title, String link,String desc,String image)
    {
        this.id=id;
        this.title=title;
        this.link=link;
        this.desc=desc;
        this.image=image;
        this.title_news=title_news;

    }

    public String getTitle() {
        return title;
    }

    public String getId() {
        return id;
    }

    public String getLink() {
        return link;
    }

    public String getImage() {
        return image;
    }

    public String getDesc() {
        return desc;
    }

    public String getTitle_news() {
        return title_news;
    }
}
