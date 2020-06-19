package com.madeveloper.kayilarcarpet.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.madeveloper.kayilarcarpet.R;
import com.madeveloper.kayilarcarpet.adapter.ProductAdapter;
import com.madeveloper.kayilarcarpet.model.Product;

public class ProductActivity extends AppCompatActivity {

    RecyclerView product_rv ;
    ProductAdapter productAdapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);


        product_rv = findViewById(R.id.product_rv);
        productAdapter = new ProductAdapter(this);
        product_rv.setLayoutManager(new GridLayoutManager(this,2));

        product_rv.setAdapter(productAdapter);


    }
}