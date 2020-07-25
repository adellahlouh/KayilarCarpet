package com.madeveloper.kayilarcarpet.dialog;

import android.content.Context;
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
import com.madeveloper.kayilarcarpet.model.Product;
import com.madeveloper.kayilarcarpet.utils.ProductUtil;

import java.util.ArrayList;
import java.util.List;

public class AddToCartDialog extends DialogFragment {

    Product product;
    DialogAddCartBinding binding;
    AddToCartAdapter adapter;
    Context context ;

    public AddToCartDialog(Context context ,Product product) {
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
        setupCartBtn(isInCart);




        getSize();

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
            ProductUtil.updateProductListCart(getContext(),product,!isInCart);
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

    private void getSize() {

        List<Product.Size> sizeList = new ArrayList<>();

        for (String json : product.getSizePrice() ){
            sizeList.add(Product.Size.getFromJson(json));
        }

        //sizeList.get(0).toString();
        adapter.setSizeList(sizeList);

    }


}
