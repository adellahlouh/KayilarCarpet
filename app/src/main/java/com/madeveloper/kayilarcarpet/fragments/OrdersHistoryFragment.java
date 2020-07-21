package com.madeveloper.kayilarcarpet.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.madeveloper.kayilarcarpet.R;
import com.madeveloper.kayilarcarpet.adapter.OrderAdapter;
import com.madeveloper.kayilarcarpet.databinding.FragmentOrdersHistoryBinding;
import com.madeveloper.kayilarcarpet.handler.OnNavigateFragment;
import com.madeveloper.kayilarcarpet.model.Order;
import com.madeveloper.kayilarcarpet.model.User;
import com.madeveloper.kayilarcarpet.utils.Constant;
import com.madeveloper.kayilarcarpet.utils.Util;

import java.util.List;


public class OrdersHistoryFragment extends BaseFragment {


    FragmentOrdersHistoryBinding binding;

    private CollectionReference ordersRef;
    private OrderAdapter adapter;
    private OnNavigateFragment onNavigateFragment;

    public OrdersHistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public String getTitle() {
        return getString(R.string.orderHistory);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        onNavigateFragment = (OnNavigateFragment) getActivity();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_orders_history, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        onNavigateFragment.onFragmentShow(this);
        ordersRef = FirebaseFirestore.getInstance().collection(Constant.ORDERS_COL);


        adapter = new OrderAdapter(getContext());
        binding.recyclerOrder.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerOrder.setAdapter(adapter);

        binding.pendingRd.setOnClickListener(v -> adapter.getFilter().filter(Order.OrderState.Pending.toString()));

        binding.onProgressRd.setOnClickListener(v -> adapter.getFilter().filter( Order.OrderState.OnProgress.toString()));

        binding.deliveredRd.setOnClickListener(v -> adapter.getFilter().filter( Order.OrderState.Delivered.toString()));

        binding.allRd.setOnClickListener(v->adapter.getFilter().filter("all"));


        loadOrders();
    }

    private void loadOrders() {


       User user = Util.getUser(getContext());


       ordersRef.whereEqualTo("userId",user.uid).get().addOnSuccessListener(queryDocumentSnapshots -> {

           List<Order> orderList = Util.getListFromCollection(queryDocumentSnapshots, Order.class);

           binding.progressCircular.setVisibility(View.GONE);
           binding.groupRd.setVisibility(View.VISIBLE);

           adapter.setOrderList(orderList);

       });

    }


}