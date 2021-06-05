package com.example.opensourcesoftwareproject_team;

import java.io.Serializable;

public class User implements Serializable {
    private String id;
    private static User user = null;

    private User (String id){
        this.id = id;
    }

    static User getInstence(String id){
        if (user == null){
            user = new User(id);
        }
        return user;
    }

    String getId(){
        return id;
    }
}
