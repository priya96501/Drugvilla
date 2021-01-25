package com.drugvilla.in.adapter.prescription;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.drugvilla.in.R;
import com.drugvilla.in.databinding.RowMyPrescriptionBinding;
import com.drugvilla.in.listener.OnCheckSelectedListener;
import com.drugvilla.in.model.Document;
import com.drugvilla.in.model.PrescriptionData;
import com.drugvilla.in.utils.AppConstant;
import com.drugvilla.in.utils.CommonUtils;
import com.squareup.picasso.Picasso;

import java.util.List;


public class PrescriptionAdapter extends RecyclerView.Adapter<PrescriptionAdapter.MyViewHolder> {
    private Context context;
    private boolean value = false;
    private String type;
    public static List<PrescriptionData> dataList;
    private OnCheckSelectedListener listener;

    public PrescriptionAdapter(Context context, List<PrescriptionData> list, OnCheckSelectedListener listener, String type) {
        this.context = context;
        this.dataList = list;
        this.listener = listener;
        this.type = type;
    }

    public void show(boolean value) {
        this.value = value;
    }

    @NonNull
    @Override
    public PrescriptionAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.row_my_prescription, parent, false);
        return new PrescriptionAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final PrescriptionAdapter.MyViewHolder holder, final int position) {
        final PrescriptionData model = dataList.get(position);
        if (model.isSelected()) {
            holder.binding.ivCheck.setChecked(true);
        } else {
            holder.binding.ivCheck.setChecked(false);
        }
        if (type.equalsIgnoreCase(AppConstant.TYPE_SELECTED_PRESCRIPTION)) {
            holder.binding.llRoot.setBackground(null);
            holder.binding.ivCross.setVisibility(View.VISIBLE);
            holder.binding.ivSelected.setVisibility(View.GONE);
            if (model.getImage() != null && !model.getImage().isEmpty()) {
                Picasso.with(context).load(AppConstant.IMAGE_URL + model.getImage())
                        .placeholder(R.drawable.gallery)
                        .error(R.drawable.gallery)
                        .into(holder.binding.ivPrescription);
            }
            CommonUtils.setRoundImage(context, holder.binding.ivPrescription, model.getImage());
        } else if (type.equalsIgnoreCase(AppConstant.TYPE_CAPTURED_IMAGE)) {
            holder.binding.llRoot.setBackground(null);
            holder.binding.ivCross.setVisibility(View.VISIBLE);
            holder.binding.ivSelected.setVisibility(View.GONE);
            if (model.getImage() != null && !model.getImage().isEmpty()) {
                Picasso.with(context).load(AppConstant.IMAGE_URL + model.getImage())
                        .placeholder(R.drawable.gallery)
                        .error(R.drawable.gallery)
                        .into(holder.binding.ivPrescription);
            }
            CommonUtils.setRoundImage(context, holder.binding.ivPrescription, model.getImage());
          //  holder.binding.ivPrescription.setImageBitmap(model.getBmp());
        } else {
            if (model.getImage() != null && !model.getImage().isEmpty()) {
                Picasso.with(context).load(AppConstant.IMAGE_URL + model.getImage())
                        .placeholder(R.drawable.gallery)
                        .error(R.drawable.gallery)
                        .into(holder.binding.ivPrescription);
            }
            CommonUtils.setRoundImage(context, holder.binding.ivPrescription, model.getImage());
        }
        if (value) {
            holder.binding.ivSelected.setVisibility(View.GONE);
        } else {
            holder.binding.ivSelected.setVisibility(View.VISIBLE);
            if (holder.binding.ivCheck.isChecked()) {
                holder.binding.ivSelected.setImageResource(R.drawable.ic_checked);
                holder.binding.llRoot.setBackground(context.getResources().getDrawable(R.drawable.border_red));
            } else {
                holder.binding.ivSelected.setImageResource(R.drawable.bullet_point_red);
                holder.binding.llRoot.setBackground(context.getResources().getDrawable(R.drawable.back_stroke));
            }
        }
        holder.binding.ivPrescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type.equalsIgnoreCase(AppConstant.TYPE_CAPTURED_IMAGE)) {
                    //  CommonUtils.setZoomView(context, model.getBmp());
                } else {
                    CommonUtils.setZoomView(context, model.getImage());
                }
            }
        });
        holder.binding.ivSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(view, position, holder.binding.ivCheck.isChecked());
            }
        });
        holder.binding.ivCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(view, position, holder.binding.ivCheck.isChecked());
            }
        });
        holder.binding.ivCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataList.remove(position);
                notifyDataSetChanged();
                notifyItemRangeChanged(position, dataList.size());
            }
        });


    }

    public void updateList(List<PrescriptionData> picturesList) {
        this.dataList = picturesList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private RowMyPrescriptionBinding binding;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
