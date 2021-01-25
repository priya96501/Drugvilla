package com.drugvilla.in.adapter.labs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.drugvilla.in.R;
import com.drugvilla.in.databinding.RowSelectedTestsBinding;
import com.drugvilla.in.listener.OnSelectedListener;
import com.drugvilla.in.model.Document;
import com.drugvilla.in.model.cart.SelectedItem;
import com.drugvilla.in.utils.AppConstant;

import java.util.List;


public class SelectedLabTestAdapter extends RecyclerView.Adapter<SelectedLabTestAdapter.MyViewHolder> {
    private Context context;
    private final OnSelectedListener selectedListener;
    private List<SelectedItem> dataList;
    private String type;


    public SelectedLabTestAdapter(Context context, List<SelectedItem> list, String type, OnSelectedListener selectedListener) {
        this.context = context;
        this.dataList = list;
        this.type = type;
        this.selectedListener = selectedListener;
    }

    @NonNull
    @Override
    public SelectedLabTestAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_selected_tests, parent, false);
        return new SelectedLabTestAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectedLabTestAdapter.MyViewHolder holder, final int position) {
        final SelectedItem model = dataList.get(position);
        holder.binding.tvTitle.setText(model.getItemName());
        holder.binding.tvPrice.setText(model.getAmount());
        holder.binding.tvBy.setText(model.getProvided_by());
        if (model.getItemType().equalsIgnoreCase("package")) {
            holder.binding.viewIncludedTest.setVisibility(View.VISIBLE);
        } else {
            holder.binding.viewIncludedTest.setVisibility(View.GONE);
        }
        if (type.equalsIgnoreCase(AppConstant.TYPE_ORDER_REVIEW)) {
            holder.binding.ivDelete.setVisibility(View.INVISIBLE);
            holder.binding.ivPoint.setVisibility(View.VISIBLE);
        } else if (type.equalsIgnoreCase(AppConstant.TYPE_CART)) {
            holder.binding.ivPoint.setVisibility(View.INVISIBLE);
            holder.binding.ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectedListener.onClick(view, position);
                    notifyDataSetChanged();
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private RowSelectedTestsBinding binding;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }

}
