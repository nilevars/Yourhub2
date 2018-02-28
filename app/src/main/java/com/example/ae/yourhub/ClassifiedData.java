package com.example.ae.yourhub;

/**
 * Created by A E on 21-Jun-17.
 */

public class ClassifiedData{
    String title;
    String description;
    String price;
    String location;
    String latitude;
    String longitude;
    String image;
    String id;
    ClassifiedData(String id, String title, String description,String price,String location,String latitude,String longitude, String image)
            {
            this.id=id;
            this.title=title;
            this.description=description;
            this.image=image;
            this.price="KWD "+price;
            this.location=location;
            this.latitude=latitude;
            this.longitude=longitude;
            }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public String getId() {
        return id;
        }

    public String getLatitude() {
        return latitude;
    }

    public String getLocation() {
        return location;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getPrice() {
        return price;
    }
}

