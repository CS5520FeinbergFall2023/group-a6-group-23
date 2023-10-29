package edu.northeastern.s3kb;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class User {

    private final String userName;
    private final String UID;

    private List<String> stickerSent = new ArrayList<>();

    private List<String> stickerReceived = new ArrayList<>();

    public User(String userName) {
        this.userName = userName;
        this.UID = UUID.randomUUID().toString();
    }
    public String getUserName() {
        return userName;
    }

    public String getUID() {
        return UID;
    }

    public List<String> getStickerSent()
    {
        return stickerSent;
    }

    public void setStickerSent(String stickerName)
    {
        stickerSent.add(stickerName);
    }

}

