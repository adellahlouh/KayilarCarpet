package com.madeveloper.kayilarcarpet.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.madeveloper.kayilarcarpet.R;
import com.madeveloper.kayilarcarpet.databinding.CardOrderBinding;
import com.madeveloper.kayilarcarpet.model.Order;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> implements Filterable {

    Context context;
    List<Order> orderList;
    List<Order> filterList;


    public OrderAdapter(Context context) {
        this.context = context;

        orderList = new ArrayList<>();
        filterList = new ArrayList<>();

    }

    public void setOrderList(List<Order> ordersList) {

        this.orderList = ordersList;
        this.filterList = ordersList;

        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public OrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardOrderBinding binding = CardOrderBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.ViewHolder holder, int position) {

        Order order = filterList.get(position);

        holder.binding.totalTv.setText(order.getTotal() + " JD");

        holder.binding.addressTv.setText(order.getAddress());

        Order.OrderState state = order.getState();

        if (state == Order.OrderState.Pending) {

            holder.binding.deliveryTv.setTextColor(Color.RED);
            holder.binding.deliveryTv.setText(context.getString(R.string.pending));

        } else if (state == Order.OrderState.OnProgress) {

            holder.binding.deliveryTv.setTextColor(Color.BLUE);
            holder.binding.deliveryTv.setText(context.getString(R.string.on_progress));

        } else if (state == Order.OrderState.Delivered) {

            holder.binding.deliveryTv.setTextColor(Color.GREEN);
            holder.binding.deliveryTv.setText(context.getString(R.string.delivered_done));

        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy  h:mm a", Locale.getDefault());
        String time = dateFormat.format(order.getTime());

        holder.binding.timeTv.setText(time);

    }

    @Override
    public int getItemCount() {
        return filterList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        CardOrderBinding binding;

        public ViewHolder(@NonNull CardOrderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                FilterResults results = new FilterResults();

                if (charSequence == null || charSequence.length() == 0 || charSequence.toString().toLowerCase().equals("all")) {
                    results.count = orderList.size();
                    results.values = orderList;

                    return results;
                }


                List<Order> listOrder = new ArrayList<>();
                String stats = charSequence.toString();

                for (Order order : orderList) {

                    if (order.getState() != null && order.getState().toString().equals(stats))
                        listOrder.add(order);

                }

                results.count = listOrder.size();
                results.values = listOrder;

                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

                filterList = (List<Order>) filterResults.values;
                notifyDataSetChanged();
            }


        };
    }

}
