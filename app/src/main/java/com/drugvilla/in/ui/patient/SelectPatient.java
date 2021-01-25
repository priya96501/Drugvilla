package com.drugvilla.in.ui.patient;

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
import android.view.View;
import android.widget.Toast;

import com.drugvilla.in.R;
import com.drugvilla.in.adapter.patient.SelectPatientAdapter;
import com.drugvilla.in.databinding.ActivitySelectPatientBinding;
import com.drugvilla.in.listener.OnCheckSelectedListener;
import com.drugvilla.in.listener.OnSelectedListener;
import com.drugvilla.in.model.Document;
import com.drugvilla.in.model.address.AddressBaseResponse;
import com.drugvilla.in.model.appointment.booking.AppointmentBookingResponse;
import com.drugvilla.in.model.patient.PatientBaseResponse;
import com.drugvilla.in.model.patient.PatientData;
import com.drugvilla.in.service.Api;
import com.drugvilla.in.service.BaseCallback;
import com.drugvilla.in.service.BaseResponse;
import com.drugvilla.in.service.RequestController;
import com.drugvilla.in.ui.address.AddAddress;
import com.drugvilla.in.ui.address.SelectAddress;
import com.drugvilla.in.ui.doctors.AppointmentDetails;
import com.drugvilla.in.ui.labs.ScheduleDetail;
import com.drugvilla.in.ui.labs.SelectLab;
import com.drugvilla.in.ui.order.PaymentSelection;
import com.drugvilla.in.utils.ActivityController;
import com.drugvilla.in.utils.AppConstant;
import com.drugvilla.in.utils.CommonUtils;
import com.drugvilla.in.utils.ProgressDialogUtils;
import com.drugvilla.in.utils.SharedPref;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectPatient extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    private Activity activity;
    private ActivitySelectPatientBinding binding;
    private SelectPatientAdapter patientAdapter;
    private List<PatientData> patientData = new ArrayList<>();
    private String from = " ", selectedTime = " ", selectedDate = " ", selectedDay = " ", selectedDoctorId = " ", collectionType = " ", SelectedPatientID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_select_patient);
        context = SelectPatient.this;
        activity = SelectPatient.this;
        getData();
        setToolbar();
        binding.swipeRefreshingLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAllPatientApi();
            }
        });
    }

    @Override
    protected void onResume() {
        getAllPatientApi();
        setAdapter();
        super.onResume();
    }

    private void getData() {
        Bundle bundle = new Bundle();
        bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.getString(AppConstant.FROM) != null && !Objects.requireNonNull(bundle.getString(AppConstant.FROM)).isEmpty()) {
                from = bundle.getString(AppConstant.FROM);
            }
            if (bundle.getString("Selected_Date") != null && !Objects.requireNonNull(bundle.getString("Selected_Date")).isEmpty()) {
                selectedDate = bundle.getString("Selected_Date");
            }
            if (bundle.getString("Selected_Day") != null && !Objects.requireNonNull(bundle.getString("Selected_Day")).isEmpty()) {
                selectedDay = bundle.getString("Selected_Day");
            }
            if (bundle.getString("Selected_Doctor_Id") != null && !Objects.requireNonNull(bundle.getString("Selected_Doctor_Id")).isEmpty()) {
                selectedDoctorId = bundle.getString("Selected_Doctor_Id");
            }
            if (bundle.getString("Selected_Time") != null && !Objects.requireNonNull(bundle.getString("Selected_Time")).isEmpty()) {
                selectedTime = bundle.getString("Selected_Time");
            }
           /* if (bundle.getString(AppConstant.Selected_Collection_Type) != null && !Objects.requireNonNull(bundle.getString(AppConstant.Selected_Collection_Type)).isEmpty()) {
                collectionType = bundle.getString(AppConstant.Selected_Collection_Type);
            }*/
        }
    }

    private void setEmptyLayout(boolean value) {
        if (value) {
            binding.emptyLayout.llRoot.setVisibility(View.VISIBLE);
            binding.emptyLayout.ivImage.setImageResource(R.drawable.ic_add_patient);
            binding.emptyLayout.btnSubmit.setText(getResources().getString(R.string.add_patient));
            binding.emptyLayout.tvHeading.setText(getResources().getString(R.string.no_patient));
            binding.emptyLayout.btnSubmit.setOnClickListener(this);
        } else {
            binding.llData.setVisibility(View.VISIBLE);
            if (from.equalsIgnoreCase(AppConstant.PROFILE)) {
                binding.btnSelectPatient.setVisibility(View.GONE);
            } else {
                binding.btnSelectPatient.setVisibility(View.VISIBLE);
            }
            binding.emptyLayout.llRoot.setVisibility(View.GONE);

        }
    }

    private void setAdapter() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
        binding.rvPatient.setLayoutManager(linearLayoutManager);
        binding.rvPatient.hasFixedSize();
        binding.rvPatient.setItemAnimator(new DefaultItemAnimator());
        patientAdapter = new SelectPatientAdapter(context, patientData, new OnCheckSelectedListener() {
            @Override
            public void onClick(View view, int position, boolean selected) {
                if (patientData.get(position).isSelected()) {
                    patientData.get(position).setSelected(false);
                } else {
                    for (int i = 0; i < patientData.size(); i++) {
                        patientData.get(i).setSelected(false);
                    }
                    patientData.get(position).setSelected(true);
                }
                patientAdapter.notifyDataSetChanged();
            }
        }, new OnSelectedListener() {
            @Override
            public void onClick(View view, int position) {
                switch (view.getId()) {
                    case R.id.tvYes:
                        deletePatientApi(patientData.get(position).getPatientId());
                        break;
                    case R.id.tvEdit:
                        Bundle bundle = new Bundle();
                        bundle.putString("Patient_Age", patientData.get(position).getAge());
                        bundle.putString("Patient_Name", patientData.get(position).getName());
                        bundle.putString("Patient_Mobile", patientData.get(position).getMobile());
                        bundle.putString("Patient_Email", patientData.get(position).getEmail());
                        bundle.putString("Patient_Gender", patientData.get(position).getGender());
                        bundle.putString("Patient_ID", patientData.get(position).getPatientId());
                        bundle.putString(AppConstant.FROM, AppConstant.TYPE_EDIT);
                        ActivityController.startActivity(activity, AddPatient.class, bundle, false, false);
                        break;
                    default:
                        break;
                }
            }
        });
        if (from.equalsIgnoreCase(AppConstant.PROFILE)) {
            patientAdapter.hide(true);
        }
        binding.rvPatient.setAdapter(patientAdapter);
        patientAdapter.notifyDataSetChanged();
    }

    private boolean isPatientChoosen() {
        boolean checked = false;
        for (int i = 0; i < patientData.size(); i++) {
            if (patientData.get(i).isSelected()) {
                SelectedPatientID = patientData.get(i).getPatientId();
                SharedPref.saveStringPreference(context, AppConstant.Selected_Patient_ID, SelectedPatientID);
                checked = true;
                break;
            } else {
                SharedPref.removeFromPreference(context, AppConstant.Selected_Patient_ID);
                checked = false;
            }
        }
        return checked;
    }

    private void setToolbar() {
        binding.menubar.tvTitle.setVisibility(View.VISIBLE);
        if (from.equalsIgnoreCase(AppConstant.PROFILE)) {
            binding.menubar.tvTitle.setText(R.string.saved_patient);
        } else {
            binding.menubar.tvTitle.setText(R.string.select_patient);
        }
        binding.menubar.ivBack.setImageResource(R.drawable.ic_keyboard_backspace_black_24dp);
        binding.menubar.ivBack.setOnClickListener(this);
        binding.tvAddNewPatient.setOnClickListener(this);
        binding.btnSelectPatient.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                finish();
                break;
            case R.id.btn_submit:
                context.startActivity(new Intent(context, AddPatient.class));
                break;
            case R.id.btnSelectPatient:
                if (isPatientChoosen()) {
                    // coming from labs section but visiting lab is selected so no address required
                    if (from.equalsIgnoreCase(AppConstant.FROM_LABS_CART)) {
                      /*  Bundle bundle = new Bundle();
                        bundle.putString(AppConstant.Selected_Collection_Type, collectionType);
                        bundle.putString(AppConstant.Selected_Patient_ID, "");
                        bundle.putString(AppConstant.FROM, AppConstant.FROM_LABS_CART);
                        ActivityController.startActivity(activity, ScheduleDetail.class, bundle, false, false);*/
                        ActivityController.startActivity(context, ScheduleDetail.class, AppConstant.FROM_LABS_CART);
                    }

                    // coming from booking lab via prescription section but visiting lab is selected so no address required
                    else if (from.equalsIgnoreCase(AppConstant.FROM_LABS_PRESCRIPTION_CART)) {
                        ActivityController.startActivity(context, ScheduleDetail.class, AppConstant.FROM_LABS_PRESCRIPTION_CART);
                    }

                    // coming from labs section but sample collection is selected so address required
                    else if (from.equalsIgnoreCase(AppConstant.FROM_LABTEST_CART)) {
                        /*Bundle bundle = new Bundle();
                        bundle.putString(AppConstant.Selected_Collection_Type, collectionType);
                        bundle.putString(AppConstant.Selected_Patient_ID, "");
                        bundle.putString(AppConstant.FROM, AppConstant.FROM_LABTEST_CART);
                        ActivityController.startActivity(activity, SelectAddress.class, bundle, false, false);*/
                        ActivityController.startActivity(context, SelectAddress.class, AppConstant.FROM_LABTEST_CART);
                    }

                    // coming from booking lab via prescription section but sample collection is selected so address required
                    else if (from.equalsIgnoreCase(AppConstant.FROM_LABTEST_PRESCRIPTION_CART)) {
                        ActivityController.startActivity(context, SelectAddress.class, AppConstant.FROM_LABTEST_PRESCRIPTION_CART);

                    }
                    // coming from booking appointment(FROM_APPOINTMENT)
                    else {
                        addAppointment();
                    }
                } else {
                    CommonUtils.setSnackbar(binding.coordinatorLayout, "Please select patient.");
                }
                break;
            case R.id.tvAddNewPatient:
                ActivityController.startActivity(context, AddPatient.class);
                break;
            default:
                break;
        }
    }

    private void addAppointment() {
        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                Api api = RequestController.createService(context, Api.class);
                api.addAppointment(SharedPref.getStringPreferences(context, AppConstant.USER_ID)
                        , selectedDoctorId, selectedDate, selectedTime, selectedDay, SelectedPatientID)
                        .enqueue(new BaseCallback<AppointmentBookingResponse>(context) {
                            @Override
                            public void onSuccess(AppointmentBookingResponse response) {
                                ProgressDialogUtils.dismiss();
                                if (response != null) {
                                    if (response.getStatus() == 1) {
                                        CommonUtils.showToastShort(context, response.getMessage());

                                        String appointmentId = response.getAppointmentBookingData().getAppointmentId();
                                        Toast.makeText(context, "fsdg  "+appointmentId, Toast.LENGTH_SHORT).show();
                                        Bundle bundle = new Bundle();
                                        bundle.putString("APPOINTMENT_ID", appointmentId);
                                        bundle.putString(AppConstant.FROM, AppConstant.TYPE_APPOINTMENT_REVIEW);
                                        ActivityController.startActivity(activity, AppointmentDetails.class, bundle, false, false);

                                    } else {
                                        CommonUtils.showToastShort(context, response.getMessage());
                                    }
                                } else {
                                    CommonUtils.showToastShort(context, context.getResources().getString(R.string.no_response));
                                }
                            }

                            @Override
                            public void onFail(Call<AppointmentBookingResponse> call, BaseResponse baseResponse) {
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

    private void deletePatientApi(String patientId) {
        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                Api api = RequestController.createService(context, Api.class);
                api.deletePatient(SharedPref.getStringPreferences(context, AppConstant.USER_ID), patientId)
                        .enqueue(new BaseCallback<BaseResponse>(context) {
                            @Override
                            public void onSuccess(BaseResponse response) {
                                ProgressDialogUtils.dismiss();
                                if (response != null) {
                                    if (response.getStatus() == 1) {
                                        CommonUtils.showToastShort(context, response.getMessage());
                                        getAllPatientApi();
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

    private void getAllPatientApi() {
        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                Api api = RequestController.createService(context, Api.class);
                api.getPatientList(SharedPref.getStringPreferences(context, AppConstant.USER_ID)).enqueue(new Callback<PatientBaseResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<PatientBaseResponse> call, @NonNull Response<PatientBaseResponse> response) {
                        binding.swipeRefreshingLayout.setRefreshing(false);
                        ProgressDialogUtils.dismiss();
                        if (response.body() != null) {
                            if (response.body().getStatus() == 1) {
                                CommonUtils.showToastShort(context, response.body().getMessage());
                                if (response.body().getPatientData() != null && !response.body().getPatientData().isEmpty()) {
                                    setEmptyLayout(false);
                                    patientData.clear();
                                    patientData.addAll(response.body().getPatientData());
                                    patientAdapter.notifyDataSetChanged();
                                } else {
                                    setEmptyLayout(true);
                                    patientData.clear();
                                    patientAdapter.notifyDataSetChanged();
                                }

                            } else {
                                setEmptyLayout(true);
                                patientData.clear();
                                patientAdapter.notifyDataSetChanged();
                                CommonUtils.showToastShort(context, response.body().getMessage());
                            }
                        } else {
                            CommonUtils.showToastShort(context, getResources().getString(R.string.no_response));
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<PatientBaseResponse> call, @NonNull Throwable t) {
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

