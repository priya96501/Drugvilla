package com.drugvilla.in.ui.order.orderDetail;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.drugvilla.in.R;
import com.drugvilla.in.adapter.order.CancellationReasonAdapter;
import com.drugvilla.in.adapter.labs.SelectedLabTestAdapter;
import com.drugvilla.in.adapter.order.SelectedProductAdapter;
import com.drugvilla.in.databinding.BottomCancellationResaonsBinding;
import com.drugvilla.in.databinding.FragmentOrderItemsBinding;
import com.drugvilla.in.databinding.WalletHelpBinding;
import com.drugvilla.in.listener.OnCheckSelectedListener;
import com.drugvilla.in.listener.OnSelectedListener;
import com.drugvilla.in.listener.OnSelectedTypeListener;
import com.drugvilla.in.model.Document;
import com.drugvilla.in.model.cart.SelectedItem;
import com.drugvilla.in.model.lab.myTest.MyTestData;
import com.drugvilla.in.model.lab.myTest.MyTestDetailResponse;
import com.drugvilla.in.model.order.myOrder.MyOrderData;
import com.drugvilla.in.model.order.myOrder.MyOrderDetailResponse;
import com.drugvilla.in.model.orderCancellation.CancellationData;
import com.drugvilla.in.model.orderCancellation.CancelllationResponse;
import com.drugvilla.in.service.Api;
import com.drugvilla.in.service.BaseCallback;
import com.drugvilla.in.service.BaseResponse;
import com.drugvilla.in.service.RequestController;
import com.drugvilla.in.utils.AppConstant;
import com.drugvilla.in.utils.CommonUtils;
import com.drugvilla.in.utils.DialogUtils;
import com.drugvilla.in.utils.ProgressDialogUtils;
import com.drugvilla.in.utils.SharedPref;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderItemsFragment extends Fragment implements View.OnClickListener {
    private FragmentOrderItemsBinding binding;
    private BottomSheetDialog dialog;
    // selected tests
    private List<Document> listData = new ArrayList<>();
    private List<SelectedItem> selectedTestDataList = new ArrayList<>();
    private SelectedLabTestAdapter testAdapter;
    private final String[] testName = {"ACE (Absolute Eosinophil Count)", "Advance Health Care", "Beta Hcg/Hcg Beta Subunit"};
    private final String[] labName = {"Sure path labs pvt ltd.", "SRL Limited", "Delhi Path Lab"};
    private final String[] price = {"500", "1250", "750"};
    private final String[] option = {"test", "package", "test"};

    // selected cancel reason
    private List<CancellationData> cancellationDataList = new ArrayList<>();
    private CancellationReasonAdapter reasonAdapter;

    // selected products
    private List<Document> listDataProduct = new ArrayList<>();
    private List<SelectedItem> selectedItemsList = new ArrayList<>();
    private SelectedProductAdapter selectedProductAdapter;

    private final int[] productImage = {R.drawable.sp3, R.drawable.sp1, R.drawable.sp2};
    private final String[] productName = {"Cadbury Bournvita Five Star Magic Health Drink - Pouch 500 gm", "Swadeshi Vatantak Vati Powder 50 gm", "Bioderma Cicabio Arnica+ Creme 40 ml"};
    private final String[] by = {"By Cadbury India Ltd", "By Swadeshi", "By Naos Skin Care India Pvt Ltd"};
    private final String[] mrp = {"500", "1250", "750"};
    private final String[] amount = {"300", "1150", "600"};

    private String orderId;
    private MyOrderData myOrderData;
    private MyTestData myTestData;

    public OrderItemsFragment(Context context, String type, String orderId) {
        this.context = context;
        this.type = type;
        this.orderId = orderId;
    }

    private Context context;
    private String type, reasonId;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_order_items, container, false);
        View view = binding.getRoot();
        setListener();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setData();
    }

    private void setData() {
        if (type.equalsIgnoreCase(AppConstant.TYPE_LABS)) {
            getCancellationReasonList("labs");
            getMyLabDetail(AppConstant.TYPE_LABS);
            binding.llSelectedLabSummary.setVisibility(View.VISIBLE);
            setSelectedTestAdapter();

        } else if (type.equalsIgnoreCase(AppConstant.ORDER_MEDICINES_FROM_PRESCRIPTION)) {
            getCancellationReasonList("order");
            getMyOrderDetail(AppConstant.ORDER_MEDICINES_FROM_PRESCRIPTION);

        } else if (type.equalsIgnoreCase(AppConstant.BOOK_LAB_TEST_FROM_PRESCRIPTION)) {
            getCancellationReasonList("labs");
            getMyLabDetail(AppConstant.BOOK_LAB_TEST_FROM_PRESCRIPTION);

        } else {
            getMyOrderDetail("ORDER");
            getCancellationReasonList("order");
            setSelectedProductAdapter();
        }
    }

    private void setRxViewsVisible(String heading, int image) {
        binding.llcallingDetails.setVisibility(View.VISIBLE);
        binding.llPaymentDetails.setVisibility(View.GONE);
        binding.llNoItems.setVisibility(View.VISIBLE);
        binding.ivNoItems.setImageResource(image);
        binding.tvNoItemDescription.setText(heading);
    }

    private void setListener() {
        binding.btnCancelOrder.setOnClickListener(this);
        //binding.btnNeedHelp.setOnClickListener(this);
    }


    private void setSelectedProductAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
        binding.rvProducts.setLayoutManager(layoutManager);
        binding.rvProducts.hasFixedSize();
        selectedProductAdapter = new SelectedProductAdapter(context, selectedItemsList, AppConstant.TYPE_ORDER_REVIEW, new OnSelectedListener() {
            @Override
            public void onClick(View view, int position) {

                // nothing
            }
        }, new OnSelectedTypeListener() {
            @Override
            public void onClick(View view, int position, String type) {
                // nothing
            }
        });
        binding.rvProducts.setAdapter(selectedProductAdapter);
    }

    private void setSelectedTestAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        binding.rvTestIncluded.setLayoutManager(layoutManager);
        binding.rvTestIncluded.hasFixedSize();

        testAdapter = new SelectedLabTestAdapter(getActivity(), selectedTestDataList, AppConstant.TYPE_ORDER_REVIEW, new OnSelectedListener() {
            @Override
            public void onClick(View view, int position) {
                //  nothing to do
            }
        });
        binding.rvTestIncluded.setAdapter(testAdapter);
    }

    private void showBottomSheetDialogNeedHelp() {
        final WalletHelpBinding walletBinding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.wallet_help, null, false);
        final BottomSheetDialog dialog = DialogUtils.createBottomDialog(context, walletBinding.getRoot());
        dialog.setCancelable(false);
        walletBinding.ivCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void showBottomSheetDialogCancelOrder() {
        final BottomCancellationResaonsBinding cancellationReasonBinding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.bottom_cancellation_resaons, null, false);
        final BottomSheetDialog dialog = DialogUtils.createBottomDialog(context, cancellationReasonBinding.getRoot());
        dialog.setCancelable(false);
        setCancellationReasonAdapter(cancellationReasonBinding.rvreasons);
        cancellationReasonBinding.ivCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        cancellationReasonBinding.btnCancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isReasonChoosen()) {
                    dialog.dismiss();
                    if (type.equalsIgnoreCase(AppConstant.TYPE_LABS)) {
                        cancelLabOrderApi();
                    } else if (type.equalsIgnoreCase(AppConstant.ORDER_MEDICINES_FROM_PRESCRIPTION)) {
                        cancelOrderApi();
                    } else if (type.equalsIgnoreCase(AppConstant.BOOK_LAB_TEST_FROM_PRESCRIPTION)) {
                        cancelLabOrderApi();
                    } else {
                        cancelOrderApi();
                    }
                } else {
                    Toast.makeText(getActivity(), "Reason not seletcted", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void setCancellationReasonAdapter(RecyclerView rvReasons) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        rvReasons.setLayoutManager(linearLayoutManager);
        rvReasons.hasFixedSize();
        rvReasons.setItemAnimator(new DefaultItemAnimator());
        reasonAdapter = new CancellationReasonAdapter(getActivity(), cancellationDataList, new OnCheckSelectedListener() {
            @Override
            public void onClick(View view, int position, boolean selected) {
                if (cancellationDataList.get(position).isSelected()) {
                    cancellationDataList.get(position).setSelected(false);
                } else {
                    for (int i = 0; i < cancellationDataList.size(); i++) {
                        cancellationDataList.get(i).setSelected(false);
                    }
                    cancellationDataList.get(position).setSelected(true);
                }
                reasonId = cancellationDataList.get(position).getReason_id();
                reasonAdapter.notifyDataSetChanged();
            }
        });
        rvReasons.setAdapter(reasonAdapter);
    }

    private boolean isReasonChoosen() {
        boolean checked = false;
        for (int i = 0; i < cancellationDataList.size(); i++) {
            if (cancellationDataList.get(i).isSelected()) {
                checked = true;
                break;
            } else {
                checked = false;
            }
        }
        return checked;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnCancelOrder:
                showBottomSheetDialogCancelOrder();
                break;
           /* case R.id.btnNeedHelp:
                showBottomSheetDialogNeedHelp();
                break;*/
            case R.id.ivCross:
                dialog.dismiss();
                break;
            default:
                break;
        }
    }

    // get order and lab test detail
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
                                            binding.deliveryCharges.setText(getResources().getString(R.string.delivery_charges));
                                            myOrderData = response.getMyOrderData();
                                            // setOrderData(myOrderData, type);
                                            if (type.equalsIgnoreCase("ORDER")) {
                                                binding.llSelectedProductSummary.setVisibility(View.VISIBLE);
                                                if (myOrderData.getSelectedItems() != null && !myOrderData.getSelectedItems().isEmpty()) {
                                                    selectedItemsList.clear();
                                                    selectedItemsList.addAll(myOrderData.getSelectedItems());
                                                    // selectedProductAdapter.notifyDataSetChanged();
                                                } else {
                                                    selectedItemsList.clear();
                                                    // selectedProductAdapter.notifyDataSetChanged();
                                                }

                                                setSelectedProductAdapter();

                                            } else {
                                                // ORDER_MEDICINES_FROM_PRESCRIPTION
                                                setRxViewsVisible(getResources().getString(R.string.we_are_confirming_your_prescription_s_nand_medicines), R.drawable.cancelorder);
                                            }


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
                                            // setLabData(myTestData, type);
                                            if (type.equalsIgnoreCase(AppConstant.TYPE_LABS)) {
                                                if (myTestData.getOrderItems() != null && !myTestData.getOrderItems().isEmpty()) {
                                                    selectedTestDataList.clear();
                                                    selectedTestDataList.addAll(myTestData.getOrderItems());
                                                    testAdapter.notifyDataSetChanged();
                                                } else {
                                                    selectedTestDataList.clear();
                                                    testAdapter.notifyDataSetChanged();
                                                }
                                            } else {// BOOK_LAB_TEST_FROM_PRESCRIPTION
                                                setRxViewsVisible("We will call you to confirm your requirements.", R.drawable.no_lab_record);
                                            }

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

    // cancel order and lab test
    private void getCancellationReasonList(String type) {

        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                Api api = RequestController.createService(context, Api.class);
                api.getCancellationReason(type).enqueue(new BaseCallback<CancelllationResponse>(context) {
                    @Override
                    public void onSuccess(CancelllationResponse response) {
                        ProgressDialogUtils.dismiss();
                        if (response != null) {
                            if (response.getStatus() == 1) {
                                CommonUtils.showToastShort(context, response.getMessage());
                                if (response.getCancellationData() != null && !response.getCancellationData().isEmpty()) {
                                    cancellationDataList.clear();
                                    cancellationDataList.addAll(response.getCancellationData());
                                } else {
                                    cancellationDataList.clear();
                                }

                            } else {

                                CommonUtils.showToastShort(context, response.getMessage());
                            }
                        } else {
                            CommonUtils.showToastShort(context, context.getResources().getString(R.string.no_response));
                        }
                    }

                    @Override
                    public void onFail(Call<CancelllationResponse> call, BaseResponse baseResponse) {
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

    private void cancelOrderApi() {
        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                Api api = RequestController.createService(context, Api.class);
                api.cancelOrder(SharedPref.getStringPreferences(context, AppConstant.USER_ID)
                        , orderId, reasonId).enqueue(new BaseCallback<BaseResponse>(context) {
                    @Override
                    public void onSuccess(BaseResponse response) {
                        ProgressDialogUtils.dismiss();
                        if (response != null) {
                            if (response.getStatus() == 1) {
                                CommonUtils.showToastShort(context, response.getMessage());
                                binding.btnCancelOrder.setVisibility(View.GONE);
                            } else {

                                CommonUtils.showToastShort(context, response.getMessage());
                            }
                        } else {
                            CommonUtils.showToastShort(context, context.getResources().getString(R.string.no_response));
                        }
                    }

                    @Override
                    public void onFail(Call<BaseResponse> call, BaseResponse baseResponse) {
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

    private void cancelLabOrderApi() {
        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                Api api = RequestController.createService(context, Api.class);
                api.cancelLab(SharedPref.getStringPreferences(context, AppConstant.USER_ID), orderId, reasonId).enqueue(new BaseCallback<BaseResponse>(context) {
                    @Override
                    public void onSuccess(BaseResponse response) {
                        ProgressDialogUtils.dismiss();
                        if (response != null) {
                            if (response.getStatus() == 1) {
                                CommonUtils.showToastShort(context, response.getMessage());
                                binding.btnCancelOrder.setVisibility(View.GONE);
                            } else {

                                CommonUtils.showToastShort(context, response.getMessage());
                            }
                        } else {
                            CommonUtils.showToastShort(context, context.getResources().getString(R.string.no_response));
                        }
                    }

                    @Override
                    public void onFail(Call<BaseResponse> call, BaseResponse baseResponse) {
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


    // setting order and labtest detail data
    private void setOrderData(MyOrderData myOrderData, String type) {
        if (type.equalsIgnoreCase("ORDER")) {

        } else {
            // ORDER_MEDICINES_FROM_PRESCRIPTION
            setRxViewsVisible(getResources().getString(R.string.we_are_confirming_your_prescription_s_nand_medicines), R.drawable.cancelorder);
        }
    }

    private void setLabData(MyTestData myTestData, String type) {
        if (type.equalsIgnoreCase(AppConstant.TYPE_LABS)) {
        } else {
            // BOOK_LAB_TEST_FROM_PRESCRIPTION
            setRxViewsVisible("We will call you to confirm your requirements.", R.drawable.no_lab_record);
        }

    }

}
