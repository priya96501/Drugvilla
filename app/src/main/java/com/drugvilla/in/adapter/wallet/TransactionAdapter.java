package com.drugvilla.in.adapter.wallet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.drugvilla.in.R;

import com.drugvilla.in.databinding.RowTransactionsBinding;
import com.drugvilla.in.model.Document;


import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.MyViewHolder> {
    private Context context;
    private List<Document> dataList;

    public TransactionAdapter(Context context, List<Document> list) {
        this.context = context;
        this.dataList = list;
    }

    @NonNull
    @Override
    public TransactionAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_transactions, parent, false);
        return new TransactionAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionAdapter.MyViewHolder holder, int position) {
        final Document model = dataList.get(position);
        holder.binding.tvCoins.setText(model.getText2());
        holder.binding.tvTitle.setText(model.getText());
        holder.binding.tvType.setText(model.getText4());
        holder.binding.tvDate.setText(model.getText3());
        if (model.getText().equalsIgnoreCase("Coins Received")) {
            holder.binding.ivCoin.setImageResource(R.drawable.ic_receive);
        } else {
            holder.binding.ivCoin.setImageResource(R.drawable.ic_paid);
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private RowTransactionsBinding binding;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
