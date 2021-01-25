package com.drugvilla.in.adapter.labs;

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
import com.drugvilla.in.model.lab.myTest.MyTestData;
import com.drugvilla.in.model.order.myOrder.MyOrderListData;
import com.drugvilla.in.ui.order.orderDetail.OrderDetails;
import com.drugvilla.in.utils.ActivityController;
import com.drugvilla.in.utils.AppConstant;

import java.util.List;

public class MyLabTestAdapter extends RecyclerView.Adapter<MyLabTestAdapter.MyViewHolder> {
    private Activity context;
    private List<MyOrderListData> labTestDataList;


    public MyLabTestAdapter(Activity context, List<MyOrderListData> list) {
        this.context = context;
        this.labTestDataList = list;

    }

    @NonNull
    @Override
    public MyLabTestAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_my_order, parent, false);
        return new MyLabTestAdapter.MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyLabTestAdapter.MyViewHolder holder, final int position) {
        final MyOrderListData myTestData = labTestDataList.get(position);

        holder.binding.tvOrderDate.setVisibility(View.GONE);
        holder.binding.tvPrice.setText(myTestData.getOrderTotal());
        holder.binding.tvOrderStatus.setText(myTestData.getOrderStatus());
        holder.binding.tvPatientName.setText(myTestData.getName());
        holder.binding.tvTime.setText(myTestData.getTime());
        holder.binding.tvDate.setText(myTestData.getDate());
        holder.binding.tvOrderId.setText("Order Id : " + myTestData.getUserOrderId());

       /* if (myTestData.getLabtestStatus().equalsIgnoreCase("1")) {
            holder.binding.tvOrderStatus.setText("Order Placed");
            holder.binding.tvOrderStatus.setTextColor(context.getResources().getColor(R.color.dark_green));
        } else if (myTestData.getLabtestStatus().equalsIgnoreCase("2")) {
            holder.binding.tvOrderStatus.setText("Order Processing");
            holder.binding.tvOrderStatus.setTextColor(context.getResources().getColor(R.color.colorYellow));
        } else {
            holder.binding.tvOrderStatus.setText("Order Cancelled");
            holder.binding.tvOrderStatus.setTextColor(context.getResources().getColor(R.color.colorRed));
        }*/
        holder.binding.btnViewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putString("ORDER_ID", myTestData.getOrderId());
                bundle.putString(AppConstant.FROM, AppConstant.TYPE_LABS);
                ActivityController.startActivity(context, OrderDetails.class, bundle, false, false);
                //ActivityController.startActivity(context, OrderDetails.class, AppConstant.TYPE_PRODUCT);
            }
        });

    }

    @Override
    public int getItemCount() {
        return labTestDataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private RowMyOrderBinding binding;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }


}


