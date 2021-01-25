package com.drugvilla.in.ui.others;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.drugvilla.in.R;
import com.drugvilla.in.databinding.ActivityContactUsBinding;
import com.drugvilla.in.service.Api;
import com.drugvilla.in.service.BaseCallback;
import com.drugvilla.in.service.BaseResponse;
import com.drugvilla.in.service.RequestController;
import com.drugvilla.in.utils.CommonUtils;
import com.drugvilla.in.utils.ProgressDialogUtils;
import com.drugvilla.in.utils.RegexUtils;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;

public class ContactUs extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    private String name, email, message, mobile;
    private ActivityContactUsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_contact_us);
        context = ContactUs.this;
        setToolbar();
        setListeners();

    }

    private void setListeners() {

        binding.btnContinue.setOnClickListener(this);
        binding.etMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                binding.tvMessageError.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.etName.addTextChangedListener(new TextWatcher() {
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

        binding.ivFb.setOnClickListener(this);
        binding.ivTwitter.setOnClickListener(this);
        binding.ivYoutube.setOnClickListener(this);
        binding.ivLinkedin.setOnClickListener(this);
    }

    private void setToolbar() {
        binding.menuBar.tvTitle.setVisibility(View.VISIBLE);
        binding.menuBar.tvTitle.setText(R.string.contact_us);
        binding.menuBar.ivBack.setImageResource(R.drawable.ic_keyboard_backspace_black_24dp);
        binding.menuBar.ivBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnContinue:
                if (isValid()) {
                    sendQuery();
                }
                break;
            case R.id.ivBack:
                finish();
                break;
            case R.id.ivFb:
                String urlFacebook = "https://www.facebook.com/drugvilla/";
                setUri(urlFacebook);
                break;
            case R.id.ivLinkedin:
                String urlLinkedin = "https://www.linkedin.com/company/drugvilla";
                setUri(urlLinkedin);
                break;
            case R.id.ivTwitter:
                String urlTwitter = "https://twitter.com/drugvilla";
                setUri(urlTwitter);
                break;
            case R.id.ivYoutube:
                String urlYoutube = "https://www.youtube.com/channel/UCI_nxeJBFDU6mPa_vIqyMaQ";
                setUri(urlYoutube);
                break;
            default:
                break;
        }
    }

    private void sendQuery() {
        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                Api api = RequestController.createService(context, Api.class);
                api.postQuery(mobile, email, name, message).enqueue(new BaseCallback<BaseResponse>(context) {
                    @Override
                    public void onSuccess(BaseResponse response) {
                        ProgressDialogUtils.dismiss();
                        if (response != null) {
                            if (response.getStatus() == 1) {
                                CommonUtils.showToastShort(context, response.getMessage());
                                binding.etEmail.getText().clear();
                                binding.etMessage.getText().clear();
                                binding.etMobile.getText().clear();
                                binding.etName.getText().clear();
                            } else {
                                CommonUtils.showToastShort(context, "Something went wrong.");
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

    private void setUri(String url) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    private boolean isValid() {
        name = Objects.requireNonNull(binding.etName.getText()).toString().trim();
        message = Objects.requireNonNull(binding.etMessage.getText()).toString().trim();
        email = Objects.requireNonNull(binding.etEmail.getText()).toString().trim();
        mobile = Objects.requireNonNull(binding.etMobile.getText()).toString().trim();
        if (name.isEmpty()) {
            CommonUtils.setErrorMessage(binding.etName, binding.tvNameError, getResources().getString(R.string.empty_name));
        } else if (name.length() < 2) {
            CommonUtils.setErrorMessage(binding.etName, binding.tvNameError, getResources().getString(R.string.invalid_name));
        } else if (email.isEmpty()) {
            CommonUtils.setErrorMessage(binding.etEmail, binding.tvEmailError, getResources().getString(R.string.empty_email));
        } else if (!RegexUtils.isValidEmail(email)) {
            CommonUtils.setErrorMessage(binding.etEmail, binding.tvEmailError, getResources().getString(R.string.invalid_email));
        } else if (mobile.isEmpty()) {
            CommonUtils.setErrorMessage(binding.etMobile, binding.tvMobileError, getResources().getString(R.string.empty_mobile));
        } else if (!RegexUtils.isValidMobileNumber(mobile)) {
            CommonUtils.setErrorMessage(binding.etMobile, binding.tvMobileError, getResources().getString(R.string.invalid_mobile));
        } else if (message.isEmpty()) {
            CommonUtils.setErrorMessage(binding.etMessage, binding.tvMessageError, getResources().getString(R.string.empty_message));
        } else if (message.length() < 5) {
            CommonUtils.setErrorMessage(binding.etMessage, binding.tvMessageError, getResources().getString(R.string.invalid_message));
        } else {
            return true;
        }
        return false;
    }
}
