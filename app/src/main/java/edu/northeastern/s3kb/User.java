package edu.northeastern.s3kb;

import android.os.Parcel;
import android.os.Parcelable;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class User {
    private String username;
    private String lastVisited;

    public User(String username, String lastVisited) {
        this.username = username;
        this.lastVisited = lastVisited;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLastVisited() {
        return lastVisited;
    }

    public void setLastVisited(String lastVisited) {
        this.lastVisited = lastVisited;
    }
}

