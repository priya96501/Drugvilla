package com.drugvilla.in.adapter.product;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.drugvilla.in.R;
import com.drugvilla.in.databinding.RowProductBinding;
import com.drugvilla.in.model.Document;
import com.drugvilla.in.ui.labs.LabDetail;
import com.drugvilla.in.ui.product.ProductDetail;
import com.drugvilla.in.utils.ActivityController;
import com.drugvilla.in.utils.AppConstant;
import java.util.List;

public class ProductCategoryAdapter extends RecyclerView.Adapter<ProductCategoryAdapter.MyViewHolder> {
    private Context context;
    private List<Document> dataList;
    private String type;

    public ProductCategoryAdapter(Context context, List<Document> list, String type) {
        this.context = context;
        this.dataList = list;
        this.type = type;
    }

    @NonNull
    @Override
    public ProductCategoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_product, parent, false);
        return new ProductCategoryAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductCategoryAdapter.MyViewHolder holder, int position) {
        final Document model = dataList.get(position);
        holder.binding.ivCategory.setImageResource(model.getImage());
        holder.binding.tvCategory.setText(model.getText());
        holder.binding.llCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (type.equalsIgnoreCase(AppConstant.TYPE_PRODUCT)) {
                    ActivityController.startActivity(context,ProductDetail.class);

                } else if (type.equalsIgnoreCase(AppConstant.TYPE_BRAND)) {
                    ActivityController.startActivity(context,ProductDetail.class);

                } else if (type.equalsIgnoreCase(AppConstant.TYPE_ALL_LABS)) {
                    ActivityController.startActivity(context,LabDetail.class);
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
