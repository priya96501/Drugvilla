package com.drugvilla.in.adapter.labs;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.drugvilla.in.R;
import com.drugvilla.in.databinding.RowHealthPackagesBinding;
import com.drugvilla.in.listener.OnSelectedListener;
import com.drugvilla.in.model.Document;
import com.drugvilla.in.model.dashboard.labDashboard.LabDashboardData;
import com.drugvilla.in.ui.doctors.DoctorListing;
import com.drugvilla.in.ui.labs.PackageDetailActivity;
import com.drugvilla.in.ui.labs.TestDetailActivity;
import com.drugvilla.in.utils.ActivityController;
import com.drugvilla.in.utils.AppConstant;

import java.util.List;

public class PopularHealthPackageAdapter extends RecyclerView.Adapter<PopularHealthPackageAdapter.MyViewHolder> {
    private Context context;
    private List<LabDashboardData> dataList;
    private final OnSelectedListener listener;
    private String type;

    private boolean value = false;

    public PopularHealthPackageAdapter(Context context, List<LabDashboardData> list, OnSelectedListener listener, String type) {
        this.context = context;
        this.dataList = list;
        this.listener = listener;
        this.type = type;
    }

    public void removeAddButton(boolean value) {
        this.value = value;
    }

    @NonNull
    @Override
    public PopularHealthPackageAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_health_packages, parent, false);
        return new PopularHealthPackageAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final PopularHealthPackageAdapter.MyViewHolder holder, final int position) {
        final LabDashboardData model = dataList.get(position);
        if (value) {
            holder.binding.btnAdd.setVisibility(View.GONE);
        }
        if (type.equalsIgnoreCase(AppConstant.TYPE_ALL_LAB_TESTS)) {
            holder.binding.llrating.setVisibility(View.GONE);
            holder.binding.tvBy.setVisibility(View.GONE);
            holder.binding.tvCertifications.setVisibility(View.GONE);
            holder.binding.tvProvidedBy.setVisibility(View.VISIBLE);
            holder.binding.ivHead.setImageResource(R.drawable.ic_all_labs);

            holder.binding.tvProvidedBy.setText("Provided by ("+model.getProvidedByLabsCount()+") labs");
        } else {
            holder.binding.tvBy.setText(model.getProvidedByLab());
            holder.binding.tvCertifications.setText(model.getCertifiedBy());
            holder.binding.tvRating.setText(model.getRating());
        }
        holder.binding.tvTitle.setText(model.getName());
        holder.binding.tvAmount.setText(model.getAmount());
        holder.binding.tvOff.setText(model.getDiscount());

        holder.binding.llPackage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (type.equalsIgnoreCase(AppConstant.TYPE_ALL_LAB_TESTS)) {
                    Intent intent = new Intent(context, TestDetailActivity.class);
                    intent.putExtra("TEST_ID", dataList.get(position).getId());
                    context.startActivity(intent);
                } else {
                    Intent intent = new Intent(context, PackageDetailActivity.class);
                    intent.putExtra("PACKAGE_ID", dataList.get(position).getId());
                    context.startActivity(intent);
                }
            }
        });
        holder.binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(view, position);
                holder.binding.btnAdd.setBackground(null);
                holder.binding.btnAdd.setTextColor(context.getResources().getColor(R.color.dark_green));
                holder.binding.btnAdd.setText("Added");
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private RowHealthPackagesBinding binding;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
