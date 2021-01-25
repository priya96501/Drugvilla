package com.drugvilla.in.ui.reminders;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.drugvilla.in.R;
import com.drugvilla.in.adapter.reminder.ReminderAdapter;
import com.drugvilla.in.databinding.ActivityRemindersBinding;
import com.drugvilla.in.listener.CommentListener;
import com.drugvilla.in.listener.OnSelectedListener;
import com.drugvilla.in.model.Document;
import com.drugvilla.in.model.patient.PatientBaseResponse;
import com.drugvilla.in.model.reminder.ReminderBaseResponse;
import com.drugvilla.in.model.reminder.ReminderData;
import com.drugvilla.in.service.Api;
import com.drugvilla.in.service.BaseCallback;
import com.drugvilla.in.service.BaseResponse;
import com.drugvilla.in.service.RequestController;
import com.drugvilla.in.ui.authentication.Signup;
import com.drugvilla.in.ui.patient.AddPatient;
import com.drugvilla.in.utils.ActivityController;
import com.drugvilla.in.utils.AppConstant;
import com.drugvilla.in.utils.CommonUtils;
import com.drugvilla.in.utils.ProgressDialogUtils;
import com.drugvilla.in.utils.SharedPref;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Reminders extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    private Activity activity;
    private ActivityRemindersBinding binding;
    private ReminderAdapter reminderAdapter;
    private List<ReminderData> listData = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_reminders);
        context = Reminders.this;
        activity = Reminders.this;
        setToolbar();
        binding.swipeRefreshingLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getRemindersList();
            }
        });
    }

    @Override
    protected void onResume() {
        getRemindersList();
        setAdapter();
        super.onResume();
    }

    private void setAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
        binding.rvReminders.setLayoutManager(layoutManager);
        binding.rvReminders.hasFixedSize();
        binding.rvReminders.setItemAnimator(new DefaultItemAnimator());
        reminderAdapter = new ReminderAdapter(context, listData, new CommentListener() {
            @Override
            public void onClick(MenuItem view, int position) {
                Bundle bundle = new Bundle();
                bundle.putString("Reminder_Start_Date", listData.get(position).getStartDate());
                bundle.putString("Reminder_Name", listData.get(position).getMedicineName());
                bundle.putString("Reminder_Morning_Time", listData.get(position).getMorning());
                bundle.putString("Reminder_Evening_Time", listData.get(position).getEvening());
                bundle.putString("Reminder_Afternoon_Time", listData.get(position).getAfternoon());
                bundle.putString("Reminder_ID", listData.get(position).getReminderId());
                bundle.putString("Reminder_Duration", listData.get(position).getReminderDuration());
                bundle.putString(AppConstant.FROM, AppConstant.TYPE_EDIT);
                ActivityController.startActivity(activity, AddReminder.class, bundle, false, false);

            }
        }, new OnSelectedListener() {
            @Override
            public void onClick(View view, int position) {
                deletereminderApi(listData.get(position).getReminderId());
            }
        });
        binding.rvReminders.setAdapter(reminderAdapter);
    }


    // visible when user have no reminders set
    private void setEmptyLayout(boolean value) {
        if (value) {
            binding.emptyLayout.llRoot.setVisibility(View.VISIBLE);
            binding.emptyLayout.ivImage.setImageResource(R.drawable.nonotificatin);
            binding.emptyLayout.btnSubmit.setText(getResources().getString(R.string.set_reminder));
            binding.emptyLayout.btnSubmit.setOnClickListener(this);
            binding.emptyLayout.tvHeading.setText(getResources().getString(R.string.no_reminder_set));
            binding.emptyLayout.tvSubHeading.setVisibility(View.VISIBLE);
            binding.emptyLayout.tvSubHeading.setText(getResources().getString(R.string.reminder_desc));
        } else {
            binding.emptyLayout.llRoot.setVisibility(View.GONE);
            binding.llData.setVisibility(View.VISIBLE);
        }
    }


    private void setToolbar() {
        binding.menubar.tvTitle.setVisibility(View.VISIBLE);
        binding.menubar.tvTitle.setText(R.string.reminders);
        binding.menubar.ivBack.setImageResource(R.drawable.ic_keyboard_backspace_black_24dp);
        binding.menubar.ivBack.setOnClickListener(this);
        binding.tvAddNewReminder.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                finish();
                break;
            case R.id.btn_submit:
                ActivityController.startActivity(context, AddReminder.class);
                break;
            case R.id.tvAddNewReminder:
                ActivityController.startActivity(context, AddReminder.class);
                break;

            default:
                break;
        }
    }

    private void getRemindersList() {
        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                Api api = RequestController.createService(context, Api.class);
                api.getReminderList(SharedPref.getStringPreferences(context, AppConstant.USER_ID)).enqueue(new Callback<ReminderBaseResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ReminderBaseResponse> call, @NonNull Response<ReminderBaseResponse> response) {
                        binding.swipeRefreshingLayout.setRefreshing(false);
                        ProgressDialogUtils.dismiss();
                        if (response.body() != null) {
                            if (response.body().getStatus() == 1) {
                                CommonUtils.showToastShort(context, response.body().getMessage());
                                if (response.body().getReminderData() != null&& !response.body().getReminderData().isEmpty()) {
                                    setEmptyLayout(false);
                                    listData.clear();
                                    listData.addAll(response.body().getReminderData());
                                    reminderAdapter.notifyDataSetChanged();
                                } else {
                                    setEmptyLayout(true);
                                    listData.clear();
                                    reminderAdapter.notifyDataSetChanged();
                                }

                            } else {
                                setEmptyLayout(true);
                                listData.clear();
                                reminderAdapter.notifyDataSetChanged();
                                CommonUtils.showToastShort(context, response.body().getMessage());
                            }
                        } else {
                            CommonUtils.showToastShort(context, getResources().getString(R.string.no_response));
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ReminderBaseResponse> call, @NonNull Throwable t) {
                        binding.swipeRefreshingLayout.setRefreshing(false);
                        ProgressDialogUtils.dismiss();
                        CommonUtils.showToastShort(context, getResources().getString(R.string.failure));
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            CommonUtils.showToastShort(context, getResources().getString(R.string.no_internet));
        }
    }

    private void deletereminderApi(String reminderId) {

        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                Api api = RequestController.createService(context, Api.class);
                api.deleteReminder(SharedPref.getStringPreferences(context, AppConstant.USER_ID), reminderId)
                        .enqueue(new BaseCallback<BaseResponse>(context) {
                            @Override
                            public void onSuccess(BaseResponse response) {
                                ProgressDialogUtils.dismiss();
                                if (response != null) {
                                    if (response.getStatus() == 1) {
                                        CommonUtils.showToastShort(context, response.getMessage());
                                        getRemindersList();
                                    } else {

                                        CommonUtils.showToastShort(context, response.getMessage());
                                    }
                                } else {
                                    CommonUtils.showToastShort(context, getResources().getString(R.string.no_response));
                                }
                            }

                            @Override
                            public void onFail(Call<BaseResponse> call, BaseResponse baseResponse) {
                                ProgressDialogUtils.dismiss();
                                CommonUtils.showToastShort(context, getResources().getString(R.string.failure));
                            }
                        });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            CommonUtils.showToastShort(context, getResources().getString(R.string.no_internet));
        }


    }
}
