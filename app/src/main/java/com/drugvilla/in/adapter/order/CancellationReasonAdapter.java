package com.drugvilla.in.adapter.order;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.drugvilla.in.R;
import com.drugvilla.in.databinding.RowCancellationReasonBinding;
import com.drugvilla.in.listener.OnCheckSelectedListener;
import com.drugvilla.in.model.Document;
import com.drugvilla.in.model.orderCancellation.CancellationData;

import java.util.List;

public class  CancellationReasonAdapter extends RecyclerView.Adapter<CancellationReasonAdapter.MyViewHolder> {
    private Context context;
   private List<CancellationData> dataList ;
    private final OnCheckSelectedListener listener;

    public CancellationReasonAdapter(Context context, List<CancellationData> list, OnCheckSelectedListener listener) {
        this.context = context;
        this.dataList = list;
        this.listener = listener;
    }


    @NonNull
    @Override
    public CancellationReasonAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_cancellation_reason, parent, false);
        return new CancellationReasonAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CancellationReasonAdapter.MyViewHolder holder, final int position) {
        final CancellationData model = dataList.get(position);
        holder.binding.tvReason.setText(model.getReason());
        if (model.isSelected()) {
            holder.binding.check.setChecked(true);
        } else {
            holder.binding.check.setChecked(false);
        }
        if (holder.binding.check.isChecked()) {
            holder.binding.ivSelected.setImageResource(R.drawable.ic_checked);
        } else {
            holder.binding.ivSelected.setImageResource(R.drawable.ic_bullet_point);
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

        private RowCancellationReasonBinding binding;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }


}
