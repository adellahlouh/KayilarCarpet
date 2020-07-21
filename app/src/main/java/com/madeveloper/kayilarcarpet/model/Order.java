package com.madeveloper.kayilarcarpet.model;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Order {

    private String id;
    private String userId;
    private String nameUser;
    private String address;
    private String phone;

    private String promoUsed;
    private int discountPercentage;

    private OrderState state = OrderState.Pending;


    @ServerTimestamp
    private Date time;

    double total;

    public Order() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getTime() {
        return time;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public OrderState getState() {
        return state;
    }

    public String getPromoUsed() {
        return promoUsed;
    }

    public void setPromoUsed(String promoUsed) {
        this.promoUsed = promoUsed;
    }

    public int getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(int discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public enum OrderState {
        Pending,
        OnProgress,
        Delivered,
    }

}
