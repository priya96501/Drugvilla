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

import com.drugvilla.in.R;
import com.drugvilla.in.databinding.ActivityForgetPasswordBinding;
import com.drugvilla.in.model.authentication.BaseResponse;
import com.drugvilla.in.model.authentication.LoginData;
import com.drugvilla.in.service.Api;
import com.drugvilla.in.service.RequestController;
import com.drugvilla.in.utils.ActivityController;
import com.drugvilla.in.utils.AppConstant;
import com.drugvilla.in.utils.CommonUtils;
import com.drugvilla.in.utils.ProgressDialogUtils;
import com.drugvilla.in.utils.RegexUtils;

import java.util.HashMap;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgetPassword extends AppCompatActivity implements View.OnClickListener {
    private ActivityForgetPasswordBinding binding;
    private Context context;
    private Activity activity;
    HashMap<String, String> map = new HashMap<>();
    private String input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_forget_password);
        context = ForgetPassword.this;
        activity = ForgetPassword.this;
        Activity activity = ForgetPassword.this;
        CommonUtils.changeStatusBarColor(activity);
        setToolbar();
        setListener();
    }

    private void setToolbar() {
        //  binding.menubar.ivBack.setImageResource(R.drawable.ic_keyboard_backspace_black_24dp);
        //  binding.menubar.ivBack.setOnClickListener(this);
        //  binding.menubar.tvTitle.setVisibility(View.VISIBLE);
        // binding.menubar.tvTitle.setText("/*Forgot Password*/");
    }

    private void setListener() {
        binding.ivBack.setOnClickListener(this);
        binding.btnContinue.setOnClickListener(this);
        binding.etEmailMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                binding.tvInputError.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    private boolean checkEmailMobile() {
        input = Objects.requireNonNull(binding.etEmailMobile.getText()).toString().trim();
        if (input.isEmpty()) {
            CommonUtils.setErrorMessage(binding.etEmailMobile, binding.tvInputError, getResources().getString(R.string.empty_email_mobile));
            return false;
        }
        if (input.contains("@")) {
            if (!RegexUtils.isValidEmail(input)) {
                CommonUtils.setErrorMessage(binding.etEmailMobile, binding.tvInputError, getResources().getString(R.string.invalid_email));
                return false;
            }
        } else {
            if (!RegexUtils.isValidMobileNumber(input)) {
                CommonUtils.setErrorMessage(binding.etEmailMobile, binding.tvInputError, getResources().getString(R.string.invalid_mobile));
                return false;
            }
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                finish();
                break;
            case R.id.btnContinue:
                if (checkEmailMobile()) {
                    //send otp on email or mobile api
                    getOtpOnEmailMobile();
                }
                break;
            default:
                break;
        }
    }


    private void getOtpOnEmailMobile() {

        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                Api api = RequestController.createService(context, Api.class);
                api.getOtp(input).enqueue(new Callback<BaseResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<BaseResponse> call, @NonNull Response<BaseResponse> response) {
                        ProgressDialogUtils.dismiss();
                        if (response.body() != null) {
                            if (response.body().getStatus().equals("1")) {
                                CommonUtils.showToastShort(context, "OTP send Successfully.");
                                Bundle bundle = new Bundle();
                                bundle.putString(AppConstant.FROM, AppConstant.FORGET_PASSWORD);
                                bundle.putString(AppConstant.USER_INPUT, binding.etEmailMobile.getText().toString());
                                ActivityController.startActivity(activity, VerifyOtp.class, bundle, false);

                            } else {
                                CommonUtils.showToastShort(context, "User not registered..");
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
}
