package com.drugvilla.in.adapter.patient;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.drugvilla.in.R;
import com.drugvilla.in.databinding.DialogBinding;
import com.drugvilla.in.databinding.RowPatientBinding;
import com.drugvilla.in.listener.OnCheckSelectedListener;
import com.drugvilla.in.listener.OnSelectedListener;
import com.drugvilla.in.model.Document;
import com.drugvilla.in.model.patient.PatientData;
import com.drugvilla.in.ui.patient.AddPatient;
import com.drugvilla.in.utils.ActivityController;
import com.drugvilla.in.utils.DialogUtils;
import java.util.List;

public class SelectPatientAdapter extends RecyclerView.Adapter<SelectPatientAdapter.MyViewHolder> {
    private Context context;
    private boolean hide_value = false;
    private List<PatientData> dataList;
    private final OnCheckSelectedListener listener;
    private final OnSelectedListener selectedListener;
    private Dialog dialog;


    public SelectPatientAdapter(Context context, List<PatientData> list, OnCheckSelectedListener listener, OnSelectedListener selectedListener) {
        this.context = context;
        this.dataList = list;
        this.listener = listener;
        this.selectedListener = selectedListener;
    }

    public void hide(boolean hide_value) {
        this.hide_value = hide_value;
    }


    @NonNull
    @Override
    public SelectPatientAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_patient, parent, false);
        return new SelectPatientAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SelectPatientAdapter.MyViewHolder holder, final int position) {
        final PatientData model = dataList.get(position);
        holder.binding.tvPatientName.setText(model.getName());
        holder.binding.tvAge.setText(model.getAge());
        holder.binding.tvEmail.setText(model.getEmail());
        holder.binding.tvNumber.setText(model.getMobile());

        if (model.isSelected()) {
            holder.binding.check.setChecked(true);
        } else {
            holder.binding.check.setChecked(false);
        }
        if (hide_value) {
            holder.binding.check.setVisibility(View.GONE);
            holder.binding.ivSelected.setVisibility(View.GONE);
        } else {
            if (holder.binding.check.isChecked()) {
                holder.binding.ivSelected.setImageResource(R.drawable.ic_checked);
                holder.binding.llRoot.setBackground(context.getResources().getDrawable(R.drawable.border_red));
            } else {
                holder.binding.ivSelected.setImageResource(R.drawable.ic_bullet_point);
                holder.binding.llRoot.setBackground(context.getResources().getDrawable(R.drawable.back_stroke));
            }
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
        holder.binding.tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedListener.onClick(view, position);
            }
        });
        holder.binding.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog("Delete Patient Details", "Are you sure you want to delete the patient's detail.", position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private RowPatientBinding binding;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }


    private void openDialog(String heading, String desc, final int position) {
        final DialogBinding dialogBinding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.dialog, null, false);
        dialog = DialogUtils.createDialog(context, dialogBinding.getRoot(), 0);
        dialog.setCancelable(false);
        dialogBinding.tvHeading.setText(heading);
        dialogBinding.tvDescription.setText(desc);
        dialogBinding.tvNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialogBinding.tvYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedListener.onClick(v, position);
                notifyDataSetChanged();
                dialog.dismiss();
            }
        });
    }
}
