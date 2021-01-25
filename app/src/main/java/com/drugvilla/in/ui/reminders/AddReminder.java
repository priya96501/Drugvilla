package com.drugvilla.in.ui.reminders;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.opengl.Visibility;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.drugvilla.in.R;
import com.drugvilla.in.databinding.ActivityAddReminderBinding;
import com.drugvilla.in.service.Api;
import com.drugvilla.in.service.BaseCallback;
import com.drugvilla.in.service.BaseResponse;
import com.drugvilla.in.service.RequestController;
import com.drugvilla.in.utils.AppConstant;
import com.drugvilla.in.utils.CommonUtils;
import com.drugvilla.in.utils.ProgressDialogUtils;
import com.drugvilla.in.utils.SharedPref;
import com.google.android.material.snackbar.Snackbar;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

import retrofit2.Call;

public class AddReminder extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    private ActivityAddReminderBinding binding;
    private int day = 0;
    private int month;
    private int year, hour, minute;
    int flag = 0, cross_flag = 0;
    private String from = " ";
    private ArrayList<String> selected_days = new ArrayList<>();
    private HashMap<String, Object> hashMap = new HashMap<>();
    private String reminder_id, Reminder_Start_Date, Reminder_Name, Reminder_Morning_Time, Reminder_Evening_Time, Reminder_Afternoon_Time, Reminder_Duration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_reminder);
        context = AddReminder.this;
        getData();
        setData();
        setToolbar();
        setViews();
        setCalender();

    }

    private void setCalender() {
        final Calendar calender = Calendar.getInstance();
        Date date = calender.getTime();
        day = calender.get(Calendar.DAY_OF_MONTH);
        month = calender.get(Calendar.MONTH);
        year = calender.get(Calendar.YEAR);
        hour = calender.get(Calendar.HOUR_OF_DAY);
        minute = calender.get(Calendar.MINUTE);

    }

    private void setToolbar() {
        binding.menubar.tvTitle.setVisibility(View.VISIBLE);
        if (from.equalsIgnoreCase(AppConstant.TYPE_EDIT)) {
            binding.menubar.tvTitle.setText(R.string.update_reminder);
        } else {
            binding.menubar.tvTitle.setText(R.string.set_reminder);
        }
        binding.menubar.ivBack.setImageResource(R.drawable.ic_keyboard_backspace_black_24dp);
        binding.menubar.ivBack.setOnClickListener(this);
    }

    private void getData() {
        Bundle bundle = new Bundle();
        bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.getString(AppConstant.FROM) != null && !Objects.requireNonNull(bundle.getString(AppConstant.FROM)).isEmpty()) {
                from = bundle.getString(AppConstant.FROM);
            }
            if (bundle.getString("Reminder_Start_Date") != null && !Objects.requireNonNull(bundle.getString("Reminder_Start_Date")).isEmpty()) {
                Reminder_Start_Date = bundle.getString("Reminder_Start_Date");
            }
            if (bundle.getString("Reminder_Name") != null && !Objects.requireNonNull(bundle.getString("Reminder_Name")).isEmpty()) {
                Reminder_Name = bundle.getString("Reminder_Name");
            }
            if (bundle.getString("Reminder_Morning_Time") != null && !Objects.requireNonNull(bundle.getString("Reminder_Morning_Time")).isEmpty()) {
                Reminder_Morning_Time = bundle.getString("Reminder_Morning_Time");
            }
            if (bundle.getString("Reminder_Evening_Time") != null && !Objects.requireNonNull(bundle.getString("Reminder_Evening_Time")).isEmpty()) {
                Reminder_Evening_Time = bundle.getString("Reminder_Evening_Time");
            }
            if (bundle.getString("Reminder_Afternoon_Time") != null && !Objects.requireNonNull(bundle.getString("Reminder_Afternoon_Time")).isEmpty()) {
                Reminder_Afternoon_Time = bundle.getString("Reminder_Afternoon_Time");
            }
            if (bundle.getString("Reminder_Duration") != null && !Objects.requireNonNull(bundle.getString("Reminder_Duration")).isEmpty()) {
                Reminder_Duration = bundle.getString("Reminder_Duration");
            }
            if (bundle.getString("Reminder_ID") != null && !Objects.requireNonNull(bundle.getString("Reminder_ID")).isEmpty()) {
                reminder_id = bundle.getString("Reminder_ID");
            }
            System.out.println(" data :  afternoon :" + Reminder_Afternoon_Time + " evening :" + Reminder_Evening_Time + " morning: " + Reminder_Morning_Time);
        }

    }

    private void setData() {
        if (from.equalsIgnoreCase(AppConstant.TYPE_EDIT)) {
            binding.tvDate.setText(Reminder_Start_Date);
            binding.etName.setText(Reminder_Name);
            if (Reminder_Duration.equalsIgnoreCase("EveryDay")) {
                binding.rbDaily.setChecked(true);
                setSelectedRadioButton(binding.rbDaily, binding.rbWeekly);
                binding.llWeekDays.setVisibility(View.GONE);
            } else {
                binding.rbWeekly.setChecked(true);
                setSelectedRadioButton(binding.rbWeekly, binding.rbDaily);
                binding.llWeekDays.setVisibility(View.VISIBLE);
            }
            if (Reminder_Morning_Time != null) {
                setSelectedTimeView(binding.tvMorningTime, Reminder_Morning_Time, binding.ivCross1, binding.tvMorning);
                binding.iv1.setImageResource(R.drawable.morningselected);
            }
            if (Reminder_Evening_Time != null) {
                setSelectedTimeView(binding.tvNightTime, Reminder_Evening_Time, binding.ivCross3, binding.tvNight);
                binding.iv3.setImageResource(R.drawable.nightselected);
            }
            if (Reminder_Afternoon_Time != null) {
                setSelectedTimeView(binding.tvDayTime, Reminder_Afternoon_Time, binding.ivCross2, binding.tvDay);
                binding.iv2.setImageResource(R.drawable.dayselected);
            }
            binding.btnSubmit.setText(getResources().getString(R.string.update_reminder));
        }
    }

    private void setSelectedRadioButton(RadioButton selected, RadioButton unSelected) {
        selected.setTextColor(getResources().getColor(R.color.colorAccent));
        unSelected.setTextColor(getResources().getColor(R.color.lightBlack));
    }

    private void setSelectedTimeView(TextView TvSelectedTime, String time, ImageView cross, TextView heading) {
        TvSelectedTime.setVisibility(View.VISIBLE);
        TvSelectedTime.setText(time);
        cross.setVisibility(View.VISIBLE);
        heading.setTextColor(getResources().getColor(R.color.colorAccent));
    }

    private void setVisibility(TextView textView, ImageView imageView, TextView textView2) {
        textView.setText("");
        textView.setVisibility(View.GONE);
        imageView.setVisibility(View.INVISIBLE);
        textView2.setTextColor(getResources().getColor(R.color.lightBlack));
    }

    private void setViews() {
        binding.llDate.setOnClickListener(this);
        binding.btnSubmit.setOnClickListener(this);
        binding.llAfternoon.setOnClickListener(this);
        binding.llEvening.setOnClickListener(this);
        binding.llMorning.setOnClickListener(this);
        binding.ivCross1.setOnClickListener(this);
        binding.ivCross2.setOnClickListener(this);
        binding.ivCross3.setOnClickListener(this);
        binding.rgDuration.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                if (checkedId == R.id.rb_daily) {
                    setSelectedRadioButton(binding.rbDaily, binding.rbWeekly);
                    binding.llWeekDays.setVisibility(View.GONE);
                } else {
                    setSelectedRadioButton(binding.rbWeekly, binding.rbDaily);
                    binding.llWeekDays.setVisibility(View.VISIBLE);
                }
            }
        });
    }


    private void setTimeDialog(final TextView textView, final TextView tvHeading, final ImageView imageView, final int value) {
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                textView.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.VISIBLE);
                tvHeading.setTextColor(getResources().getColor(R.color.colorAccent));
                textView.setText(selectedHour + ":" + selectedMinute);
                if (value == 1) {
                    binding.iv1.setImageResource(R.drawable.morningselected);
                    flag = 1;
                } else if (value == 2) {
                    binding.iv2.setImageResource(R.drawable.dayselected);
                    flag = 2;
                } else if (value == 3) {
                    binding.iv3.setImageResource(R.drawable.nightselected);
                    flag = 3;
                } else {
                    flag = 0;
                }
            }


        }, hour, minute, false);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                finish();
                break;
            case R.id.llAfternoon:
                setTimeDialog(binding.tvDayTime, binding.tvDay, binding.ivCross2, 2);
                break;
            case R.id.llEvening:
                setTimeDialog(binding.tvNightTime, binding.tvNight, binding.ivCross3, 3);
                break;
            case R.id.llMorning:
                setTimeDialog(binding.tvMorningTime, binding.tvMorning, binding.ivCross1, 1);
                break;
            case R.id.ivCross1:
                flag = 0;
                setVisibility(binding.tvMorningTime, binding.ivCross1, binding.tvMorning);
                binding.iv1.setImageResource(R.drawable.morningunselected);
                break;
            case R.id.ivCross2:
                flag = 0;
                setVisibility(binding.tvDayTime, binding.ivCross2, binding.tvDay);
                binding.iv2.setImageResource(R.drawable.dayunselected);
                break;
            case R.id.ivCross3:
                flag = 0;
                setVisibility(binding.tvNightTime, binding.ivCross3, binding.tvNight);
                binding.iv3.setImageResource(R.drawable.nightunselected);
                break;
            case R.id.btnSubmit:
                if (isValid()) {
                    if (binding.rgDuration.getCheckedRadioButtonId() == R.id.rb_weekly) {
                        String result = "Selected Days";
                        if (binding.day1.isChecked()) {
                            selected_days.add("SUNDAY");
                            result += "\nSUNDAY";
                        }
                        if (binding.day2.isChecked()) {
                            selected_days.add("MONDAY");
                            result += "\nMONDAY";
                        }
                        if (binding.day3.isChecked()) {
                            selected_days.add("TUESDAY");
                            result += "\nTUESDAY";
                        }
                        if (binding.day4.isChecked()) {
                            selected_days.add("WEDNESDAY");
                            result += "\nWEDNESDAY";
                        }
                        if (binding.day5.isChecked()) {
                            selected_days.add("THURSDAY");
                            result += "\nTHURSDAY";
                        }
                        if (binding.day6.isChecked()) {
                            selected_days.add("FRIDAY");
                            result += "\nFRIDAY";
                        }
                        if (binding.day7.isChecked()) {
                            selected_days.add("SATURDAY");
                            result += "\nSATURDAY";
                        }
                        System.out.println("Selected days : " + selected_days);
                        System.out.println("Selected days in string: " + result);
                    } else {
                        selected_days.clear();
                    }

                    if (from.equalsIgnoreCase(AppConstant.TYPE_EDIT)) {
                        updateReminderApi();
                    } else {
                        addReminderApi();
                    }
                }
                break;
            case R.id.llDate:
                setDateCalender();
                break;
            default:
                break;
        }
    }

    private boolean isValid() {
        if (Objects.requireNonNull(binding.etName.getText()).toString().isEmpty()) {
            setSnackbar("Please provide medicine name.");
            return false;
        } else if (binding.tvDayTime.getText().toString().isEmpty() && binding.tvMorningTime.getText().toString().isEmpty() && binding.tvNightTime.getText().toString().isEmpty()) {
            setSnackbar("Please select reminder category.");
            return false;
        } else if (binding.rgDuration.getCheckedRadioButtonId() == -1) {
            setSnackbar("Please select duration.");
            return false;
        } else if (!isSelected()) {
            setSnackbar("Please select atleast one day.");
            return false;
        } else if (binding.tvDate.getText().equals("Select Date")) {
            setSnackbar("Please select start date.");
            return false;

        } else {
            return true;
        }
    }

    private boolean isSelected() {
        if (binding.rgDuration.getCheckedRadioButtonId() == R.id.rb_weekly) {
            if (binding.day1.isChecked() || binding.day2.isChecked() || binding.day3.isChecked() || binding.day4.isChecked() ||
                    binding.day5.isChecked() || binding.day6.isChecked() || binding.day7.isChecked()) {
                return true;
            } else {
                return false;
            }
        } else
            return true;
    }

    private void setSnackbar(String message) {
        Snackbar snackbar = Snackbar.make(binding.coordinatorLayout, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    private void setDateCalender() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String input = String.valueOf(new StringBuilder().append(year).append("-").append(monthOfYear + 1).append("-").append(dayOfMonth));
                        binding.tvDate.setText(input);
                        binding.tvDate.setTextColor(getResources().getColor(R.color.lightBlack));
                    }

                }, year, month, day);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();

    }


    private void updateReminderApi() {
        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                Api api = RequestController.createService(context, Api.class);
                hashMap.put("user_id", SharedPref.getStringPreferences(context, AppConstant.USER_ID));
                hashMap.put("reminder_id", reminder_id);
                hashMap.put("medicine_name", binding.etName.getText().toString());
                hashMap.put("start_date", binding.tvDate.getText().toString());
                if (binding.rgDuration.getCheckedRadioButtonId() == R.id.rb_daily) {
                    hashMap.put("reminder_duration", binding.rbDaily.getText().toString());
                    hashMap.remove("selected_days");
                } else {
                    hashMap.put("reminder_duration", binding.rbWeekly.getText().toString());
                    hashMap.put("selected_days", selected_days);
                }
                hashMap.put("morning", binding.tvMorningTime.getText().toString());
                hashMap.put("afternoon", binding.tvDayTime.getText().toString());
                hashMap.put("evening", binding.tvNightTime.getText().toString());
                api.updateReminder(hashMap).enqueue
                        (new BaseCallback<BaseResponse>(context) {
                            @Override
                            public void onSuccess(com.drugvilla.in.service.BaseResponse response) {
                                ProgressDialogUtils.dismiss();
                                if (response != null) {
                                    if (response.getStatus() == 1) {
                                        CommonUtils.showToastShort(context, response.getMessage());
                                        finish();
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

    private void addReminderApi() {
        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                Api api = RequestController.createService(context, Api.class);
                hashMap.put("user_id", SharedPref.getStringPreferences(context, AppConstant.USER_ID));
                hashMap.put("medicine_name", binding.etName.getText().toString());
                hashMap.put("start_date", binding.tvDate.getText().toString());
                if (binding.rgDuration.getCheckedRadioButtonId() == R.id.rb_daily) {
                    hashMap.put("reminder_duration", binding.rbDaily.getText().toString());
                    hashMap.remove("selected_days");
                } else {
                    hashMap.put("reminder_duration", binding.rbWeekly.getText().toString());
                    hashMap.put("selected_days", selected_days);
                }
                hashMap.put("morning", binding.tvMorningTime.getText().toString());
                hashMap.put("afternoon", binding.tvDayTime.getText().toString());
                hashMap.put("evening", binding.tvNightTime.getText().toString());
                api.addReminder(hashMap).enqueue
                        (new BaseCallback<BaseResponse>(context) {
                            @Override
                            public void onSuccess(com.drugvilla.in.service.BaseResponse response) {
                                ProgressDialogUtils.dismiss();
                                if (response != null) {
                                    if (response.getStatus() == 1) {
                                        CommonUtils.showToastShort(context, response.getMessage());
                                        finish();
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
