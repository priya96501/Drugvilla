package com.drugvilla.in.ui.address;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.drugvilla.in.R;
import com.drugvilla.in.adapter.address.SelectAddressAdapter;
import com.drugvilla.in.databinding.ActivitySelectAddressBinding;
import com.drugvilla.in.listener.OnCheckSelectedListener;
import com.drugvilla.in.listener.OnSelectedListener;
import com.drugvilla.in.model.address.AddressBaseResponse;
import com.drugvilla.in.model.address.AddressData;
import com.drugvilla.in.model.order.saveOrder.SaveOrderResponse;
import com.drugvilla.in.service.Api;
import com.drugvilla.in.service.BaseCallback;
import com.drugvilla.in.service.BaseResponse;
import com.drugvilla.in.service.RequestController;
import com.drugvilla.in.ui.labs.ScheduleDetail;
import com.drugvilla.in.ui.order.OrderConfirmation;
import com.drugvilla.in.ui.order.OrderReview;
import com.drugvilla.in.utils.ActivityController;
import com.drugvilla.in.utils.AppConstant;
import com.drugvilla.in.utils.CommonUtils;
import com.drugvilla.in.utils.ProgressDialogUtils;
import com.drugvilla.in.utils.SharedPref;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectAddress extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    private Activity activity;
    private ActivitySelectAddressBinding binding;
    private String from = " ", selectedAddressID;
    private SelectAddressAdapter addressAdapter;
    private ArrayList<AddressData> addressData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_select_address);
        context = SelectAddress.this;
        activity = SelectAddress.this;
        getData();
        setToolbar();

        binding.swipeRefreshingLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAllAddrestApi();
            }
        });
    }

    @Override
    protected void onResume() {
        getAllAddrestApi();
        setAdapter();
        super.onResume();
    }

    private void setEmptyLayout(boolean value) {
        if (value) {
            binding.llData.setVisibility(View.GONE);
            binding.emptyLayout.llRoot.setVisibility(View.VISIBLE);
            binding.emptyLayout.ivImage.setImageResource(R.drawable.ic_add_address);
            binding.emptyLayout.btnSubmit.setText(getResources().getString(R.string.add_address));
            binding.emptyLayout.tvHeading.setText(getResources().getString(R.string.no_address));
            binding.emptyLayout.btnSubmit.setOnClickListener(this);
        } else {
            binding.emptyLayout.llRoot.setVisibility(View.GONE);
            if (from.equalsIgnoreCase(AppConstant.PROFILE)) {
                binding.btnSelectAddress.setVisibility(View.GONE);
            } else {
                binding.btnSelectAddress.setVisibility(View.VISIBLE);
            }
            binding.llData.setVisibility(View.VISIBLE);
        }
    }

    private void getData() {
        Bundle bundle = new Bundle();
        bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.getString(AppConstant.FROM) != null && !Objects.requireNonNull(bundle.getString(AppConstant.FROM)).isEmpty()) {
                from = bundle.getString(AppConstant.FROM);
            }
        }
    }

    private void setAdapter() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
        binding.rvAddress.setLayoutManager(linearLayoutManager);
        binding.rvAddress.hasFixedSize();
        binding.rvAddress.setItemAnimator(new DefaultItemAnimator());
        addressAdapter = new SelectAddressAdapter(context, addressData, new OnCheckSelectedListener() {
            @Override
            public void onClick(View view, int position, boolean selected) {
                if (addressData.get(position).isSelected()) {
                    addressData.get(position).setSelected(false);
                } else {
                    for (int i = 0; i < addressData.size(); i++) {
                        addressData.get(i).setSelected(false);
                    }
                    addressData.get(position).setSelected(true);
                }
                addressAdapter.notifyDataSetChanged();
            }
        }, new OnSelectedListener() {
            @Override
            public void onClick(View view, int position) {
                switch (view.getId()) {
                    case R.id.tvYes:
                        deleteAddressApi(addressData.get(position).getAddressId());
                        break;
                    case R.id.tvEdit:
                        Bundle bundle = new Bundle();
                        bundle.putString("Address_Type", addressData.get(position).getAddressType());
                        bundle.putString("Address_Name", addressData.get(position).getName());
                        bundle.putString("Address_Mobile", addressData.get(position).getMobile());
                        bundle.putString("Address_Line1", addressData.get(position).getAddressLine1());
                        bundle.putString("Address_Line2", addressData.get(position).getAddressLine2());
                        bundle.putString("Address_Pincode", addressData.get(position).getPincode());
                        bundle.putString("Address_City", addressData.get(position).getCity());
                        bundle.putString("Address_State", addressData.get(position).getState());
                        bundle.putString("Address_IsDefault", addressData.get(position).getIsDefault());
                        bundle.putString("Address_ID", addressData.get(position).getAddressId());
                        bundle.putString("Other_Address_Name", addressData.get(position).getOther_address_name());
                        bundle.putString(AppConstant.FROM, AppConstant.TYPE_EDIT);
                        ActivityController.startActivity(activity, AddAddress.class, bundle, false, false);
                        break;
                    default:
                        break;
                }

            }
        });
        if (from.equalsIgnoreCase(AppConstant.PROFILE)) {
            addressAdapter.hide(true);
        }
        binding.rvAddress.setAdapter(addressAdapter);
        addressAdapter.notifyDataSetChanged();

    }

    private boolean isAddressChoosen() {
        boolean checked = false;
        for (int i = 0; i < addressData.size(); i++) {
            if (addressData.get(i).isSelected()) {
                selectedAddressID = addressData.get(i).getAddressId();
                SharedPref.saveStringPreference(context, AppConstant.Selected_Address_ID, selectedAddressID);
                checked = true;
                break;
            } else {
                SharedPref.removeFromPreference(context, AppConstant.Selected_Address_ID);
                checked = false;
            }
        }
        return checked;
    }

    private void setToolbar() {
        binding.menubar.tvTitle.setVisibility(View.VISIBLE);
        if (from.equalsIgnoreCase(AppConstant.PROFILE)) {
            binding.menubar.tvTitle.setText(R.string.saved_address);
        } else {
            binding.menubar.tvTitle.setText(R.string.select_address);
        }
        binding.menubar.ivBack.setImageResource(R.drawable.ic_keyboard_backspace_black_24dp);
        binding.menubar.ivBack.setOnClickListener(this);
        binding.tvAddNewAddress.setOnClickListener(this);
        binding.btnSelectAddress.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                finish();
                break;
            case R.id.btn_submit:
                ActivityController.startActivity(activity, AddAddress.class);
                break;
            case R.id.btnSelectAddress:
                if (isAddressChoosen()) {
                    if (from.equalsIgnoreCase(AppConstant.FROM_SELECT_PATIENT)) {
                        ActivityController.startActivity(context, ScheduleDetail.class, AppConstant.FROM_SELECT_PATIENT);
                    } else if (from.equalsIgnoreCase(AppConstant.ORDER_MEDICINES_FROM_PRESCRIPTION)) {

                        savePrescriptionOrderData();
                       // saveOrderData(AppConstant.ORDER_MEDICINES_FROM_PRESCRIPTION);

                    } else if (from.equalsIgnoreCase(AppConstant.FROM_LABTEST_PRESCRIPTION_CART)) {
                        ActivityController.startActivity(context, ScheduleDetail.class, AppConstant.FROM_LABTEST_PRESCRIPTION_CART);
                    } else if (from.equalsIgnoreCase(AppConstant.FROM_LABTEST_CART)) {
                        ActivityController.startActivity(context, ScheduleDetail.class, AppConstant.FROM_LABTEST_CART);
                    } else {
                        saveOrderData(/*AppConstant.FROM_PRODUCT*/);

                    }
                } else {
                    CommonUtils.setSnackbar(binding.coordinatorLayout, "Please select address.");
                }
                break;
            case R.id.tvAddNewAddress:
                ActivityController.startActivity(activity, AddAddress.class);
                break;
            default:
                break;
        }
    }


    private void getAllAddrestApi() {
        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                Api api = RequestController.createService(context, Api.class);
                api.getAddressList(SharedPref.getStringPreferences(context, AppConstant.USER_ID))
                        .enqueue(new BaseCallback<AddressBaseResponse>(context) {
                            @Override
                            public void onSuccess(AddressBaseResponse response) {
                                binding.swipeRefreshingLayout.setRefreshing(false);
                                ProgressDialogUtils.dismiss();
                                if (response != null) {
                                    if (response.getStatus() == 1) {
                                        CommonUtils.showToastShort(context, response.getMessage());

                                        if (response.getAddressData() != null && !response.getAddressData().isEmpty()) {
                                            setEmptyLayout(false);
                                            addressData.clear();
                                            addressData.addAll(response.getAddressData());
                                            addressAdapter.notifyDataSetChanged();
                                        } else {
                                            setEmptyLayout(true);
                                            addressData.clear();
                                            addressAdapter.notifyDataSetChanged();
                                        }

                                    } else {
                                        setEmptyLayout(true);
                                        addressData.clear();
                                        addressAdapter.notifyDataSetChanged();
                                        CommonUtils.showToastShort(context, response.getMessage());
                                    }
                                } else {
                                    CommonUtils.showToastShort(context, getResources().getString(R.string.no_response));
                                }
                            }

                            @Override
                            public void onFail(Call<AddressBaseResponse> call, BaseResponse baseResponse) {
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

    private void deleteAddressApi(String addressId) {
        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                Api api = RequestController.createService(context, Api.class);
                api.deleteAddress(SharedPref.getStringPreferences(context, AppConstant.USER_ID), addressId)
                        .enqueue(new BaseCallback<BaseResponse>(context) {
                            @Override
                            public void onSuccess(BaseResponse response) {
                                ProgressDialogUtils.dismiss();
                                if (response != null) {
                                    if (response.getStatus() == 1) {
                                        CommonUtils.showToastShort(context, response.getMessage());
                                        getAllAddrestApi();
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

    private void saveOrderData(/*final String type*/) {
        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                Api api = RequestController.createService(context, Api.class);
                HashMap<String, String> map = new HashMap<>();
                map.put("user_id", SharedPref.getStringPreferences(context, AppConstant.USER_ID));
                map.put("address_id", selectedAddressID);
                map.put("prescription_id", " ");
                api.saveOrderDetail(map).enqueue(new BaseCallback<SaveOrderResponse>(context) {
                    @Override
                    public void onSuccess(SaveOrderResponse response) {
                        ProgressDialogUtils.dismiss();
                        if (response != null) {
                            if (response.getStatus() == 1) {
                                SharedPref.saveStringPreference(context, AppConstant.ORDERID, response.getSaveOrderData().getOrderId());
                                CommonUtils.showToastShort(context, response.getMessage());
                                ActivityController.startActivity(context, OrderReview.class, AppConstant.FROM_PRODUCT);

                                /*if (type.equalsIgnoreCase(AppConstant.ORDER_MEDICINES_FROM_PRESCRIPTION)) {
                                    ActivityController.startActivity(context, OrderReview.class, AppConstant.ORDER_MEDICINES_FROM_PRESCRIPTION);
                                } else {
                                    ActivityController.startActivity(context, OrderReview.class, AppConstant.FROM_PRODUCT);
                                }*/

                            } else {
                                CommonUtils.showToastShort(context, response.getMessage());
                            }
                        } else {
                            CommonUtils.showToastShort(context, getResources().getString(R.string.no_response));
                        }
                    }

                    @Override
                    public void onFail(Call<SaveOrderResponse> call, BaseResponse baseResponse) {
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

    private void savePrescriptionOrderData() {
        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                Api api = RequestController.createService(context, Api.class);
                HashMap<String, String> map = new HashMap<>();
                map.put("user_id", SharedPref.getStringPreferences(context, AppConstant.USER_ID));
                map.put("address_id", selectedAddressID);
                map.put("prescription_id", "");
                map.put("pateint_id", "");
                map.put("book_lab", "0");
                api.savePrescriptionOrderDetail(map).enqueue(new BaseCallback<SaveOrderResponse>(context) {
                    @Override
                    public void onSuccess(SaveOrderResponse response) {
                        ProgressDialogUtils.dismiss();
                        if (response != null) {
                            if (response.getStatus() == 1) {
                                SharedPref.saveStringPreference(context, AppConstant.ORDERID, response.getSaveOrderData().getOrderId());
                                CommonUtils.showToastShort(context, response.getMessage());
                                ActivityController.startActivity(context, OrderReview.class, AppConstant.FROM_PRODUCT);
                                ActivityController.startActivity(context, OrderReview.class, AppConstant.ORDER_MEDICINES_FROM_PRESCRIPTION);
                            } else {
                                CommonUtils.showToastShort(context, response.getMessage());
                            }
                        } else {
                            CommonUtils.showToastShort(context, getResources().getString(R.string.no_response));
                        }
                    }

                    @Override
                    public void onFail(Call<SaveOrderResponse> call, BaseResponse baseResponse) {
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
