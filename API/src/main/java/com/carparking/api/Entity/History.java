package com.carparking.api.Entity;

import javax.persistence.*;

@Entity
@Table(name = "\"history\"")
public class History {

    @Id
    @Column(name = "booking_id")
    private Integer bookingId;

    @Column(name = "in_time")
    private Long inTime;

    @Column(name = "out_time")
    private Long outTime;


    @Column(name = "slot_duration")
    private Integer slotDuration;

    @Column(name = "status")
    private String status;

    @Column(name = "bill")
    private Double bill;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "parking_id")
    private Integer parkingId;

    public History(Long inTime, Long outTime, Integer slotDuration, String status, Double bill, Integer userId, Integer parkingId) {
        this.inTime = inTime;
        this.outTime = outTime;
        this.slotDuration = slotDuration;
        this.status = status;
        this.bill = bill;
        this.userId = userId;
        this.parkingId = parkingId;
    }

    public History(Integer bookingId, Long inTime, Long outTime, Integer slotDuration, String status, Double bill, Integer userId, Integer parkingId) {
        this.bookingId = bookingId;
        this.inTime = inTime;
        this.outTime = outTime;
        this.slotDuration = slotDuration;
        this.status = status;
        this.bill = bill;
        this.userId = userId;
        this.parkingId = parkingId;
    }

    public History(){

    }

    public Integer getBookingId() {
        return bookingId;
    }

    public void setBookingId(Integer bookingId) {
        this.bookingId = bookingId;
    }

    public Long getInTime() {
        return inTime;
    }

    public void setInTime(Long inTime) {
        this.inTime = inTime;
    }

    public Long getOutTime() {
        return outTime;
    }

    public void setOutTime(Long outTime) {
        this.outTime = outTime;
    }

    public Integer getSlotDuration() {
        return slotDuration;
    }

    public void setSlotDuration(Integer slotDuration) {
        this.slotDuration = slotDuration;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getBill() {
        return bill;
    }

    public void setBill(Double bill) {
        this.bill = bill;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getParkingId() {
        return parkingId;
    }

    public void setParkingId(Integer parkingId) {
        this.parkingId = parkingId;
    }
}
