package com.drugvilla.in.ui.patient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;


import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import com.drugvilla.in.R;
import com.drugvilla.in.databinding.ActivityAddPatientBinding;

import com.drugvilla.in.service.Api;
import com.drugvilla.in.service.BaseCallback;
import com.drugvilla.in.service.BaseResponse;
import com.drugvilla.in.service.RequestController;
import com.drugvilla.in.utils.AppConstant;
import com.drugvilla.in.utils.CommonUtils;
import com.drugvilla.in.utils.ProgressDialogUtils;
import com.drugvilla.in.utils.RegexUtils;
import com.drugvilla.in.utils.SharedPref;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

import retrofit2.Call;

public class AddPatient extends AppCompatActivity implements View.OnClickListener {
    private ActivityAddPatientBinding binding;
    int flag = 0;
    private Context context;
    private String name, email, mobile, age, selected_gender, patient_ID;
    private String from = " ";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_patient);
        context = AddPatient.this;
        getData();
        setData();
        setToolbar();
        setViews();

    }

    private void setData() {
        if (from.equalsIgnoreCase(AppConstant.TYPE_EDIT)) {
            binding.etAge.setText(age);
            binding.etEmail.setText(email);
            binding.etMobile.setText(mobile);
            binding.etPatientName.setText(name);
            if (selected_gender.equalsIgnoreCase("Female")) {
                selected_gender = setViewColor(binding.tvFemale, binding.tvMale);
            } else {
                selected_gender = setViewColor(binding.tvMale, binding.tvFemale);
            }
            binding.btnSubmit.setText(getResources().getString(R.string.update_patient));
        }
    }

    private void getData() {
        Bundle bundle = new Bundle();
        bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.getString(AppConstant.FROM) != null && !Objects.requireNonNull(bundle.getString(AppConstant.FROM)).isEmpty()) {
                from = bundle.getString(AppConstant.FROM);
            }
            if (bundle.getString("Patient_Age") != null && !Objects.requireNonNull(bundle.getString("Patient_Age")).isEmpty()) {
                age = bundle.getString("Patient_Age");
            }
            if (bundle.getString("Patient_Name") != null && !Objects.requireNonNull(bundle.getString("Patient_Name")).isEmpty()) {
                name = bundle.getString("Patient_Name");
            }
            if (bundle.getString("Patient_Mobile") != null && !Objects.requireNonNull(bundle.getString("Patient_Mobile")).isEmpty()) {
                mobile = bundle.getString("Patient_Mobile");
            }
            if (bundle.getString("Patient_Email") != null && !Objects.requireNonNull(bundle.getString("Patient_Email")).isEmpty()) {
                email = bundle.getString("Patient_Email");
            }
            if (bundle.getString("Patient_Gender") != null && !Objects.requireNonNull(bundle.getString("Patient_Gender")).isEmpty()) {
                selected_gender = bundle.getString("Patient_Gender");
            }
            if (bundle.getString("Patient_ID") != null && !Objects.requireNonNull(bundle.getString("Patient_ID")).isEmpty()) {
                patient_ID = bundle.getString("Patient_ID");
            }
        }

    }

    private void setViews() {
        binding.btnSubmit.setOnClickListener(this);
        binding.tvFemale.setOnClickListener(this);
        binding.tvMale.setOnClickListener(this);
        binding.etAge.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                binding.tvAgeError.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.etPatientName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                binding.tvNameError.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.etMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                binding.tvMobileError.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                binding.tvEmailError.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setToolbar() {
        binding.menubar.tvTitle.setVisibility(View.VISIBLE);
        if (from.equalsIgnoreCase(AppConstant.TYPE_EDIT)) {
            binding.menubar.tvTitle.setText(R.string.update_patient);
        } else {
            binding.menubar.tvTitle.setText(R.string.add_patient);
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
                        updatePatient();
                    } else {
                        savePatient();
                    }
                }
                break;
            case R.id.tvMale:
                flag = 1;
                selected_gender = setViewColor(binding.tvMale, binding.tvFemale);
                binding.tvGenderError.setVisibility(View.GONE);
                break;
            case R.id.tvFemale:
                flag = 2;
                selected_gender = setViewColor(binding.tvFemale, binding.tvMale);
                binding.tvGenderError.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }


    private boolean isValid() {
        name = Objects.requireNonNull(binding.etPatientName.getText()).toString().trim();
        age = Objects.requireNonNull(binding.etAge.getText()).toString().trim();
        email = Objects.requireNonNull(binding.etEmail.getText()).toString().trim();
        mobile = Objects.requireNonNull(binding.etMobile.getText()).toString().trim();


        if (name.isEmpty()) {
            CommonUtils.setErrorMessage(binding.etPatientName, binding.tvNameError, getResources().getString(R.string.empty_patient_name));
        } else if (name.length() < 2) {
            CommonUtils.setErrorMessage(binding.etPatientName, binding.tvNameError, getResources().getString(R.string.invalid_name));
        } else if (age.isEmpty()) {
            CommonUtils.setErrorMessage(binding.etAge, binding.tvAgeError, getResources().getString(R.string.empty_patient_age));
        } else if (flag == 0) {
            binding.tvGenderError.setVisibility(View.VISIBLE);
            binding.tvGenderError.setText(getResources().getString(R.string.empty_gender));
        } else if (mobile.isEmpty()) {
            CommonUtils.setErrorMessage(binding.etMobile, binding.tvMobileError, getResources().getString(R.string.empty_mobile));
        } else if (!RegexUtils.isValidMobileNumber(mobile)) {
            CommonUtils.setErrorMessage(binding.etMobile, binding.tvMobileError, getResources().getString(R.string.invalid_mobile));
        } else if (email.isEmpty()) {
            CommonUtils.setErrorMessage(binding.etEmail, binding.tvEmailError, getResources().getString(R.string.empty_email));
        } else if (!RegexUtils.isValidEmail(email)) {
            CommonUtils.setErrorMessage(binding.etEmail, binding.tvEmailError, getResources().getString(R.string.invalid_email));
        } else {
            return true;
        }
        return false;
    }

    private String setViewColor(TextView tvSelected, TextView tvUnselected) {
        tvSelected.setBackground(getResources().getDrawable(R.drawable.border_red));
        tvUnselected.setBackground(getResources().getDrawable(R.drawable.back_stroke));
        tvSelected.setTextColor(getResources().getColor(R.color.colorAccent));
        tvUnselected.setTextColor(getResources().getColor(R.color.lightGray));
        return tvSelected.getText().toString().trim();
    }

    private void savePatient() {
        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                Api api = RequestController.createService(context, Api.class);
                api.addPatient(SharedPref.getStringPreferences(context, AppConstant.USER_ID), name, mobile, email, age, selected_gender).enqueue
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

    private void updatePatient() {
        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                Api api = RequestController.createService(context, Api.class);
                api.updatePatient(SharedPref.getStringPreferences(context, AppConstant.USER_ID), patient_ID, name, mobile, email, age, selected_gender).enqueue
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

