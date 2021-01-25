package com.drugvilla.in.ui.address;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.drugvilla.in.R;
import com.drugvilla.in.databinding.ActivityAddAddressBinding;
import com.drugvilla.in.model.address.AddressBaseResponse;
import com.drugvilla.in.model.address.CheckPincodeBaseResponse;
import com.drugvilla.in.service.Api;
import com.drugvilla.in.service.BaseCallback;
import com.drugvilla.in.service.BaseResponse;
import com.drugvilla.in.service.RequestController;
import com.drugvilla.in.service.request.AddressRequest;
import com.drugvilla.in.utils.AppConstant;
import com.drugvilla.in.utils.CommonUtils;
import com.drugvilla.in.utils.ProgressDialogUtils;
import com.drugvilla.in.utils.RegexUtils;
import com.drugvilla.in.utils.SharedPref;

import java.util.Objects;

import retrofit2.Call;

public class AddAddress extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    private ActivityAddAddressBinding binding;
    private int flag = 0;
    private boolean is_default;
    private String Selected_Address_Type, name, pincode, mobile, address, landmark, city, state, Address_ID, Address_IsDefault, other_address_name;
    private String from = " ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_address);
        context = AddAddress.this;
        getData();
        setData();
        setToolbar();
        setViews();

    }

    private void setData() {
        if (from.equalsIgnoreCase(AppConstant.TYPE_EDIT)) {
            binding.etCity.setText(city);
            binding.etAddress1.setText(address);
            binding.etState.setText(state);
            binding.etName.setText(name);
            binding.etAddress2.setText(landmark);
            binding.etPincode.setText(pincode);
            binding.etMobile.setText(mobile);
            if (Selected_Address_Type.equalsIgnoreCase("Work")) {
                Selected_Address_Type = setselectedBackground(binding.tvWork, binding.llWork);
                flag = 2;
            } else if (Selected_Address_Type.equalsIgnoreCase("Home")) {
                Selected_Address_Type = setselectedBackground(binding.tvHome, binding.llHome);
                flag = 1;
            } else {
                Selected_Address_Type = setselectedBackground(binding.tvOther, binding.llOthers);
                flag = 3;
                binding.otherAddressName.setVisibility(View.VISIBLE);
                binding.etOtherAddName.setText(other_address_name);
            }
            if (Address_IsDefault.equalsIgnoreCase("1")) {
                binding.cbDefaultAddress.setChecked(true);
                is_default = true;
            } else {
                binding.cbDefaultAddress.setChecked(false);
                is_default = false;
            }
            binding.btnSubmit.setText(getResources().getString(R.string.update_address));
        }
    }

    private void getData() {
        Bundle bundle = new Bundle();
        bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.getString(AppConstant.FROM) != null && !Objects.requireNonNull(bundle.getString(AppConstant.FROM)).isEmpty()) {
                from = bundle.getString(AppConstant.FROM);
            }
            if (bundle.getString("Address_Type") != null && !Objects.requireNonNull(bundle.getString("Address_Type")).isEmpty()) {
                Selected_Address_Type = bundle.getString("Address_Type");
            }
            if (bundle.getString("Address_Name") != null && !Objects.requireNonNull(bundle.getString("Address_Name")).isEmpty()) {
                name = bundle.getString("Address_Name");
            }
            if (bundle.getString("Address_Mobile") != null && !Objects.requireNonNull(bundle.getString("Address_Mobile")).isEmpty()) {
                mobile = bundle.getString("Address_Mobile");
            }
            if (bundle.getString("Address_Line1") != null && !Objects.requireNonNull(bundle.getString("Address_Line1")).isEmpty()) {
                address = bundle.getString("Address_Line1");
            }
            if (bundle.getString("Address_Line2") != null && !Objects.requireNonNull(bundle.getString("Address_Line2")).isEmpty()) {
                landmark = bundle.getString("Address_Line2");
            }
            if (bundle.getString("Address_City") != null && !Objects.requireNonNull(bundle.getString("Address_City")).isEmpty()) {
                city = bundle.getString("Address_City");
            }
            if (bundle.getString("Address_State") != null && !Objects.requireNonNull(bundle.getString("Address_State")).isEmpty()) {
                state = bundle.getString("Address_State");
            }
            if (bundle.getString("Address_Pincode") != null && !Objects.requireNonNull(bundle.getString("Address_Pincode")).isEmpty()) {
                pincode = bundle.getString("Address_Pincode");
            }
            if (bundle.getString("Address_ID") != null && !Objects.requireNonNull(bundle.getString("Address_ID")).isEmpty()) {
                Address_ID = bundle.getString("Address_ID");
            }

            if (bundle.getString("Address_IsDefault") != null && !Objects.requireNonNull(bundle.getString("Address_IsDefault")).isEmpty()) {
                Address_IsDefault = bundle.getString("Address_IsDefault");
            }
            if (bundle.getString("Other_Address_Name") != null && !Objects.requireNonNull(bundle.getString("Other_Address_Name")).isEmpty()) {
                other_address_name = bundle.getString("Other_Address_Name");
            }
        }

    }

    private void setViews() {
        binding.btnSubmit.setOnClickListener(this);
        binding.llHome.setOnClickListener(this);
        binding.llOthers.setOnClickListener(this);
        binding.llWork.setOnClickListener(this);
        binding.etAddress1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.tvAddress1Error.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        binding.etAddress2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.tvAddress2Error.setVisibility(View.GONE);
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
        binding.etOtherAddName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.tvOtherAddError.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        binding.etPincode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.tvPincodeError.setVisibility(View.GONE);
                if (s.length() == 6) {
                    checkPincode();
                }
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
        binding.etCity.setEnabled(false);
        binding.etState.setEnabled(false);
        binding.cbDefaultAddress.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                is_default = isChecked;
            }
        });
    }

    private void setToolbar() {
        binding.menubar.tvTitle.setVisibility(View.VISIBLE);
        if (from.equalsIgnoreCase(AppConstant.TYPE_EDIT)) {
            binding.menubar.tvTitle.setText(R.string.update_address);
        } else {
            binding.menubar.tvTitle.setText(R.string.add_address);
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
                    System.out.println("selected address type : " + Selected_Address_Type);
                    System.out.println("is default address : " + is_default);
                    if (from.equalsIgnoreCase(AppConstant.TYPE_EDIT)) {
                        updateAddress();
                    } else {
                        saveAddress();
                    }
                }
                break;
            case R.id.llHome:
                flag = 1;
                Selected_Address_Type = setselectedBackground(binding.tvHome, binding.llHome);
                setUnselectedBackground(binding.tvOther, binding.tvWork, binding.llOthers, binding.llWork);
                binding.otherAddressName.setVisibility(View.GONE);
                break;
            case R.id.llOthers:
                flag = 3;
                Selected_Address_Type = setselectedBackground(binding.tvOther, binding.llOthers);
                setUnselectedBackground(binding.tvHome, binding.tvWork, binding.llHome, binding.llWork);
                binding.otherAddressName.setVisibility(View.VISIBLE);
                break;
            case R.id.llWork:
                flag = 2;
                Selected_Address_Type = setselectedBackground(binding.tvWork, binding.llWork);
                setUnselectedBackground(binding.tvOther, binding.tvHome, binding.llOthers, binding.llHome);
                binding.otherAddressName.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }


    private void setUnselectedBackground(TextView textView1, TextView textView2, LinearLayout linearLayout1, LinearLayout linearLayout2) {
        linearLayout1.setBackground(getResources().getDrawable(R.drawable.back_white));
        textView1.setTextColor(getResources().getColor(R.color.lightGray));
        linearLayout2.setBackground(getResources().getDrawable(R.drawable.back_white));
        textView2.setTextColor(getResources().getColor(R.color.lightGray));

    }

    private String setselectedBackground(TextView textView, LinearLayout linearLayout) {
        linearLayout.setBackground(getResources().getDrawable(R.drawable.button_red));
        textView.setTextColor(getResources().getColor(R.color.colorWhite));
        return textView.getText().toString();

    }

    private boolean isValid() {
        name = Objects.requireNonNull(binding.etName.getText()).toString().trim();
        pincode = Objects.requireNonNull(binding.etPincode.getText()).toString().trim();
        city = Objects.requireNonNull(binding.etCity.getText()).toString().trim();
        state = Objects.requireNonNull(binding.etState.getText()).toString().trim();
        mobile = Objects.requireNonNull(binding.etMobile.getText()).toString().trim();
        address = Objects.requireNonNull(binding.etAddress1.getText()).toString().trim();
        landmark = Objects.requireNonNull(binding.etAddress2.getText()).toString().trim();
        other_address_name = Objects.requireNonNull(binding.etOtherAddName.getText()).toString().trim();
        if (name.isEmpty()) {
            CommonUtils.setErrorMessage(binding.etName, binding.tvNameError, getResources().getString(R.string.empty_name));
        } else if (name.length() < 2) {
            CommonUtils.setErrorMessage(binding.etName, binding.tvNameError, getResources().getString(R.string.invalid_name));
        } else if (mobile.isEmpty()) {
            CommonUtils.setErrorMessage(binding.etMobile, binding.tvMobileError, getResources().getString(R.string.empty_mobile));
        } else if (!RegexUtils.isValidMobileNumber(mobile)) {
            CommonUtils.setErrorMessage(binding.etMobile, binding.tvMobileError, getResources().getString(R.string.invalid_mobile));
        } else if (pincode.isEmpty()) {
            CommonUtils.setErrorMessage(binding.etPincode, binding.tvPincodeError, getResources().getString(R.string.empty_pincode));
        } else if (pincode.length() < 6) {
            CommonUtils.setErrorMessage(binding.etPincode, binding.tvPincodeError, getResources().getString(R.string.invalid_pincode));
        } else if (address.isEmpty()) {
            CommonUtils.setErrorMessage(binding.etAddress1, binding.tvAddress1Error, getResources().getString(R.string.empty_address));
        } else if (address.length() < 2) {
            CommonUtils.setErrorMessage(binding.etAddress1, binding.tvAddress1Error, getResources().getString(R.string.invalid_address));
        } else if (landmark.isEmpty()) {
            CommonUtils.setErrorMessage(binding.etAddress2, binding.tvAddress2Error, getResources().getString(R.string.empty_landmark));
        } else if (landmark.length() < 2) {
            CommonUtils.setErrorMessage(binding.etAddress2, binding.tvAddress2Error, getResources().getString(R.string.invalid_landmark));
        } else if (flag == 0) {
            CommonUtils.setSnackbar(binding.coordinatorLayout, "Please select address type.");
        } else if (flag == 3 && other_address_name.isEmpty()) {
            CommonUtils.setErrorMessage(binding.etOtherAddName, binding.tvOtherAddError, getResources().getString(R.string.empty_address));
        } else if (flag == 3 && other_address_name.length() < 3) {
            CommonUtils.setErrorMessage(binding.etOtherAddName, binding.tvOtherAddError, getResources().getString(R.string.invalid_address));
        } else {
            return true;
        }
        return false;
    }


    private void updateAddress() {
        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                Api api = RequestController.createService(context, Api.class);
                api.updateAddress(SharedPref.getStringPreferences(context, AppConstant.USER_ID), Address_ID,
                        name, mobile, pincode, state, city, address, landmark, Selected_Address_Type, other_address_name, is_default).enqueue
                        (new BaseCallback<com.drugvilla.in.service.BaseResponse>(context) {
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
                            public void onFail(Call<com.drugvilla.in.service.BaseResponse> call, BaseResponse baseResponse) {
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

    private void checkPincode() {
        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                Api api = RequestController.createService(context, Api.class);
                api.checkPincode(pincode).enqueue(new BaseCallback<CheckPincodeBaseResponse>(context) {
                    @Override
                    public void onSuccess(CheckPincodeBaseResponse response) {
                        ProgressDialogUtils.dismiss();
                        if (response != null) {
                            if (response.getStatus() == 1) {
                                CommonUtils.showToastShort(context, response.getMessage());
                                binding.etState.setText(response.getData().getState());
                                binding.etCity.setText(response.getData().getCity());
                            } else {
                                CommonUtils.showToastShort(context, response.getMessage());
                            }
                        } else {
                            CommonUtils.showToastShort(context, getResources().getString(R.string.no_response));
                        }
                    }

                    @Override
                    public void onFail(Call<CheckPincodeBaseResponse> call, BaseResponse baseResponse) {
                        ProgressDialogUtils.dismiss();
                        CommonUtils.showToastShort(context, getResources().getString(R.string.failure));
                        // TODO : remove
                        binding.etState.setText("Delhi");
                        binding.etCity.setText("Delhi");
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            CommonUtils.showToastShort(context, getResources().getString(R.string.no_internet));
        }
    }

    private void saveAddress() {
        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                Api api = RequestController.createService(context, Api.class);
                api.addAddress(SharedPref.getStringPreferences(context, AppConstant.USER_ID),
                        name, mobile, pincode, state, city, address, landmark, Selected_Address_Type, other_address_name, is_default).enqueue
                        (new BaseCallback<com.drugvilla.in.service.BaseResponse>(context) {
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
                            public void onFail(Call<com.drugvilla.in.service.BaseResponse> call, BaseResponse baseResponse) {
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
