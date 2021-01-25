package com.drugvilla.in.adapter.product;

import android.app.Activity;
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
import com.drugvilla.in.databinding.RowCategoriesBinding;
import com.drugvilla.in.model.Document;
import com.drugvilla.in.model.dashboard.CategoryData;
import com.drugvilla.in.ui.doctors.DoctorListing;
import com.drugvilla.in.ui.labs.ListingActivity;
import com.drugvilla.in.ui.product.ProductMenu;
import com.drugvilla.in.utils.ActivityController;
import com.drugvilla.in.utils.AppConstant;
import com.drugvilla.in.utils.CommonUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {
    private Activity context;
    private List<CategoryData> dataList;
    private String type;

    public CategoryAdapter(Activity context, List<CategoryData> list, String type) {
        this.context = context;
        this.dataList = list;
        this.type = type;
    }

    @NonNull
    @Override
    public CategoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_categories, parent, false);
        return new CategoryAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.MyViewHolder holder, final int position) {
        final CategoryData model = dataList.get(position);

        if (type.equalsIgnoreCase(AppConstant.TYPE_SUB_PRODUCT_CATEGORY)) {
            holder.binding.ivSmall.setVisibility(View.VISIBLE);
            holder.binding.llCategory.setBackground(context.getResources().getDrawable(R.drawable.back_stroke));
            holder.binding.ivBig.setVisibility(View.GONE);
            if (model.getImage() != null && !model.getImage().isEmpty()) {
                Picasso.with(context).load(AppConstant.IMAGE_URL + model.getImage())
                        .placeholder(R.drawable.gallery)
                        .error(R.drawable.gallery)
                        .into(holder.binding.ivSmall);
            }

        } else if (type.equalsIgnoreCase(AppConstant.TYPE_LABS_CATEGORY)) {
            holder.binding.ivSmall.setVisibility(View.VISIBLE);
            holder.binding.ivBig.setVisibility(View.GONE);
            if (model.getImage() != null && !model.getImage().isEmpty()) {
                Picasso.with(context).load(AppConstant.IMAGE_URL + model.getImage())
                        .placeholder(R.drawable.gallery)
                        .error(R.drawable.gallery)
                        .into(holder.binding.ivSmall);
            }
        } else {
            holder.binding.ivSmall.setVisibility(View.GONE);
            holder.binding.ivBig.setVisibility(View.VISIBLE);
            if (model.getImage() != null && !model.getImage().isEmpty()) {
                Picasso.with(context).load(AppConstant.IMAGE_URL + model.getImage())
                        .placeholder(R.drawable.gallery)
                        .error(R.drawable.gallery)
                        .into(holder.binding.ivBig);
            }

        }
        holder.binding.tvCategory.setText(model.getTitle());
        holder.binding.llCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* if (type.equalsIgnoreCase(AppConstant.TYPE_DOCTOR_CATEGORY)) {
                    ActivityController.startActivity(context, DoctorListing.class);
                } else */if (type.equalsIgnoreCase(AppConstant.TYPE_LABS_CATEGORY)) {
                    ActivityController.startActivity(context, ListingActivity.class);
                } else /*if (type.equalsIgnoreCase(AppConstant.TYPE_PRODUCT_CATEGORY))*/ {
                    Bundle bundle = new Bundle();
                    bundle.putString("PRODUCT_CATEGORY_ID", dataList.get(position).getId());
                    bundle.putString(AppConstant.FROM, AppConstant.TYPE_PRODUCT_CATEGORY);
                    ActivityController.startActivity(context, ProductMenu.class, bundle, false, false);
                    //ActivityController.startActivity(context, ProductMenu.class, AppConstant.TYPE_PRODUCT_CATEGORY);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (dataList != null) {
            if (type.equalsIgnoreCase(AppConstant.TYPE_LABS_CATEGORY)) {
                if (dataList.size() >= 4)
                    return 4;
                return dataList.size();
            } else {
                if (dataList.size() >= 3)
                    return 3;
                return dataList.size();
            }
        }
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private RowCategoriesBinding binding;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
