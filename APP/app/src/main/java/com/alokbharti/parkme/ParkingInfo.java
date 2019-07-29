package com.alokbharti.parkme;

public class ParkingInfo {
    private int parkingId;
    private String parkingName;
    private String parkingAddress;
    private float latitude;
    private float longitude;
    private int totalSlots;
    private int availableSlots;

    public ParkingInfo(int parkingId, String parkingName, String parkingAddress, float latitude, float longitude, int totalSlots, int availableSlots) {
        this.parkingId = parkingId;
        this.parkingName = parkingName;
        this.parkingAddress = parkingAddress;
        this.latitude = latitude;
        this.longitude = longitude;
        this.totalSlots = totalSlots;
        this.availableSlots = availableSlots;
    }

    public int getParkingId() {
        return parkingId;
    }

    public void setParkingId(int parkingId) {
        this.parkingId = parkingId;
    }

    public String getParkingName() {
        return parkingName;
    }

    public void setParkingName(String parkingName) {
        this.parkingName = parkingName;
    }

    public String getParkingAddress() {
        return parkingAddress;
    }

    public void setParkingAddress(String parkingAddress) {
        this.parkingAddress = parkingAddress;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public int getTotalSlots() {
        return totalSlots;
    }

    public void setTotalSlots(int totalSlots) {
        this.totalSlots = totalSlots;
    }

    public int getAvailableSlots() {
        return availableSlots;
    }

    public void setAvailableSlots(int availableSlots) {
        this.availableSlots = availableSlots;
    }
}
