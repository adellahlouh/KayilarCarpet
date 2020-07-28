package com.madeveloper.kayilarcarpet.dialog;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.madeveloper.kayilarcarpet.R;
import com.madeveloper.kayilarcarpet.adapter.AddToCartAdapter;
import com.madeveloper.kayilarcarpet.databinding.DialogAddCartBinding;
import com.madeveloper.kayilarcarpet.model.CartItem;
import com.madeveloper.kayilarcarpet.model.Product;
import com.madeveloper.kayilarcarpet.utils.ProductUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AddToCartDialog extends DialogFragment {

    Product product;
    DialogAddCartBinding binding;
    AddToCartAdapter adapter;
    Context context;

    public AddToCartDialog(Context context, Product product) {
        this.context = context;
        this.product = product;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_add_cart, null, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new AddToCartAdapter(getContext());

        binding.addToCartRv.setLayoutManager(new LinearLayoutManager(getContext()));

        binding.addToCartRv.setAdapter(adapter);

        List<String> cartIdsProduct = ProductUtil.getCartIDsList(getContext());

        boolean isInCart = cartIdsProduct.contains(product.getId());

        if (isInCart)
            getSizeSelected();
        else
            showTotal(new ArrayList<>());

        setupCartBtn(isInCart);
        getSizeOfProducts();

        adapter.setOnSizeSelected(this::showTotal);

    }

    private void getSizeSelected() {


        CartItem cartItem = ProductUtil.getCartItemWithId(context, product.getId());

        if (cartItem != null) {

            List<Product.Size> sizeList = cartItem.convertSizeJsonToObjects();

            showTotal(sizeList);
            adapter.setSizeSelected(sizeList);

        } else
            showTotal(new ArrayList<>());


    }

    private void showTotal(List<Product.Size> sizeListSelected) {

        double total = 0.0, oldPriceTotal;

        for (Product.Size size : sizeListSelected)
            total += (size.price * size.count);

        if (product.isOffer() && total > 0) {

            oldPriceTotal = total;

            total = oldPriceTotal - (oldPriceTotal * product.getOfferPercent() / 100.0);

            binding.offerPriceTv.setVisibility(View.VISIBLE);
            binding.offerPriceTv.setPaintFlags(binding.offerPriceTv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


            binding.productPriceTv.setText(getString(R.string.total_0_00_jd, String.format(Locale.getDefault(), "%.2f", total)));
            binding.offerPriceTv.setText(getString(R.string.jd, String.format(Locale.getDefault(), "%.2f", oldPriceTotal)));

        } else {

            binding.productPriceTv.setText(getString(R.string.total_0_00_jd, String.format(Locale.getDefault(), "%.2f", total)));
            binding.offerPriceTv.setVisibility(View.GONE);

        }


    }

    private void setupCartBtn(boolean isInCart) {

        if (isInCart) {

            binding.addToCartBtn.setBackgroundColor(getResources().getColor(R.color.white));
            binding.addToCartBtn.setTextColor(getResources().getColor(R.color.red));
            binding.addToCartBtn.setBorderColor(getResources().getColor(R.color.colorPrimaryDark));
            binding.addToCartBtn.setIconResource(R.drawable.ic_remove_cart);
            binding.addToCartBtn.setText(getString(R.string.remove_from_cart));


        } else {

            binding.addToCartBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            binding.addToCartBtn.setTextColor(getResources().getColor(R.color.white));
            binding.addToCartBtn.setIconResource(R.drawable.ic_add_shopping_cart);
            binding.addToCartBtn.setText(getString(R.string.add_to_cart));

        }

        binding.addToCartBtn.setOnClickListener(view1 -> {

            if (adapter.getSizeSelected().isEmpty()) {
                Toast.makeText(context, getString(R.string.select_one_size_at_least), Toast.LENGTH_LONG).show();
                return;
            }

            //convert size to json object
            List<String> jsonSize = new ArrayList<>();
            for (Product.Size size : adapter.getSizeSelected())
                jsonSize.add(size.toString());

            CartItem cartItem = new CartItem();

            cartItem.setProductId(product.getId());
            cartItem.setImgUrl(product.getImageUrls().get(0));
            cartItem.setProductNameAr(product.getNameAr());
            cartItem.setProductNameEn(product.getNameEn());
            cartItem.setOffer(product.isOffer());
            cartItem.setOfferPercent(product.getOfferPercent());
            cartItem.setProductDesAr(product.getDesAr());
            cartItem.setProductDesEn(product.getDesEn());
            cartItem.setSizesSelected(jsonSize);

            ProductUtil.updateCartItemsListCart(getContext(), cartItem, !isInCart);
            setupCartBtn(!isInCart);
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes(params);
    }

    private void getSizeOfProducts() {

        List<Product.Size> sizeList = new ArrayList<>();

        for (String json : product.getSizePrice()) {
            Product.Size size = Product.Size.getFromJson(json);

            if (!sizeList.contains(size))
                sizeList.add(size);
        }

        //sizeList.get(0).toString();
        adapter.setSizeList(sizeList);

    }


}
