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
import com.drugvilla.in.databinding.RowTopBrandBinding;
import com.drugvilla.in.model.Document;
import com.drugvilla.in.model.dashboard.HomeSubData;
import com.drugvilla.in.ui.product.ProductMenu;
import com.drugvilla.in.ui.product.ProductsListing;
import com.drugvilla.in.utils.ActivityController;
import com.drugvilla.in.utils.AppConstant;
import com.drugvilla.in.utils.CommonUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BrandAdapter extends RecyclerView.Adapter<BrandAdapter.MyViewHolder> {
    private Context context;
    private Activity activity;
    private List<HomeSubData> dataList;
    private MyViewHolder myViewHolder;
    private LayoutInflater inflater;
    private int size;

    public BrandAdapter(Context context, List<HomeSubData> list) {
        this.context = context;
        this.dataList = list;
        this.inflater = LayoutInflater.from(context);
      //  size = CommonUtils.getWidth(activity) / 3;
    }


    @NonNull
    @Override
    public BrandAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_top_brand, parent, false);
        this.myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BrandAdapter.MyViewHolder holder, int position) {
        final HomeSubData model = dataList.get(position);
        holder.binding.tvBrandName.setText(model.getTitle());
        if (model.getImage() != null && !model.getImage().isEmpty()) {
            Picasso.with(context).load(/*AppConstant.IMAGE_URL + */model.getImage())
                    .placeholder(R.drawable.gallery)
                    .error(R.drawable.gallery)
                    .into(holder.binding.ivBrandImage);
        }
        holder.binding.llRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityController.startActivity(activity, ProductMenu.class, AppConstant.TYPE_BRAND);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private RowTopBrandBinding binding;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
