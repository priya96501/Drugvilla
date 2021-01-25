package com.drugvilla.in.ui.labs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.drugvilla.in.R;
import com.drugvilla.in.adapter.schedule.TimeSlotSelectionAdapter;
import com.drugvilla.in.databinding.ActivityScheduleDetailBinding;
import com.drugvilla.in.listener.OnCheckSelectedListener;
import com.drugvilla.in.model.Document;
import com.drugvilla.in.model.appointment.timeSlot.TimeSlotData;
import com.drugvilla.in.model.appointment.timeSlot.TimeSlotResponse;
import com.drugvilla.in.model.order.saveOrder.SaveOrderResponse;
import com.drugvilla.in.service.Api;
import com.drugvilla.in.service.BaseCallback;
import com.drugvilla.in.service.BaseResponse;
import com.drugvilla.in.service.RequestController;
import com.drugvilla.in.ui.order.OrderReview;
import com.drugvilla.in.utils.ActivityController;
import com.drugvilla.in.utils.AppConstant;
import com.drugvilla.in.utils.CommonUtils;
import com.drugvilla.in.utils.ProgressDialogUtils;
import com.drugvilla.in.utils.SharedPref;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;

public class ScheduleDetail extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    private ActivityScheduleDetailBinding binding;
    private List<Document> listData = new ArrayList<>();
    private List<Document> listData2 = new ArrayList<>();
    private TimeSlotSelectionAdapter timeSlotAdapter;
    private String from = " ", selectedTime, selectedDate, selectedDay;
    private final String[] time = {"6:00 AM - 7:00 AM", "7:00 AM - 8:00 AM", "8:00 AM - 9:00 AM", "9:00 AM - 10:00 AM",
            "10:00 AM - 11:00 AM", "11:00 AM - 12:00 AM", "12:00 AM - 01:00 AM", "01:00 AM - 02:00 AM"};

    private List<TimeSlotData> timeSlotDataList = new ArrayList<>();
    private int day = 0;
    private int month;
    private int year, hour, minute;
    private Date date = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_schedule_detail);
        context = ScheduleDetail.this;
        getData();
        setCalender();
        setToolbar();
    }

    private void getData() {
        Bundle bundle = new Bundle();
        bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.getString(AppConstant.FROM) != null && !Objects.requireNonNull(bundle.getString(AppConstant.FROM)).isEmpty()) {
                from = bundle.getString(AppConstant.FROM);
            }
        }
    }

    private void setCalender() {
        final Calendar calender = Calendar.getInstance();
        date = calender.getTime();
        day = calender.get(Calendar.DAY_OF_MONTH);
        month = calender.get(Calendar.MONTH);
        year = calender.get(Calendar.YEAR);
        hour = calender.get(Calendar.HOUR_OF_DAY);
        minute = calender.get(Calendar.MINUTE);
    }

    private void setToolbar() {
        binding.menubar.tvTitle.setVisibility(View.VISIBLE);
        binding.menubar.tvTitle.setText(R.string.select_slot);
        binding.menubar.ivBack.setImageResource(R.drawable.ic_keyboard_backspace_black_24dp);
        binding.menubar.ivBack.setOnClickListener(this);
        binding.btnSubmit.setOnClickListener(this);
        binding.llSelectDate.setOnClickListener(this);
    }


    private void setTimeAdapter() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
        binding.rvTimeSlot.setLayoutManager(linearLayoutManager);
        binding.rvTimeSlot.hasFixedSize();
        binding.rvTimeSlot.setItemAnimator(new DefaultItemAnimator());
        listData.clear();
        listData = PrepareDataMessage();
        timeSlotAdapter = new TimeSlotSelectionAdapter(context, listData, new OnCheckSelectedListener() {
            @Override
            public void onClick(View view, int position, boolean selected) {
                if (listData.get(position).isSelected()) {
                    listData.get(position).setSelected(false);
                } else {
                    for (int i = 0; i < listData.size(); i++) {
                        listData.get(i).setSelected(false);
                    }
                    listData.get(position).setSelected(true);
                }
                timeSlotAdapter.notifyDataSetChanged();
            }
        });
        binding.rvTimeSlot.setAdapter(timeSlotAdapter);
    }

    private List<Document> PrepareDataMessage() {
        List<Document> data = new ArrayList<>();
        for (String s : time) {
            Document document = new Document();
            document.setText(s);
            data.add(document);
        }
        listData.addAll(data);
        return data;
    }

    private boolean isTimeChoosen() {
        boolean checked = false;
        for (int i = 0; i < listData.size(); i++) {
            if (listData.get(i).isSelected()) {
                //TODO : save selected time
                selectedTime = listData.get(i).getText();
                SharedPref.saveStringPreference(context, AppConstant.Selected_Time, selectedTime);
                checked = true;
                break;
            } else {
                checked = false;
            }
        }
        return checked;
    }

    private boolean isDateChoosen() {
        if (binding.tvSelectedDate.getText().toString().equalsIgnoreCase("Date") && binding.tvSelectedDay.getText().toString().equalsIgnoreCase("Day")) {
            CommonUtils.setSnackbar(binding.coordinatorLayout, "Please select a date.");
            return false;

        } else {
            return true;
        }

       /* boolean checked = false;
        for (int i = 0; i < listData2.size(); i++) {
            if (listData2.get(i).isSelected()) {
                //TODO : save selected date
                selectedDate = listData2.get(i).getText();
                SharedPref.saveStringPreference(context, AppConstant.Selected_Date, selectedDate);
                checked = true;
                break;
            } else {
                checked = false;
            }
        }
        return checked;*/
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                finish();
                break;

            case R.id.llSelectDate:
                setDateCalender();
                break;

            case R.id.btnSubmit:
                if (isDateChoosen()) {
                    if (isTimeChoosen()) {
                        // visit lab option selected (no address required) from book from prescription flow
                        if (from.equalsIgnoreCase(AppConstant.FROM_LABS_PRESCRIPTION_CART)) {
                            ActivityController.startActivity(context, OrderReview.class, AppConstant.FROM_LABS_PRESCRIPTION_CART);
                        }
                        // collect sample option selected (address required) from book from prescription flow
                        else if (from.equalsIgnoreCase(AppConstant.FROM_LABTEST_PRESCRIPTION_CART)) {
                            ActivityController.startActivity(context, OrderReview.class, AppConstant.FROM_LABTEST_PRESCRIPTION_CART);
                        }
                        // collect sample option selected (address required)
                        else if (from.equalsIgnoreCase(AppConstant.FROM_LABTEST_CART)) {
                            saveOrderDetails(AppConstant.FROM_LABTEST_CART);
                            //ActivityController.startActivity(context, OrderReview.class, AppConstant.FROM_LABTEST_CART);
                        }
                        // visit lab option selected (no address required)
                        else {
                            saveOrderDetails(AppConstant.TYPE_LABS);
                            // ActivityController.startActivity(context, OrderReview.class, AppConstant.TYPE_LABS);
                        }

                    } else {
                        CommonUtils.setSnackbar(binding.coordinatorLayout, "Please select a time slot.");
                    }
                }

               /*
                if (!isDateChoosen()) {
                    CommonUtils.setSnackbar(binding.coordinatorLayout, "Please select a date.");
                } else if (!isTimeChoosen()) {
                    CommonUtils.setSnackbar(binding.coordinatorLayout, "Please select a time slot.");
                } else {
                    // visit lab option selected (no address required) from book from prescription flow
                    if (from.equalsIgnoreCase(AppConstant.FROM_LABS_PRESCRIPTION_CART)) {
                        ActivityController.startActivity(context, OrderReview.class, AppConstant.FROM_LABS_PRESCRIPTION_CART);
                    }
                    // collect sample option selected (address required) from book from prescription flow
                    else if (from.equalsIgnoreCase(AppConstant.FROM_LABTEST_PRESCRIPTION_CART)) {
                        ActivityController.startActivity(context, OrderReview.class, AppConstant.FROM_LABTEST_PRESCRIPTION_CART);
                    }
                    // collect sample option selected (address required)
                    else if (from.equalsIgnoreCase(AppConstant.FROM_LABTEST_CART)) {
                        saveOrderDetails(AppConstant.FROM_LABTEST_CART);
                        //ActivityController.startActivity(context, OrderReview.class, AppConstant.FROM_LABTEST_CART);
                    }
                    // visit lab option selected (no address required)
                    else {
                        saveOrderDetails(AppConstant.TYPE_LABS);
                        // ActivityController.startActivity(context, OrderReview.class, AppConstant.TYPE_LABS);
                    }
                }*/
                break;
            default:
                break;
        }
    }

    private void setDateCalender() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String input = String.valueOf(new StringBuilder().append(year).append("-").append(monthOfYear + 1).append("-").append(dayOfMonth));
                        binding.llSelectedDateData.setVisibility(View.VISIBLE);
                        binding.tvSelectedDate.setText(input);
                        selectedDay = getNameOfDay(year, monthOfYear, dayOfMonth);
                        binding.tvSelectedDay.setText(selectedDay);
                        // TODO : call api
                        // getTimeSlot(selectedDay, input);
                        setTimeAdapter();
                    }

                }, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    public String getNameOfDay(int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, monthOfYear);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String days[] = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        int dayIndex = calendar.get(Calendar.DAY_OF_WEEK);
        return days[dayIndex - 1];
    }

    private void saveOrderDetails(final String type) {

        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                Api api = RequestController.createService(context, Api.class);
                HashMap<String, String> map = new HashMap<>();
                map.put("user_id", SharedPref.getStringPreferences(context, AppConstant.USER_ID));
                map.put("collection_type", SharedPref.getStringPreferences(context, AppConstant.Selected_Collection_Type));
                map.put("selected_date", binding.tvSelectedDate.getText().toString());
                map.put("selected_time", selectedTime);
                map.put("selected_patient_id", SharedPref.getStringPreferences(context, AppConstant.Selected_Patient_ID));
                if (type.equalsIgnoreCase(AppConstant.FROM_LABTEST_CART)) {
                    map.put("selected_address_id", SharedPref.getStringPreferences(context, AppConstant.Selected_Address_ID));
                }
                api.saveLabTestOrderDetails(map).enqueue(new BaseCallback<SaveOrderResponse>(context) {
                    @Override
                    public void onSuccess(SaveOrderResponse response) {
                        ProgressDialogUtils.dismiss();
                        if (response != null) {
                            if (response.getStatus() == 1) {
                                CommonUtils.showToastShort(context, response.getMessage());
                                String labOrderId = response.getSaveOrderData().getOrderId();
                                SharedPref.saveStringPreference(context, AppConstant.LabOrderId, labOrderId);
                                ActivityController.startActivity(context, OrderReview.class, type);
                            } else {

                                CommonUtils.showToastShort(context, response.getMessage());
                            }
                        } else {
                            CommonUtils.showToastShort(context, getResources().getString(R.string.no_response));
                        }
                    }

                    @Override
                    public void onFail(Call<SaveOrderResponse> call, BaseResponse baseResponse) {
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

    private void getTimeSlot(String day, String date) {

        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                Api api = RequestController.createService(context, Api.class);
                api.getLabTimeSlot(SharedPref.getStringPreferences(context, AppConstant.USER_ID), "", date, day)
                        .enqueue(new BaseCallback<TimeSlotResponse>(context) {
                            @Override
                            public void onSuccess(TimeSlotResponse response) {
                                ProgressDialogUtils.dismiss();
                                if (response != null) {
                                    if (response.getStatus() == 1) {
                                        CommonUtils.showToastShort(context, response.getMessage());
                                        if (response.getTimeSlotData() != null && !response.getTimeSlotData().isEmpty()) {
                                            timeSlotDataList.clear();
                                            timeSlotDataList.addAll(response.getTimeSlotData());
                                            timeSlotAdapter.notifyDataSetChanged();
                                        } else {
                                            timeSlotDataList.clear();
                                            timeSlotAdapter.notifyDataSetChanged();
                                        }

                                    } else {
                                        timeSlotDataList.clear();
                                        timeSlotAdapter.notifyDataSetChanged();
                                        CommonUtils.showToastShort(context, response.getMessage());
                                    }
                                } else {
                                    CommonUtils.showToastShort(context, getResources().getString(R.string.no_response));
                                }
                            }

                            @Override
                            public void onFail(Call<TimeSlotResponse> call, BaseResponse baseResponse) {
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

