package com.carparking.api.Entity;

import javax.persistence.*;

@Entity
@Table(name = "\"parking\"")
public class Parking {

    @Id
    @Column(name = "parking_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer parkingId;

    @Column(name = "parking_name")
    private String parkingName;

    @Column(name = "address")
    private String address;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "total_slots")
    private Integer totalSlots;

    @Column(name = "available_slots")
    private Integer availableSlots;

    @Override
    public String toString() {
        return "Parking{" +
                "parkingId=" + parkingId +
                ", parkingName='" + parkingName + '\'' +
                ", address='" + address + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", totalSlots=" + totalSlots +
                ", availableSlots=" + availableSlots +
                '}';
    }

    public Parking(String parkingName, String address, Double latitude, Double longitude, Integer totalSlots, Integer availableSlots) {
        this.parkingName = parkingName;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.totalSlots = totalSlots;
        this.availableSlots = availableSlots;
    }

    public Parking(Integer parkingId, String parkingName, String address, Double latitude, Double longitude, Integer totalSlots, Integer availableSlots) {
        this.parkingId = parkingId;
        this.parkingName = parkingName;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.totalSlots = totalSlots;
        this.availableSlots = availableSlots;
    }

    public Parking(){

    }

    public Integer getParkingId() {
        return parkingId;
    }

    public void setParkingId(Integer parkingId) {
        this.parkingId = parkingId;
    }

    public String getParkingName() {
        return parkingName;
    }

    public void setParkingName(String parkingName) {
        this.parkingName = parkingName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Integer getTotalSlots() {
        return totalSlots;
    }

    public void setTotalSlots(Integer totalSlots) {
        this.totalSlots = totalSlots;
    }

    public Integer getAvailableSlots() {
        return availableSlots;
    }

    public void setAvailableSlots(Integer availableSlots) {
        this.availableSlots = availableSlots;
    }


}
