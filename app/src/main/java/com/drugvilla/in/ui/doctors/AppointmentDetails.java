package com.drugvilla.in.ui.doctors;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.drugvilla.in.R;
import com.drugvilla.in.databinding.ActivityAppointmentDetailsBinding;
import com.drugvilla.in.model.appointment.booking.ConfirmBookingResponse;
import com.drugvilla.in.model.appointment.booking.review.AppointmentReviewData;
import com.drugvilla.in.model.appointment.booking.review.AppointmentReviewResponse;
import com.drugvilla.in.model.appointment.myAppointment.AppointmentData;
import com.drugvilla.in.model.appointment.myAppointment.AppointmentResponse;
import com.drugvilla.in.model.patient.PatientData;
import com.drugvilla.in.model.patient.PatientData2;
import com.drugvilla.in.service.Api;
import com.drugvilla.in.service.BaseCallback;
import com.drugvilla.in.service.BaseResponse;
import com.drugvilla.in.service.RequestController;
import com.drugvilla.in.ui.labs.PackageDetailActivity;
import com.drugvilla.in.ui.order.MyOrder;
import com.drugvilla.in.ui.order.OrderConfirmation;
import com.drugvilla.in.utils.ActivityController;
import com.drugvilla.in.utils.AppConstant;
import com.drugvilla.in.utils.CommonUtils;
import com.drugvilla.in.utils.ProgressDialogUtils;
import com.drugvilla.in.utils.SharedPref;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppointmentDetails extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    private Activity activity;
    private ActivityAppointmentDetailsBinding binding;
    private String appointment_ID, APPOINTMENT_ID;
    private String from = " ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_appointment_details);
        context = AppointmentDetails.this;
        activity = AppointmentDetails.this;
        getData();
        setToolbar();
        setViews();
    }


    private void getData() {
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.getStringExtra("APPOINTMENT_ID") != null && !intent.getStringExtra("APPOINTMENT_ID").isEmpty()) {
                appointment_ID = intent.getStringExtra("APPOINTMENT_ID");
            }
        }

        Bundle bundle = new Bundle();
        bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.getString(AppConstant.FROM) != null && !Objects.requireNonNull(bundle.getString(AppConstant.FROM)).isEmpty()) {
                from = bundle.getString(AppConstant.FROM);
            }

            if (bundle.getString("APPOINTMENT_ID") != null && !Objects.requireNonNull(bundle.getString("APPOINTMENT_ID")).isEmpty()) {
                appointment_ID = bundle.getString("APPOINTMENT_ID");
            }
        }
    }

    private void setViews() {
        binding.btnSubmit.setOnClickListener(this);
        binding.llMyAppointments.setOnClickListener(this);
        if (from.equalsIgnoreCase(AppConstant.TYPE_APPOINTMENT_REVIEW)) {
            binding.llMyAppointments.setVisibility(View.GONE);
            binding.llBookingDetails.setVisibility(View.GONE);
            binding.llSelectedPatient.setVisibility(View.VISIBLE);
            binding.btnSubmit.setVisibility(View.VISIBLE);
            getAppointmentReviewApi();
        } else {
            getAppointmentDetailApi();
        }
    }

    private void setToolbar() {
        binding.menubar.tvTitle.setVisibility(View.VISIBLE);
        binding.menubar.ivBack.setImageResource(R.drawable.ic_keyboard_backspace_black_24dp);
        binding.menubar.ivBack.setOnClickListener(this);

        if (from.equalsIgnoreCase(AppConstant.TYPE_APPOINTMENT_REVIEW)) {
            binding.menubar.tvTitle.setText(R.string.appointment_review);
        } else {
            binding.menubar.tvTitle.setText(R.string.appointment_details);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                finish();
                break;
            case R.id.btnSubmit:
                confirmAppointment();
                break;
            case R.id.llMyAppointments:
                ActivityController.startActivity(context, MyOrder.class, AppConstant.MY_APPOINTMENT);
                finish();
                break;
            default:
                break;
        }
    }

    private void confirmAppointment() {
        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                Api api = RequestController.createService(context, Api.class);
                api.confirmAppointment(SharedPref.getStringPreferences(context, AppConstant.USER_ID), APPOINTMENT_ID)
                        .enqueue(new BaseCallback<ConfirmBookingResponse>(context) {
                            @Override
                            public void onSuccess(ConfirmBookingResponse response) {
                                ProgressDialogUtils.dismiss();
                                if (response != null) {
                                    if (response.getStatus() == 1) {
                                        CommonUtils.showToastShort(context, response.getMessage());
                                        Intent intent = new Intent(context, OrderConfirmation.class);
                                        intent.putExtra("APPOINTMENT_ID", response.getConfirmBookingData().getAppointmentId());
                                        context.startActivity(intent);
                                    } else {
                                        CommonUtils.showToastShort(context, response.getMessage());
                                    }
                                } else {
                                    CommonUtils.showToastShort(context, context.getResources().getString(R.string.no_response));
                                }
                            }

                            @Override
                            public void onFail(Call<ConfirmBookingResponse> call, BaseResponse baseResponse) {
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

    private void getAppointmentDetailApi() {

        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                Api api = RequestController.createService(context, Api.class);
                api.getAppointmentDetail(SharedPref.getStringPreferences(context, AppConstant.USER_ID), appointment_ID)
                        .enqueue(new BaseCallback<AppointmentResponse>(context) {
                            @Override
                            public void onSuccess(AppointmentResponse response) {
                                ProgressDialogUtils.dismiss();
                                if (response != null) {
                                    if (response.getStatus() == 1) {
                                        CommonUtils.showToastShort(context, response.getMessage());
                                        if (response.getAppointmentData() != null) {
                                            AppointmentData appointmentData = response.getAppointmentData();
                                            binding.tvAddress.setText(appointmentData.getDoctor_location());
                                            binding.tvAppointmentId.setText(appointmentData.getAppointment_id());
                                            binding.tvAppointmentPrice.setText(appointmentData.getDoctor_fess());
                                            binding.tvClinicName.setText(appointmentData.getClinic_name());
                                            binding.tvdate.setText(appointmentData.getAppointment_date());
                                            binding.tvDoctorName.setText(appointmentData.getDoctor_name());
                                            binding.tvPatientName.setText(appointmentData.getBooked_for());
                                            binding.tvTime.setText(appointmentData.getAppointment_time());
                                            binding.tvBookingStatus.setText(appointmentData.getAppointment_status());
                                            if (appointmentData.getDoctor_profile() != null && !appointmentData.getDoctor_profile().isEmpty()) {
                                                Picasso.with(context).load(appointmentData.getDoctor_profile())
                                                        .error(R.drawable.default_user)
                                                        .placeholder(R.drawable.default_user)
                                                        .into(binding.ivDoctor);
                                            }


                                        }
                                    } else {

                                        CommonUtils.showToastShort(context, response.getMessage());
                                    }
                                } else {
                                    CommonUtils.showToastShort(context, context.getResources().getString(R.string.no_response));
                                }
                            }

                            @Override
                            public void onFail(Call<AppointmentResponse> call, BaseResponse baseResponse) {
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

    private void getAppointmentReviewApi() {

        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                Api api = RequestController.createService(context, Api.class);
                api.getAppointmentReview(SharedPref.getStringPreferences(context, AppConstant.USER_ID), appointment_ID)
                        .enqueue(new BaseCallback<AppointmentReviewResponse>(context) {
                            @Override
                            public void onSuccess(AppointmentReviewResponse response) {
                                ProgressDialogUtils.dismiss();
                                if (response != null) {
                                    if (response.getStatus() == 1) {
                                        CommonUtils.showToastShort(context, response.getMessage());
                                        if (response.getAppointmentReviewData() != null) {
                                            AppointmentReviewData appointmentData = response.getAppointmentReviewData();
                                            APPOINTMENT_ID = response.getAppointmentReviewData().getAppointment_id();
                                            binding.tvAddress.setText(appointmentData.getDoctorLocation());
                                            binding.tvAppointmentPrice.setText(appointmentData.getDoctorFees());
                                            binding.tvDoctorName.setText(appointmentData.getDoctorName());
                                            binding.tvClinicName.setText(appointmentData.getClinic_name());
                                            binding.tvdate.setText(appointmentData.getAppointment_date());
                                            binding.tvTime.setText(appointmentData.getAppointment_time());
                                            if (appointmentData.getDoctorProfile() != null && !appointmentData.getDoctorProfile().isEmpty()) {
                                                Picasso.with(context).load(appointmentData.getDoctorProfile())
                                                        .error(R.drawable.default_user)
                                                        .placeholder(R.drawable.default_user)
                                                        .into(binding.ivDoctor);
                                            }

                                            if (appointmentData.getSelectedPatientData() != null) {
                                                PatientData patientData = appointmentData.getSelectedPatientData();
                                                binding.tvUserAge.setText("Age: "+patientData.getAge());
                                                binding.tvUserGender.setText("Gender: "+patientData.getGender());
                                                binding.tvEmail.setText(patientData.getEmail());
                                                binding.tvUserName.setText(patientData.getName());
                                            }

                                        }
                                    } else {

                                        CommonUtils.showToastShort(context, response.getMessage());
                                    }
                                } else {
                                    CommonUtils.showToastShort(context, context.getResources().getString(R.string.no_response));
                                }
                            }

                            @Override
                            public void onFail(Call<AppointmentReviewResponse> call, BaseResponse baseResponse) {
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
