package com.alokbharti.parkme.Pojo;

public class UserInfo {

    private String name;

    private Integer userId;

    private String password;

    private Integer balance;

    private String phoneNo;

    private String email;

    public UserInfo(String name, Integer userId, String password, Integer balance, String phoneNo, String email) {
        this.name = name;
        this.userId = userId;
        this.password = password;
        this.balance = balance;
        this.phoneNo = phoneNo;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "name='" + name + '\'' +
                ", userId=" + userId +
                ", password='" + password + '\'' +
                ", balance=" + balance +
                ", phoneNo='" + phoneNo + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
