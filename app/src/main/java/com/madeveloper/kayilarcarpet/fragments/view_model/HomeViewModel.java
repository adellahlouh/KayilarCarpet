package com.madeveloper.kayilarcarpet.fragments.view_model;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.madeveloper.kayilarcarpet.model.Section;
import com.madeveloper.kayilarcarpet.model.Slider;

import java.util.List;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<List<Slider>> sliderData = new MutableLiveData<>();

    private MutableLiveData<List<Section>> sectionsData = new MutableLiveData<>();

    public List<Slider> getSliderData() {
        return sliderData.getValue();
    }

    public void setSliderData(List<Slider> sliderList) {
        sliderData.setValue(sliderList);
    }

    public List<Section> getSectionsData() {
        return sectionsData.getValue();
    }

    public void setSectionsData(List<Section> sectionsList) {
        this.sectionsData.setValue(sectionsList);
    }
}
