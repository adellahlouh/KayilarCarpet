package com.madeveloper.kayilarcarpet.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.transition.Slide;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.madeveloper.kayilarcarpet.R;
import com.madeveloper.kayilarcarpet.adapter.SliderAdapter;
import com.madeveloper.kayilarcarpet.databinding.FragmentDescriptionProductBinding;
import com.madeveloper.kayilarcarpet.handler.OnNavigateFragment;
import com.madeveloper.kayilarcarpet.model.Product;
import com.madeveloper.kayilarcarpet.model.Slider;
import com.madeveloper.kayilarcarpet.utils.ProductUtil;
import com.madeveloper.kayilarcarpet.utils.Util;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

public class DescriptionProductFragment extends BaseFragment {

    public static final String ARG_PRODUCT = "product";

    FragmentDescriptionProductBinding binding;
    Product product;
    private OnNavigateFragment onNavigateFragment;


    public DescriptionProductFragment() {
        // Required empty public constructor
    }

    @Override
    public String getTitle() {
        return Util.isEnglishDevice()? product.getNameEn(): product.getNameAr();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String gsonProduct = getArguments().getString(ARG_PRODUCT);

            product = new Gson().fromJson(gsonProduct,Product.class);
        }

        onNavigateFragment = (OnNavigateFragment) getActivity();

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_description_product, container, false);


        return binding.getRoot();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onNavigateFragment.onFragmentShow(this);


        loadSlider();

        binding.nameProductTx.setText(getTitle());
        binding.descriptionTv.setText(Util.isEnglishDevice() ? product.getDesEn() : product.getDesEn());

        binding.productPriceTv.setText(product.getPrice()+" JD");

        if (product.isOffer()) {
            binding.productOldPriceTv.setVisibility(View.VISIBLE);
            binding.productOldPriceTv.setPaintFlags(  binding.productOldPriceTv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            binding.productOldPriceTv.setText(product.getOldPrice() + " JD");

            int percent = (int) ((1.0 - (product.getPrice() / product.getOldPrice())) * 100.0);

            binding.offerTx.setVisibility(View.VISIBLE);
            binding.offerTx.setText("OFF "+percent+"%");

        }

        binding.backBtn.setOnClickListener(view1 -> getActivity().onBackPressed());


        List<String> favIdProduct = ProductUtil.getFavIDsList(getContext());
        if(favIdProduct.contains(product.getId()))
            binding.favBtn.setLiked(true);
        else
            binding.favBtn.setLiked(false);

        binding.favBtn.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                ProductUtil.updateProductListFav(getContext(),product,true);
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                ProductUtil.updateProductListFav(getContext(),product,false);
            }
        });


    }

    private void loadSlider() {

        SliderAdapter sliderAdapter = new SliderAdapter(getContext());
        binding.imageSlider.setSliderAdapter(sliderAdapter);

        binding.imageSlider.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        binding.imageSlider.setSliderTransformAnimation(SliderAnimations.ZOOMOUTTRANSFORMATION);
        binding.imageSlider.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        binding.imageSlider.setIndicatorSelectedColor(getResources().getColor(R.color.colorPrimary));
        binding.imageSlider.setIndicatorUnselectedColor(Color.WHITE);
        binding.imageSlider.setScrollTimeInSec(4);
        binding.imageSlider.setIndicatorAnimation(IndicatorAnimationType.WORM);
        binding.imageSlider.startAutoCycle();
        binding.imageSlider.setIndicatorEnabled(true);



        List<Slider> sliderList = new ArrayList<>();
        for(String url : product.getImageUrls()){
            Slider slider = new Slider();
            slider.setImageUrl(url);

            sliderList.add(slider);
        }

        sliderAdapter.setSliderList(sliderList);
        binding.imageSlider.dataSetChanged();

    }


}