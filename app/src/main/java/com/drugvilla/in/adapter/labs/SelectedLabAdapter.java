package com.drugvilla.in.adapter.labs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.drugvilla.in.R;
import com.drugvilla.in.databinding.DialogPriceBreakupBinding;
import com.drugvilla.in.databinding.RowSelectLabBinding;
import com.drugvilla.in.listener.OnCheckSelectedListener;
import com.drugvilla.in.model.Document;
import com.drugvilla.in.model.lab.labs.LabData;
import com.drugvilla.in.utils.DialogUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SelectedLabAdapter extends RecyclerView.Adapter<SelectedLabAdapter.MyViewHolder> {
    private Context context;
    private List<LabData> dataList;
    private final OnCheckSelectedListener listener;

    public SelectedLabAdapter(Context context, List<LabData> list, OnCheckSelectedListener listener) {
        this.context = context;
        this.dataList = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SelectedLabAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_select_lab, parent, false);
        return new SelectedLabAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SelectedLabAdapter.MyViewHolder holder, final int position) {

        final LabData model = dataList.get(position);
        if (model.isSelected()) {
            holder.binding.check.setChecked(true);
        } else {
            holder.binding.check.setChecked(false);
        }
        if (holder.binding.check.isChecked()) {
            holder.binding.llSelectLab.setBackground(context.getResources().getDrawable(R.drawable.round_green));
            holder.binding.btnSelectLab.setText("Selected");
            holder.binding.ivTick.setVisibility(View.VISIBLE);
        } else {
            holder.binding.llSelectLab.setBackground(context.getResources().getDrawable(R.drawable.button_red));
            holder.binding.btnSelectLab.setText("Select Lab");
            holder.binding.ivTick.setVisibility(View.GONE);
        }
        holder.binding.tvMRP.setPaintFlags(holder.binding.tvMRP.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.binding.tvTitle.setText(model.getLab_name());
        holder.binding.tvAddress.setText(model.getAddress());
        holder.binding.tvAmount.setText(model.getTest_amount());
        holder.binding.tvMRP.setText(model.getTest_mrp());
        holder.binding.tvOfferCoupon.setText(model.getTest_discount());
        holder.binding.tvRating.setText(model.getLab_rating());
        holder.binding.tvCertifiedBy.setText(model.getCertified_by());
        if (model.getLab_image() != null && !model.getLab_image().isEmpty()) {
            Picasso.with(context).load(/*AppConstant.IMAGE_URL + */model.getLab_image())
                    .placeholder(R.color.transparent)
                    .error(R.color.transparent)
                    .into(holder.binding.ivLab);
        }
        holder.binding.llLabs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // context.startActivity(new Intent(context, LabDetail.class));
            }
        });
        holder.binding.tvPriceBreakup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog(position);
            }
        });
        holder.binding.btnSelectLab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v, position, holder.binding.check.isChecked());
            }
        });
    }

    private void openDialog(int position) {
        final DialogPriceBreakupBinding priceDialog = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.dialog_price_breakup, null, false);
        final Dialog dialog = DialogUtils.createDialog(context, priceDialog.getRoot(), 0);
        dialog.setCancelable(false);

        priceDialog.tvTestName.setText(dataList.get(position).getLab_name());
        priceDialog.tvOff.setText(dataList.get(position).getTest_discount());
        priceDialog.tvOffPrice.setText("- " + context.getResources().getString(R.string.Rs) + dataList.get(position).getTest_amount());
        priceDialog.tvBestPrice.setText(context.getResources().getString(R.string.Rs) + dataList.get(position).getTest_amount());
        priceDialog.btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }


    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private RowSelectLabBinding binding;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
