package com.madeveloper.kayilarcarpet.model;

import java.util.ArrayList;
import java.util.List;

public class Product {

    private String id;

    private String nameAr,nameEn,size;
    private String desAr,desEn;

    private String sectionId;
    private String sectionNameAr,sectionNameEn;

    private double price;
    private double oldPrice;
    boolean isOffer, isVisible;

    private List<String> imageUrls;

    public Product() {
    }

    public Product(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNameAr() {
        return nameAr;
    }

    public void setNameAr(String nameAr) {
        this.nameAr = nameAr;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getDesAr() {
        return desAr;
    }

    public void setDesAr(String desAr) {
        this.desAr = desAr;
    }

    public String getDesEn() {
        return desEn;
    }

    public void setDesEn(String desEn) {
        this.desEn = desEn;
    }

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    public String getSectionNameAr() {
        return sectionNameAr;
    }

    public void setSectionNameAr(String sectionNameAr) {
        this.sectionNameAr = sectionNameAr;
    }

    public String getSectionNameEn() {
        return sectionNameEn;
    }

    public void setSectionNameEn(String sectionNameEn) {
        this.sectionNameEn = sectionNameEn;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(double oldPrice) {
        this.oldPrice = oldPrice;
    }

    public boolean isOffer() {
        return isOffer;
    }

    public void setOffer(boolean offer) {
        isOffer = offer;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public List<String> getImageUrls() {
        if(imageUrls == null)
            imageUrls = new ArrayList<>();
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
