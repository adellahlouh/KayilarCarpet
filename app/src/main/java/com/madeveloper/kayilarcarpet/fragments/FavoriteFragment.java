package com.madeveloper.kayilarcarpet.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.madeveloper.kayilarcarpet.R;
import com.madeveloper.kayilarcarpet.adapter.ProductAdapter;
import com.madeveloper.kayilarcarpet.adapter.SectionAdapter;
import com.madeveloper.kayilarcarpet.databinding.FragmentFavoriteBinding;
import com.madeveloper.kayilarcarpet.handler.OnNavigateFragment;
import com.madeveloper.kayilarcarpet.model.Product;
import com.madeveloper.kayilarcarpet.utils.ProductUtil;

public class FavoriteFragment extends BaseFragment {

    private OnNavigateFragment onNavigateFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        onNavigateFragment = (OnNavigateFragment) getActivity();
    }

    FragmentFavoriteBinding binding ;
    ProductAdapter productAdapter ;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_favorite, container, false);

        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        onNavigateFragment.onFragmentShow(this);
        productAdapter = new ProductAdapter(getContext());

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.favoriteRv.setLayoutManager(layoutManager);
        binding.favoriteRv.setAdapter(productAdapter);
        productAdapter.setProductList(ProductUtil.getFavProductList(getContext()));

        productAdapter.setOnItemClick((int pos, Product product) -> {

            Bundle bundle = new Bundle();
            bundle.putString(DescriptionProductFragment.ARG_PRODUCT, new Gson().toJson(product));
            onNavigateFragment.onNavigate(R.id.descriptionProductFragment, bundle);

        });



    }
}