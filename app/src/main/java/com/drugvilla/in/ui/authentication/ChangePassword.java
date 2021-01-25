package com.drugvilla.in.ui.authentication;

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
import com.drugvilla.in.databinding.ActivityChangePasswordBinding;
import com.drugvilla.in.service.Api;

import com.drugvilla.in.service.BaseCallback;
import com.drugvilla.in.service.BaseResponse;
import com.drugvilla.in.service.RequestController;
import com.drugvilla.in.utils.ActivityController;
import com.drugvilla.in.utils.AppConstant;
import com.drugvilla.in.utils.CommonUtils;
import com.drugvilla.in.utils.ProgressDialogUtils;
import com.drugvilla.in.utils.RegexUtils;
import com.drugvilla.in.utils.SharedPref;

import java.util.Objects;

import retrofit2.Call;

public class ChangePassword extends AppCompatActivity implements View.OnClickListener {
    private ActivityChangePasswordBinding binding;
    private Context context;
    private Activity activity;
    private String old_password, password, cnf_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_change_password);
        context = ChangePassword.this;
        activity = ChangePassword.this;
        Activity activity = ChangePassword.this;
        CommonUtils.changeStatusBarColor(activity);
        setToolbar();
        setListener();
    }

    private void setToolbar() {
      /*  binding.menubar.ivBack.setImageResource(R.drawable.ic_keyboard_backspace_black_24dp);
        binding.menubar.ivBack.setOnClickListener(this);
        binding.menubar.tvTitle.setVisibility(View.VISIBLE);
        binding.menubar.tvTitle.setText("Change Password");*/
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
        binding.etOldPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                binding.tvOldPassError.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private boolean isValidData() {
        old_password = Objects.requireNonNull(binding.etOldPassword.getText()).toString().trim();
        password = Objects.requireNonNull(binding.etPassword.getText()).toString().trim();
        cnf_password = Objects.requireNonNull(binding.etCnfPassword.getText()).toString().trim();
        if (old_password.isEmpty()) {
            CommonUtils.setErrorMessage(binding.etOldPassword, binding.tvOldPassError, getResources().getString(R.string.empty_old_password));
        } else if (old_password.length() < 6 || old_password.length() > 16) {
            CommonUtils.setErrorMessage(binding.etOldPassword, binding.tvOldPassError, getResources().getString(R.string.invalid_password));
        } else if (password.isEmpty()) {
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                finish();
                break;
            case R.id.btnContinue:
                if (isValidData()) {
                    setNewPassword();
                }
                break;
            default:
                break;
        }
    }

    public void setNewPassword() {
        if (CommonUtils.isOnline(context))
            try {
                ProgressDialogUtils.show(context);
                String userId = SharedPref.getStringPreferences(context, AppConstant.USER_ID);
                Api api = RequestController.createService(context, Api.class);
                api.changePassword(old_password, password, userId).enqueue(new BaseCallback<BaseResponse>(context) {
                    @Override
                    public void onSuccess(BaseResponse response) {
                        ProgressDialogUtils.dismiss();
                        if (response != null) {
                            if (response.getStatus() == 1) {
                                CommonUtils.showToastShort(context, "Password Changed Successfully.");
                                SharedPref.clearPrefs(context);
                                ActivityController.startActivity(activity, Login.class, true, true);

                            } else {
                                CommonUtils.showToastShort(context, "Incorrect old password.");
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
        else {
            CommonUtils.showToastShort(context, getResources().getString(R.string.no_internet));
        }
    }
}
