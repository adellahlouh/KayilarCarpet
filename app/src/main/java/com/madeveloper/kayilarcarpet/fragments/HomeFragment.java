package com.madeveloper.kayilarcarpet.fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.madeveloper.kayilarcarpet.R;
import com.madeveloper.kayilarcarpet.adapter.SectionAdapter;
import com.madeveloper.kayilarcarpet.adapter.SliderAdapter;
import com.madeveloper.kayilarcarpet.databinding.FragmentHomeBinding;
import com.madeveloper.kayilarcarpet.fragments.view_model.HomeViewModel;
import com.madeveloper.kayilarcarpet.handler.OnNavigateFragment;

import com.madeveloper.kayilarcarpet.model.Section;
import com.madeveloper.kayilarcarpet.model.Slider;
import com.madeveloper.kayilarcarpet.utils.Constant;
import com.madeveloper.kayilarcarpet.utils.Util;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class HomeFragment extends BaseFragment {

    private CollectionReference refSlider;
    private CollectionReference refSection;
    private OnNavigateFragment onNavigateFragment;
    private HomeViewModel homeViewModel;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        onNavigateFragment = (OnNavigateFragment) getActivity();
    }

    FragmentHomeBinding binding;
    SliderAdapter sliderAdapter;

    SectionAdapter sectionAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);


        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        onNavigateFragment.onFragmentShow(this);
        homeViewModel =new ViewModelProvider(this).get(HomeViewModel.class);


        refSlider = FirebaseFirestore.getInstance().collection(Constant.SLIDER_IMAGE_COL);
        refSection = FirebaseFirestore.getInstance().collection(Constant.SECTIONS_COL);

        sliderAdapter = new SliderAdapter(getContext());


        sectionAdapter = new SectionAdapter(getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.sectionRv.setLayoutManager(layoutManager);
        binding.sectionRv.setAdapter(sectionAdapter);


        sectionAdapter.setOnItemClick((pos, section) -> {

            Bundle bundle = new Bundle();
            bundle.putString(ListProductFragment.ARG_SECTION, new Gson().toJson(section));

            onNavigateFragment.onNavigate(R.id.productsFragment, bundle);
        });

        createSliderDetails();
        createSectionDetails();

    }

    void createSliderDetails() {
        binding.imageSlider.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        binding.imageSlider.setSliderTransformAnimation(SliderAnimations.ZOOMOUTTRANSFORMATION);
        binding.imageSlider.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        binding.imageSlider.setIndicatorSelectedColor(getResources().getColor(R.color.colorPrimary));
        binding.imageSlider.setIndicatorUnselectedColor(Color.WHITE);
        binding.imageSlider.setScrollTimeInSec(6);
        binding.imageSlider.startAutoCycle();
        binding.imageSlider.setIndicatorEnabled(true);
        binding.imageSlider.setSliderAdapter(sliderAdapter);

        if (homeViewModel.getSliderData() != null) {
            binding.sliderProgress.setVisibility(View.GONE);
            sliderAdapter.setSliderList(homeViewModel.getSliderData());

            return;
        }

        refSlider.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                ArrayList<Slider> sliderList = Util.getListFromCollection(Objects.requireNonNull(task.getResult()), Slider.class);

                binding.sliderProgress.setVisibility(View.GONE);

                homeViewModel.setSliderData(sliderList);
                sliderAdapter.setSliderList(sliderList);


            }
        });
    }

    private void createSectionDetails() {

        if (homeViewModel.getSliderData() != null) {
            binding.sectionsProgress.setVisibility(View.GONE);
            sectionAdapter.setSectionList(homeViewModel.getSectionsData());
            return;
        }

        refSection.get().addOnCompleteListener(task -> {

            if (task.isSuccessful()) {
                List<Section> sectionList = Util.getListFromCollection(Objects.requireNonNull(task.getResult()), Section.class);

                binding.sectionsProgress.setVisibility(View.GONE);

                homeViewModel.setSectionsData(sectionList);

                sectionAdapter.setSectionList(sectionList);

            }

        });
    }


}