package com.drugvilla.in.adapter.order;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.drugvilla.in.R;
import com.drugvilla.in.databinding.RowMyOrderBinding;
import com.drugvilla.in.model.order.myOrder.MyOrderData;
import com.drugvilla.in.model.order.myOrder.MyOrderListData;
import com.drugvilla.in.ui.order.orderDetail.OrderDetails;
import com.drugvilla.in.utils.ActivityController;
import com.drugvilla.in.utils.AppConstant;

import java.util.List;

public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.MyViewHolder> {
    private Activity context;
    private List<MyOrderListData> orderDataList;

    public MyOrderAdapter(Activity context, List<MyOrderListData> list) {
        this.context = context;
        this.orderDataList = list;
    }

    @NonNull
    @Override
    public MyOrderAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_my_order, parent, false);
        return new MyOrderAdapter.MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyOrderAdapter.MyViewHolder holder, final int position) {

        final MyOrderListData myOrderData = orderDataList.get(position);
        holder.binding.llMyLabTestData.setVisibility(View.GONE);

        holder.binding.tvPatientName.setText(myOrderData.getName());
        holder.binding.tvPrice.setText(myOrderData.getOrderTotal());
        holder.binding.tvOrderDate.setText(myOrderData.getDate());
        holder.binding.tvOrderId.setText("Order Id : " + myOrderData.getUserOrderId());
        holder.binding.tvOrderStatus.setText(myOrderData.getOrderStatus());


        if (myOrderData.getOrderStatus().equalsIgnoreCase("confirmed")) {
            holder.binding.tvOrderStatus.setText("Order Confirmed");
            holder.binding.tvOrderStatus.setTextColor(context.getResources().getColor(R.color.dark_green));
        } else if (myOrderData.getOrderStatus().equalsIgnoreCase("pending")) {
            holder.binding.tvOrderStatus.setText("Order Processing");
            holder.binding.tvOrderStatus.setTextColor(context.getResources().getColor(R.color.colorYellow));
        } else {
            holder.binding.tvOrderStatus.setText("Order Cancelled");
            holder.binding.tvOrderStatus.setTextColor(context.getResources().getColor(R.color.colorRed));
        }

        holder.binding.btnViewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                // bundle.putString("ORDER_ID", myOrderData.getLabtestId());
                bundle.putString(AppConstant.FROM, AppConstant.TYPE_LABS);
                ActivityController.startActivity(context, OrderDetails.class, bundle, false, false);
                // ActivityController.startActivity(context, OrderDetails.class, AppConstant.TYPE_LABS);
            }
        });

    }

    @Override
    public int getItemCount() {
        return orderDataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private RowMyOrderBinding binding;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }


}


