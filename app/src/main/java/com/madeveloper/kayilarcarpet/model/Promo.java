package com.madeveloper.kayilarcarpet.model;

public class Promo {

    String key ;
    String value ;
    boolean visible;
    double percentage ;


    public Promo() {

    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public boolean getVisible() {
        return visible;
    }

    public double getPercentage() {
        return percentage;
    }
}
