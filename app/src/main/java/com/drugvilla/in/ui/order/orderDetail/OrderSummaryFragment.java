package com.drugvilla.in.ui.order.orderDetail;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.drugvilla.in.R;
import com.drugvilla.in.databinding.FragmentOrderSummaryBinding;
import com.drugvilla.in.model.lab.myTest.MyTestData;
import com.drugvilla.in.model.lab.myTest.MyTestDetailResponse;
import com.drugvilla.in.model.order.myOrder.MyOrderData;
import com.drugvilla.in.model.order.myOrder.MyOrderDetailResponse;
import com.drugvilla.in.service.Api;
import com.drugvilla.in.service.BaseCallback;
import com.drugvilla.in.service.BaseResponse;
import com.drugvilla.in.service.RequestController;
import com.drugvilla.in.utils.AppConstant;
import com.drugvilla.in.utils.CommonUtils;
import com.drugvilla.in.utils.ProgressDialogUtils;
import com.drugvilla.in.utils.SharedPref;

import retrofit2.Call;

public class OrderSummaryFragment extends Fragment implements View.OnClickListener {
    private FragmentOrderSummaryBinding binding;
    private Context context;
    private String type;
    private MyOrderData myOrderData;
    private MyTestData myTestData;
    private String orderId;


    public OrderSummaryFragment(Context context, String type, String orderId) {
        this.context = context;
        this.type = type;
        this.orderId = orderId;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_order_summary, container, false);
        View view = binding.getRoot();
        binding.ivUploadedRX.setOnClickListener(this);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setData();
    }

    private void setData() {
        if (type.equalsIgnoreCase(AppConstant.TYPE_LABS)) {
            getMyLabDetail(AppConstant.TYPE_LABS);

        } else if (type.equalsIgnoreCase(AppConstant.ORDER_MEDICINES_FROM_PRESCRIPTION)) {
            getMyOrderDetail(AppConstant.ORDER_MEDICINES_FROM_PRESCRIPTION);

        } else if (type.equalsIgnoreCase(AppConstant.BOOK_LAB_TEST_FROM_PRESCRIPTION)) {
            getMyLabDetail(AppConstant.BOOK_LAB_TEST_FROM_PRESCRIPTION);

        } else {
            getMyOrderDetail("ORDER");
            binding.llSlotSelected.setVisibility(View.GONE);
        }
    }

    private void setRXViewsVisible() {
        binding.llcallingDetails.setVisibility(View.VISIBLE);
        binding.llBillingDetailsRX.setVisibility(View.VISIBLE);
        binding.llUploadedPrescription.setVisibility(View.VISIBLE);
    }

    private void getMyOrderDetail(final String type) {
        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                Api api = RequestController.createService(context, Api.class);
                api.getMyOrderDetail(SharedPref.getStringPreferences(context, AppConstant.USER_ID), orderId)
                        .enqueue(new BaseCallback<MyOrderDetailResponse>(context) {
                            @Override
                            public void onSuccess(MyOrderDetailResponse response) {
                                ProgressDialogUtils.dismiss();
                                if (response != null) {
                                    if (response.getStatus() == 1) {
                                        CommonUtils.showToastShort(context, response.getMessage());
                                        if (response.getMyOrderData() != null) {
                                            myOrderData = response.getMyOrderData();
                                            setOrderData(myOrderData, type);

                                            //TODO : set uploaded rx recycler view and set data accordingly if type is ORDER_MEDICINES_FROM_PRESCRIPTION
                                        }
                                    } else {
                                        CommonUtils.showToastShort(context, response.getMessage());
                                    }
                                } else {
                                    CommonUtils.showToastShort(context, getResources().getString(R.string.no_response));
                                }
                            }

                            @Override
                            public void onFail(Call<MyOrderDetailResponse> call, BaseResponse baseResponse) {
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

    private void setOrderData(MyOrderData myOrderData, String type) {
        // order details
        binding.tvOrderStatus.setText(myOrderData.getOrderStatus());
        binding.tvOrderDate.setText(myOrderData.getOrderDate());
        binding.tvOrderId.setText(myOrderData.getUserOrderId());
        binding.tvPatientName.setText(myOrderData.getUserName());

        // address details
        binding.tvAddress.setText(myOrderData.getSelectedAddress().getState());
        binding.tvAddress2.setText(myOrderData.getSelectedAddress().getCity());
        binding.tvAddressType.setText(myOrderData.getSelectedAddress().getAddressType());
        binding.tvMobileNumber.setText(myOrderData.getSelectedAddress().getMobile());

        if (type.equalsIgnoreCase(AppConstant.ORDER_MEDICINES_FROM_PRESCRIPTION)) {
            setRXViewsVisible();
            binding.tvCallingTimings.setText(myOrderData.getPrescriptionVerificationCallDuration());
        }

    }

    private void getMyLabDetail(final String type) {
        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                Api api = RequestController.createService(context, Api.class);
                api.getMyTestDetail(SharedPref.getStringPreferences(context, AppConstant.USER_ID), orderId)
                        .enqueue(new BaseCallback<MyTestDetailResponse>(context) {
                    @Override
                    public void onSuccess(MyTestDetailResponse response) {
                        ProgressDialogUtils.dismiss();
                        if (response != null) {
                            if (response.getStatus() == 1) {
                                CommonUtils.showToastShort(context, response.getMessage());
                                if (response.getMyTestData() != null) {
                                    myTestData = response.getMyTestData();
                                    setLabData(myTestData, type);

                                    //TODO : set uploaded rx recycler view and set data accordingly if type is BOOK_LAB_TEST_FROM_PRESCRIPTION

                                }
                            } else {
                                CommonUtils.showToastShort(context, response.getMessage());
                            }
                        } else {
                            CommonUtils.showToastShort(context, getResources().getString(R.string.no_response));
                        }
                    }

                    @Override
                    public void onFail(Call<MyTestDetailResponse> call, BaseResponse baseResponse) {
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

    private void setLabData(MyTestData myTestData, String type) {
        binding.llSlotSelected.setVisibility(View.VISIBLE);
        binding.llSelectedLabDetails.setVisibility(View.VISIBLE);

        // order details
        binding.tvOrderStatus.setText(myTestData.getOrderStatus());
        // binding.tvOrderDate.setText(myTestData.getOrder_date());
        binding.tvOrderId.setText(myTestData.getUserLabtestId());
        binding.tvPatientName.setText(myTestData.getSelectedPatient().getName());
        // lab test date and time
        binding.tvAppointmentDate.setText(myTestData.getSelectedDateTime().getSelectedDate());
        binding.tvAppointmenttime.setText(myTestData.getSelectedDateTime().getSelectedTime());
        // address details
        binding.tvAddress.setText(myTestData.getSelectedAddress().getState());
        binding.tvAddress2.setText(myTestData.getSelectedAddress().getCity() + ", " + myTestData.getSelectedAddress().getPincode());
        binding.tvAddressType.setText(myTestData.getSelectedAddress().getAddressType());
        binding.tvMobileNumber.setText(myTestData.getSelectedAddress().getMobile());
        // lab details
      /*  binding.tvLabCertified.setText(myTestData.getLab_certification());
        binding.tvLabLocation.setText(myTestData.getLab_location());
        binding.tvLabName.setText(myTestData.getSelected_lab_name());*/
        if (type.equalsIgnoreCase(AppConstant.BOOK_LAB_TEST_FROM_PRESCRIPTION)) {
            setRXViewsVisible();
            binding.tvCallingTimings.setText(myTestData.getPrescriptionVerificationCallDuration());
        }


    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ivUploadedRX) {
            CommonUtils.setZoomView(context, R.drawable.rx);
        }
    }
}
