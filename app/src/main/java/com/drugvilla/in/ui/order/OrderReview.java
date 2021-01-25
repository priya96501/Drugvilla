package com.drugvilla.in.ui.order;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.drugvilla.in.R;
import com.drugvilla.in.adapter.labs.SelectedLabTestAdapter;
import com.drugvilla.in.adapter.order.SelectedProductAdapter;
import com.drugvilla.in.adapter.prescription.PrescriptionAdapter;
import com.drugvilla.in.databinding.ActivityOrderReviewBinding;
import com.drugvilla.in.listener.OnCheckSelectedListener;
import com.drugvilla.in.listener.OnSelectedListener;
import com.drugvilla.in.listener.OnSelectedTypeListener;
import com.drugvilla.in.model.Document;
import com.drugvilla.in.model.PrescriptionData;
import com.drugvilla.in.model.address.AddressData;
import com.drugvilla.in.model.cart.SelectedItem;
import com.drugvilla.in.model.order.SelectedDateTime;
import com.drugvilla.in.model.order.labOrderReview.LabOrderReviewData;
import com.drugvilla.in.model.order.labOrderReview.LabOrderReviewResponse;
import com.drugvilla.in.model.order.orderReview.OrderReviewData;
import com.drugvilla.in.model.order.orderReview.OrderReviewResponse;
import com.drugvilla.in.model.order.orderReview.fromRX.ReviewData;
import com.drugvilla.in.model.order.orderReview.fromRX.ReviewResponse;
import com.drugvilla.in.model.order.saveOrder.SaveOrderResponse;
import com.drugvilla.in.model.order.saveOrder.SaveOrderResponse2;
import com.drugvilla.in.model.patient.PatientData;
import com.drugvilla.in.service.Api;
import com.drugvilla.in.service.BaseCallback;
import com.drugvilla.in.service.BaseResponse;
import com.drugvilla.in.service.RequestController;
import com.drugvilla.in.utils.ActivityController;
import com.drugvilla.in.utils.AppConstant;
import com.drugvilla.in.utils.CommonUtils;
import com.drugvilla.in.utils.ProgressDialogUtils;
import com.drugvilla.in.utils.SharedPref;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;

public class OrderReview extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    private ActivityOrderReviewBinding binding;
    private String from = " ";
    private List<Document> listData = new ArrayList<>();

    private List<SelectedItem> selectedTestList = new ArrayList<>();
    private List<SelectedItem> selectedItemsList = new ArrayList<>();
    private List<PrescriptionData> prescriptionDataList = new ArrayList<>();
    private SelectedLabTestAdapter testAdapter;
    private SelectedProductAdapter selectedProductAdapter;

    // selected tests
    private final String[] testName = {"ACE (Absolute Eosinophil Count)", "Advance Health Care", "Beta Hcg/Hcg Beta Subunit"};
    private final String[] labName = {"Sure path labs pvt ltd.", "SRL Limited", "Delhi Path Lab"};
    private final String[] price = {"500", "1250", "750"};
    private final String[] option = {"test", "package", "test"};
    // selected products
    private List<Document> listDataProduct = new ArrayList<>();
    private final int[] productImage = {R.drawable.sp3, R.drawable.sp1, R.drawable.sp2};
    private final String[] productName = {"Cadbury Bournvita Five Star Magic Health Drink - Pouch 500 gm", "Swadeshi Vatantak Vati Powder 50 gm", "Bioderma Cicabio Arnica+ Creme 40 ml"};
    private final String[] by = {"By Cadbury India Ltd", "By Swadeshi", "By Naos Skin Care India Pvt Ltd"};
    private final String[] mrp = {"500", "1250", "750"};
    private final String[] amount = {"300", "1150", "600"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order_review);
        context = OrderReview.this;
        getData();
        setToolbar();
        setData();
    }

    private void setData() {
        binding.btnSelectPaymentModeRX.setOnClickListener(this);
        binding.btnSelectPaymentMode.setOnClickListener(this);
        //  binding.ivUploadedRX.setOnClickListener(this);
        binding.btnContinue.setOnClickListener(this);
        //  CommonUtils.setImageRound(context, binding.ivUploadedRX, R.drawable.rx);

        if (from.equalsIgnoreCase(AppConstant.TYPE_LABS)) {
            // coming from collection type2 = visiting lab
            getLabOrderReview("type2");
            setSelectedTestAdapter();
        } else if (from.equalsIgnoreCase(AppConstant.FROM_LABTEST_CART)) {
            // coming from collection type1 = sample collection from home
            getLabOrderReview("type1");
            setSelectedTestAdapter();
        } else if (from.equalsIgnoreCase(AppConstant.ORDER_MEDICINES_FROM_PRESCRIPTION)) {
            getPrescriptionOrderReview();
            setPrescriptionAdapter();

        } else if (from.equalsIgnoreCase(AppConstant.FROM_LABS_PRESCRIPTION_CART)) {
            setViewsVisibility();
            binding.btnSelectPaymentModeRX.setVisibility(View.VISIBLE);
        } else if (from.equalsIgnoreCase(AppConstant.FROM_LABTEST_PRESCRIPTION_CART)) {
            setViewsVisibility();
            binding.btnSelectPaymentModeRX.setVisibility(View.VISIBLE);

        } else {
            getOrderReview(/*"from_without_RX"*/);
            setSelectedProductAdapter();
            setPrescriptionAdapter();
        }
    }


    private void setViewsVisibility() {
        binding.llOrderThroughPrescription.setVisibility(View.VISIBLE);
        binding.llOrderDetails.setVisibility(View.VISIBLE);
        binding.llPaymentInfoWithRX.setVisibility(View.VISIBLE);
        binding.llPaymentInfoWithoutRX.setVisibility(View.GONE);
        binding.llDetails.setVisibility(View.GONE);
        binding.llSelectedLabTest.setVisibility(View.GONE);
        binding.llSelectedProducts.setVisibility(View.GONE);
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

    private void setToolbar() {
        binding.menubar.tvTitle.setVisibility(View.VISIBLE);
        binding.menubar.tvTitle.setText(R.string.order_review);
        binding.menubar.ivBack.setImageResource(R.drawable.ic_keyboard_backspace_black_24dp);
        binding.menubar.ivBack.setOnClickListener(this);
    }

    private void setSelectedProductAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
        binding.rvSelectedProducts.setLayoutManager(layoutManager);
        binding.rvSelectedProducts.hasFixedSize();
        selectedProductAdapter = new SelectedProductAdapter(context, selectedItemsList, AppConstant.TYPE_ORDER_REVIEW, new OnSelectedListener() {
            @Override
            public void onClick(View view, int position) {

                // nothing to do as item is shown only , it cant be removed
            }
        }, new OnSelectedTypeListener() {
            @Override
            public void onClick(View view, int position, String type) {
                // nothing to do as quantity is just shown here not changed
            }
        });
        binding.rvSelectedProducts.setAdapter(selectedProductAdapter);
    }


    private void setSelectedTestAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
        binding.rvTestIncluded.setLayoutManager(layoutManager);
        binding.rvTestIncluded.hasFixedSize();
        testAdapter = new SelectedLabTestAdapter(context, selectedTestList, AppConstant.TYPE_ORDER_REVIEW, new OnSelectedListener() {
            @Override
            public void onClick(View view, int position) {
                // nothing to do
            }
        });
        binding.rvTestIncluded.setAdapter(testAdapter);
    }

    private void setPrescriptionAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
        binding.rvPrescriptions.setLayoutManager(layoutManager);
        binding.rvPrescriptions.hasFixedSize();
        PrescriptionAdapter prescriptionAdapter = new PrescriptionAdapter(context, prescriptionDataList, new OnCheckSelectedListener() {
            @Override
            public void onClick(View view, int position, boolean selected) {
                // noting to do as checkbox functionality is not needed
            }
        }, " ");
        prescriptionAdapter.show(true);
        binding.rvPrescriptions.setAdapter(prescriptionAdapter);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivUploadedRX:
                CommonUtils.setZoomView(context, R.drawable.rx);
                break;
            case R.id.ivBack:
                finish();
                break;
            case R.id.btnContinue:
                if (from.equalsIgnoreCase(AppConstant.ORDER_MEDICINES_FROM_PRESCRIPTION))
                    confirmPrescriptionOrderApi();
                else {
                    confirmOrderApi();
                }
                break;
            case R.id.btnSelectPaymentMode:
                if (from.equalsIgnoreCase(AppConstant.TYPE_LABS)) {
                    ActivityController.startActivity(context, PaymentSelection.class, AppConstant.TYPE_LABS);
                } else {
                    ActivityController.startActivity(context, PaymentSelection.class, AppConstant.TYPE_PRODUCT);
                }
                break;

            // Not needed as no payment selection if there is prescription in order.

          /*  case R.id.btnSelectPaymentModeRX:
                if (from.equalsIgnoreCase(AppConstant.ORDER_MEDICINES_FROM_PRESCRIPTION)) {
                    ActivityController.startActivity(context, PaymentSelection.class, AppConstant.ORDER_MEDICINES_FROM_PRESCRIPTION);
                } else {
                    ActivityController.startActivity(context, PaymentSelection.class, AppConstant.BOOK_LAB_TEST_FROM_PRESCRIPTION);
                }
                break;*/

            default:
                break;
        }
    }

    private void confirmOrderApi() {
        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                Api api = RequestController.createService(context, Api.class);
                HashMap<String, String> map = new HashMap<>();
                map.put("user_id", SharedPref.getStringPreferences(context, AppConstant.USER_ID));
                map.put("order_id", SharedPref.getStringPreferences(context, AppConstant.ORDERID));
                map.put("payment_type", "Pending");
                api.confirmOrder(map).enqueue(new BaseCallback<SaveOrderResponse2>(context) {
                    @Override
                    public void onSuccess(SaveOrderResponse2 response) {
                        ProgressDialogUtils.dismiss();
                        if (response != null) {
                            if (response.getStatus() == 1) {
                                SharedPref.saveStringPreference(context, AppConstant.ORDERID, response.getSaveOrderData().getOrderId());
                                CommonUtils.showToastShort(context, response.getMessage());

                                ActivityController.startActivity(context, OrderConfirmation.class, AppConstant.TYPE_PRODUCT);

                            } else {
                                CommonUtils.showToastShort(context, response.getMessage());
                            }
                        } else {
                            CommonUtils.showToastShort(context, getResources().getString(R.string.no_response));
                        }
                    }

                    @Override
                    public void onFail(Call<SaveOrderResponse2> call, BaseResponse baseResponse) {
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

    private void confirmPrescriptionOrderApi() {
        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                Api api = RequestController.createService(context, Api.class);
                api.confirmPrescriptionOrder(SharedPref.getStringPreferences(context, AppConstant.USER_ID),
                        SharedPref.getStringPreferences(context, AppConstant.ORDERID)).enqueue(new BaseCallback<SaveOrderResponse>(context) {
                    @Override
                    public void onSuccess(SaveOrderResponse response) {
                        ProgressDialogUtils.dismiss();
                        if (response != null) {
                            if (response.getStatus() == 1) {
                                SharedPref.saveStringPreference(context, AppConstant.ORDERID, response.getSaveOrderData().getOrderId());
                                CommonUtils.showToastShort(context, response.getMessage());
                                ActivityController.startActivity(context, OrderConfirmation.class, AppConstant.ORDER_MEDICINES_FROM_PRESCRIPTION);

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

    private void getLabOrderReview(final String type) {
        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                Api api = RequestController.createService(context, Api.class);
                api.getLabTestOrderReview(SharedPref.getStringPreferences(context, AppConstant.USER_ID), SharedPref.getStringPreferences(context, AppConstant.LabOrderId))
                        .enqueue(new BaseCallback<LabOrderReviewResponse>(context) {
                            @Override
                            public void onSuccess(LabOrderReviewResponse response) {
                                ProgressDialogUtils.dismiss();
                                if (response != null) {
                                    if (response.getStatus() == 1) {
                                        binding.llOrderDetails.setVisibility(View.VISIBLE);
                                        binding.llSelectedLabTest.setVisibility(View.VISIBLE);
                                        binding.llSelectedPatient.setVisibility(View.VISIBLE);
                                        binding.llSelectedSlot.setVisibility(View.VISIBLE);
                                        binding.tvHeadingAddress.setText(getResources().getString(R.string.sample_pickup_address));
                                        binding.deliveryCharges.setText(getResources().getString(R.string.sample_collection_charges));

                                        CommonUtils.showToastShort(context, response.getMessage());
                                        LabOrderReviewData data = response.getData();
                                        SharedPref.saveStringPreference(context, AppConstant.LabOrderId, data.getOrderId());
                                        setTestOrderReview(data, type);
                                        if (data.getSelectedItems() != null && !data.getSelectedItems().isEmpty()) {
                                            selectedTestList.clear();
                                            selectedTestList.addAll(data.getSelectedItems());
                                            testAdapter.notifyDataSetChanged();
                                        } else {
                                            selectedTestList.clear();
                                            testAdapter.notifyDataSetChanged();
                                        }

                                    } else {
                                        CommonUtils.showToastShort(context, response.getMessage());
                                    }
                                } else {
                                    CommonUtils.showToastShort(context, getResources().getString(R.string.no_response));
                                }
                            }

                            @Override
                            public void onFail(Call<LabOrderReviewResponse> call, BaseResponse baseResponse) {
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

    private void setTestOrderReview(LabOrderReviewData data, String type) {
        PatientData dataSelectedPatient = data.getSelectedPatient();
        AddressData dataSelectedAddress = data.getSelectedAddress();
        SelectedDateTime dataSelectedDateTime = data.getSelectedDateTime();

        if (type.equalsIgnoreCase("type1")) {
            binding.tvAddressType.setText(dataSelectedAddress.getAddressType());
            binding.tvAddress.setText(dataSelectedAddress.getCity());
            StringBuffer stringBuffer = new StringBuffer();
            binding.tvAddress2.setText(stringBuffer.append(dataSelectedAddress.getState()).append(" , ").append(dataSelectedAddress.getPincode()));
            binding.tvNumber.setText(dataSelectedAddress.getMobile());
            binding.llSampleCollectionCharge.setVisibility(View.VISIBLE);
        } else {
            binding.llSelectedAddress.setVisibility(View.GONE);
            binding.llSampleCollectionCharge.setVisibility(View.GONE);
        }

        binding.tvPatientAge.setText(dataSelectedPatient.getAge());
        binding.tvPatientGender.setText(dataSelectedPatient.getGender());
        binding.tvPatientName.setText(dataSelectedPatient.getName());
        binding.tvEmail.setText(dataSelectedPatient.getEmail());

        binding.tvDate.setText(dataSelectedDateTime.getSelectedDate());
        binding.tvTime.setText(dataSelectedDateTime.getSelectedTime());

        binding.tvTotalOrderAmount.setText(data.getOrderTotal());
        binding.tvBagTotal.setText(data.getTotalMrp());
        binding.tvOrderTotal.setText(data.getOrderTotal());
        binding.tvDiscount.setText(data.getTotalDiscount());


    }

    private void getOrderReview(/*final String type*/) {
        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                Api api = RequestController.createService(context, Api.class);
                api.getOrderReview(SharedPref.getStringPreferences(context, AppConstant.USER_ID),
                        SharedPref.getStringPreferences(context, AppConstant.ORDERID))
                        .enqueue(new BaseCallback<OrderReviewResponse>(context) {
                            @Override
                            public void onSuccess(OrderReviewResponse response) {
                                ProgressDialogUtils.dismiss();
                                if (response != null) {
                                    if (response.getStatus() == 1) {
                                        CommonUtils.showToastShort(context, response.getMessage());
                                        binding.llOrderDetails.setVisibility(View.VISIBLE);
                                        binding.tvHeadingAddress.setText(getResources().getString(R.string.delivery_address));
                                        binding.deliveryCharges.setText(getResources().getString(R.string.delivery_charges));
                                        OrderReviewData data = response.getOrderReviewData();
                                        SharedPref.saveStringPreference(context, AppConstant.ORDERID, data.getOrderId());
                                        setOrderReview(data/*, type*/);

                                        if (response.getSelectedItems() != null && !response.getSelectedItems().isEmpty()) {
                                            selectedItemsList.clear();
                                            selectedItemsList.addAll(response.getSelectedItems());
                                        } else {
                                            selectedItemsList.clear();
                                        }
                                        if (data.getPrescriptionDataList() != null && !data.getPrescriptionDataList().isEmpty()) {
                                            binding.llPrescriptionSection.setVisibility(View.VISIBLE);
                                            prescriptionDataList.clear();
                                            prescriptionDataList.addAll(data.getPrescriptionDataList());
                                        } else {
                                            prescriptionDataList.clear();
                                        }
                                        setSelectedProductAdapter();
                                        setPrescriptionAdapter();

                                    } else {
                                        CommonUtils.showToastShort(context, response.getMessage());
                                    }
                                } else {
                                    CommonUtils.showToastShort(context, getResources().getString(R.string.no_response));
                                }
                            }

                            @Override
                            public void onFail(Call<OrderReviewResponse> call, BaseResponse baseResponse) {
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

    private void setOrderReview(OrderReviewData data/*, String type*/) {
        AddressData dataSelectedAddress = data.getSelectedAddress();
        binding.tvAddressType.setText(dataSelectedAddress.getAddressType());
        binding.tvAddress.setText(new StringBuffer(dataSelectedAddress.getAddressLine1()).append(" , ").append(dataSelectedAddress.getAddressLine2()));
        StringBuffer stringBuffer = new StringBuffer();
        binding.tvAddress2.setText(stringBuffer.append(dataSelectedAddress.getState()).append(" , ").append(dataSelectedAddress.getCity()).append(" , ").append(dataSelectedAddress.getPincode()));
        binding.tvNumber.setText(dataSelectedAddress.getMobile());
        /* if (type.equalsIgnoreCase("from_without_RX")) {*/
        binding.tvTotalOrderAmount.setText(new StringBuffer(getResources().getString(R.string.Rs)).append(" ").append(data.getOrderTotal()));
        binding.tvBagTotal.setText(new StringBuffer(getResources().getString(R.string.Rs)).append(" ").append(data.getTotalMrp()));
        binding.tvOrderTotal.setText(new StringBuffer(getResources().getString(R.string.Rs)).append(" ").append(data.getOrderTotal()));
        binding.tvTotalCharges.setText(new StringBuffer(getResources().getString(R.string.Rs)).append(" ").append(data.getOrderTotal()));
        binding.tvDiscount.setText(data.getTotalDiscount());
       /* } else {
            setViewsVisibility();
            if (data.getIsPrescribable().equalsIgnoreCase("1")) {
                binding.btnContinue.setVisibility(View.VISIBLE);
                binding.btnSelectPaymentMode.setVisibility(View.GONE);
            }
        }*/
    }

    private void getPrescriptionOrderReview() {
        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                Api api = RequestController.createService(context, Api.class);
                api.getPrescriptionOrderReview(SharedPref.getStringPreferences(context, AppConstant.USER_ID),
                        SharedPref.getStringPreferences(context, AppConstant.ORDERID))
                        .enqueue(new BaseCallback<ReviewResponse>(context) {
                            @Override
                            public void onSuccess(ReviewResponse response) {
                                ProgressDialogUtils.dismiss();
                                if (response != null) {
                                    if (response.getStatus() == 1) {
                                        CommonUtils.showToastShort(context, response.getMessage());
                                        binding.llOrderDetails.setVisibility(View.VISIBLE);
                                        binding.tvHeadingAddress.setText(getResources().getString(R.string.delivery_address));
                                        binding.deliveryCharges.setText(getResources().getString(R.string.delivery_charges));
                                        ReviewData data = response.getOrderReviewData();
                                        SharedPref.saveStringPreference(context, AppConstant.ORDERID, data.getOrderId());
                                        setPrescriptionOrderReview(data);

                                        if (data.getPrescriptionDataList() != null && !data.getPrescriptionDataList().isEmpty()) {
                                            binding.llPrescriptionSection.setVisibility(View.VISIBLE);
                                            prescriptionDataList.clear();
                                            prescriptionDataList.addAll(data.getPrescriptionDataList());

                                        } else {
                                            prescriptionDataList.clear();
                                        }

                                        setPrescriptionAdapter();
                                    } else {
                                        CommonUtils.showToastShort(context, response.getMessage());
                                    }
                                } else {
                                    CommonUtils.showToastShort(context, getResources().getString(R.string.no_response));
                                }
                            }

                            @Override
                            public void onFail(Call<ReviewResponse> call, BaseResponse baseResponse) {
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

    private void setPrescriptionOrderReview(ReviewData data) {

        AddressData dataSelectedAddress = data.getSelectedAddress();
        binding.tvAddressType.setText(dataSelectedAddress.getAddressType());
        binding.tvAddress.setText(new StringBuffer(dataSelectedAddress.getAddressLine1())
                .append(" , ").append(dataSelectedAddress.getAddressLine2()));
        StringBuffer stringBuffer = new StringBuffer();
        binding.tvAddress2.setText(stringBuffer.append(dataSelectedAddress.getState())
                .append(" , ").append(dataSelectedAddress.getCity()).append(" , ").append(dataSelectedAddress.getPincode()));
        binding.tvNumber.setText(dataSelectedAddress.getMobile());

        if (data.getSelectedPatient() != null && !data.getSelectedPatient().equals(" ")) {
            PatientData patientData = data.getSelectedPatient();
            binding.llSelectedPatient.setVisibility(View.VISIBLE);
            binding.tvPatientAge.setText(patientData.getAge());
            binding.tvPatientGender.setText(patientData.getGender());
            binding.tvPatientName.setText(patientData.getName());
            binding.tvEmail.setText(patientData.getEmail());
        }
        setViewsVisibility();
        binding.tvTimings.setText(data.getPharmacistCall());
        binding.btnContinue.setVisibility(View.VISIBLE);
        binding.btnSelectPaymentMode.setVisibility(View.GONE);
    }
}
