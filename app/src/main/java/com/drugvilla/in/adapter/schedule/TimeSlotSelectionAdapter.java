package com.drugvilla.in.adapter.schedule;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.drugvilla.in.R;
import com.drugvilla.in.databinding.RowTimeSlotBinding;
import com.drugvilla.in.listener.OnCheckSelectedListener;
import com.drugvilla.in.model.Document;
import java.util.List;

public class TimeSlotSelectionAdapter extends RecyclerView.Adapter<TimeSlotSelectionAdapter.MyViewHolder> {
    private Context context;
    private List<Document> dataList;
    private final OnCheckSelectedListener listener;

    public TimeSlotSelectionAdapter(Context context, List<Document> list, OnCheckSelectedListener listener) {
        this.context = context;
        this.dataList = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TimeSlotSelectionAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_time_slot, parent, false);
        return new TimeSlotSelectionAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final TimeSlotSelectionAdapter.MyViewHolder holder, final int position) {
        final Document list = dataList.get(position);
        if (list.isSelected()) {
            holder.binding.check.setChecked(true);
        } else {
            holder.binding.check.setChecked(false);
        }
        if (holder.binding.check.isChecked()) {
            holder.binding.tvTime.setTextColor(context.getResources().getColor(R.color.colorAccent));
        } else {
            holder.binding.tvTime.setTextColor(context.getResources().getColor(R.color.lightGray));
        }
        holder.binding.tvTime.setText(list.getText());
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

        private RowTimeSlotBinding binding;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
