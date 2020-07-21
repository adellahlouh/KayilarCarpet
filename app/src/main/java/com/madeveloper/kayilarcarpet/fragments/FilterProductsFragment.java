package com.madeveloper.kayilarcarpet.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.madeveloper.kayilarcarpet.R;
import com.madeveloper.kayilarcarpet.adapter.ProductAdapter;
import com.madeveloper.kayilarcarpet.databinding.FragmentFilterProductsBinding;
import com.madeveloper.kayilarcarpet.fragments.view_model.ListProductViewModel;
import com.madeveloper.kayilarcarpet.handler.OnNavigateFragment;
import com.madeveloper.kayilarcarpet.model.Product;
import com.madeveloper.kayilarcarpet.utils.Constant;
import com.madeveloper.kayilarcarpet.utils.Util;

import java.util.List;

public class FilterProductsFragment extends BaseFragment {


    private OnNavigateFragment onNavigateFragment;
    private ListProductViewModel listProductViewModel;

    public FilterProductsFragment() {
        // Required empty public constructor
    }

    FragmentFilterProductsBinding binding;
    CollectionReference productsReference;

    ProductAdapter adapter;


    @Override
    public String getTitle() {
        return getString(R.string.search_hint);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onNavigateFragment = (OnNavigateFragment) getActivity();

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_filter_products, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listProductViewModel = new ViewModelProvider(this).get(ListProductViewModel.class);


        onNavigateFragment.onFragmentShow(this);
        productsReference = FirebaseFirestore.getInstance().collection(Constant.PRODUCTS_COL);


        adapter = new ProductAdapter(getContext());
        binding.productsRv.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.productsRv.setAdapter(adapter);


        loadProducts();

        adapter.setOnItemClick((int pos, Product product) -> {
            Bundle bundle = new Bundle();
            bundle.putString(DescriptionProductFragment.ARG_PRODUCT, new Gson().toJson(product));

            onNavigateFragment.onNavigate(R.id.descriptionProductFragment, bundle);
        });



        binding.searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                binding.searchBar.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.trim().isEmpty())
                    adapter.getFilter().filter(newText);
                return true;
            }
        });


    }

    private void loadProducts() {

        if (listProductViewModel.getProductList() != null) {
            binding.progressCircular.setVisibility(View.GONE);
            adapter.setProductList(listProductViewModel.getProductList());
            adapter.getFilter().filter(binding.searchBar.getQuery().toString());
            return;
        }

        productsReference.get().addOnSuccessListener(queryDocumentSnapshots -> {


            List<Product> productList = Util.getListFromCollection(queryDocumentSnapshots, Product.class);

            binding.progressCircular.setVisibility(View.GONE);
            adapter.setProductList(productList);
            listProductViewModel.setProductList(productList);

        });

    }



}