package com.drugvilla.in.ui.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.View;

import com.drugvilla.in.R;
import com.drugvilla.in.databinding.ActivitySignupBinding;
import com.drugvilla.in.model.authentication.BaseResponse;
import com.drugvilla.in.service.Api;
import com.drugvilla.in.service.RequestController;
import com.drugvilla.in.utils.ActivityController;
import com.drugvilla.in.utils.AppConstant;
import com.drugvilla.in.utils.CommonUtils;
import com.drugvilla.in.utils.ProgressDialogUtils;
import com.drugvilla.in.utils.RegexUtils;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;

public class Signup extends AppCompatActivity implements View.OnClickListener {
    private ActivitySignupBinding binding;
    private Context context;
    private int flag = 0;
    private Activity activity;
    private String first_name, last_name, email, mobile, password, cnf_password, referal_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_signup);
        context = Signup.this;
        activity = Signup.this;
        Activity activity = Signup.this;
        CommonUtils.changeStatusBarColor(activity);
        setListener();
        setToolbar();
    }

    private void setListener() {
        SpannableString spannableStr = new SpannableString("Have an account? Login");
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.RED);
        spannableStr.setSpan(foregroundColorSpan, 17, 22, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        binding.tvAlreadyUser.setText(spannableStr);
        binding.tvAlreadyUser.setOnClickListener(this);
        binding.tvReferralCode.setOnClickListener(this);
        binding.btnSignup.setOnClickListener(this);
        binding.ivBack.setOnClickListener(this);
        binding.etLname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                binding.tvfnameError.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                binding.tvfnameError.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                binding.tvPasswordError.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.etMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                binding.tvMobileError.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.etCnfPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                binding.tvCnfPasswordError.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                binding.tvEmailError.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.etReferralCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                binding.tvReferalCodeError.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    private void setToolbar() {
        //  binding.menubar.ivBack.setImageResource(R.drawable.ic_keyboard_backspace_black_24dp);
        // binding.menubar.ivBack.setOnClickListener(this);
        //  binding.menubar.tvTitle.setVisibility(View.VISIBLE);
        //   binding.menubar.tvTitle.setText("Login");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvReferralCode:
                binding.tvReferralCode.setVisibility(View.GONE);
                binding.referralCode.setVisibility(View.VISIBLE);
                //  flag = 1;
                break;
            case R.id.ivBack:
                finish();
                break;
            case R.id.tvAlreadyUser:
                ActivityController.startActivity(activity, Login.class, true);
                break;
            case R.id.btnSignup:
                if (isValidData()) {
                    getSignUpApi();
                   /* if (flag == 1) {
                        if (isValidCode()) {
                            getSignUpApi();
                        }
                    } else {
                        getSignUpApi();
                    }*/
                }
                break;
            default:
                break;
        }
    }

    private boolean isValidCode() {
        referal_code = binding.etReferralCode.getText().toString().trim();
        if (referal_code.isEmpty()) {
            CommonUtils.setErrorMessage(binding.etReferralCode, binding.tvReferalCodeError, getResources().getString(R.string.empty_referalcode));
        } else if (referal_code.length() < 6) {
            CommonUtils.setErrorMessage(binding.etReferralCode, binding.tvReferalCodeError, getResources().getString(R.string.invalid_code));
        } else {
            return true;
        }
        return false;
    }

    private void getSignUpApi() {
        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                Api api = RequestController.createService(context, Api.class);
                api.getSignup(first_name, last_name, email, mobile, password,
                        binding.etReferralCode.getText().toString()).enqueue(new Callback<BaseResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<BaseResponse> call, @NonNull retrofit2.Response<BaseResponse> response) {
                        ProgressDialogUtils.dismiss();
                        if (response.body() != null) {
                            if (response.body().getStatus().equals("1")) {
                                CommonUtils.showToastShort(context, "Otp has been sent on your mobile number.");
                                Bundle bundle = new Bundle();
                                bundle.putString(AppConstant.FROM, AppConstant.SIGNUP);
                                bundle.putString(AppConstant.USER_MOBILE, mobile);
                                bundle.putString(AppConstant.USER_EMAIL, email);
                                ActivityController.startActivity(activity, VerifyOtp.class, bundle);
                            } else {
                                CommonUtils.showToastShort(context, response.body().getMessage());
                            }
                        } else {
                            CommonUtils.showToastShort(context,getResources().getString(R.string.no_response));
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

    private boolean isValidData() {
        first_name = Objects.requireNonNull(binding.etName.getText()).toString().trim();
        last_name = Objects.requireNonNull(binding.etLname.getText()).toString().trim();
        email = Objects.requireNonNull(binding.etEmail.getText()).toString().trim();
        mobile = Objects.requireNonNull(binding.etMobile.getText()).toString().trim();
        password = Objects.requireNonNull(binding.etPassword.getText()).toString().trim();
        cnf_password = Objects.requireNonNull(binding.etCnfPassword.getText()).toString().trim();

        if (first_name.isEmpty()) {
            CommonUtils.setErrorMessage(binding.etName, binding.tvfnameError, getResources().getString(R.string.empty_name));
        } else if (first_name.length() < 2) {
            CommonUtils.setErrorMessage(binding.etName, binding.tvfnameError, getResources().getString(R.string.invalid_name));
        } else if (last_name.isEmpty()) {
            CommonUtils.setErrorMessage(binding.etLname, binding.tvlnameError, getResources().getString(R.string.empty_name));
        } else if (last_name.length() < 2) {
            CommonUtils.setErrorMessage(binding.etLname, binding.tvlnameError, getResources().getString(R.string.invalid_name));
        } else if (email.isEmpty()) {
            CommonUtils.setErrorMessage(binding.etEmail, binding.tvEmailError, getResources().getString(R.string.empty_email));
        } else if (!RegexUtils.isValidEmail(email)) {
            CommonUtils.setErrorMessage(binding.etEmail, binding.tvEmailError, getResources().getString(R.string.invalid_email));
        } else if (mobile.isEmpty()) {
            CommonUtils.setErrorMessage(binding.etMobile, binding.tvMobileError, getResources().getString(R.string.empty_mobile));
        } else if (!RegexUtils.isValidMobileNumber(mobile)) {
            CommonUtils.setErrorMessage(binding.etMobile, binding.tvMobileError, getResources().getString(R.string.invalid_mobile));
        } else if (password.isEmpty()) {
            CommonUtils.setErrorMessage(binding.etPassword, binding.tvPasswordError, getResources().getString(R.string.empty_password));
        } else if (password.length() < 6 || password.length() > 16) {
            CommonUtils.setErrorMessage(binding.etPassword, binding.tvPasswordError, getResources().getString(R.string.invalid_password));
        } else if (!RegexUtils.isPasswordMatch(password, cnf_password)) {
            CommonUtils.setErrorMessage(binding.etCnfPassword, binding.tvCnfPasswordError, getResources().getString(R.string.password_not_match));
        } else {
            return true;
        }
        return false;
    }
}
