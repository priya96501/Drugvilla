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
import com.drugvilla.in.databinding.RowAllCategoryBinding;
import com.drugvilla.in.model.Document;
import com.drugvilla.in.model.product.category.ProductCategoryData;
import com.drugvilla.in.ui.order.orderDetail.OrderDetails;
import com.drugvilla.in.ui.product.ProductMenu;
import com.drugvilla.in.utils.ActivityController;
import com.drugvilla.in.utils.AppConstant;
import com.drugvilla.in.utils.CommonUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CategoriesViewAdapter extends RecyclerView.Adapter<CategoriesViewAdapter.MyViewHolder> {
    private Activity context;
  //  private List<Document> dataList;
    private List<ProductCategoryData> dataList;

    public CategoriesViewAdapter(Activity context, List<ProductCategoryData> list) {
        this.context = context;
        this.dataList = list;
    }


    @NonNull
    @Override
    public CategoriesViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_all_category, parent, false);
        return new CategoriesViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriesViewAdapter.MyViewHolder holder, final int position) {
        final ProductCategoryData model = dataList.get(position);
        if (model.getCategoryImage() != null && !model.getCategoryImage().isEmpty()) {
            Picasso.with(context).load(/*AppConstant.IMAGE_URL + */model.getCategoryImage())
                    .placeholder(R.drawable.gallery)
                    .error(R.drawable.gallery)
                    .into(holder.binding.ivCategory);
        }
        holder.binding.tvCategoryName.setText(model.getCategoryName());
        holder.binding.tvSubCategoryName.setText(model.getProductSubCategoryDataList().toString().replace("["," ")
                .replace("]"," "));
        holder.binding.llRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putString("PRODUCT_CATEGORY_ID", dataList.get(position).getCategoryId());
                bundle.putString(AppConstant.FROM, AppConstant.TYPE_PRODUCT_CATEGORY);
                ActivityController.startActivity(context, ProductMenu.class, bundle, false, false);

              //  ActivityController.startActivity(context, ProductMenu.class, AppConstant.TYPE_PRODUCT_CATEGORY);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private RowAllCategoryBinding binding;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
