package com.drugvilla.in.adapter.product;

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
import com.drugvilla.in.databinding.RowHealthConcernBinding;
import com.drugvilla.in.model.Document;
import com.drugvilla.in.model.dashboard.HomeSubData;
import com.drugvilla.in.ui.product.ProductMenu;
import com.drugvilla.in.utils.ActivityController;
import com.drugvilla.in.utils.AppConstant;
import com.drugvilla.in.utils.CommonUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HealthConcernAdapter extends RecyclerView.Adapter<HealthConcernAdapter.MyViewHolder> {
    private Context context;
    private List<HomeSubData> dataList;
    private String type;

    public HealthConcernAdapter(Context context, List<HomeSubData> list, String type) {
        this.context = context;
        this.dataList = list;
        this.type = type;
    }


    @NonNull
    @Override
    public HealthConcernAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_health_concern, parent, false);
        return new HealthConcernAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HealthConcernAdapter.MyViewHolder holder, int position) {
        final HomeSubData model = dataList.get(position);
        if (type.equalsIgnoreCase(AppConstant.HEALTH_CONCERN_ROUND)) {
            holder.binding.ivBackRound.setVisibility(View.VISIBLE);
            holder.binding.tvTitleRound.setVisibility(View.VISIBLE);
            holder.binding.ivBack.setVisibility(View.GONE);
            holder.binding.tvTitle.setVisibility(View.GONE);
        }

        if (model.getImage() != null && !model.getImage().isEmpty()) {
            Picasso.with(context).load(/*AppConstant.IMAGE_URL + */model.getImage())
                    .placeholder(R.drawable.gallery)
                    .error(R.drawable.gallery)
                    .into(holder.binding.ivBack);
        }

        if (model.getImage() != null && !model.getImage().isEmpty()) {
            Picasso.with(context).load(/*AppConstant.IMAGE_URL + */model.getImage())
                    .placeholder(R.drawable.gallery)
                    .error(R.drawable.gallery)
                    .into(holder.binding.ivBackRound);
        }
        holder.binding.tvTitle.setText(model.getTitle());
        holder.binding.tvTitleRound.setText(model.getTitle());
        holder.binding.llRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityController.startActivity(context, ProductMenu.class, AppConstant.TYPE_HEALTH_CONCERN);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private RowHealthConcernBinding binding;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
