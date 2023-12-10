package edu.northeastern.s3kb;

public class House {

    private String houseId;
    private String noOfRoom;
    private String houseDescription;
    private String houseLocation;
    private String rentPerRoom;
    private String userId;
    private String country;
    private String state;
    private String type;
    private String baths;
    private String address;
    private String houseImage;

    public House(String houseId, String noOfRoom, String houseDescription, String houseLocation, String rentPerRoom,
                 String userId, String country, String state, String type,
                 String baths, String address, String houseImage)
    {
        this.houseId = houseId;
        this.noOfRoom = noOfRoom;
        this.houseDescription = houseDescription;
        this.houseLocation = houseLocation;
        this.rentPerRoom = rentPerRoom;
        this.userId = userId;
        this.country = country;
        this.state = state;
        this.type = type;
        this.baths = baths;
        this.address = address;
        this.houseImage = houseImage;
    }

    public String getHouseId() {
        return houseId;
    }

    public void setHouseId(String houseId) {
        this.houseId = houseId;
    }

    public String getNoOfRoom() {
        return noOfRoom;
    }

    public void setNoOfRoom(String noOfRoom) {
        this.noOfRoom = noOfRoom;
    }

    public String getHouseDescription() {
        return houseDescription;
    }

    public void setHouseDescription(String houseDescription) {
        this.houseDescription = houseDescription;
    }

    public String getHouseLocation() {
        return houseLocation;
    }

    public void setHouseLocation(String houseLocation) {
        this.houseLocation = houseLocation;
    }

    public String getRentPerRoom() {
        return rentPerRoom;
    }

    public void setRentPerRoom(String rentPerRoom) {
        this.rentPerRoom = rentPerRoom;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBaths() {
        return baths;
    }

    public void setBaths(String baths) {
        this.baths = baths;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getHouseImage() {
        return houseImage;
    }

    public void setHouseImage(String houseImage) {
        this.houseImage = houseImage;
    }


}
