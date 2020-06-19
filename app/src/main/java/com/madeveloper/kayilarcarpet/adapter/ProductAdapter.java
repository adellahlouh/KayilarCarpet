package com.madeveloper.kayilarcarpet.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.madeveloper.kayilarcarpet.R;
import com.madeveloper.kayilarcarpet.model.Product;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    Context context ;
    List<Product> productList ;

    public ProductAdapter(Context context) {
        this.context = context;
    }

    public void setProductList(List<Product> productList ){
        this.productList = productList ;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams")
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_product, null);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

       // Product product = productList.get(position);

        holder.oldPrice.setPaintFlags(holder.oldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        CheckBox favoriteChk ;
        ImageView productImg ;
        TextView oldPrice  ;
        CardView productCard ;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            favoriteChk = itemView.findViewById(R.id.favorite_ch);
            productImg = itemView.findViewById(R.id.product_img);
            oldPrice = itemView.findViewById(R.id.oldPrice_tx);
            productCard = itemView.findViewById(R.id.product_card);
        }
    }
}
