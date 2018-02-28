package com.example.ae.yourhub;

/**
 * Created by A E on 3/13/2017.
 */

public class ForumData {
    String title;
    String description;
    String image;
    String id;
    int type;
    int like;
    String time;
    ForumData(String title, String description, String image, int type)
    {
        this.title=title;
        this.description=description;
        this.image=image;
        this.type=type;
    }
    ForumData(String id, String title, String description, String image, int type, String time)
    {
        this.id=id;
        this.title=title;
        this.description=description;
        this.image=image;
        this.type=type;
        this.time=time;
    }
    ForumData(String id, String title, String description, String image, int type, String time, int like)
    {
        this.id=id;
        this.title=title;
        this.description=description;
        this.image=image;
        this.type=type;
        this.time=time;
        this.like=like;
    }
    String getTitle()
    {
        return title;
    }
    String getDescription()
    {
        return description;
    }
    String getImage()
    {
        return image;
    }
    public String getId() {
        return id;
    }

    public String getTime() {
        return time;
    }

    public int getType() {
        return type;
    }

    public int getLike() {
        return like;
    }
}
