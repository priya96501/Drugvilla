package com.drugvilla.in.adapter.doctors;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.drugvilla.in.R;
import com.drugvilla.in.databinding.RowCategoriesBinding;
import com.drugvilla.in.model.doctor.DoctorCategoryData;
import com.drugvilla.in.ui.doctors.DoctorListing;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DoctorCategoryAdapter extends RecyclerView.Adapter<DoctorCategoryAdapter.MyViewHolder> {
    private Context context;
    private List<DoctorCategoryData> dataList;
    private boolean isLoadingAdded = false;

    public DoctorCategoryAdapter(Context context, List<DoctorCategoryData> list) {
        this.context = context;
        this.dataList = list;
    }

    @NonNull
    @Override
    public DoctorCategoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_categories, parent, false);
        return new DoctorCategoryAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorCategoryAdapter.MyViewHolder holder, final int position) {
        final DoctorCategoryData model = dataList.get(position);
        holder.binding.ivSmall.setVisibility(View.GONE);
        holder.binding.ivBig.setVisibility(View.VISIBLE);
        if (model.getCategoryImage() != null && !model.getCategoryImage().isEmpty()) {
            Picasso.with(context).load(/*AppConstant.IMAGE_URL + */model.getCategoryImage())
                    .placeholder(R.drawable.gallery)
                    .error(R.drawable.gallery)
                    .into(holder.binding.ivBig);
        }
        holder.binding.tvCategory.setText(model.getCategoryName());
        holder.binding.llCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DoctorListing.class);
                intent.putExtra("Doctor_Category_ID", dataList.get(position).getCategoryId());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private RowCategoriesBinding binding;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }

    public void add(DoctorCategoryData mc) {
        dataList.add(mc);
        notifyItemInserted(dataList.size() - 1);
    }
    public void remove(DoctorCategoryData membersData) {
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



    public void setList(List<DoctorCategoryData> list) {
        this.dataList = list;
        notifyDataSetChanged();
    }

    public void addAll(List<DoctorCategoryData> newList) {
        int lastIndex = dataList.size() - 1;
        dataList.addAll(newList);
        notifyItemRangeInserted(lastIndex, newList.size());
    }
    public DoctorCategoryData getItem(int position) {
        return dataList.get(position);
    }

}
