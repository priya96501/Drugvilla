package com.drugvilla.in.adapter.product.sortFilter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.drugvilla.in.R;
import com.drugvilla.in.databinding.RowTitleBinding;
import com.drugvilla.in.listener.OnCheckSelectedListener;
import com.drugvilla.in.model.Document;
import com.drugvilla.in.model.product.filters.FilterCategoryData;

import java.util.List;

public class FilterTypeAdapter extends RecyclerView.Adapter<FilterTypeAdapter.MyViewHolder> {
    private Context context;
    private List<FilterCategoryData> dataList;
    private OnCheckSelectedListener listener;

    public FilterTypeAdapter(Context context, List<FilterCategoryData> list, OnCheckSelectedListener listener) {
        this.context = context;
        this.dataList = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FilterTypeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(context).inflate(R.layout.row_title, parent, false);
        return new FilterTypeAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FilterTypeAdapter.MyViewHolder holder, final int position) {

        final FilterCategoryData model = dataList.get(position);
        holder.binding.tvFilterType.setText(model.getCategoryName());
        holder.binding.tvFilterTypeCount.setText(model.getCategoryCount());
        if (model.isSelected()) {
            holder.binding.check.setChecked(true);
        } else {
            holder.binding.check.setChecked(false);
        }
        if (holder.binding.check.isChecked()) {
            holder.binding.llRoot.setBackgroundColor(context.getResources().getColor(R.color.colorWhite));
            holder.binding.tvFilterType.setTypeface(Typeface.create("assistant_regular", Typeface.BOLD));
            holder.binding.tvFilterType.setTextColor(context.getResources().getColor(R.color.colorBlack));
        } else {
            holder.binding.llRoot.setBackgroundColor(context.getResources().getColor(R.color.transparent));
            holder.binding.tvFilterType.setTypeface(Typeface.create("assistant_regular", Typeface.NORMAL));
            holder.binding.tvFilterType.setTextColor(context.getResources().getColor(R.color.lightBlack));
        }
        holder.binding.llRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(view, position, holder.binding.check.isChecked());
            }
        });
        holder.binding.check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(view, position, holder.binding.check.isChecked());
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private RowTitleBinding binding;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
