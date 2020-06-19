package com.madeveloper.kayilarcarpet.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.transition.Slide;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.madeveloper.kayilarcarpet.R;
import com.madeveloper.kayilarcarpet.adapter.SectionAdapter;
import com.madeveloper.kayilarcarpet.adapter.SliderAdapter;
import com.madeveloper.kayilarcarpet.model.Section;
import com.madeveloper.kayilarcarpet.model.Slider;
import com.madeveloper.kayilarcarpet.model.User;
import com.madeveloper.kayilarcarpet.utils.Constant;
import com.madeveloper.kayilarcarpet.utils.Util;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeActivity extends AppCompatActivity {

    FirebaseFirestore refSlider, refSection;
    SliderAdapter sliderAdapter;
    List<Slider> listSlider;
    SectionAdapter sectionAdapter;
    RecyclerView section_rv;
    RecyclerView.LayoutManager layoutManager;

    SliderView sliderView;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        sliderView = findViewById(R.id.imageSlider);
        section_rv = findViewById(R.id.section_rv);

        refSlider = FirebaseFirestore.getInstance();
        refSection = FirebaseFirestore.getInstance();

        listSlider = new ArrayList<>();

        sliderAdapter = new SliderAdapter(this);


        sectionAdapter = new SectionAdapter(this);
        layoutManager = new LinearLayoutManager(this);
        section_rv.setLayoutManager(layoutManager);
        section_rv.setAdapter(sectionAdapter);

        createSliderDetails();

        createSectionDetails();

        User user = Util.getUser(this);

        assert user != null;
        //Toast.makeText(this, user.getName(), Toast.LENGTH_SHORT).show();
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    void createSliderDetails() {
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.ZOOMOUTTRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(getColor(R.color.colorPrimary));
        sliderView.setIndicatorUnselectedColor(Color.WHITE);
        sliderView.setScrollTimeInSec(4); //set scroll delay in seconds :
        sliderView.startAutoCycle();
        sliderView.setSliderAdapter(sliderAdapter);

        refSlider.collection(Constant.SLIDER_IMAGE_COL).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<Slider> sliderList = Util.getListFromCollection(Objects.requireNonNull(task.getResult()), Slider.class);
                sliderAdapter.setSliderList(sliderList);
            }
        });
    }

    private void createSectionDetails() {

        refSection.collection(Constant.SECTIONS_COL).get().addOnCompleteListener(task -> {

            if (task.isSuccessful()) {
                List<Section> sectionList = Util.getListFromCollection(Objects.requireNonNull(task.getResult()), Section.class);
                sectionAdapter.setSectionList(sectionList);
            }

        });
    }


}