package com.madeveloper.kayilarcarpet.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.madeveloper.kayilarcarpet.R;
import com.madeveloper.kayilarcarpet.model.Section;

import java.util.ArrayList;
import java.util.List;

public class SectionAdapter extends RecyclerView.Adapter<SectionAdapter.ViewHolder> {

    Context context;
    List<Section> sectionList;
    private OnItemClick onItemClick;

    public SectionAdapter(Context context) {
        this.context = context;

        sectionList = new ArrayList<>();
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public void setSectionList(List<Section> sectionList) {
        this.sectionList = sectionList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SectionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams")
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_section, null);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull SectionAdapter.ViewHolder holder, int position) {

        Section section = sectionList.get(position);

        Glide.with(context).load(section.getImageUrl()).into(holder.sectionImg);
        holder.titleTx.setText(section.getNameEn());

        holder.shopBt.setOnClickListener(view -> {
            if (onItemClick != null) onItemClick.onClick(position, section);
        });

    }

    @Override
    public int getItemCount() {
        return sectionList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        MaterialButton shopBt;
        TextView titleTx;
        ImageView sectionImg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            shopBt = itemView.findViewById(R.id.section_shop_bt);
            titleTx = itemView.findViewById(R.id.section_title_tx);
            sectionImg = itemView.findViewById(R.id.section_img);

        }
    }


    public interface OnItemClick {
        void onClick(int pos, Section section);
    }

}
