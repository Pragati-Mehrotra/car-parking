package com.alokbharti.parkme.Pojo;

public class History {

    private Integer bookingId;
    private Long inTime;
    private Long outTime;
    private Integer slotDuration;
    private String status;
    private Double bill;
    private Integer userId;
    private Integer parkingId;
    private String parkingName;

    public History(Integer bookingId, Long inTime, Long outTime, Integer slotDuration, String status, Double bill, Integer userId, Integer parkingId, String parkingName) {
        this.bookingId = bookingId;
        this.inTime = inTime;
        this.outTime = outTime;
        this.slotDuration = slotDuration;
        this.status = status;
        this.bill = bill;
        this.userId = userId;
        this.parkingId = parkingId;
        this.parkingName = parkingName;
    }

    public History(){

    }

    @Override
    public String toString() {
        return "History{" +
                "bookingId=" + bookingId +
                ", inTime=" + inTime +
                ", outTime=" + outTime +
                ", slotDuration=" + slotDuration +
                ", status='" + status + '\'' +
                ", bill=" + bill +
                ", userId=" + userId +
                ", parkingId=" + parkingId +
                ", parkingName='" + parkingName + '\'' +
                '}';
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

    public String getParkingName() {
        return parkingName;
    }

    public void setParkingName(String parkingName) {
        this.parkingName = parkingName;
    }
}
