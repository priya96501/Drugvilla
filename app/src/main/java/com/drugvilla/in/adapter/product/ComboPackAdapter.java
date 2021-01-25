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
import com.drugvilla.in.databinding.RowComboPackBinding;
import com.drugvilla.in.model.Document;
import java.util.List;

public class ComboPackAdapter extends RecyclerView.Adapter<ComboPackAdapter.MyViewHolder> {
    private Context context;
    private List<Document> dataList;

    public ComboPackAdapter(Context context, List<Document> list) {
        this.context = context;
        this.dataList = list;
    }


    @NonNull
    @Override
    public ComboPackAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_combo_pack, parent, false);
        return new ComboPackAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ComboPackAdapter.MyViewHolder holder, int position) {
        final Document model = dataList.get(position);
        holder.binding.ivProduct.setImageResource(model.getImage());
        holder.binding.tvPack.setText(model.getText());
        holder.binding.tvAmount.setText(model.getText2());
        holder.binding.tvOff.setText(model.getText3());
        holder.binding.tvMRP.setText(model.getText4());
        holder.binding.tvMRP.setPaintFlags(holder.binding.tvMRP.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private RowComboPackBinding binding;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
