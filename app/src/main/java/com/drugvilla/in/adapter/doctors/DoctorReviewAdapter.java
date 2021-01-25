package com.drugvilla.in.adapter.doctors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.drugvilla.in.R;
import com.drugvilla.in.databinding.RowDoctorReviewBinding;
import com.drugvilla.in.model.Document;
import com.drugvilla.in.model.ReviewData;
import com.drugvilla.in.utils.AppConstant;
import com.drugvilla.in.utils.CommonUtils;


import java.util.List;

public class DoctorReviewAdapter extends RecyclerView.Adapter<DoctorReviewAdapter.MyViewHolder> {
    private Context context;
    private List<ReviewData> dataList;
    private String type;

    public DoctorReviewAdapter(Context context, List<ReviewData> list, String type) {
        this.context = context;
        this.dataList = list;
        this.type = type;
    }

    @NonNull
    @Override
    public DoctorReviewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_doctor_review, parent, false);
        return new DoctorReviewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorReviewAdapter.MyViewHolder holder, int position) {
        final ReviewData model = dataList.get(position);
        if (model != null) {
            /*if (type.equalsIgnoreCase(AppConstant.TYPE_DOCTOR_CATEGORY)) {

                    holder.binding.llrating.setVisibility(View.VISIBLE);
                    holder.binding.tvRating.setText(model.getUserRating());

            } else {
                holder.binding.llrating.setVisibility(View.VISIBLE);
                holder.binding.tvRating.setText(model.getUserRating());
            }*/

            if (!model.getUserRating().equals("0") && model.getUserRating() != null && !model.getUserRating().isEmpty()) {
                holder.binding.llrating.setVisibility(View.VISIBLE);
                holder.binding.tvRating.setText(model.getUserRating());
            }

            if (model.getUserName() != null && !model.getUserName().isEmpty()) {
                holder.binding.tvUser.setText(model.getUserName().substring(0, 1));
            }

            holder.binding.tvTitle.setText(model.getUserName());
            holder.binding.tvComment.setText(model.getUserReview());


            CommonUtils.getRandomColors(holder.binding.tvUser);
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private RowDoctorReviewBinding binding;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
