package com.madeveloper.kayilarcarpet.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
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
import com.madeveloper.kayilarcarpet.model.CartItem;
import com.madeveloper.kayilarcarpet.model.Product;
import com.madeveloper.kayilarcarpet.utils.ProductUtil;
import com.madeveloper.kayilarcarpet.utils.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    Context context ;
    private List<CartItem> cartItems;
    private boolean isEnglish;
    List<Product.Size> sizeList ;

    private ProductAdapter.OnItemClick onItemClick;
    List<String> cartIdProduct;


    public CartAdapter(Context context) {
        this.context = context;

        cartItems = new ArrayList<>();

        isEnglish = Util.isEnglishDevice(context);

        cartIdProduct = ProductUtil.getCartIDsList(context);

        sizeList = new ArrayList<>();
    }

    public void setCartItems(List<CartItem> cartItems /*,List<Product.Size> sizeList*/ ) {

        this.cartItems = cartItems;
        //this.sizeList = sizeList ;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams")
        View inflate = LayoutInflater.from(context).inflate(R.layout.card_cart, null);
        return new CartAdapter.ViewHolder(inflate);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CartAdapter.ViewHolder holder, int position) {
        CartItem cartItem = cartItems.get(position);

        Glide.with(context).asBitmap().load(cartItem.getImgUrl()).into(holder.productImg);

        holder.nameProductTx.setText(isEnglish ?cartItem.getProductNameEn() :cartItem.getProductNameAr());


        holder.priceTx.setText(context.getString(R.string.jd,String.format(Locale.getDefault(),"%.2f", cartItem.getTotalPrice())));

        if (cartItem.isOffer()) {
            holder.oldPrice.setVisibility(View.VISIBLE);
            holder.oldPrice.setPaintFlags(holder.oldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

           // int percent = (int) ((1.0 - (product.getPrice() / product.getOldPrice())) * 100.0);

            holder.oldPrice.setText(cartItem.getOldTotalPrice() + " JD");
            holder.offerPercent_tv.setText(context.getString(R.string.off) + cartItem.getOfferPercent() + "%");
        } else {
            holder.oldPrice.setVisibility(View.GONE);
            holder.offerPercent_tv.setVisibility(View.GONE);
        }

        holder.descriptionTx.setText(isEnglish ? cartItem.getProductDesEn() : cartItem.getProductDesAr());


    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public void setOnItemClick(ProductAdapter.OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameProductTx, oldPrice,priceTx ,descriptionTx,offerPercent_tv;
        ImageView productImg ;
        ImageButton deleteImg ;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nameProductTx = itemView.findViewById(R.id.nameProduct_tx);
            oldPrice = itemView.findViewById(R.id.oldPrice_tx);
            priceTx = itemView.findViewById(R.id.price_tx);
            descriptionTx = itemView.findViewById(R.id.description_tx);
            productImg = itemView.findViewById(R.id.product_img);
            deleteImg = itemView.findViewById(R.id.delete_img);
            offerPercent_tv = itemView.findViewById(R.id.offerPercent_tx);


        }
    }

    public interface OnItemClick {
        void onClick(int pos, Product product);
    }


}
