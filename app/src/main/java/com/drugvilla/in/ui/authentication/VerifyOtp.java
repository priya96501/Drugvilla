package com.drugvilla.in.ui.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.drugvilla.in.R;
import com.drugvilla.in.databinding.ActivityVerifyOtpBinding;
import com.drugvilla.in.model.authentication.BaseResponse;
import com.drugvilla.in.model.authentication.LoginData;
import com.drugvilla.in.service.Api;
import com.drugvilla.in.service.BaseCallback;
import com.drugvilla.in.service.RequestController;
import com.drugvilla.in.ui.startAndDashboard.Home;
import com.drugvilla.in.utils.ActivityController;
import com.drugvilla.in.utils.AppConstant;
import com.drugvilla.in.utils.CommonUtils;
import com.drugvilla.in.utils.PrefManager;
import com.drugvilla.in.utils.ProgressDialogUtils;
import com.drugvilla.in.utils.SharedPref;

import java.util.HashMap;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyOtp extends AppCompatActivity implements View.OnClickListener {
    private ActivityVerifyOtpBinding binding;
    private String otp1;
    private String otp2;
    private String otp4;
    private String otp3;
    HashMap<String, String> map = new HashMap<>();
    private String from = " ", device_token = "", mobile_number = "", email = "", input = "";
    private Context context;
    private Activity activity;
    private String OTP;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_verify_otp);
        context = VerifyOtp.this;
        activity = VerifyOtp.this;
        Activity activity = VerifyOtp.this;
        // fetching device token from prefrence manager
        PrefManager prefManager = PrefManager.getInstance(activity);
        device_token = prefManager.getPreference(AppConstant.DEVICE_TOKEN_);
        // setting status bar color
        CommonUtils.changeStatusBarColor(activity);
        setToolbar();
        setListener();
        getData();
    }

    private void getData() {
        Bundle bundle = new Bundle();
        bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.getString(AppConstant.FROM) != null && !Objects.requireNonNull(bundle.getString(AppConstant.FROM)).isEmpty()) {
                from = bundle.getString(AppConstant.FROM);
            }
            if (bundle.getString(AppConstant.USER_EMAIL) != null && !Objects.requireNonNull(bundle.getString(AppConstant.USER_EMAIL)).isEmpty()) {
                email = bundle.getString(AppConstant.USER_EMAIL);
            }
            if (bundle.getString(AppConstant.USER_MOBILE) != null && !Objects.requireNonNull(bundle.getString(AppConstant.USER_MOBILE)).isEmpty()) {
                mobile_number = bundle.getString(AppConstant.USER_MOBILE);
            }
            if (bundle.getString(AppConstant.USER_INPUT) != null && !Objects.requireNonNull(bundle.getString(AppConstant.USER_INPUT)).isEmpty()) {
                input = bundle.getString(AppConstant.USER_INPUT);
            }

        }
    }

    private void setToolbar() {
      /*  binding.menubar.ivBack.setImageResource(R.drawable.ic_keyboard_backspace_black_24dp);
        binding.menubar.ivBack.setOnClickListener(this);
        binding.menubar.tvTitle.setVisibility(View.VISIBLE);
        binding.menubar.tvTitle.setText("Verify OTP");*/
    }

    private void setListener() {
        binding.btnContinue.setOnClickListener(this);
        binding.tvResendOtp.setOnClickListener(this);
        binding.ivBack.setOnClickListener(this);
        binding.etOtp1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 1) {
                    binding.etOtp2.requestFocus();
                }
                binding.etOtp1.setBackground(getResources().getDrawable(R.drawable.back_white));
                binding.tvOtpError.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.etOtp2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 1) {
                    binding.etOtp3.requestFocus();
                } else {
                    binding.etOtp1.requestFocus();
                }
                binding.etOtp2.setBackground(getResources().getDrawable(R.drawable.back_white));
                binding.tvOtpError.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.etOtp3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 1) {
                    binding.etOtp4.requestFocus();
                } else {
                    binding.etOtp2.requestFocus();
                }
                binding.etOtp3.setBackground(getResources().getDrawable(R.drawable.back_white));
                binding.tvOtpError.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.etOtp4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence.length() == 0) {
                    binding.etOtp3.requestFocus();
                }
                binding.etOtp4.setBackground(getResources().getDrawable(R.drawable.back_white));
                binding.tvOtpError.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }


    private boolean isValidOtp() {
        otp1 = binding.etOtp1.getText().toString().trim();
        otp2 = binding.etOtp2.getText().toString().trim();
        otp3 = binding.etOtp3.getText().toString().trim();
        otp4 = binding.etOtp4.getText().toString().trim();
        if (otp1.isEmpty() && otp2.isEmpty() && otp3.isEmpty() && otp4.isEmpty()) {
            setErrorBackground();
            binding.tvOtpError.setVisibility(View.VISIBLE);
            binding.tvOtpError.setText(getResources().getString(R.string.empty_otp));
            return false;
        } else if (otp1.isEmpty()) {
            CommonUtils.setErrorMessage(binding.etOtp1, binding.tvOtpError, getResources().getString(R.string.invalid_otp));
            binding.etOtp1.setBackground(getResources().getDrawable(R.drawable.border_red));
        } else if (otp2.isEmpty()) {
            CommonUtils.setErrorMessage(binding.etOtp2, binding.tvOtpError, getResources().getString(R.string.invalid_otp));
            binding.etOtp2.setBackground(getResources().getDrawable(R.drawable.border_red));
        } else if (otp3.isEmpty()) {
            CommonUtils.setErrorMessage(binding.etOtp3, binding.tvOtpError, getResources().getString(R.string.invalid_otp));
            binding.etOtp3.setBackground(getResources().getDrawable(R.drawable.border_red));
        } else if (otp4.isEmpty()) {
            CommonUtils.setErrorMessage(binding.etOtp4, binding.tvOtpError, getResources().getString(R.string.invalid_otp));
            binding.etOtp4.setBackground(getResources().getDrawable(R.drawable.border_red));
        } else {
            return true;
        }
        return false;
    }

    private void setErrorBackground() {
        binding.etOtp1.setBackground(getResources().getDrawable(R.drawable.border_red));
        binding.etOtp2.setBackground(getResources().getDrawable(R.drawable.border_red));
        binding.etOtp3.setBackground(getResources().getDrawable(R.drawable.border_red));
        binding.etOtp4.setBackground(getResources().getDrawable(R.drawable.border_red));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                finish();
                break;
            case R.id.tvResendOtp:
                if (from.equalsIgnoreCase(AppConstant.SIGNUP)) {
                    // resend otp for signup verification
                    getOtp();
                } else {
                    // resend otp  for forgot password and updating email/mobile
                    getOtpOnEmailMobile();
                }
                break;
            case R.id.btnContinue:

                if (isValidOtp()) {
                    OTP = new StringBuilder(4).append(otp1).append(otp2).append(otp3).append(otp4).toString();
                    if (from.equalsIgnoreCase(AppConstant.SIGNUP)) {
                        // verify otp api for signup verification
                        verifyOtpForSignup(OTP);
                    } else if (from.equalsIgnoreCase(AppConstant.PROFILE)) {
                        // verify otp api for updating email/mobile verification
                        verifyOtpForUpdate(OTP);
                    } else {
                        // verify otp api for forgot password and for updating email and mobile from user profile
                        verifyOtp(OTP);
                    }
                }
                break;
            default:
                break;
        }
    }

    private void verifyOtpForUpdate(String otp) {
        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                Api api = RequestController.createService(context, Api.class);
                api.verifyOtpUpdate(SharedPref.getStringPreferences(context, AppConstant.USER_ID), input, otp).enqueue(new Callback<LoginData>() {
                    @Override
                    public void onResponse(@NonNull Call<LoginData> call, @NonNull Response<LoginData> response) {
                        ProgressDialogUtils.dismiss();
                        if (response.body() != null) {
                            if (response.body().getStatus().equalsIgnoreCase("1")) {
                                CommonUtils.showToastShort(context, response.body().getMessage());
                                String User_Name = new StringBuilder().append(response.body().getFName()).append(" ").append(response.body().getLName()).toString();
                                System.out.println("user name :  " + User_Name);
                                SharedPref.saveStringPreference(context, AppConstant.USER_NAME, User_Name);
                                SharedPref.saveStringPreference(context, AppConstant.USER_MOBILE, response.body().getMobile());
                                SharedPref.saveStringPreference(context, AppConstant.USER_EMAIL, response.body().getEmail());
                                SharedPref.saveStringPreference(context, AppConstant.USER_ID, response.body().getUserId());
                                SharedPref.saveStringPreference(context, AppConstant.USER_REFERAL_CODE, response.body().getReferalCode());
                                finish();
                            } else {
                                CommonUtils.showToastShort(context, "Invalid OTP.");
                            }
                        } else {
                            CommonUtils.showToastShort(context, getResources().getString(R.string.no_response));
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<LoginData> call, @NonNull Throwable t) {
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

    private void getOtpOnEmailMobile() {

        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                Api api = RequestController.createService(context, Api.class);
                api.getOtp(input).enqueue(new Callback<BaseResponse>() {
                    @Override
                    public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                        ProgressDialogUtils.dismiss();
                        if (response.body() != null) {
                            if (response.body().getStatus().equals("1")) {
                                CommonUtils.showToastShort(context, "OTP send Successfully.");
                            } else {
                                CommonUtils.showToastShort(context, "User not registered..");
                            }
                        } else {
                            CommonUtils.showToastShort(context, getResources().getString(R.string.no_response));
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseResponse> call, Throwable t) {
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

    private void getOtp() {
        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                Api api = RequestController.createService(context, Api.class);
                api.getResendOtp(mobile_number, email).enqueue(new Callback<BaseResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<BaseResponse> call, @NonNull Response<BaseResponse> response) {
                        ProgressDialogUtils.dismiss();
                        if (response.body() != null) {
                            if (response.body().getStatus().equals("1")) {
                                CommonUtils.showToastShort(context, "Otp send successfully.");
                            } else {
                                CommonUtils.showToastShort(context, "Error");
                            }
                        } else {
                            CommonUtils.showToastShort(context, getResources().getString(R.string.no_response));
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<BaseResponse> call, @NonNull Throwable t) {
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

    public void verifyOtpForSignup(String otp) {
        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                Api api = RequestController.createService(context, Api.class);
                api.verifyOtpSignup(mobile_number, email, otp, device_token).enqueue(new Callback<LoginData>() {
                    @Override
                    public void onResponse(@NonNull Call<LoginData> call, @NonNull Response<LoginData> response) {
                        ProgressDialogUtils.dismiss();
                        if (response.body() != null) {
                            if (response.body().getStatus().equals("1")) {
                                CommonUtils.showToastShort(context, "Registration Successfull.");
                                SharedPref.saveBooleanPreferences(context, AppConstant.IS_LOGIN, true);
                                String User_Name = new StringBuilder().append(response.body().getFName()).append(" ").append(response.body().getLName()).toString();
                                System.out.println("user name :  " + User_Name);
                                SharedPref.saveStringPreference(context, AppConstant.USER_NAME, User_Name);
                                SharedPref.saveStringPreference(context, AppConstant.USER_MOBILE, response.body().getMobile());
                                SharedPref.saveStringPreference(context, AppConstant.USER_EMAIL, response.body().getEmail());
                                SharedPref.saveStringPreference(context, AppConstant.USER_ID, response.body().getUserId());
                                SharedPref.saveStringPreference(context, AppConstant.USER_REFERAL_CODE, response.body().getReferalCode());
                                ActivityController.startActivity(activity, Home.class, true, true);

                            } else {
                                CommonUtils.showToastShort(context, "Invalid OTP");
                                setErrorBackground();
                            }
                        } else {
                            CommonUtils.showToastShort(context, getResources().getString(R.string.no_response));
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<LoginData> call, @NonNull Throwable t) {
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

    private void verifyOtp(String otp) {
        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                Api api = RequestController.createService(context, Api.class);
                api.verifyOtp(input, otp).enqueue(new BaseCallback<com.drugvilla.in.service.BaseResponse>(context) {
                    @Override
                    public void onSuccess(com.drugvilla.in.service.BaseResponse response) {
                        ProgressDialogUtils.dismiss();
                        if (response != null) {
                            if (response.getStatus() == 1) {
                                CommonUtils.showToastShort(context, "Otp verified successfully.");
                                Bundle bundle = new Bundle();
                                bundle.putString(AppConstant.USER_INPUT, input);
                                ActivityController.startActivity(activity, ResetPassword.class, bundle, false);
                            } else {
                                CommonUtils.showToastShort(context, "Invalid OTP.");
                            }
                        } else {
                            CommonUtils.showToastShort(context, getResources().getString(R.string.no_response));
                        }
                    }

                    @Override
                    public void onFail(Call<com.drugvilla.in.service.BaseResponse> call, com.drugvilla.in.service.BaseResponse baseResponse) {
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
