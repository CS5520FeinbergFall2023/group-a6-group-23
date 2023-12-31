package edu.northeastern.s3kb;

public class Property {
    private String houseId;
    private String noOfRoom;
    private String rentPerRoom;
    private String houseDescription;
    private String houseLocation;
    private String houseImage;
    private String userId;
    private String country;
    private String state;
    private String type;
    private String baths;
    private String address;


    public String getHouseImage() {
        return houseImage;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setHouseImage(String houseImage) {
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

    public String getRentPerRoom() {
        return rentPerRoom;
    }

    public void setRentPerRoom(String rentPerRoom) {
        this.rentPerRoom = rentPerRoom;
    }

    public String getHouseDescription() {
        return houseDescription;
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

    public void setHouseDescription(String houseDescription) {
        this.houseDescription = houseDescription;
    }

    public String getHouseLocation() {
        return houseLocation;
    }


    public void setHouseLocation(String houseLocation) {
        this.houseLocation = houseLocation;
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

    public Property() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Property(String houseId, String noOfRoom, String rentPerRoom,
                    String houseDescription, String houseLocation, String houseImage,
                    String userId, String country, String state, String type,String baths,String address) {
        this.houseId = houseId;
        this.noOfRoom = noOfRoom;
        this.rentPerRoom = rentPerRoom;
        this.houseDescription = houseDescription;
        this.houseLocation = houseLocation;
        this.houseImage = houseImage;
        this.userId = userId;
        this.country=country;
        this.state = state;
        this.type = type;
        this.baths = baths;
        this.address =  address;
    }
    //remove
    public Property(String rentPerRoom, String houseLocation) {
        this.rentPerRoom = rentPerRoom;
        this.houseLocation = houseLocation;
    }
}