package com.madeveloper.kayilarcarpet.model;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Product {

    private String id;

    private String nameAr, nameEn;
    private String desAr, desEn;

    private String sectionId;
    private String sectionNameAr, sectionNameEn;

    boolean isOffer, isVisible;
    private int offerPercent;

    //size,price in jd as jsonObject
    private List<String> sizePrice;

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


    public boolean isOffer() {
        return isOffer;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public void setOffer(boolean offer) {
        isOffer = offer;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public List<String> getImageUrls() {
        if (imageUrls == null)
            imageUrls = new ArrayList<>();
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }


    public List<String> getSizePrice() {
        if (sizePrice == null)
            sizePrice = new ArrayList<>();
        return sizePrice;
    }


    public void addSizePrice(String json) {
        getSizePrice().add(json);
    }

    public void removeSize(int pos) {
        getSizePrice().remove(pos);
    }

    public int getOfferPercent() {
        return offerPercent;
    }

    public void setOfferPercent(int offerPercent) {
        this.offerPercent = offerPercent;
    }


    public static class Size {

        public int width, length;
        public double price;
        //used in cart
        public int count;


        @NotNull
        @Override
        public String toString() {

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("width", width);
                jsonObject.put("length", length);
                jsonObject.put("price", price);
                jsonObject.put("count", count);

            } catch (JSONException e) {
                e.printStackTrace();

                return "{}";
            }

            return jsonObject.toString();
        }

        public static Size getFromJson(String json){

            Size size = new Size();

            try {
                JSONObject jsonObject = new JSONObject(json);

                size.width = jsonObject.getInt("width");
                size.length = jsonObject.getInt("length");
                size.price = jsonObject.getDouble("price");

                if(jsonObject.has("count"))
                    size.count = jsonObject.getInt("count");




            } catch (JSONException e) {
                e.printStackTrace();
            }



            return  size;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Size size = (Size) o;
            return width == size.width &&
                    length == size.length &&
                    Double.compare(size.price, price) == 0 ;
        }

        @Override
        public int hashCode() {
            return Objects.hash(width, length, price, count);
        }
    }
}
