package com.example.ae.yourhub;

/**
 * Created by A E on 29-Jun-17.
 */

public class CommunityData {
    String id;
    String name;
    String admin;
    CommunityData( String id,String name,String admin)
    {
        this.id=id;
        this.name=name;
        this.admin=admin;
    }

    public String getId() {
        return id;
    }

    public String getAdmin() {
        return admin;
    }

    public String getName() {
        return name;
    }
}
