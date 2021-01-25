package com.drugvilla.in.ui.order;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.drugvilla.in.R;
import com.drugvilla.in.databinding.ActivityOrderConfirmationBinding;
import com.drugvilla.in.model.appointment.booking.AppointmentOrderDeatilResponse;
import com.drugvilla.in.model.appointment.booking.AppointmentOrderDetailData;
import com.drugvilla.in.model.appointment.booking.ConfirmBookingResponse;
import com.drugvilla.in.model.order.orderDetail.OrderDetailData;
import com.drugvilla.in.model.order.orderDetail.OrderDetailResponse;
import com.drugvilla.in.service.Api;
import com.drugvilla.in.service.BaseCallback;
import com.drugvilla.in.service.BaseResponse;
import com.drugvilla.in.service.RequestController;
import com.drugvilla.in.ui.startAndDashboard.Home;
import com.drugvilla.in.ui.doctors.AppointmentDetails;
import com.drugvilla.in.ui.order.orderDetail.OrderDetails;
import com.drugvilla.in.utils.ActivityController;
import com.drugvilla.in.utils.AppConstant;
import com.drugvilla.in.utils.CommonUtils;
import com.drugvilla.in.utils.ProgressDialogUtils;
import com.drugvilla.in.utils.SharedPref;

import java.util.Objects;

import retrofit2.Call;

public class OrderConfirmation extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    private Activity activity;
    private String from = " ", APPOINTMENT_ID = "",  ORDER_ID =" ";
    private ActivityOrderConfirmationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order_confirmation);
        context = OrderConfirmation.this;
        activity = OrderConfirmation.this;
        getData();
        setData();
    }

    private void setData() {
        binding.btnOrderDetails.setOnClickListener(this);
        binding.btnOrderDetailsProduct.setOnClickListener(this);
        binding.btnAppointmentDetails.setOnClickListener(this);
        binding.btnGoHome.setOnClickListener(this);
        binding.btnOrderDetailsRX.setOnClickListener(this);

        // case for lab test booking
        if (from.equalsIgnoreCase(AppConstant.TYPE_LABS)) {
            binding.llDoctorConsultation.setVisibility(View.VISIBLE);
            getLabTestOrderDetail();
        }
        // case for product order
        else if (from.equalsIgnoreCase(AppConstant.TYPE_PRODUCT)) {
            binding.llReferEarn.setVisibility(View.VISIBLE);
            getOrderDetail(AppConstant.TYPE_PRODUCT);
        }
        // case for book labtest from prescription
        else if (from.equalsIgnoreCase(AppConstant.BOOK_LAB_TEST_FROM_PRESCRIPTION)) {
            addContent(R.drawable.lab_order_successfull, getResources().getString(R.string.booking_successfull), getResources().getString(R.string.thank_you_for_placing_order_we_have_sent_you_an_sms_and_email_with_the_details));

            binding.llDoctorConsultation.setVisibility(View.VISIBLE);
            binding.llcallingDetails.setVisibility(View.VISIBLE);
            binding.llViewOrderDetailRX.setVisibility(View.VISIBLE);
        }
        // case for order med from prescription
        else if (from.equalsIgnoreCase(AppConstant.ORDER_MEDICINES_FROM_PRESCRIPTION)) {
            binding.llReferEarn.setVisibility(View.VISIBLE);
            getOrderDetail(AppConstant.ORDER_MEDICINES_FROM_PRESCRIPTION);

        }
        // case for appointment booking
        else {
            binding.llDoctorConsultation.setVisibility(View.VISIBLE);
            getAppointmentOrderDetail();
        }
    }

    private void addContent(int image, String heading, String description) {
        binding.ivOrder.setImageResource(image);
        binding.tvheading.setText(heading);
        binding.tvDescription.setText(description);
    }


    private void getData() {
        Bundle bundle = new Bundle();
        bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.getString(AppConstant.FROM) != null && !Objects.requireNonNull(bundle.getString(AppConstant.FROM)).isEmpty()) {
                from = bundle.getString(AppConstant.FROM);
            }
        }

        Intent intent = getIntent();
        if (intent != null) {
            if (intent.getStringExtra("APPOINTMENT_ID") != null && !intent.getStringExtra("APPOINTMENT_ID").isEmpty()) {
                APPOINTMENT_ID = intent.getStringExtra("APPOINTMENT_ID");
            }
        }
    }

    @Override
    public void onClick(View view) {
        Bundle bundle = new Bundle();
        switch (view.getId()) {

            case R.id.btnGoHome:
                ActivityController.startActivity(activity, Home.class, true, true);
                break;
            case R.id.btnOrderDetailsRX:
                if (from.equalsIgnoreCase(AppConstant.ORDER_MEDICINES_FROM_PRESCRIPTION)) {
                    ActivityController.startActivity(context, OrderDetails.class, AppConstant.ORDER_MEDICINES_FROM_PRESCRIPTION);
                } else {
                    ActivityController.startActivity(context, OrderDetails.class, AppConstant.BOOK_LAB_TEST_FROM_PRESCRIPTION);
                }
                break;
            case R.id.btnOrderDetails:
                bundle.putString("ORDER_ID", "myTestData.getOrderId()");
                bundle.putString(AppConstant.FROM, AppConstant.TYPE_LABS);
                ActivityController.startActivity(activity, OrderDetails.class, bundle, false, false);
               // ActivityController.startActivity(context, OrderDetails.class, AppConstant.TYPE_LABS);
                break;
            case R.id.btnOrderDetailsProduct:

                bundle.putString("ORDER_ID",ORDER_ID);
                bundle.putString(AppConstant.FROM, AppConstant.TYPE_PRODUCT);
                ActivityController.startActivity(activity, OrderDetails.class, bundle, false, false);
              //  ActivityController.startActivity(context, OrderDetails.class, AppConstant.TYPE_PRODUCT);
                break;
            case R.id.btnAppointmentDetails:
                Intent intent = new Intent(context, AppointmentDetails.class);
                intent.putExtra("APPOINTMENT_ID", APPOINTMENT_ID);
                context.startActivity(intent);
                break;
            default:
                break;
        }
    }

    private void getOrderDetail(final String type) {
        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                Api api = RequestController.createService(context, Api.class);
                api.getOrderDetail(SharedPref.getStringPreferences(context, AppConstant.USER_ID), SharedPref.getStringPreferences(context, AppConstant.ORDERID))
                        .enqueue(new BaseCallback<OrderDetailResponse>(context) {
                            @Override
                            public void onSuccess(OrderDetailResponse response) {
                                ProgressDialogUtils.dismiss();
                                if (response != null) {
                                    if (response.getStatus() == 1) {
                                        CommonUtils.showToastShort(context, response.getMessage());
                                        if (response.getOrderDetailData() != null) {
                                            binding.llProductSummary.setVisibility(View.VISIBLE);
                                            OrderDetailData data = response.getOrderDetailData();
                                            binding.tvUserName2.setText(data.getBooked_for());
                                            binding.tvPriceProduct.setText(data.getOrder_total());

                                            if (data.getPharmacistCall() != null && !data.getPharmacistCall().isEmpty()) {
                                                binding.llcallingDetails.setVisibility(View.VISIBLE);
                                                binding.tvTimings.setText(data.getPharmacistCall());
                                            }


                                            if (type.equalsIgnoreCase(AppConstant.ORDER_MEDICINES_FROM_PRESCRIPTION)) {
                                                addContent(R.drawable.product_order_successfull,
                                                        getResources().getString(R.string.order_placed_successfully),
                                                        getResources().getString(R.string.rx_send_for_review));
                                                if (data.getPharmacistCall() != null && !data.getPharmacistCall().isEmpty()) {
                                                    binding.llcallingDetails.setVisibility(View.VISIBLE);
                                                    binding.tvTimings.setText(response.getOrderDetailData().getPharmacistCall());
                                                }
                                                binding.llViewOrderDetailRX.setVisibility(View.VISIBLE);


                                            } else {

                                                addContent(R.drawable.product_order_successfull, getResources().getString(R.string.order_placed_successfully),
                                                        getResources().getString(R.string.thank_you_for_placing_order_we_have_sent_you_an_sms_and_email_with_the_details));
                                            }

                                             ORDER_ID = response.getOrderDetailData().getOrder_id();

                                            SharedPref.saveStringPreference(context, AppConstant.ORDERID, response.getOrderDetailData().getOrder_id());
                                        }
                                    } else {

                                        CommonUtils.showToastShort(context, response.getMessage());
                                    }
                                } else {
                                    CommonUtils.showToastShort(context, context.getResources().getString(R.string.no_response));
                                }
                            }

                            @Override
                            public void onFail(Call<OrderDetailResponse> call, BaseResponse baseResponse) {
                                ProgressDialogUtils.dismiss();
                                CommonUtils.showToastShort(context, context.getResources().getString(R.string.failure));
                            }
                        });

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            CommonUtils.showToastShort(context, context.getResources().getString(R.string.no_internet));
        }
    }

    private void getAppointmentOrderDetail() {
        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                Api api = RequestController.createService(context, Api.class);
                api.getappointmentorderdetail(SharedPref.getStringPreferences(context, AppConstant.USER_ID), APPOINTMENT_ID)
                        .enqueue(new BaseCallback<AppointmentOrderDeatilResponse>(context) {
                            @Override
                            public void onSuccess(AppointmentOrderDeatilResponse response) {
                                ProgressDialogUtils.dismiss();
                                if (response != null) {
                                    if (response.getStatus() == 1) {
                                        CommonUtils.showToastShort(context, response.getMessage());
                                        if (response.getAppointmentOrderDetailData() != null) {
                                            binding.llAppointmentSummary.setVisibility(View.VISIBLE);
                                            addContent(R.drawable.lab_order_successfull, getResources().getString(R.string.appointment_booked_successfully), getResources().getString(R.string.thank_you_for_placing_order_we_have_sent_you_an_sms_and_email_with_the_details));
                                            AppointmentOrderDetailData data = response.getAppointmentOrderDetailData();
                                            binding.tvPatientName.setText(data.getBooked_for());
                                            binding.tvAppointmentId.setText(data.getAppointment_order_id());
                                            binding.tvDateAppointment.setText(data.getAppointment_date());
                                            binding.tvTimeAppointment.setText(data.getAppointment_time());
                                            binding.tvPriceAppointment.setText(data.getOrder_total());
                                            APPOINTMENT_ID = response.getAppointmentOrderDetailData().getAppointment_order_id();
                                        }
                                    } else {
                                        CommonUtils.showToastShort(context, response.getMessage());
                                    }
                                } else {
                                    CommonUtils.showToastShort(context, context.getResources().getString(R.string.no_response));
                                }
                            }

                            @Override
                            public void onFail(Call<AppointmentOrderDeatilResponse> call, BaseResponse baseResponse) {
                                ProgressDialogUtils.dismiss();
                                CommonUtils.showToastShort(context, context.getResources().getString(R.string.failure));
                            }
                        });

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            CommonUtils.showToastShort(context, context.getResources().getString(R.string.no_internet));
        }
    }

    private void getLabTestOrderDetail() {
        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                Api api = RequestController.createService(context, Api.class);
                api.getLabOrderDetail(SharedPref.getStringPreferences(context, AppConstant.USER_ID), SharedPref.getStringPreferences(context, AppConstant.LabOrderId))
                        .enqueue(new BaseCallback<OrderDetailResponse>(context) {
                            @Override
                            public void onSuccess(OrderDetailResponse response) {
                                ProgressDialogUtils.dismiss();
                                if (response != null) {
                                    if (response.getStatus() == 1) {
                                        CommonUtils.showToastShort(context, response.getMessage());
                                        if (response.getOrderDetailData() != null) {
                                            binding.llLabSummary.setVisibility(View.VISIBLE);
                                            binding.llReportDetails.setVisibility(View.VISIBLE);
                                            addContent(R.drawable.lab_order_successfull, getResources().getString(R.string.booking_successfull), getResources().getString(R.string.thank_you_for_placing_order_we_have_sent_you_an_sms_and_email_with_the_details));
                                            OrderDetailData data = response.getOrderDetailData();
                                            binding.tvEmailUser.setText(data.getEmail());
                                            binding.tvMobileUser.setText(data.getMobile());
                                            binding.tvUserName.setText(data.getBooked_for());
                                            binding.tvDate.setText(data.getDate());
                                            binding.tvTime.setText(data.getTime());
                                            binding.tvPriceLabTest.setText(data.getOrder_total());
                                        }
                                    } else {

                                        CommonUtils.showToastShort(context, response.getMessage());
                                    }
                                } else {
                                    CommonUtils.showToastShort(context, context.getResources().getString(R.string.no_response));
                                }
                            }

                            @Override
                            public void onFail(Call<OrderDetailResponse> call, BaseResponse baseResponse) {
                                ProgressDialogUtils.dismiss();
                                CommonUtils.showToastShort(context, context.getResources().getString(R.string.failure));
                            }
                        });

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            CommonUtils.showToastShort(context, context.getResources().getString(R.string.no_internet));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ActivityController.startActivity(activity, Home.class, true, true);
    }
}
