package com.madeveloper.kayilarcarpet.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.madeveloper.kayilarcarpet.R;
import com.madeveloper.kayilarcarpet.model.Product;
import com.madeveloper.kayilarcarpet.utils.ProductUtil;
import com.madeveloper.kayilarcarpet.utils.Util;

import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    Context context ;
    private List<Product> productList;
    private boolean isEnglish;

    private ProductAdapter.OnItemClick onItemClick;
    List<String> cartIdProduct;


    public CartAdapter(Context context) {
        this.context = context;

        productList = new ArrayList<>();

        isEnglish = Util.isEnglishDevice();

        cartIdProduct = ProductUtil.getCartIDsList(context);
    }

    public void setProductList(List<Product> productList) {

        this.productList = productList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams")
        View inflate = LayoutInflater.from(context).inflate(R.layout.card_cart, null);
        return new CartAdapter.ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.ViewHolder holder, int position) {
        Product product = productList.get(position);

        Glide.with(context).asBitmap().load(product.getImageUrls().get(0)).into(holder.productImg);

        holder.nameProductTx.setText(isEnglish ?product.getNameEn() :product.getNameAr());


        holder.priceTx.setText(String.valueOf(product.getPrice()));

        holder.oldPriceTx.setPaintFlags(holder.oldPriceTx.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        holder.oldPriceTx.setText(String.valueOf(product.getOldPrice()));

        holder.descriptionTx.setText(isEnglish ? product.getDesEn() : product.getDesAr());



    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public void setOnItemClick(ProductAdapter.OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameProductTx, oldPriceTx ,priceTx ,descriptionTx;
        ImageView productImg ;
        ImageButton deleteImg ;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nameProductTx = itemView.findViewById(R.id.nameProduct_tx);
            oldPriceTx = itemView.findViewById(R.id.oldPrice_tx);
            priceTx = itemView.findViewById(R.id.price_tx);
            descriptionTx = itemView.findViewById(R.id.description_tx);
            productImg = itemView.findViewById(R.id.product_img);
            deleteImg = itemView.findViewById(R.id.delete_img);

        }
    }

    public interface OnItemClick {
        void onClick(int pos, Product product);
    }


}