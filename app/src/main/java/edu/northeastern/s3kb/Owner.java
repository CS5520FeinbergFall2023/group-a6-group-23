package edu.northeastern.s3kb;

public class Owner {

    private String userName;
    private String address;
    private String unitNumber;
    private String propertyType;

    private String city;

    private String state;

    private int zipcode;


    public Owner(String username, String address, String unitNumber, String propertyType, String city, String state,
                 int zipcode) {
        this.userName = username;
        this.address = address;
        this.unitNumber = unitNumber;
        this.propertyType = propertyType;
        this.city = city;
        this.state = state;
        this.zipcode = zipcode;
    }

    public String getUsername() {
        return userName;
    }

    public void setUsername(String username) {
        this.userName = username;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUnitNumber() {
        return unitNumber;
    }

    public void setUnitNumber(String unitNumber) {
        this.unitNumber = unitNumber;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getZipcode() {
        return zipcode;
    }

    public void setZipcode(int zipcode) {
        this.zipcode = zipcode;
    }


}