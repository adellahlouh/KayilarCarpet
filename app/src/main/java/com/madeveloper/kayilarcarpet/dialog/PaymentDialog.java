package com.madeveloper.kayilarcarpet.dialog;


import android.annotation.SuppressLint;
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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;
import com.google.firestore.v1.DocumentTransform;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.madeveloper.kayilarcarpet.R;
import com.madeveloper.kayilarcarpet.databinding.DialogPaymentBinding;
import com.madeveloper.kayilarcarpet.model.Order;
import com.madeveloper.kayilarcarpet.model.Product;
import com.madeveloper.kayilarcarpet.model.User;
import com.madeveloper.kayilarcarpet.utils.Constant;
import com.madeveloper.kayilarcarpet.utils.ProductUtil;
import com.madeveloper.kayilarcarpet.utils.Util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;


public class PaymentDialog extends DialogFragment {


    DialogPaymentBinding binding;
    private List<Product> productList;

    private OnOrderSend onOrderSend;


    private KProgressHUD kProgressHUD;
    private double total;

    public PaymentDialog( List<Product> productList) {
        this.productList = productList;


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_payment, null, false);

        kProgressHUD = Util.getProgressDialog(getContext(),"Loading ...");

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        showTotal();

        binding.buyBt.setOnClickListener(v -> sendOrder());

    }

    private void sendOrder() {

        if(total <= 0)
            return;


        kProgressHUD.show();

        User user = Util.getUser(getContext());
        DocumentReference ordersRef = FirebaseFirestore.getInstance().collection(Constant.ORDERS_COL).document();

        WriteBatch batch = FirebaseFirestore.getInstance().batch();

        Order order  = new Order();

        order.setId(ordersRef.getId());
        order.setNameUser(user.name);
        order.setUserId(user.uid);
        order.setPhone(user.phone);
        order.setTotal(total);

        batch.set(ordersRef,order);

        for(Product product : productList){
            batch.set(ordersRef.collection("product").document(product.getId()),product);
        }


        batch.commit().addOnSuccessListener(aVoid -> {

            kProgressHUD.dismiss();
            ProductUtil.removeAllCart(getContext());
            if(onOrderSend != null)
                onOrderSend.onSend();

            Toast.makeText(getContext(), "You Order is send", Toast.LENGTH_SHORT).show();

            dismiss();

        });


    }

    @SuppressLint("SetTextI18n")
    private void showTotal() {

        total = 0.0;

        for (int i = 0; i < productList.size(); i++) {
            Product product = productList.get(i);
            total += product.getPrice();
        }

        NumberFormat formatter = new DecimalFormat("###,###,##0.00");

        String priceFormatter = formatter.format(total);

        binding.totalTx.setText("JD " + priceFormatter);

    }

    @Override
    public void onResume() {
        super.onResume();
        WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes(params);
    }

    public void setOnOrderSend(OnOrderSend onOrderSend) {
        this.onOrderSend = onOrderSend;
    }


    public interface OnOrderSend{
        void onSend();
    }

}
