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
import com.drugvilla.in.databinding.RowCategoriesBinding;
import com.drugvilla.in.databinding.RowCertificationBinding;
import com.drugvilla.in.model.Document;
import com.drugvilla.in.model.lab.LabCertification;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CertificationAdapter extends RecyclerView.Adapter<CertificationAdapter.MyViewHolder> {
    private Context context;
    private List<LabCertification> dataList;

    public CertificationAdapter(Context context, List<LabCertification> list) {
        this.context = context;
        this.dataList = list;
    }

    @NonNull
    @Override
    public CertificationAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_certification, parent, false);
        return new CertificationAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CertificationAdapter.MyViewHolder holder, int position) {
        final LabCertification model = dataList.get(position);
        holder.binding.tvCertificationName.setText(model.getCertification_name());
        if (model.getCertification_image() != null && !model.getCertification_image().isEmpty()) {
            Picasso.with(context).load(/*AppConstant.IMAGE_URL + */model.getCertification_image())
                    .placeholder(R.drawable.gallery)
                    .error(R.drawable.gallery)
                    .into(holder.binding.ivCertification);
        }

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private RowCertificationBinding binding;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
