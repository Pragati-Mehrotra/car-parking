package com.carparking.api.Entity;

import javax.persistence.*;

@Entity
@Table(name = "\"user\"")
public class User {

    @Column(name = "name")
    private String name;

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer userId;

    @Column(name = "password")
    private String password;

    @Column(name = "balance")
    private Integer balance;

    @Column(name = "phone_no")
    private String phoneNo;

    @Column(name = "email")
    private String email;

    public User(String name, String password, Integer balance, String phoneNo, String email) {
        this.name = name;
        this.password = password;
        this.balance = balance;
        this.phoneNo = phoneNo;
        this.email = email;
    }

    public User(String name,Integer userId, String password, Integer balance, String phoneNo, String email) {
        this.name = name;
        this.userId = userId;
        this.password = password;
        this.balance = balance;
        this.phoneNo = phoneNo;
        this.email = email;
    }

    public User(){

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
        return "User{" +
                "name='" + name + '\'' +
                ", userId=" + userId +
                ", password='" + password + '\'' +
                ", balance=" + balance +
                ", phoneNo='" + phoneNo + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
