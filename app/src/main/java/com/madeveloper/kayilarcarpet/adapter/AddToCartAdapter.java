package com.madeveloper.kayilarcarpet.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.madeveloper.kayilarcarpet.databinding.CardAddToCartBinding;
import com.madeveloper.kayilarcarpet.dialog.AddToCartDialog;
import com.madeveloper.kayilarcarpet.model.CartItem;
import com.madeveloper.kayilarcarpet.model.Product;
import com.madeveloper.kayilarcarpet.utils.Util;

import java.util.ArrayList;
import java.util.List;

public class AddToCartAdapter extends RecyclerView.Adapter<AddToCartAdapter.ViewHolder> {


    private Context context;
    private List<Product.Size> sizeList;

    private List<Product.Size> sizeSelected;
    private OnSizeSelected onSizeSelected;

    public AddToCartAdapter(Context context) {
        this.context = context;
        sizeList = new ArrayList<>();
        sizeSelected = new ArrayList<>();
    }


    public void setSizeList(List<Product.Size> sizeList) {
        this.sizeList = sizeList;
    }

    public List<Product.Size> getSizeSelected() {
        return sizeSelected;
    }

    public void setSizeSelected(List<Product.Size> sizeSelected) {
        this.sizeSelected = sizeSelected;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardAddToCartBinding binding = CardAddToCartBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Product.Size size = sizeList.get(position);

        holder.binding.sizeTx.setText(size.width + "x" + size.length+"cm");

        holder.binding.priceTx.setText(size.price + " JD");

        int posSelected = sizeSelected.indexOf(size);

        if (posSelected != -1) {

            holder.binding.countTx.setText(sizeSelected.get(posSelected).count + "");

            holder.binding.addToCartCheck.setChecked(true);

        } else {

            holder.binding.countTx.setText("1");

            holder.binding.addToCartCheck.setChecked(false);
        }

        holder.binding.addToCartCheck.setOnCheckedChangeListener((compoundButton, b) -> {

            size.count = Integer.parseInt(holder.binding.countTx.getText().toString());

            if (b)
                sizeSelected.add(size);
            else
                sizeSelected.remove(size);


            if (onSizeSelected != null)
                onSizeSelected.onSelected(sizeSelected);
        });

        holder.binding.plusBt.setOnClickListener(v -> {

            int count = Integer.parseInt(holder.binding.countTx.getText().toString());

            if (count < 100) {
                ++count;
                holder.binding.countTx.setText(count + "");
            }

        });


        holder.binding.minusBt.setOnClickListener(v -> {

            int count = Integer.parseInt(holder.binding.countTx.getText().toString());

            if (count > 1) {
                --count;
                holder.binding.countTx.setText(count + "");
            }

        });

    }


    @Override
    public void onViewRecycled(@NonNull ViewHolder holder) {
        super.onViewRecycled(holder);
        holder.binding.addToCartCheck.setOnCheckedChangeListener(null);
        holder.binding.plusBt.setOnClickListener(null);
        holder.binding.minusBt.setOnClickListener(null);
    }

    @Override
    public int getItemCount() {
        return sizeList.size();
    }

    public void setOnSizeSelected(OnSizeSelected onSizeSelected) {
        this.onSizeSelected = onSizeSelected;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardAddToCartBinding binding;

        public ViewHolder(@NonNull CardAddToCartBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


    public interface OnSizeSelected {

        void onSelected(List<Product.Size> allSizeSelected);
    }
}
