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
import com.drugvilla.in.databinding.RowDoctorsBinding;
import com.drugvilla.in.model.doctor.DoctorData;
import com.drugvilla.in.ui.doctors.BookAppointment;
import com.drugvilla.in.ui.doctors.DoctorProfile;
import com.drugvilla.in.utils.ActivityController;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FindDoctorAdapter extends RecyclerView.Adapter<FindDoctorAdapter.MyViewHolder> {
    private Context context;
    private List<DoctorData> dataList;
    private boolean isLoadingAdded = false;

    public FindDoctorAdapter(Context context, List<DoctorData> list) {
        this.context = context;
        this.dataList = list;
    }

    @NonNull
    @Override
    public FindDoctorAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_doctors, parent, false);
        return new FindDoctorAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FindDoctorAdapter.MyViewHolder holder, final int position) {
        final DoctorData model = dataList.get(position);
        if (model.getDoctorProfile() != null && !model.getDoctorProfile().isEmpty()) {
            Picasso.with(context).load(model.getDoctorProfile())
                    .error(R.drawable.default_user)
                    .placeholder(R.drawable.default_user)
                    .into(holder.binding.ivProfile);
        }
        holder.binding.tvCategory.setText(model.getDoctorCategoryName());
        holder.binding.tvName.setText(model.getDoctorName());
        holder.binding.tvExperience.setText(model.getDoctorExperience());
        holder.binding.tvAddress.setText(model.getDoctorLocation());
        holder.binding.btnBookAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityController.startActivity(context, BookAppointment.class);
            }
        });
        holder.binding.btnViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DoctorProfile.class);
                intent.putExtra("DOCTOR_ID", dataList.get(position).getDoctorId());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private RowDoctorsBinding binding;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }

    public void add(DoctorData mc) {
        dataList.add(mc);
        notifyItemInserted(dataList.size() - 1);
    }
    public void remove(DoctorData membersData) {
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



    public void setList(List<DoctorData> list) {
        this.dataList = list;
        notifyDataSetChanged();
    }

    public void addAll(List<DoctorData> newList) {
        int lastIndex = dataList.size() - 1;
        dataList.addAll(newList);
        notifyItemRangeInserted(lastIndex, newList.size());
    }
    public DoctorData getItem(int position) {
        return dataList.get(position);
    }

}
