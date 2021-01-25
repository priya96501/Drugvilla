package com.drugvilla.in.adapter.product;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.drugvilla.in.R;
import com.drugvilla.in.databinding.RowSubstituteBinding;
import com.drugvilla.in.model.Document;
import com.drugvilla.in.model.product.productdetail.ProductData;
import com.drugvilla.in.ui.product.MedicineDetail;
import com.drugvilla.in.utils.ActivityController;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SubstituteAdapter extends RecyclerView.Adapter<SubstituteAdapter.MyViewHolder> {
    private Context context;
    private List<ProductData> dataList;


    public SubstituteAdapter(Context context, List<ProductData> list) {
        this.context = context;
        this.dataList = list;
    }

    @NonNull
    @Override
    public SubstituteAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_substitute, parent, false);
        return new SubstituteAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubstituteAdapter.MyViewHolder holder, final int position) {
        final ProductData model = dataList.get(position);
        holder.binding.tvProductName.setText(model.getName());
        holder.binding.tvBy.setText(model.getProvidedBy());
        holder.binding.tvOff.setText(model.getDiscount());
        holder.binding.tvAmount.setText(model.getAmount());
        holder.binding.tvMRP.setText(model.getMrp());
        holder.binding.tvMRP.setPaintFlags(holder.binding.tvMRP.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        if (model.getImage() != null && !model.getImage().isEmpty()) {
            Picasso.with(context)
                    .load(model.getImage())
                    .error(R.color.transparent)
                    .placeholder(R.color.transparent)
                    .into(holder.binding.ivProduct);
        }
        holder.binding.llPackage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityController.startActivity(context, MedicineDetail.class);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private RowSubstituteBinding binding;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
