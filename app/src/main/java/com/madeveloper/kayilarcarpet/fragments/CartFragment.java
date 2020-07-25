package com.madeveloper.kayilarcarpet.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.madeveloper.kayilarcarpet.R;
import com.madeveloper.kayilarcarpet.adapter.CartAdapter;
import com.madeveloper.kayilarcarpet.databinding.FragmentCartBinding;
import com.madeveloper.kayilarcarpet.dialog.PaymentDialog;
import com.madeveloper.kayilarcarpet.handler.OnNavigateFragment;
import com.madeveloper.kayilarcarpet.model.CartItem;
import com.madeveloper.kayilarcarpet.model.Product;
import com.madeveloper.kayilarcarpet.utils.ProductUtil;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;


public class CartFragment extends BaseFragment {

    private OnNavigateFragment onNavigateFragment;
    private List<CartItem> cartItems;
    CartAdapter cartAdapter;

    public CartFragment() {
        // Required empty public constructor
    }

    FragmentCartBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onNavigateFragment = (OnNavigateFragment) getActivity();

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cart, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        onNavigateFragment.onFragmentShow(this);
        cartAdapter = new CartAdapter(getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.cartRv.setLayoutManager(layoutManager);
        binding.cartRv.setAdapter(cartAdapter);



        setUpAdapter();


        cartAdapter.setOnItemClick((int pos, Product product) -> {
            Bundle bundle = new Bundle();
            bundle.putString(DescriptionProductFragment.ARG_PRODUCT, new Gson().toJson(product));
            onNavigateFragment.onNavigate(R.id.descriptionProductFragment, bundle);
        });


        binding.checkoutBt.setOnClickListener(view1 -> showEditDialog());

    }

    private void showEditDialog() {

        if(cartItems.isEmpty()){
            Toast.makeText(getContext(), R.string.no_item_inside_in_cart, Toast.LENGTH_SHORT).show();
            return;
        }

        FragmentManager fm = getActivity().getSupportFragmentManager();
        PaymentDialog paymentDialog = new PaymentDialog(cartItems);


        paymentDialog.show(fm, "fragment_edit_name");

        paymentDialog.setOnOrderSend(this::setUpAdapter);

    }

    @SuppressLint("SetTextI18n")
    private void setUpAdapter() {

        cartItems = ProductUtil.getCartItemsList(getContext());
        cartAdapter.setCartItems(cartItems);

        if(cartItems.isEmpty())
            binding.emptyView.setVisibility(View.VISIBLE);

        binding.itemTx.setText(getString(R.string.item) + " " + cartItems.size());

        double total = 0.0;
        CartItem cartItem;

        for (int i = 0; i < cartItems.size(); i++) {
            cartItem = cartItems.get(i);
            total += cartItem.getTotalPrice();
        }
        NumberFormat formatter = new DecimalFormat("###,###,##0.00");

        String priceFormatter = formatter.format(total);

        binding.totalTx.setText(getContext().getString(R.string.total)+" JD " + priceFormatter);

    }


}