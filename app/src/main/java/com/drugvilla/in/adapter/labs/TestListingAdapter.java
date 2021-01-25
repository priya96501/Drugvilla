package com.drugvilla.in.adapter.labs;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.drugvilla.in.R;

import com.drugvilla.in.databinding.RowListingViewBinding;
import com.drugvilla.in.listener.OnSelectedListener;
import com.drugvilla.in.model.Document;
import com.drugvilla.in.model.doctor.DoctorData;
import com.drugvilla.in.model.lab.test.TestData;
import com.drugvilla.in.ui.doctors.DoctorListing;
import com.drugvilla.in.ui.labs.PackageDetailActivity;
import com.drugvilla.in.ui.labs.TestDetailActivity;
import com.drugvilla.in.ui.order.Cart;
import com.drugvilla.in.utils.ActivityController;
import com.drugvilla.in.utils.AppConstant;

import java.util.List;

public class TestListingAdapter extends RecyclerView.Adapter<TestListingAdapter.MyViewHolder> {
    private Context context;
    private List<TestData> dataList;
    private final OnSelectedListener listener;
    private boolean isLoadingAdded = false;

    public TestListingAdapter(Context context, List<TestData> list, OnSelectedListener listener) {
        this.context = context;
        this.dataList = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TestListingAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_listing_view, parent, false);
        return new TestListingAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final TestListingAdapter.MyViewHolder holder, final int position) {
        final TestData model = dataList.get(position);
        holder.binding.ivHead.setImageResource(R.drawable.ic_all_labs);
        holder.binding.viewIncludedTest.setVisibility(View.GONE);
        holder.binding.tvTitle.setText(model.getTest_name());
        holder.binding.tvProvidedBy.setText("Provided by ("+model.getProvided_by_labs()+") Labs");
        holder.binding.tvAmount.setText(model.getAmount());
        holder.binding.tvOff.setText(model.getDiscount());
        holder.binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityController.startActivity(context, Cart.class, AppConstant.TYPE_LAB_CART);
            }
        });
        holder.binding.llPackage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, TestDetailActivity.class);
                intent.putExtra("TEST_ID", dataList.get(position).getTest_id());
                context.startActivity(intent);


            }
        });
        holder.binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(view, position);
               /* holder.binding.btnAdd.setBackground(context.getResources().getDrawable(R.drawable.round_border_green));
                holder.binding.btnAdd.setTextColor(context.getResources().getColor(R.color.dark_green));
                holder.binding.btnAdd.setText("Added");*/
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private RowListingViewBinding binding;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
    public void add(TestData mc) {
        dataList.add(mc);
        notifyItemInserted(dataList.size() - 1);
    }
    public void remove(TestData membersData) {
        int position = dataList.indexOf(membersData);
        if (position > -1) {
            dataList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }



    public void setList(List<TestData> list) {
        this.dataList = list;
        notifyDataSetChanged();
    }

    public void addAll(List<TestData> newList) {
        int lastIndex = dataList.size() - 1;
        dataList.addAll(newList);
        notifyItemRangeInserted(lastIndex, newList.size());
    }
    public TestData getItem(int position) {
        return dataList.get(position);
    }

}
