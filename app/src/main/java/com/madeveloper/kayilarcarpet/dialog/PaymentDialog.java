package com.madeveloper.kayilarcarpet.dialog;


import android.annotation.SuppressLint;
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

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.madeveloper.kayilarcarpet.R;
import com.madeveloper.kayilarcarpet.databinding.DialogPaymentBinding;
import com.madeveloper.kayilarcarpet.model.Order;
import com.madeveloper.kayilarcarpet.model.Product;
import com.madeveloper.kayilarcarpet.model.Promo;
import com.madeveloper.kayilarcarpet.model.User;
import com.madeveloper.kayilarcarpet.utils.Constant;
import com.madeveloper.kayilarcarpet.utils.FCM;
import com.madeveloper.kayilarcarpet.utils.ProductUtil;
import com.madeveloper.kayilarcarpet.utils.Util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;


public class PaymentDialog extends DialogFragment {

    DialogPaymentBinding binding;

    private List<Product> productList;
    private OnOrderSend onOrderSend;
    private KProgressHUD kProgressHUD;

    private double total;
    private Promo promo;
    Order order;

    public PaymentDialog(List<Product> productList) {
        this.productList = productList;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_payment, null, false);

        kProgressHUD = Util.getProgressDialog(getContext(), "Loading ...");

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        showTotal();

        binding.buyBt.setOnClickListener(v -> sendOrder());

        binding.applyPromoBt.setOnClickListener(view1 -> applyPromo());


    }

    private void sendOrder() {

        if (total <= 0)
            return;


        kProgressHUD.show();

        User user = Util.getUser(getContext());
        DocumentReference ordersRef = FirebaseFirestore.getInstance().collection(Constant.ORDERS_COL).document();

        WriteBatch batch = FirebaseFirestore.getInstance().batch();

        order = new Order();

        order.setId(ordersRef.getId());
        order.setNameUser(user.name);
        order.setUserId(user.uid);
        order.setPhone(user.phone);
        order.setTotal(total);

        if (promo != null) {
            order.setPromoUsed(promo.getValue());
            order.setDiscountPercentage(promo.getPercentage());

        }

        if (!binding.addressEt.getText().toString().isEmpty())
            order.setAddress(binding.addressEt.getText().toString());
        else {
            binding.addressEt.setError(getString(R.string.enter_your_address));
            kProgressHUD.dismiss();
            return;
        }

        batch.set(ordersRef, order);

        for (Product product : productList) {
            batch.set(ordersRef.collection("product").document(product.getId()), product);
        }


        batch.commit().addOnSuccessListener(aVoid -> {

            kProgressHUD.dismiss();
            ProductUtil.removeAllCart(getContext());
            if (onOrderSend != null)
                onOrderSend.onSend();

            Toast.makeText(getContext(), getContext().getString(R.string.your_order_is_send), Toast.LENGTH_LONG).show();

            FCM.sendNotificationToTopic(getContext(),
                    FCM.createNotificationObject(user.getName() + " طلب جديد من العميل ",
                            user.getPhone() + "رقم هاتف العميل : "),
                    null, Constant.TOPIC_ADMIN
            );

            dismiss();

        });


    }

    @SuppressLint("SetTextI18n")
    private void applyPromo() {

        String promoKey = binding.couponEt.getText().toString().trim();


        if (promoKey.isEmpty())
            return;

        User user = Util.getUser(getContext());

        KProgressHUD progressHUD = Util.getProgressDialog(getContext(), getString(R.string.checking));
        progressHUD.show();

        DocumentReference promoRef = FirebaseFirestore.getInstance().collection(Constant.PROMO_COL)
                .document(promoKey);


        promoRef.get().addOnSuccessListener(documentSnapshot -> {

            promo = documentSnapshot.toObject(Promo.class);


            if (!documentSnapshot.exists() || !promo.getVisible()) {

                progressHUD.dismiss();
                Toast.makeText(getContext(), getString(R.string.promo_not_valid), Toast.LENGTH_LONG).show();

                return;
            }

            promoRef.collection("used").document(user.uid).get().addOnSuccessListener(documentSnapshot1 -> {

                progressHUD.dismiss();

                if (documentSnapshot1.exists()) {
                    Toast.makeText(getContext(), getString(R.string.cant_use_same_promo), Toast.LENGTH_LONG).show();
                    return;
                }


                promoRef.collection("used").document(user.uid).set(new HashMap<>());

                binding.applyPromoBt.setVisibility(View.GONE);
                binding.percentPromoTv.setVisibility(View.VISIBLE);
                binding.percentPromoTv.setText(getString(R.string.sale) + " %" + promo.getPercentage());

                double discount = promo.getPercentage() / 100;


                total = total - (total * discount);
                NumberFormat formatter = new DecimalFormat("###,###,##0.00");

                String priceFormatter = formatter.format(total);
                binding.totalTx.setText(priceFormatter + " JD");


            });


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

    public interface OnOrderSend {
        void onSend();
    }

}
