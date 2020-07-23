package com.madeveloper.kayilarcarpet.fragments;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.madeveloper.kayilarcarpet.R;
import com.madeveloper.kayilarcarpet.adapter.ProductAdapter;
import com.madeveloper.kayilarcarpet.databinding.FragmentListProductBinding;
import com.madeveloper.kayilarcarpet.fragments.view_model.HomeViewModel;
import com.madeveloper.kayilarcarpet.fragments.view_model.ListProductViewModel;
import com.madeveloper.kayilarcarpet.handler.OnNavigateFragment;
import com.madeveloper.kayilarcarpet.model.Product;
import com.madeveloper.kayilarcarpet.model.Section;
import com.madeveloper.kayilarcarpet.utils.Constant;
import com.madeveloper.kayilarcarpet.utils.Util;

import java.util.List;


public class ListProductFragment extends BaseFragment {

    public static final String ARG_SECTION = "id section";
    private Section section;
    private OnNavigateFragment onNavigateFragment;
    private FragmentListProductBinding binding;
    private CollectionReference productsRef;
    private ProductAdapter productAdapter;
    private ListProductViewModel listProductViewModel;


    public ListProductFragment() {
        // Required empty public constructor
    }

    public static ListProductFragment newInstance(String sectionId) {
        ListProductFragment fragment = new ListProductFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SECTION, sectionId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public String getTitle() {
        return Util.isEnglishDevice(getContext()) ? section.getNameEn() : section.getNameAr();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        onNavigateFragment = (OnNavigateFragment) getActivity();
        if (getArguments() != null) {
            String sectionGson = getArguments().getString(ARG_SECTION);
            section = new Gson().fromJson(sectionGson, Section.class);
        }

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list_product, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listProductViewModel = new ViewModelProvider(this).get(ListProductViewModel.class);


        onNavigateFragment.onFragmentShow(this);
        productsRef = FirebaseFirestore.getInstance().collection(Constant.PRODUCTS_COL);

        productAdapter = new ProductAdapter(getContext());
        binding.productRv.setLayoutManager(new LinearLayoutManager(getContext()));

        binding.productRv.setAdapter(productAdapter);


        productAdapter.setOnItemClick((int pos, Product product) -> {
            Bundle bundle = new Bundle();
            bundle.putString(DescriptionProductFragment.ARG_PRODUCT, new Gson().toJson(product));

            onNavigateFragment.onNavigate(R.id.descriptionProductFragment, bundle);
        });

        loadProduct();

    }

    private void loadProduct() {

        if (listProductViewModel.getProductList() != null) {
            binding.progressCircular.setVisibility(View.GONE);
            productAdapter.setProductList(listProductViewModel.getProductList());
            return;
        }

        productsRef.whereEqualTo("sectionId", section.getId()).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    List<Product> productList = Util.getListFromCollection(queryDocumentSnapshots, Product.class);

                    binding.progressCircular.setVisibility(View.GONE);

                    productAdapter.setProductList(productList);
                    listProductViewModel.setProductList(productList);

                });

    }



}