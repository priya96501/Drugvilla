package com.drugvilla.in.adapter.address;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.drugvilla.in.R;
import com.drugvilla.in.databinding.DialogBinding;
import com.drugvilla.in.databinding.RowAddressBinding;
import com.drugvilla.in.listener.OnCheckSelectedListener;
import com.drugvilla.in.listener.OnSelectedListener;
import com.drugvilla.in.model.Document;
import com.drugvilla.in.model.address.AddressData;
import com.drugvilla.in.ui.address.AddAddress;
import com.drugvilla.in.utils.ActivityController;
import com.drugvilla.in.utils.AppConstant;
import com.drugvilla.in.utils.DialogUtils;

import java.util.List;
import java.util.Objects;

public class SelectAddressAdapter extends RecyclerView.Adapter<SelectAddressAdapter.MyViewHolder> {
    private Context context;
    private List<AddressData> dataList;
    private boolean hide_value = false;
    private final OnCheckSelectedListener listener;
    private final OnSelectedListener selectedListener;
    private Dialog dialog;

    public SelectAddressAdapter(Context context, List<AddressData> list, OnCheckSelectedListener listener, OnSelectedListener selectedListener) {
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
    public SelectAddressAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.row_address, parent, false);
        return new SelectAddressAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SelectAddressAdapter.MyViewHolder holder, final int position) {

        final AddressData model = dataList.get(position);
        String Type = model.getAddressType();
        holder.binding.tvType.setText(model.getAddressType());
        holder.binding.tvName.setText(model.getName());
        holder.binding.tvNumber.setText(model.getMobile());
        String address = new StringBuilder().append(model.getAddressLine1()).append(" ").append(model.getAddressLine2()).toString();
        holder.binding.tvAddress.setText(address);
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

        if (Type.equalsIgnoreCase("Home")) {
            holder.binding.ivType.setImageResource(R.drawable.house);
        } else if (Type.equalsIgnoreCase("Work")) {
            holder.binding.ivType.setImageResource(R.drawable.office);
        } else {
            holder.binding.ivType.setImageResource(R.drawable.other);
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
                openDialog("Delete Address", "Are you sure you want to delete the address permanentaly.", position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private RowAddressBinding binding;

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
