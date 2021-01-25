package com.drugvilla.in.ui.authentication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.drugvilla.in.R;
import com.drugvilla.in.databinding.ActivityResetPasswordBinding;
import com.drugvilla.in.service.Api;
import com.drugvilla.in.service.BaseCallback;
import com.drugvilla.in.service.BaseResponse;
import com.drugvilla.in.service.RequestController;
import com.drugvilla.in.utils.ActivityController;
import com.drugvilla.in.utils.AppConstant;
import com.drugvilla.in.utils.CommonUtils;
import com.drugvilla.in.utils.ProgressDialogUtils;
import com.drugvilla.in.utils.RegexUtils;

import java.util.HashMap;
import java.util.Objects;

import retrofit2.Call;

public class ResetPassword extends AppCompatActivity implements View.OnClickListener {
    private ActivityResetPasswordBinding binding;
    private Context context;
    private Activity activity;
    private String input = "";
    private String password,cnf_password;
    Bundle bundle = new Bundle();
    HashMap<String, String> map = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_reset_password);
        context = ResetPassword.this;
        activity = ResetPassword.this;
        CommonUtils.changeStatusBarColor(activity);
        getData();
        setToolbar();
        setListener();
    }

    private void getData() {
        bundle = getIntent().getExtras();

        if (bundle != null) {
            if (getIntent().getStringExtra(AppConstant.USER_INPUT) != null) {
                input = getIntent().getStringExtra(AppConstant.USER_INPUT);
            }
        }
    }

    private void setListener() {
        binding.btnContinue.setOnClickListener(this);
        binding.ivBack.setOnClickListener(this);
        binding.etCnfPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                binding.tvCnfPassError.setVisibility(View.GONE);
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
                binding.tvNewPassError.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void setToolbar() {
      /*  binding.menubar.ivBack.setImageResource(R.drawable.ic_keyboard_backspace_black_24dp);
        binding.menubar.ivBack.setOnClickListener(this);
        binding.menubar.tvTitle.setVisibility(View.VISIBLE);
        binding.menubar.tvTitle.setText("Reset Password");*/
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                finish();
                break;
            case R.id.btnContinue:
                if (isValidData()) {
                    resetPassword();
                }
                break;
            default:
                break;
        }
    }

    private boolean isValidData() {
         password = Objects.requireNonNull(binding.etPassword.getText()).toString().trim();
         cnf_password = Objects.requireNonNull(binding.etCnfPassword.getText()).toString().trim();
        if (password.isEmpty()) {
            CommonUtils.setErrorMessage(binding.etPassword, binding.tvNewPassError, getResources().getString(R.string.empty_new_password));
        } else if (password.length() < 6 || password.length() > 16) {
            CommonUtils.setErrorMessage(binding.etPassword, binding.tvNewPassError, getResources().getString(R.string.invalid_password));
        } else if (!RegexUtils.isPasswordMatch(password, cnf_password)) {
            CommonUtils.setErrorMessage(binding.etCnfPassword, binding.tvCnfPassError, getResources().getString(R.string.password_not_match));
        } else {
            return true;
        }
        return false;
    }

    private void resetPassword() {
        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                Api api = RequestController.createService(context, Api.class);
                api.resetPassword(input,password).enqueue(new BaseCallback<BaseResponse>(context) {
                    @Override
                    public void onSuccess(BaseResponse response) {
                        ProgressDialogUtils.dismiss();
                        if (response != null) {
                            if (response.getStatus() == 1) {
                                Toast.makeText(context, response.getMessage(), Toast.LENGTH_SHORT).show();
                                ActivityController.startActivity(activity, Login.class, true, true);
                            } else {
                                Toast.makeText(context, "Something went wrong.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, getResources().getString(R.string.no_response), Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFail(Call<BaseResponse> call, BaseResponse baseResponse) {
                        ProgressDialogUtils.dismiss();
                        Toast.makeText(context, getResources().getString(R.string.failure), Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(context, getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }
    }
}
