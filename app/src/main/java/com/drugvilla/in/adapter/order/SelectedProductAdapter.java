package com.drugvilla.in.adapter.order;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.drugvilla.in.R;
import com.drugvilla.in.databinding.RowSelectedProductBinding;
import com.drugvilla.in.listener.OnSelectedListener;
import com.drugvilla.in.listener.OnSelectedTypeListener;
import com.drugvilla.in.model.Document;
import com.drugvilla.in.model.cart.SelectedItem;
import com.drugvilla.in.utils.AppConstant;
import com.squareup.picasso.Picasso;

import java.util.List;

import static android.os.Build.VERSION_CODES.O;

public class SelectedProductAdapter extends RecyclerView.Adapter<SelectedProductAdapter.MyViewHolder> {
    private Context context;
    private List<SelectedItem> dataList;
    private final OnSelectedListener selectedListener;
    private final OnSelectedTypeListener selectedTypeListener;
    private String type;
    int minteger = 1;

    public SelectedProductAdapter(Context context, List<SelectedItem> list, String type, OnSelectedListener selectedListener,OnSelectedTypeListener selectedTypeListener) {
        this.context = context;
        this.dataList = list;
        this.type = type;
        this.selectedListener = selectedListener;
        this.selectedTypeListener = selectedTypeListener;
    }


    @NonNull
    @Override
    public SelectedProductAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_selected_product, parent, false);
        return new SelectedProductAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SelectedProductAdapter.MyViewHolder holder, final int position) {
        final SelectedItem model = dataList.get(position);
        if (type.equalsIgnoreCase(AppConstant.TYPE_ORDER_REVIEW)) {
            holder.binding.llQuantityRemove.setVisibility(View.GONE);
            holder.binding.llQuantityDelivery.setVisibility(View.VISIBLE);

        } else if (type.equalsIgnoreCase(AppConstant.TYPE_CART)) {
            holder.binding.llQuantityDelivery.setVisibility(View.GONE);
            holder.binding.llQuantityRemove.setVisibility(View.VISIBLE);
            if (model.getItemType().equalsIgnoreCase("drug")) {
                holder.binding.ivPrescription.setVisibility(View.VISIBLE);
            }
        }
        if (model.getItemImage() != null && !model.getItemImage().isEmpty()) {
            Picasso.with(context).load(/*AppConstant.IMAGE_URL + */model.getItemImage())
                    .placeholder(R.color.transparent)
                    .error(R.color.transparent)
                    .into(holder.binding.ivProduct);
        }
        holder.binding.tvTitle.setText(model.getItemName());
        holder.binding.tvQuantity.setText(model.getItem_quantity());
        holder.binding.tvBy.setText(model.getProvided_by());
        holder.binding.tvMRP.setText(model.getMrp());
        holder.binding.tvPrice.setText(model.getAmount());
        holder.binding.tvMRP.setPaintFlags(holder.binding.tvMRP.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.binding.tvRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedListener.onClick(view, position);
                notifyDataSetChanged();
            }
        });
        holder.binding.btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(minteger <= 1)) {
                    minteger = minteger - 1;
                }
                display(minteger, holder.binding.tvCount);
                selectedTypeListener.onClick(view, position,holder.binding.tvCount.getText().toString());
                notifyDataSetChanged();
            }
        });
        holder.binding.btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notifyDataSetChanged();
                if (minteger <= 29) {
                    minteger = minteger + 1;
                }
                display(minteger, holder.binding.tvCount);
                selectedTypeListener.onClick(view, position,holder.binding.tvCount.getText().toString());
                notifyDataSetChanged();
            }


        });

    }

    private void display(int number, TextView tvCount) {
        tvCount.setText("" + number);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private RowSelectedProductBinding binding;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
