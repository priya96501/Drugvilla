package com.drugvilla.in.adapter.labs;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.drugvilla.in.R;
import com.drugvilla.in.databinding.RowFindLabsBinding;
import com.drugvilla.in.model.Document;
import com.drugvilla.in.model.doctor.DoctorData;
import com.drugvilla.in.model.lab.labs.LabData;
import com.drugvilla.in.ui.doctors.DoctorListing;
import com.drugvilla.in.ui.labs.LabDetail;
import com.drugvilla.in.utils.ActivityController;
import com.squareup.picasso.Picasso;

import java.util.List;

public class LabsAdapter extends RecyclerView.Adapter<LabsAdapter.MyViewHolder> {
    private Context context;
    private List<LabData> dataList;
    private boolean isLoadingAdded = false;

    public LabsAdapter(Context context, List<LabData> list) {
        this.context = context;
        this.dataList = list;
    }

    @NonNull
    @Override
    public LabsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_find_labs, parent, false);
        return new LabsAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LabsAdapter.MyViewHolder holder, final int position) {
        final LabData model = dataList.get(position);
        holder.binding.tvTitle.setText(model.getLab_name());
        holder.binding.tvAddress.setText(model.getAddress());
        holder.binding.tvLabRating.setText(model.getLab_rating());
        holder.binding.tvCertifiedBy.setText(model.getCertified_by());
        holder.binding.tvLabUsers.setText(model.getTest_done()+"  lab tests booked this week");
        if (model.getLab_image() != null && !model.getLab_image().isEmpty()) {
            Picasso.with(context)
                    .load(model.getLab_image())
                    .error(R.color.transparent)
                    .placeholder(R.color.transparent)
                    .into(holder.binding.ivLab);
        }

        holder.binding.llLabs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, LabDetail.class);
                intent.putExtra("LAB_ID", dataList.get(position).getLab_id());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private RowFindLabsBinding binding;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
    public void add(LabData mc) {
        dataList.add(mc);
        notifyItemInserted(dataList.size() - 1);
    }
    public void remove(LabData membersData) {
        int position = dataList.indexOf(membersData);
        if (position > -1) {
            dataList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }



    public void setList(List<LabData> list) {
        this.dataList = list;
        notifyDataSetChanged();
    }

    public void addAll(List<LabData> newList) {
        int lastIndex = dataList.size() - 1;
        dataList.addAll(newList);
        notifyItemRangeInserted(lastIndex, newList.size());
    }
    public LabData getItem(int position) {
        return dataList.get(position);
    }

}
