package com.carparking.api.Entity;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "\"booking\"")
public class Booking {

    @Id
    @Column(name = "booking_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer bookingId;

    @Column(name = "in_time")
    private Timestamp inTime;

    @Column(name = "out_time")
    private Timestamp outTime;

    @Column(name = "in_otp")
    private Integer inOtp;

    @Column(name = "out_otp")
    private Integer outOpt;

    @Column(name = "slot_duration")
    private Integer slotDuration;

    @Column(name = "status")
    private String status;

    @Column(name = "bill")
    private Double bill;

    @Column(name = "user_id")
    private Integer UserId;

    @Column(name = "parking_id")
    private Integer parkingId;

    public Booking(Timestamp inTime, Timestamp outTime, Integer inOtp, Integer outOpt, Integer slotDuration, String status, Double bill, Integer userId, Integer parkingId) {
        this.inTime = inTime;
        this.outTime = outTime;
        this.inOtp = inOtp;
        this.outOpt = outOpt;
        this.slotDuration = slotDuration;
        this.status = status;
        this.bill = bill;
        UserId = userId;
        this.parkingId = parkingId;
    }

    public Booking(Integer bookingId, Timestamp inTime, Timestamp outTime, Integer inOtp, Integer outOpt, Integer slotDuration, String status, Double bill, Integer userId, Integer parkingId) {
        this.bookingId = bookingId;
        this.inTime = inTime;
        this.outTime = outTime;
        this.inOtp = inOtp;
        this.outOpt = outOpt;
        this.slotDuration = slotDuration;
        this.status = status;
        this.bill = bill;
        UserId = userId;
        this.parkingId = parkingId;
    }

    public Booking(){

    }

    public Integer getBookingId() {
        return bookingId;
    }

    public void setBookingId(Integer bookingId) {
        this.bookingId = bookingId;
    }

    public Timestamp getInTime() {
        return inTime;
    }

    public void setInTime(Timestamp inTime) {
        this.inTime = inTime;
    }

    public Timestamp getOutTime() {
        return outTime;
    }

    public void setOutTime(Timestamp outTime) {
        this.outTime = outTime;
    }

    public Integer getInOtp() {
        return inOtp;
    }

    public void setInOtp(Integer inOtp) {
        this.inOtp = inOtp;
    }

    public Integer getOutOpt() {
        return outOpt;
    }

    public void setOutOpt(Integer outOpt) {
        this.outOpt = outOpt;
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
        return UserId;
    }

    public void setUserId(Integer userId) {
        UserId = userId;
    }

    public Integer getParkingId() {
        return parkingId;
    }

    public void setParkingId(Integer parkingId) {
        this.parkingId = parkingId;
    }
}
