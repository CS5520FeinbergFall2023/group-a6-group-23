package edu.northeastern.s3kb;

public class Owner {

    private String ownerName;
    private String address;
    private String unitNumber;
    private String propertyType;

    private String city;
    
    private String state;
    
    private int zipcode;
    

    public Owner(String ownerName, String address, String unitNumber, String propertyType, String city, String state,
                 int zipCode) {
        this.ownerName = ownerName;
        this.address = address;
        this.unitNumber = unitNumber;
        this.propertyType = propertyType;
        this.city = city;
        this.state = state;
        this.zipcode = zipCode;
    }

    public String getUsername() {
        return ownerName;
    }

    public void setUsername(String ownerName) {
        this.ownerName = ownerName;
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
