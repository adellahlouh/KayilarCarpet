package com.madeveloper.kayilarcarpet.utils;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.madeveloper.kayilarcarpet.model.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductUtil {



    public static List<String> getFavIDsList(Context context) {
        return Util.loadArrayFromPreference(context, Constant.FAV_ID_PREF);
    }

    public static List<Product> getFavProductList(Context context) {

        List<Product> productList = new ArrayList<>();

        List<String> gsonList = Util.loadArrayFromPreference(context, Constant.FAV_PRODUCT_PREF);

        for (String gson : gsonList)
            productList.add(new Gson().fromJson(gson, Product.class));

        return productList;
    }

    public static void updateProductListFav(Context context, Product product, boolean add) {

        //save for id list
        List<String> productIdList = getFavIDsList(context);
        if (add)
            productIdList.add(product.getId());
        else
            productIdList.remove(product.getId());

        Util.saveArrayToPreference(context, Constant.FAV_ID_PREF, productIdList);

        //save for object json list
        List<String> gsonProductList = Util.loadArrayFromPreference(context, Constant.FAV_PRODUCT_PREF);
        if (add)
            gsonProductList.add(new Gson().toJson(product));
        else{
            for(int i = 0 ; i< gsonProductList.size() ; i++){
                if(new Gson().fromJson(gsonProductList.get(i),Product.class).getId().equals(product.getId())){
                    gsonProductList.remove(i);
                    break;
                }
            }
        }


        Util.saveArrayToPreference(context, Constant.FAV_PRODUCT_PREF, gsonProductList);

    }


    public static List<String> getCartIDsList(Context context){
        return Util.loadArrayFromPreference(context, Constant.CART_ID_PREF);
    }

    public static List<Product> getCartProductList (Context context){

        List<Product> productList = new ArrayList<>();

        List<String> gsonList = Util.loadArrayFromPreference(context, Constant.CART_PRODUCT_PREF);

        for (String gson : gsonList)
            productList.add(new Gson().fromJson(gson, Product.class));

        return productList;

    }

    public static void removeAllCart(Context context){
        Util.saveArrayToPreference(context, Constant.CART_ID_PREF, new ArrayList<>());
        Util.saveArrayToPreference(context, Constant.CART_PRODUCT_PREF, new ArrayList<>());

    }

    public static void updateProductListCart(Context context, Product product, boolean add) {

        //save for id list
        List<String> productIdList = getCartIDsList(context);
        if (add)
            productIdList.add(product.getId());
        else
            productIdList.remove(product.getId());

        Util.saveArrayToPreference(context, Constant.CART_ID_PREF, productIdList);

        //save for object json list
        List<String> gsonProductList = Util.loadArrayFromPreference(context, Constant.CART_PRODUCT_PREF);
        if (add)
            gsonProductList.add(new Gson().toJson(product));
        else{
            for(int i = 0 ; i< gsonProductList.size() ; i++){
                if(new Gson().fromJson(gsonProductList.get(i),Product.class).getId().equals(product.getId())){
                    gsonProductList.remove(i);
                    break;
                }
            }
        }


        Util.saveArrayToPreference(context, Constant.CART_PRODUCT_PREF, gsonProductList);

    }





}
