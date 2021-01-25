package com.drugvilla.in.adapter.product.sortFilter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;


import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.drugvilla.in.R;

import com.drugvilla.in.databinding.RowFilterListBinding;
import com.drugvilla.in.model.Document;
import com.drugvilla.in.model.product.filters.FilterData;

import java.util.List;

public class FilterListingAdapter extends RecyclerView.Adapter<FilterListingAdapter.MyViewHolder> {
    private Context context;
 //  private List<Document> dataList;
   private List<FilterData> dataList;

    public FilterListingAdapter(Context context, List<FilterData> list) {
        this.context = context;
        this.dataList = list;
    }


    @NonNull
    @Override
    public FilterListingAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_filter_list, parent, false);
        return new FilterListingAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FilterListingAdapter.MyViewHolder holder, final int position) {
        holder.binding.check.setOnCheckedChangeListener(null);
        final FilterData model = dataList.get(position);
        holder.binding.tvItemsCount.setText(model.getFilterCount());
        holder.binding.check.setText(model.getFilterName());
        holder.binding.check.setChecked(dataList.get(position).isSelected());
        holder.binding.check.setTag(dataList.get(position));
        if (model.isSelected()) {
            holder.binding.check.setChecked(true);
        } else {
            holder.binding.check.setChecked(false);
        }
        if (holder.binding.check.isChecked()) {
            holder.binding.check.setTextColor(context.getResources().getColor(R.color.colorAccent));
        }
        holder.binding.check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                dataList.get(holder.getAdapterPosition()).setSelected(isChecked);

            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private RowFilterListBinding binding;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }

    }

}
