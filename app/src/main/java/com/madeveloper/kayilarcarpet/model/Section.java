package com.madeveloper.kayilarcarpet.model;

public class Section {

    String imageUrl ;
    String nameEn,nameAr;
    String id ;

    public Section() {
    }

    public Section(String id) {
        this.id = id;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getNameEn() {
        return nameEn;
    }

    public String getId() {
        return id;
    }

    public void setNameAr(String nameAr) {
        this.nameAr = nameAr;
    }

    public String getNameAr() {
        return nameAr;
    }
}
