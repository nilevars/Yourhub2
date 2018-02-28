package com.example.ae.yourhub;

/**
 * Created by A E on 3/14/2017.
 */

public class UserComments {
    String userid,name,description,time;
    UserComments(String userid, String name, String description, String time)
    {
        this.userid=userid;
        this.name=name;
        this.description=description;
        this.time=time;
    }
    String getUserid()
    {
        return userid;
    }
    String getName()
    {
        return name;
    }
    String getDescription()
    {
        return description;
    }

    public String getTime() {
        return time;
    }
}


