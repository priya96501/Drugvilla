package com.drugvilla.in.ui.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.drugvilla.in.R;
import com.drugvilla.in.databinding.ActivityProfileBinding;
import com.drugvilla.in.databinding.EditProfileOptionsBinding;
import com.drugvilla.in.model.authentication.BaseResponse;
import com.drugvilla.in.service.Api;
import com.drugvilla.in.service.RequestController;
import com.drugvilla.in.ui.address.SelectAddress;
import com.drugvilla.in.ui.authentication.ChangePassword;
import com.drugvilla.in.ui.authentication.VerifyOtp;
import com.drugvilla.in.ui.offer.Offer;
import com.drugvilla.in.ui.order.MyOrder;
import com.drugvilla.in.ui.patient.SelectPatient;
import com.drugvilla.in.ui.prescription.MyPrescription;
import com.drugvilla.in.ui.reminders.Reminders;
import com.drugvilla.in.utils.ActivityController;
import com.drugvilla.in.utils.AppConstant;
import com.drugvilla.in.utils.CommonUtils;
import com.drugvilla.in.utils.DialogUtils;
import com.drugvilla.in.utils.ProgressDialogUtils;
import com.drugvilla.in.utils.RegexUtils;
import com.drugvilla.in.utils.SharedPref;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Profile extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    private Activity activity;
    private ActivityProfileBinding binding;
    int flag_email = 0;
    int flag_mobile = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile);
        context = Profile.this;
        activity = Profile.this;
        setToolbar();
        setListener();
        setData();
    }

    private void setData() {
        binding.tvEmail.setText(SharedPref.getStringPreferences(context, AppConstant.USER_EMAIL));
        binding.tvMobile.setText(SharedPref.getStringPreferences(context, AppConstant.USER_MOBILE));
        binding.tvName.setText(SharedPref.getStringPreferences(context, AppConstant.USER_NAME));
        String first = binding.tvName.getText().toString().substring(0, 1);
        binding.tvUser.setText(first);
        CommonUtils.getRandomColors(binding.tvUser);
    }

    private void setToolbar() {
        binding.menubar.tvTitle.setVisibility(View.VISIBLE);
        binding.menubar.tvTitle.setText(R.string.profile);
        binding.menubar.ivBack.setImageResource(R.drawable.ic_keyboard_backspace_black_24dp);
        binding.menubar.ivBack.setOnClickListener(this);
    }

    private void setListener() {
        binding.llAddress.setOnClickListener(this);
        binding.llPatient.setOnClickListener(this);
        binding.llChangePassword.setOnClickListener(this);
        binding.llCoins.setOnClickListener(this);
        binding.llPrescription.setOnClickListener(this);
        binding.llWhishlist.setOnClickListener(this);
        binding.tvEdit.setOnClickListener(this);
        binding.llReferEarn.setOnClickListener(this);
        binding.llReminders.setOnClickListener(this);
        binding.llOrders.setOnClickListener(this);
        binding.llOffers.setOnClickListener(this);
        binding.llMyAppointments.setOnClickListener(this);
        binding.llMyLabTest.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                finish();
                break;
            case R.id.tvEdit:
                showBottomSheetDialog();
                break;
            case R.id.llAddress:
                ActivityController.startActivity(context, SelectAddress.class, AppConstant.PROFILE);
                break;
            case R.id.llPatient:
                ActivityController.startActivity(context, SelectPatient.class, AppConstant.PROFILE);
                break;
            case R.id.llPrescription:
                ActivityController.startActivity(context, MyPrescription.class, AppConstant.PROFILE);
                break;
            case R.id.llOffers:
                ActivityController.startActivity(context, Offer.class);
                break;
            case R.id.llWhishlist:
                ActivityController.startActivity(context, Wishlist.class);
                break;
            case R.id.llReferEarn:
                ActivityController.startActivity(context, RefferEarn.class);
                break;
            case R.id.llReminders:
                ActivityController.startActivity(context, Reminders.class);
                break;
            case R.id.llChangePassword:
                ActivityController.startActivity(context, ChangePassword.class);
                break;
            case R.id.llCoins:
                ActivityController.startActivity(context, Wallet.class);
                break;
            case R.id.llMyAppointments:
                ActivityController.startActivity(context, MyOrder.class, AppConstant.MY_APPOINTMENT);
                break;
            case R.id.llMyLabTest:
                ActivityController.startActivity(context, MyOrder.class, AppConstant.MY_LAB_TESTS);
                break;
            case R.id.llOrders:
                ActivityController.startActivity(context, MyOrder.class, AppConstant.MY_ORDERS);
                break;
            default:
                break;
        }
    }


    public void showBottomSheetDialog() {

        final EditProfileOptionsBinding editBinding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.edit_profile_options, null, false);
        final BottomSheetDialog dialog = DialogUtils.createBottomDialog(context, editBinding.getRoot());
        dialog.setCancelable(false);
        editBinding.ivCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag_email = 0;
                flag_mobile = 0;
                dialog.dismiss();
            }
        });
        editBinding.etEmail.setText(SharedPref.getStringPreferences(context, AppConstant.USER_EMAIL));
        editBinding.etMobile.setText(SharedPref.getStringPreferences(context, AppConstant.USER_MOBILE));
        editBinding.etEmail.setEnabled(false);
        editBinding.etMobile.setEnabled(false);
        setTextWatcher(editBinding.etEmail, editBinding.etMobile, editBinding.tvEmailError, editBinding.tvMobileError);
        editBinding.tvEditEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag_email == 0) {
                    editBinding.etEmail.setEnabled(true);
                    editBinding.etEmail.setFocusable(true);
                    editBinding.etEmail.getText().clear();
                    editBinding.tvEditEmail.setText("Verify");
                    flag_email = 1;
                } else {
                    if (isValidEmail(editBinding.etEmail, editBinding.tvEmailError)) {
                        String input = editBinding.etEmail.getText().toString().trim();
                        //  sent to verify otp screen to verify otp for verifying new email
                        getOtpToUpdate(input);
                        dialog.dismiss();
                        flag_email = 0;
                    }

                }
            }
        });
        editBinding.tvEditMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag_mobile == 0) {
                    editBinding.etMobile.setEnabled(true);
                    editBinding.etMobile.setFocusable(true);
                    editBinding.etMobile.getText().clear();
                    editBinding.tvEditMobile.setText("Verify");
                    flag_mobile = 1;
                } else {
                    if (isValidMobile(editBinding.etMobile, editBinding.tvMobileError)) {
                        String input = editBinding.etMobile.getText().toString().trim();
                        //sent to verify otp screen to verify otp for verifying new mobile
                        getOtpToUpdate(input);
                        dialog.dismiss();
                        flag_mobile = 0;
                    }

                }

            }

        });
    }

    private void getOtpToUpdate(final String input) {
        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                Api api = RequestController.createService(context, Api.class);
                api.getOtpUpdate(SharedPref.getStringPreferences(context, AppConstant.USER_ID), input).enqueue(new Callback<BaseResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<BaseResponse> call, @NonNull Response<BaseResponse> response) {
                        ProgressDialogUtils.dismiss();
                        if (response.body() != null) {
                            if (response.body().getStatus().equals("1")) {
                                CommonUtils.showToastShort(context, "Otp send successfully.");
                                Bundle bundle = new Bundle();
                                bundle.putString(AppConstant.FROM, AppConstant.PROFILE);
                                bundle.putString(AppConstant.USER_INPUT, input);
                                ActivityController.startActivity(activity, VerifyOtp.class, bundle, false);

                            } else {
                                CommonUtils.showToastShort(context, "User not registered.");
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

    private void setTextWatcher(EditText etEmail, EditText etMobile, final TextView tvEmailError, final TextView tvMobileError) {
        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvEmailError.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvMobileError.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private boolean isValidEmail(EditText etEmail, TextView tvEmailError) {
        if (etEmail.getText().toString().isEmpty()) {
            CommonUtils.setErrorMessage(etEmail, tvEmailError, getResources().getString(R.string.empty_email));
        } else if (!RegexUtils.isValidEmail(etEmail.getText().toString().trim())) {
            CommonUtils.setErrorMessage(etEmail, tvEmailError, getResources().getString(R.string.invalid_email));
        } else {
            return true;
        }
        return false;
    }

    private boolean isValidMobile(EditText etMobile, TextView tvMobileError) {
        if (etMobile.getText().toString().isEmpty()) {
            CommonUtils.setErrorMessage(etMobile, tvMobileError, getResources().getString(R.string.empty_mobile));
        } else if (!RegexUtils.isValidMobileNumber(etMobile.getText().toString().trim())) {
            CommonUtils.setErrorMessage(etMobile, tvMobileError, getResources().getString(R.string.invalid_mobile));
        } else {
            return true;
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        setData();
    }
}
