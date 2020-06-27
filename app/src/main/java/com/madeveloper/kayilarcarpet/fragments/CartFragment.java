package com.madeveloper.kayilarcarpet.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.madeveloper.kayilarcarpet.R;
import com.madeveloper.kayilarcarpet.adapter.CartAdapter;
import com.madeveloper.kayilarcarpet.databinding.FragmentCartBinding;
import com.madeveloper.kayilarcarpet.handler.OnNavigateFragment;
import com.madeveloper.kayilarcarpet.model.Product;
import com.madeveloper.kayilarcarpet.utils.ProductUtil;
import com.madeveloper.kayilarcarpet.utils.Util;

import java.util.List;


public class CartFragment extends BaseFragment {

    private OnNavigateFragment onNavigateFragment;

    public CartFragment() {
        // Required empty public constructor
    }

    FragmentCartBinding cartBinding ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onNavigateFragment = (OnNavigateFragment) getActivity();

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        cartBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_cart, container, false);
        return cartBinding.getRoot();
    }


    CartAdapter cartAdapter ;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        onNavigateFragment.onFragmentShow(this);
        cartAdapter = new CartAdapter(getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        cartBinding.cartRv.setLayoutManager(layoutManager);
        cartBinding.cartRv.setAdapter(cartAdapter);

        List<Product> productList= ProductUtil.getCartProductList(getContext());
        cartAdapter.setProductList(productList);



        cartAdapter.setOnItemClick((int pos, Product product) -> {
            Bundle bundle = new Bundle();

            bundle.putString(DescriptionProductFragment.ARG_PRODUCT,new Gson().toJson(product));

            onNavigateFragment.onNavigate(R.id.descriptionProductFragment,bundle);
        });


    }


}