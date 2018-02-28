package com.example.ae.yourhub;

/**
 * Created by A E on 12-Jul-17.
 */

public class EventData {
    String id,name,images,date,time,location;
    EventData(String id,String name,String images,String date,String time,String location)
    {
        this.id=id;
        this.name=name;
        this.images=images;
        this.date=date;
        this.location=location;
        this.time=time;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getImages() {
        return images;
    }

    public String getLocation() {
        return location;
    }

    public String getTime() {
        return time;
    }
}
