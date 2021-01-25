package com.drugvilla.in.adapter.reminder;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.drugvilla.in.R;
import com.drugvilla.in.databinding.DialogBinding;
import com.drugvilla.in.databinding.RowRemindersBinding;
import com.drugvilla.in.listener.CommentListener;
import com.drugvilla.in.listener.OnSelectedListener;
import com.drugvilla.in.model.reminder.ReminderData;
import com.drugvilla.in.utils.DialogUtils;

import java.util.List;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.MyViewHolder> {
    private Context context;
    private List<ReminderData> dataList;
    boolean value;
    private Dialog dialog;
    private CommentListener listener;
    private OnSelectedListener onSelectedListener;


    public ReminderAdapter(Context context, List<ReminderData> list, CommentListener listener, OnSelectedListener onSelectedListener) {
        this.context = context;
        this.dataList = list;
        this.listener = listener;
        this.onSelectedListener = onSelectedListener;
    }


    @NonNull
    @Override
    public ReminderAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_reminders, parent, false);
        return new ReminderAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ReminderAdapter.MyViewHolder holder, final int position) {
        final ReminderData model = dataList.get(position);
        holder.binding.tvTitle.setText(model.getMedicineName());
        String morning = model.getMorning();
        String day = model.getAfternoon();
        String night = model.getEvening();

        if (morning != null) {
            holder.binding.tvMorningTime.setText(morning);
        }
        if (day != null) {
            holder.binding.tvDayTime.setText(day);
        }
        if (night != null) {
            holder.binding.tvNightTime.setText(night);
        }
        holder.binding.ivOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final PopupMenu popup = new PopupMenu(context, holder.binding.ivOptions);
                popup.getMenuInflater().inflate(R.menu.poupup_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.delete:
                                openDialog("Delete Reminder", "Are you sure you want to delete the reminder?", position);
                                break;
                            case R.id.edit:
                                listener.onClick(item, position);
                                break;
                            default:
                                break;
                        }
                        return true;
                    }
                });
                popup.show();
            }
        });
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
                onSelectedListener.onClick(v, position);
                dialog.dismiss();
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private RowRemindersBinding binding;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
