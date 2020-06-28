package com.madeveloper.kayilarcarpet.fragments.view_model;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.madeveloper.kayilarcarpet.model.Product;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ListProductViewModel extends ViewModel {

    private MutableLiveData<List<Product>> productData = new MutableLiveData<>();


    public void setProductList(List<Product> products) {
        productData.setValue(products);
    }


    @Nullable
    public List<Product> getProductList() {
        return productData.getValue();
    }


}
