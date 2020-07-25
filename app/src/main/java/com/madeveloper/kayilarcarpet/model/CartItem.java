package com.madeveloper.kayilarcarpet.model;

import java.util.ArrayList;
import java.util.List;

public class CartItem {

    private String productId;

    private String productNameAr,productNameEn,productDesAr,productDesEn;
    private String imgUrl;

    private List<String> sizesSelected;

    private boolean isOffer;
    private int offerPercent = 0;

    public CartItem() {
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductNameAr() {
        return productNameAr;
    }

    public void setProductNameAr(String productNameAr) {
        this.productNameAr = productNameAr;
    }

    public String getProductNameEn() {
        return productNameEn;
    }

    public void setProductNameEn(String productNameEn) {
        this.productNameEn = productNameEn;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public List<String> getSizesSelected() {
        if(sizesSelected == null)
            sizesSelected =  new ArrayList<>();
        return sizesSelected;
    }

    public void setSizesSelected(List<String> sizesSelected) {
        this.sizesSelected = sizesSelected;
    }

    public List<Product.Size> convertSizeJsonToObjects() {

        List<Product.Size> sizeList = new ArrayList<>();

        for(String json :getSizesSelected())
            sizeList.add(Product.Size.getFromJson(json));

        return sizeList;
    }

    public double getTotalPrice(){
        double total = 0;

        List<Product.Size> sizeList = convertSizeJsonToObjects();


        for (Product.Size size: sizeList) {
            double price= (size.price * size.count);

            if(isOffer)
                price = price - price * offerPercent/100.0;

            total += price;
        }
        return total;
    }

    public boolean isOffer() {
        return isOffer;
    }

    public void setOffer(boolean offer) {
        isOffer = offer;
    }

    public int getOfferPercent() {
        return offerPercent;
    }

    public void setOfferPercent(int offerPercent) {
        this.offerPercent = offerPercent;
    }

    public double getOldTotalPrice() {
        return getTotalPrice() * 100 / offerPercent;
    }

    public String getProductDesEn() {
        return productDesEn;
    }

    public void setProductDesEn(String productDesEn) {
        this.productDesEn = productDesEn;
    }

    public String getProductDesAr() {
        return productDesAr;
    }

    public void setProductDesAr(String productDesAr) {
        this.productDesAr = productDesAr;
    }
}
