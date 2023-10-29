package edu.northeastern.s3kb;

public class User {
    private String username;
    private long lastVisitedEpochSecond;

    public User(String username, long lastVisitedEpochSecond) {
        this.username = username;
        this.lastVisitedEpochSecond = lastVisitedEpochSecond;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getLastVisitedEpochSecond() {
        return lastVisitedEpochSecond;
    }

    public void setLastVisitedEpochSecond(long lastVisitedEpochSecond) {
        this.lastVisitedEpochSecond = lastVisitedEpochSecond;
    }
}
