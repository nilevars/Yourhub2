package com.example.ae.yourhub;

/**
 * Created by A E on 20-Jul-17.
 */

public class DirectoryData {
    String name;
    String link;
    String type;
    String location;
    String latitude;
    String longitude;
    String image;
    String id;
    DirectoryData(String id, String name, String link,String type,String location,String latitude,String longitude, String image)
    {
        this.id=id;
        this.name=name;
        this.link=link;
        this.image="http://q8hub.com/yourhub/uploads/"+image;
        this.type=type;
        this.location=location;
        this.latitude=latitude;
        this.longitude=longitude;
    }

    public String getId() {
        return id;
    }

    public String getLink() {
        return link;
    }

    public String getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getType() {
        return type;
    }
}
