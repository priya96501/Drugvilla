package com.drugvilla.in.adapter.product.sortFilter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.drugvilla.in.R;
import com.drugvilla.in.databinding.RowSortingBinding;
import com.drugvilla.in.listener.OnSelectedListener;
import com.drugvilla.in.model.Document;
import com.drugvilla.in.model.product.sort.SortingData;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SortingListAdapter extends RecyclerView.Adapter<SortingListAdapter.MyViewHolder> {
    private Context context;
    private List<SortingData> dataList;
    private final OnSelectedListener listener;

    public SortingListAdapter(Context context, List<SortingData> list, OnSelectedListener listener) {
        this.context = context;
        this.dataList = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SortingListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_sorting, parent, false);
        return new SortingListAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SortingListAdapter.MyViewHolder holder, final int position) {
        final SortingData model = dataList.get(position);
        if (model.getSortingImage() != null && !model.getSortingImage().isEmpty()) {
            Picasso.with(context).load(/*AppConstant.IMAGE_URL + */model.getSortingImage())
                    .placeholder(R.drawable.gallery)
                    .error(R.drawable.gallery)
                    .into(holder.binding.ivSortingType);
        }
        holder.binding.tvSortingType.setText(model.getSortingType());
        holder.binding.llRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(view, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private RowSortingBinding binding;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
