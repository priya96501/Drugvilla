package com.drugvilla.in.adapter.doctors;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.drugvilla.in.R;
import com.drugvilla.in.databinding.DialogBinding;
import com.drugvilla.in.databinding.RowMyAppointmentBinding;
import com.drugvilla.in.model.appointment.myAppointment.AppointmentData;
import com.drugvilla.in.service.Api;
import com.drugvilla.in.service.BaseCallback;
import com.drugvilla.in.service.BaseResponse;
import com.drugvilla.in.service.RequestController;
import com.drugvilla.in.ui.doctors.AppointmentDetails;
import com.drugvilla.in.ui.doctors.BookAppointment;
import com.drugvilla.in.utils.ActivityController;
import com.drugvilla.in.utils.AppConstant;
import com.drugvilla.in.utils.CommonUtils;
import com.drugvilla.in.utils.DialogUtils;
import com.drugvilla.in.utils.ProgressDialogUtils;
import com.drugvilla.in.utils.SharedPref;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;

public class MyAppointmentAdapter extends RecyclerView.Adapter<MyAppointmentAdapter.MyViewHolder> {
    private Activity context;
    private List<AppointmentData> dataList;
    private Dialog dialog;
    private boolean isLoadingAdded = false;


    public MyAppointmentAdapter(Activity context, List<AppointmentData> list) {
        this.context = context;
        this.dataList = list;

    }

    @NonNull
    @Override
    public MyAppointmentAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_my_appointment, parent, false);
        return new MyAppointmentAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyAppointmentAdapter.MyViewHolder holder, final int position) {
        final AppointmentData model = dataList.get(position);
        if (model.getDoctor_profile() != null && !model.getDoctor_profile().isEmpty()) {
            Picasso.with(context).load(model.getDoctor_profile())
                    .error(R.drawable.default_user)
                    .placeholder(R.drawable.default_user)
                    .into(holder.binding.ivProfile);
        }
        holder.binding.tvName.setText(model.getDoctor_name());
        holder.binding.tvAppointmentId.setText("Appointment Id : " + model.getAppointment_id());
        holder.binding.tvDate.setText(model.getAppointment_date());
        holder.binding.tvTime.setText(model.getAppointment_time());
        holder.binding.tvPlace.setText(model.getDoctor_location());
        holder.binding.llRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AppointmentDetails.class);
                intent.putExtra("APPOINTMENT_ID", dataList.get(position).getAppointment_id());
                context.startActivity(intent);
            }
        });
        holder.binding.tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog(position, true, holder.binding.AppointmentCancelled, holder.binding.llAppointmentStatus);
            }
        });
        holder.binding.tvReschedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog(position, false, holder.binding.AppointmentCancelled, holder.binding.llAppointmentStatus);
            }
        });


    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private RowMyAppointmentBinding binding;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }


    @SuppressLint("SetTextI18n")
    private void openDialog(final int position, final boolean delete, final TextView appointmentCancelled, final LinearLayout llAppointmentStatus) {
        final DialogBinding dialogBinding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.dialog, null, false);
        dialog = DialogUtils.createDialog(context, dialogBinding.getRoot(), 0);
        dialog.setCancelable(false);
        if (delete) {
            dialogBinding.tvHeading.setText("Cancel appointment");
            dialogBinding.tvDescription.setText("Are you sure you want to cancel the appointment with " + dataList.get(position).getDoctor_name() + " ?");
        } else {
            dialogBinding.tvHeading.setText("Reschedule appointment");
            dialogBinding.tvDescription.setText("Are you sure you want to reschedule the appointment with " + dataList.get(position).getDoctor_name() + " ?");
        }
        dialogBinding.tvNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialogBinding.tvYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (delete) {
                    cancelAppointment(dataList.get(position).getAppointment_id(), appointmentCancelled, llAppointmentStatus);
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putString("Doctor_Profile", dataList.get(position).getDoctor_profile());
                    bundle.putString("Doctor_Name", dataList.get(position).getDoctor_name());
                    bundle.putString("Doctor_Fees", dataList.get(position).getDoctor_fess());
                    bundle.putString("Appointment_Date", dataList.get(position).getAppointment_date());
                    bundle.putString("Appointment_Time", dataList.get(position).getAppointment_time());
                    bundle.putString("Doctor_Location", dataList.get(position).getDoctor_location());
                    bundle.putString("Appointment_ID", dataList.get(position).getAppointment_id());
                    bundle.putString(AppConstant.FROM, AppConstant.TYPE_EDIT);
                    ActivityController.startActivity(context, BookAppointment.class, bundle, false, false);
                }
                dialog.dismiss();
            }
        });
    }

    private void cancelAppointment(String appointmentId, final TextView appointmentCancelled, final LinearLayout llAppointmentStatus) {
        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                Api api = RequestController.createService(context, Api.class);
                api.deleteAppointment(SharedPref.getStringPreferences(context, AppConstant.USER_ID), appointmentId)
                        .enqueue(new BaseCallback<BaseResponse>(context) {
                            @Override
                            public void onSuccess(BaseResponse response) {
                                ProgressDialogUtils.dismiss();
                                if (response != null) {
                                    if (response.getStatus() == 1) {
                                        CommonUtils.showToastShort(context, response.getMessage());
                                        notifyDataSetChanged();
                                        appointmentCancelled.setVisibility(View.VISIBLE);
                                        llAppointmentStatus.setVisibility(View.GONE);
                                    } else {

                                        CommonUtils.showToastShort(context, response.getMessage());
                                    }
                                } else {
                                    CommonUtils.showToastShort(context, context.getResources().getString(R.string.no_response));
                                }
                            }

                            @Override
                            public void onFail(Call<BaseResponse> call, BaseResponse baseResponse) {
                                ProgressDialogUtils.dismiss();
                                CommonUtils.showToastShort(context, context.getResources().getString(R.string.failure));
                            }
                        });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            CommonUtils.showToastShort(context, context.getResources().getString(R.string.no_internet));
        }

    }


    public void add(AppointmentData mc) {
        dataList.add(mc);
        notifyItemInserted(dataList.size() - 1);
    }
    public void remove(AppointmentData membersData) {
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



    public void setList(List<AppointmentData> list) {
        this.dataList = list;
        notifyDataSetChanged();
    }

    public void addAll(List<AppointmentData> newList) {
        int lastIndex = dataList.size() - 1;
        dataList.addAll(newList);
        notifyItemRangeInserted(lastIndex, newList.size());
    }
    public AppointmentData getItem(int position) {
        return dataList.get(position);
    }

}

