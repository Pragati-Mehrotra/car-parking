package com.carparking.api.Entity;


import javax.persistence.*;

@Entity
@Table(name = "user", schema = "public")
public class User {

    @Column
    private String name;

    @Id
    @Column
    @GeneratedValue
    private Integer user_id;

    @Column
    private String password;

    @Column
    private Integer balance;

    @Column(name = "phone_no")
    private String phone;

    @Column
    private String email;

    public User(String name, Integer user_id, String password, Integer balance, String phone_no, String email) {
        this.name = name;
        this.user_id = user_id;
        this.password = password;
        this.balance = balance;
        this.phone = phone_no;
        this.email = email;
    }

    public User(String name, Integer uid, String password, Integer balance, String phone_no) {
        this.name = name;
        this.user_id = user_id;
        this.password = password;
        this.balance = balance;
        this.phone = phone_no;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", user_id=" + user_id +
                ", password='" + password + '\'' +
                ", balance=" + balance +
                ", phone_no='" + phone + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    public User() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getUid() {
        return user_id;
    }

    public void setUid(Integer uid) {
        this.user_id = uid;
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

    public String getPhone_no() {
        return phone;
    }

    public void setPhone_no(String phone_no) {
        this.phone = phone_no;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
