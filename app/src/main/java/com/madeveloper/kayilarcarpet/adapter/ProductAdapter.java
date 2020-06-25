package com.madeveloper.kayilarcarpet.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.madeveloper.kayilarcarpet.R;
import com.madeveloper.kayilarcarpet.model.Product;
import com.madeveloper.kayilarcarpet.model.Section;
import com.madeveloper.kayilarcarpet.utils.Constant;
import com.madeveloper.kayilarcarpet.utils.ProductUtil;
import com.madeveloper.kayilarcarpet.utils.Util;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private Context context;
    private boolean isEnglish;
    private List<Product> productList;
    private OnItemClick onItemClick;

    List<String> favIdProduct;

    public ProductAdapter(Context context) {
        this.context = context;

        productList = new ArrayList<>();

        isEnglish = Util.isEnglishDevice();

        favIdProduct = ProductUtil.getFavIDsList(context);
    }

    public void setProductList(List<Product> productList) {

        this.productList = productList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams")
        View inflate = LayoutInflater.from(context).inflate(R.layout.card_product, null);
        return new ViewHolder(inflate);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Product product = productList.get(position);


        holder.oldPrice.setPaintFlags(holder.oldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        holder.title_tv.setText(isEnglish ? product.getNameEn() : product.getNameAr());
        holder.description_tv.setText(isEnglish ? product.getDesEn() : product.getDesAr());
        holder.price_tv.setText(product.getPrice() + " JD");


        Glide.with(context).asBitmap().load(product.getImageUrls().get(0)).into(holder.productImg);


        holder.favoriteChk.setChecked(favIdProduct.contains(product.getId()));
        holder.favoriteChk.setOnCheckedChangeListener((compoundButton, b) -> ProductUtil.updateProductListFav(context,product,b));

        if (product.isOffer()) {
            holder.oldPrice.setVisibility(View.VISIBLE);
            holder.oldPrice.setPaintFlags(holder.oldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            int percent = (int) ((1.0 - (product.getPrice() / product.getOldPrice())) * 100.0);

            holder.oldPrice.setText(product.getOldPrice() + " JD");
            holder.offerPercent_tv.setText("OFF " + percent + "%");
        } else {
            holder.oldPrice.setVisibility(View.GONE);
            holder.offerPercent_tv.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(view -> onItemClick.onClick(position, product));


    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        CheckBox favoriteChk;
        ImageView productImg;
        TextView oldPrice, description_tv, title_tv, price_tv, offerPercent_tv;
        CardView productCard;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            favoriteChk = itemView.findViewById(R.id.favorite_ch);
            title_tv = itemView.findViewById(R.id.title_tx);
            offerPercent_tv = itemView.findViewById(R.id.offerPercent_tx);
            price_tv = itemView.findViewById(R.id.price_tx);
            description_tv = itemView.findViewById(R.id.description_tx);
            productImg = itemView.findViewById(R.id.product_img);
            oldPrice = itemView.findViewById(R.id.oldPrice_tx);
            productCard = itemView.findViewById(R.id.product_card);
        }
    }


    public interface OnItemClick {
        void onClick(int pos, Product product);
    }

}
