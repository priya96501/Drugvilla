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
import com.drugvilla.in.ui.order.orderDetail.OrderDetails;
import com.drugvilla.in.ui.product.ProductMenu;
import com.drugvilla.in.ui.product.ProductsListing;
import com.drugvilla.in.utils.ActivityController;
import com.drugvilla.in.utils.AppConstant;
import com.drugvilla.in.utils.CommonUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AllCategoryAdapter extends RecyclerView.Adapter<AllCategoryAdapter.MyViewHolder> {
    private Activity activity;
    private List<CategoryData> dataList;
    private String type;

    public AllCategoryAdapter(Activity activity, List<CategoryData> list, String type) {
        this.activity = activity;
        this.dataList = list;
        this.type = type;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.row_categories, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final CategoryData model = dataList.get(position);
        if (type.equalsIgnoreCase(AppConstant.TYPE_SUB_PRODUCT_CATEGORY)) {
            holder.binding.ivSmall.setVisibility(View.VISIBLE);
            holder.binding.llCategory.setBackground(activity.getResources().getDrawable(R.drawable.back_stroke));
            holder.binding.ivBig.setVisibility(View.GONE);
            if (model.getImage() != null && !model.getImage().isEmpty()) {
                Picasso.with(activity).load(AppConstant.IMAGE_URL + model.getImage())
                        .placeholder(R.drawable.gallery)
                        .error(R.drawable.gallery)
                        .into(holder.binding.ivSmall);
            }
        } else if (type.equalsIgnoreCase(AppConstant.TYPE_LABS_CATEGORY)) {
            holder.binding.ivSmall.setVisibility(View.VISIBLE);
            holder.binding.ivBig.setVisibility(View.GONE);
            if (model.getImage() != null && !model.getImage().isEmpty()) {
                Picasso.with(activity).load(AppConstant.IMAGE_URL + model.getImage())
                        .placeholder(R.drawable.gallery)
                        .error(R.drawable.gallery)
                        .into(holder.binding.ivSmall);
            }
        } /*else if (type.equalsIgnoreCase(AppConstant.TYPE_PRODUCT_MIX_CATEGORY)) {
            holder.binding.ivSmall.setVisibility(View.VISIBLE);
            holder.binding.ivBig.setVisibility(View.GONE);
            if (model.getImage() != null && !model.getImage().isEmpty()) {
                Picasso.with(activity).load(AppConstant.IMAGE_URL + model.getImage())
                        .placeholder(R.drawable.gallery)
                        .error(R.drawable.gallery)
                        .into(holder.binding.ivSmall);
            }*/

         else {
            holder.binding.ivSmall.setVisibility(View.GONE);
            holder.binding.ivBig.setVisibility(View.VISIBLE);
            if (model.getImage() != null && !model.getImage().isEmpty()) {
                Picasso.with(activity).load(AppConstant.IMAGE_URL + model.getImage())
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
                    ActivityController.startActivity(activity, DoctorListing.class);
                } else*/
                if (type.equalsIgnoreCase(AppConstant.TYPE_LABS_CATEGORY)) {

                    ActivityController.startActivity(activity, ListingActivity.class);
                } else if (type.equalsIgnoreCase(AppConstant.TYPE_PRODUCT_CATEGORY)) {
                    Bundle bundle = new Bundle();
                    bundle.putString("PRODUCT_CATEGORY_ID", dataList.get(position).getId());
                    bundle.putString(AppConstant.FROM, AppConstant.TYPE_PRODUCT_CATEGORY);
                    ActivityController.startActivity(activity, ProductMenu.class, bundle, false, false);

                    // ActivityController.startActivity(activity, ProductMenu.class, AppConstant.TYPE_PRODUCT_CATEGORY);
                } else {

                    Bundle bundle = new Bundle();
                    bundle.putString("PRODUCT_SUB_CATEGORY_ID", dataList.get(position).getId());
                    bundle.putString(AppConstant.FROM, AppConstant.TYPE_PRODUCT);
                    ActivityController.startActivity(activity, ProductsListing.class, bundle, false, false);

                    // ActivityController.startActivity(activity, ProductsListing.class, AppConstant.TYPE_PRODUCT);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private RowCategoriesBinding binding;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
