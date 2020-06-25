package com.madeveloper.kayilarcarpet.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.madeveloper.kayilarcarpet.R;
import com.madeveloper.kayilarcarpet.model.Slider;
import com.madeveloper.kayilarcarpet.utils.Constant;
import com.madeveloper.kayilarcarpet.utils.Util;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.List;


public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderAdapterVH> {


    List<Slider> sliderList;
    Context context;


    public SliderAdapter(Context context) {
        this.context = context;
    }

    public void setSliderList(List<Slider> sliderList) {
        this.sliderList = sliderList;
        notifyDataSetChanged();

    }

    @Override
    public SliderAdapter.SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        @SuppressLint("InflateParams")
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_layout_item, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapter.SliderAdapterVH holder, int position) {

        Slider slider = sliderList.get(position);

        holder.txSliderDes.setText(Util.isEnglishDevice()? slider.getTitleEn(): slider.getTitleAr());
        Glide.with(context).load(slider.getImageUrl()).into(holder.imgSlider);
    }

    @Override
    public int getCount() {
        return sliderList.size();
    }

    public static class SliderAdapterVH extends SliderViewAdapter.ViewHolder {
        ImageView imgSlider;
        TextView txSliderDes;

        public SliderAdapterVH(View itemView) {
            super(itemView);
            imgSlider = itemView.findViewById(R.id.imgSlider);
            txSliderDes = itemView.findViewById(R.id.txSliderDes);

        }
    }
}
