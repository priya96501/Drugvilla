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
import com.drugvilla.in.databinding.RowProductBinding;
import com.drugvilla.in.model.Document;
import com.drugvilla.in.model.dashboard.HomeSubData;
import com.drugvilla.in.ui.labs.LabDetail;
import com.drugvilla.in.ui.product.ProductDetail;
import com.drugvilla.in.ui.product.ProductMenu;
import com.drugvilla.in.utils.ActivityController;
import com.drugvilla.in.utils.AppConstant;
import com.drugvilla.in.utils.CommonUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {
    private Context context;
    private List<HomeSubData> dataList;
    private String type;
    private boolean valueRating = false;
    private boolean valueOffer = false;

    public ProductAdapter(Context context, List<HomeSubData> list, String type) {
        this.context = context;
        this.dataList = list;
        this.type = type;
    }

    public void showRating(boolean value) {
        this.valueRating = value;
    }
    public void showOffer(boolean value) {
        this.valueOffer = value;
    }

    @NonNull
    @Override
    public ProductAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_product, parent, false);
        return new ProductAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.MyViewHolder holder, final int position) {
        final HomeSubData model = dataList.get(position);
        if (valueRating) {
            holder.binding.llrating.setVisibility(View.VISIBLE); }
        if (valueOffer) {
            holder.binding.tvOffer.setVisibility(View.VISIBLE); }

        if (model.getImage() != null && !model.getImage().isEmpty()) {
            Picasso.with(context).load(AppConstant.IMAGE_URL + model.getImage())
                    .placeholder(R.drawable.gallery)
                    .error(R.drawable.gallery)
                    .into(holder.binding.ivCategory);
        }

        holder.binding.tvCategory.setText(model.getTitle());
        holder.binding.llCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (type.equalsIgnoreCase(AppConstant.TYPE_BRAND)) {

                    ActivityController.startActivity(context, ProductMenu.class, AppConstant.TYPE_BRAND);
                } else if (type.equalsIgnoreCase(AppConstant.TYPE_ALL_LABS)) {
                    ActivityController.startActivity(context,LabDetail.class);
                } else {
                    Intent intent;
                    intent = new Intent(context, ProductDetail.class);
                    intent.putExtra("PRODUCT_ID", dataList.get(position).getId());
                    context.startActivity(intent);
                    //ActivityController.startActivity(context,ProductDetail.class);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private RowProductBinding binding;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
