package com.drugvilla.in.ui.doctors;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import com.drugvilla.in.R;
import com.drugvilla.in.databinding.ActivityBookAppointmentBinding;
import com.drugvilla.in.model.Document;
import com.drugvilla.in.model.appointment.timeSlot.TimeSlotData;
import com.drugvilla.in.model.appointment.timeSlot.TimeSlotResponse;
import com.drugvilla.in.service.Api;
import com.drugvilla.in.service.BaseCallback;
import com.drugvilla.in.service.BaseResponse;
import com.drugvilla.in.service.RequestController;
import com.drugvilla.in.ui.address.AddAddress;
import com.drugvilla.in.ui.patient.SelectPatient;
import com.drugvilla.in.utils.ActivityController;
import com.drugvilla.in.utils.AppConstant;
import com.drugvilla.in.utils.CommonUtils;
import com.drugvilla.in.utils.ProgressDialogUtils;
import com.drugvilla.in.utils.SharedPref;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;

public class BookAppointment extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    private Activity activity;
    private ActivityBookAppointmentBinding binding;
    private int day = 0;
    private int month;
    private int year, hour, minute;
    private Date date = null;
    private List<Document> listData = new ArrayList<>();
    private String[] time = {"Schedule Time", "12:00 pm - 12:15 pm", "12:15 pm - 12:30 pm", "11:30 m - 12:30 pm", "12:30 pm - 12:45 pm", "04:45 pm - 05:00 pm", "12:45 pm - 01:00 pm", "04:30 pm - 04:45 pm",
            "01:00 pm - 01:15 pm", "01:15 pm - 01:30 pm", "01:30 pm - 01:45 pm", "01:45 pm - 02:00 pm",
            "01:00 pm - 02:15 pm", "02:15 pm - 02:30 pm", "02:30 pm - 02:45 pm", "02:45 pm - 03:00 pm",
            "03:00 pm - 03:15 pm", "03:15 pm - 03:30 pm", "03:30 pm - 03:45 pm", "03:45 pm - 04:00 pm",
            "04:00 pm - 04:15 pm", "04:15 pm - 04:30 pm"};


    private String Doctor_Profile, Doctor_Name, Doctor_Fees, Appointment_Date, Appointment_Time, Doctor_Location, Appointment_ID;
    private String from = " ", doctor_ID = " ", selectedDay;
    private List<TimeSlotData> timeSlotData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_book_appointment);
        activity = BookAppointment.this;
        context = BookAppointment.this;
        getData();
        setData();
        setToolbar();
        setCalender();
        setListener();
        // TODO : call this method inside getTimeSlotApi() and remove from here
        setSpinner();
    }

    private void setData() {
        if (from.equalsIgnoreCase(AppConstant.TYPE_EDIT)) {
            binding.tvDoctorName.setText(Doctor_Name);
            binding.tvScheduleDate.setText(Appointment_Date);
            binding.tvConsultationFees.setText("Consultation Fees : " + Doctor_Fees);
            binding.tvAddress.setText(Doctor_Location);
            binding.btnSubmit.setText(getResources().getString(R.string.reschedule_appointment));
            // TODO : set the selected time in spinner (Appointment_Time)
            // TODO : set doctor proofile also
           /* if (Doctor_Profile != null && !Doctor_Profile.isEmpty()) {
                Picasso.with(context).load(Doctor_Profile)
                        .error(R.drawable.default_user)
                        .placeholder(R.drawable.default_user)
                        .into(binding.ivDoctor);
            }*/
        }
    }

    private void getData() {
        Bundle bundle = new Bundle();
        bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.getString(AppConstant.FROM) != null && !Objects.requireNonNull(bundle.getString(AppConstant.FROM)).isEmpty()) {
                from = bundle.getString(AppConstant.FROM);
            }
            if (bundle.getString("Doctor_Profile") != null && !Objects.requireNonNull(bundle.getString("Doctor_Profile")).isEmpty()) {
                Doctor_Profile = bundle.getString("Doctor_Profile");
            }
            if (bundle.getString("Doctor_Name") != null && !Objects.requireNonNull(bundle.getString("Doctor_Name")).isEmpty()) {
                Doctor_Name = bundle.getString("Doctor_Name");
            }
            if (bundle.getString("Doctor_Fees") != null && !Objects.requireNonNull(bundle.getString("Doctor_Fees")).isEmpty()) {
                Doctor_Fees = bundle.getString("Doctor_Fees");
            }
            if (bundle.getString("Appointment_Date") != null && !Objects.requireNonNull(bundle.getString("Appointment_Date")).isEmpty()) {
                Appointment_Date = bundle.getString("Appointment_Date");
            }
            if (bundle.getString("Appointment_Time") != null && !Objects.requireNonNull(bundle.getString("Appointment_Time")).isEmpty()) {
                Appointment_Time = bundle.getString("Appointment_Time");
            }
            if (bundle.getString("Appointment_ID") != null && !Objects.requireNonNull(bundle.getString("Appointment_ID")).isEmpty()) {
                Appointment_ID = bundle.getString("Appointment_ID");
            }
            if (bundle.getString("Doctor_Location") != null && !Objects.requireNonNull(bundle.getString("Doctor_Location")).isEmpty()) {
                Doctor_Location = bundle.getString("Doctor_Location");
            }
        }

    }


    private List<Document> PrepareDataMessage() {
        List<Document> data = new ArrayList<>();
        for (int i = 0; i < time.length; i++) {
            Document document = new Document();
            document.setText(time[i]);
            data.add(document);
        }
        listData.addAll(data);
        return data;
    }

    private void setSpinner() {
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, time);
        // TODO : use list timeslotdata
        // ArrayAdapter<TimeSlotData> spinnerArrayAdapter = new ArrayAdapter<TimeSlotData>(this, R.layout.spinner_item, timeSlotData);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        binding.tvTime.setAdapter(spinnerArrayAdapter);
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

    private void setListener() {
        binding.btnSubmit.setOnClickListener(this);
        binding.llDate.setOnClickListener(this);
        binding.llTime.setOnClickListener(this);
    }

    private void setSelectedView(RadioButton selected, RadioButton unselected, LinearLayout llVisible, LinearLayout llGone) {
        selected.setTextColor(getResources().getColor(R.color.colorAccent));
        unselected.setTextColor(getResources().getColor(R.color.lightGray));
        llVisible.setVisibility(View.VISIBLE);
        llGone.setVisibility(View.GONE);
    }

    private void setToolbar() {
        binding.menubar.tvTitle.setVisibility(View.VISIBLE);
        if (from.equalsIgnoreCase(AppConstant.TYPE_EDIT)) {
            binding.menubar.tvTitle.setText(R.string.reschedule_appointment);
        } else {
            binding.menubar.tvTitle.setText(R.string.book_appointment);
        }
        binding.menubar.ivBack.setImageResource(R.drawable.ic_keyboard_backspace_black_24dp);
        binding.menubar.ivBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                finish();
                break;
            case R.id.btnSubmit:
                if (isValid()) {
                    if (from.equalsIgnoreCase(AppConstant.TYPE_EDIT)) {
                        updateAppointment();
                    } else {
                        Bundle bundle = new Bundle();
                        bundle.putString("Selected_Time", binding.tvTime.getSelectedItem().toString());
                        bundle.putString("Selected_Day", selectedDay);
                        bundle.putString("Selected_Date", binding.tvScheduleDate.getText().toString());
                        bundle.putString("Selected_Doctor_Id", doctor_ID);
                        bundle.putString(AppConstant.FROM, AppConstant.FROM_APPOINTMENT);
                        ActivityController.startActivity(activity, SelectPatient.class, bundle, false, false);
                    }

                }
                break;
            case R.id.llDate:
                setDateCalender();
                break;
            case R.id.llTime:
                //  setTimeDialog();
                break;
            default:
                break;
        }
    }


    private void setTimeDialog() {
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                //  binding.tvScheduleTime.setText(selectedHour + ":" + selectedMinute);
            }
        }, hour, minute, false);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    private void setDateCalender() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String input = String.valueOf(new StringBuilder().append(year).append("-").append(monthOfYear + 1).append("-").append(dayOfMonth));
                        binding.tvScheduleDate.setText(input);
                        selectedDay = getNameOfDay(year, monthOfYear, dayOfMonth);
                        Toast.makeText(context, "selected day : " + selectedDay, Toast.LENGTH_SHORT).show();
                        getTimeSlot(selectedDay, input);
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

    private boolean isValid() {
        if (binding.tvScheduleDate.getText().equals("Schedule Date")) {
            CommonUtils.setSnackbar(binding.coordinatorLayout, "Please select schedule date.");
            return false;
        } else if (binding.tvTime.getSelectedItem().equals("Schedule Time")) {
            CommonUtils.setSnackbar(binding.coordinatorLayout, "Please select schedule time.");
            return false;
        }
        /* else if (binding.tvScheduleTime.getText().equals("Schedule Time")) {
            setSnackbar("Please select schedule time.");
            return false;
        } *//*else if (binding.rgUser.getCheckedRadioButtonId() == -1) {
            setSnackbar("Please select a patient.");
            return false;
        } */
        else {
            return true;
        }
    }

    private void getTimeSlot(String day, String date) {
        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                Api api = RequestController.createService(context, Api.class);
                api.getTimeSlot(doctor_ID, day, date).enqueue(new BaseCallback<TimeSlotResponse>(context) {
                    @Override
                    public void onSuccess(TimeSlotResponse response) {
                        ProgressDialogUtils.dismiss();
                        if (response != null) {
                            if (response.getStatus() == 1) {
                                CommonUtils.showToastShort(context, response.getMessage());
                                timeSlotData.clear();
                                timeSlotData.addAll(response.getTimeSlotData());
                                setSpinner();

                            } else {

                                CommonUtils.showToastShort(context, response.getMessage());
                            }
                        } else {
                            CommonUtils.showToastShort(context, context.getResources().getString(R.string.no_response));
                        }
                    }

                    @Override
                    public void onFail(Call<TimeSlotResponse> call, BaseResponse baseResponse) {
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

    private void updateAppointment() {
        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                Api api = RequestController.createService(context, Api.class);
                api.updateAppointment(SharedPref.getStringPreferences(context, AppConstant.USER_ID)
                        , Appointment_ID, binding.tvScheduleDate.getText().toString(),
                        binding.tvTime.getSelectedItem().toString()).enqueue(new BaseCallback<BaseResponse>(context) {
                    @Override
                    public void onSuccess(BaseResponse response) {
                        ProgressDialogUtils.dismiss();
                        if (response != null) {
                            if (response.getStatus() == 1) {
                                CommonUtils.showToastShort(context, response.getMessage());
                                finish();
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

}
