package edu.northeastern.s3kb;

import java.time.LocalDateTime;

public class Sticker implements Comparable<Sticker> {
    private int imageId;
    private String from;
    private String to;
    private String sendTime;

    public Sticker(int imageId, String from, String to, String sendTime) {
        this.imageId = imageId;
        this.from = from;
        this.to = to;
        this.sendTime = sendTime;
    }

    public Sticker() {
    }

    // implement getter

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getSendTime() {
        return sendTime;
    }

    // sort sticker
    @Override
    public int compareTo(Sticker other) {
        return this.sendTime.compareTo(other.getSendTime());
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    @Override
    public String toString() {
        return "Sticker{" +
                "imageId=" + imageId +
                ", fromUser='" + this.sendTime + '\'' +
                ", toUser='" + to + '\'' +
                ", sendTimeEpochSecond='" + sendTime+ '\'' +
                '}';
    }
}
