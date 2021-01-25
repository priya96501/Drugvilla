package com.drugvilla.in.ui.order;

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
import com.drugvilla.in.adapter.doctors.MyAppointmentAdapter;
import com.drugvilla.in.adapter.labs.MyLabTestAdapter;
import com.drugvilla.in.adapter.order.MyOrderAdapter;
import com.drugvilla.in.databinding.ActivityMyOrderBinding;
import com.drugvilla.in.model.appointment.myAppointment.AppointmentData;
import com.drugvilla.in.model.appointment.myAppointment.AppointmentListResponse;
import com.drugvilla.in.model.lab.myTest.MyTestData;
import com.drugvilla.in.model.lab.myTest.MyTestListResponse;
import com.drugvilla.in.model.order.myOrder.MyOrderData;
import com.drugvilla.in.model.order.myOrder.MyOrderListData;
import com.drugvilla.in.model.order.myOrder.MyOrderListResponse;
import com.drugvilla.in.service.Api;
import com.drugvilla.in.service.RequestController;
import com.drugvilla.in.utils.AppConstant;
import com.drugvilla.in.utils.CommonUtils;
import com.drugvilla.in.utils.ProgressDialogUtils;
import com.drugvilla.in.utils.SharedPref;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyOrder extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    private Activity activity;
    private ActivityMyOrderBinding binding;
    private String from = " ";

    private MyAppointmentAdapter myAppointmentAdapter;
    private MyOrderAdapter myOrderAdapter;
    private MyLabTestAdapter mylabTestAdapter;
    private List<AppointmentData> appointmentData = new ArrayList<>();
    private List<MyOrderListData> myTestDataList = new ArrayList<>();
    private List<MyOrderListData> myOrderDataList = new ArrayList<>();


    // Index from which pagination should start (0 is 1st page in our case)
    private static final int PAGE_START = 1;
    // Indicates if footer ProgressBar is shown (i.e. next page is loading)
    private boolean isLoading = false;
    // If current page is the last page (Pagination will stop after this page load)
    private boolean isLastPage = false;
    // indicates the current page which Pagination is fetching.
    private int currentPage = PAGE_START;
    private Api api;
    final int pageSize = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_order);
        context = MyOrder.this;
        activity = MyOrder.this;
        getData();
        setToolbar();
        binding.swipeRefreshingLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (from.equalsIgnoreCase(AppConstant.MY_ORDERS)) {
                    // TODO : pagination
                    getMyOrders();
                } else if (from.equalsIgnoreCase(AppConstant.MY_LAB_TESTS)) {
                    // TODO : pagination
                    getMyLabTest();
                } else {
                    getMyAppointments();
                }
            }


        });
    }


    @Override
    protected void onResume() {
        // case1 :  my orders
        if (from.equalsIgnoreCase(AppConstant.MY_ORDERS)) {
            getMyOrders();
            setOrderAdapter();
        }
        // case2 :  my lab tests
        else if (from.equalsIgnoreCase(AppConstant.MY_LAB_TESTS)) {
            getMyLabTest();
            setLabAdapter();
        }
        // case3 :  my appointments
        else {
            getMyAppointments();
            setAppointmentAdapter();
        }
        super.onResume();
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
        binding.menubar.ivBack.setImageResource(R.drawable.ic_keyboard_backspace_black_24dp);
        binding.menubar.ivBack.setOnClickListener(this);
        if (from.equalsIgnoreCase(AppConstant.MY_ORDERS)) {
            binding.menubar.tvTitle.setText(R.string.my_orders);
        } else if (from.equalsIgnoreCase(AppConstant.MY_LAB_TESTS)) {
            binding.menubar.tvTitle.setText(R.string.my_lab_tests);
        } else {
            binding.menubar.tvTitle.setText(getResources().getString(R.string.my_appointment));
        }
    }

    private void setEmptyLayout(boolean value, String type) {
        if (value) {
            binding.emptyLayout.llRoot.setVisibility(View.VISIBLE);
            binding.llData.setVisibility(View.GONE);
            binding.emptyLayout.tvSubHeading.setVisibility(View.VISIBLE);
            binding.emptyLayout.btnSubmit.setVisibility(View.GONE);
            if (type.equalsIgnoreCase("Appointment")) {
                binding.emptyLayout.ivImage.setImageResource(R.drawable.nobookappointment);
                // binding.emptyLayout.btnSubmit.setText(getResources().getString(R.string.book_appointment));
                binding.emptyLayout.tvHeading.setText(getResources().getString(R.string.no_appointment_booked));
                binding.emptyLayout.tvSubHeading.setText(getResources().getString(R.string.order_desc_appointment));
            } else if (type.equalsIgnoreCase("LabTest")) {
                binding.emptyLayout.ivImage.setImageResource(R.drawable.no_lab_record);
                //  binding.emptyLayout.btnSubmit.setText(getResources().getString(R.string.book_lab_tests));
                binding.emptyLayout.tvHeading.setText(getResources().getString(R.string.no_lab_record));
                binding.emptyLayout.tvSubHeading.setText(getResources().getString(R.string.order_desc_lab));
            } else {
                binding.emptyLayout.ivImage.setImageResource(R.drawable.cancelorder);
                // binding.emptyLayout.btnSubmit.setText(getResources().getString(R.string.order_now));
                binding.emptyLayout.tvHeading.setText(getResources().getString(R.string.no_available_order));
                binding.emptyLayout.tvSubHeading.setText(getResources().getString(R.string.order_desc));
            }
        } else {
            binding.emptyLayout.llRoot.setVisibility(View.GONE);
            binding.llData.setVisibility(View.VISIBLE);
        }
    }

    private void setAppointmentAdapter() {
        final LinearLayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
        binding.rvMyOrders.setLayoutManager(layoutManager);
        binding.rvMyOrders.hasFixedSize();
        binding.rvMyOrders.setItemAnimator(new DefaultItemAnimator());
        appointmentData.clear();
        myAppointmentAdapter = new MyAppointmentAdapter(activity, appointmentData);
        binding.rvMyOrders.setAdapter(myAppointmentAdapter);
       /* binding.rvMyOrders.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                // number of visible items
                int visibleItemCount = layoutManager.getChildCount();
                // number of items in layout
                int totalItemCount = layoutManager.getItemCount();
                // the position of first visible item
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                boolean isNotLoadingAndNotLastPage = !isLoading && !isLastPage;
                // flag if number of visible items is at the last
                boolean isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount;
                // validate non negative values
                boolean isValidFirstItem = firstVisibleItemPosition >= 0;
                // validate total items are more than possible visible items
                boolean totalIsMoreThanVisible = totalItemCount >= pageSize;
                // flag to know whether to load more
                boolean shouldLoadMore = isValidFirstItem && isAtLastItem && totalIsMoreThanVisible && isNotLoadingAndNotLastPage;

                if (shouldLoadMore) loadMoreItems(false);
            }
        });

        // load the first page
        loadMoreItems(true);*/
    }
    /*private void loadMoreItems(final boolean isFirstPage) {
        // change loading state
        isLoading = true;
        currentPage = currentPage + 1;

        api.getAllApointment(SharedPref.getStringPreferences(context, AppConstant.USER_ID),currentPage).enqueue(new Callback<AppointmentListResponse>() {
            @Override
            public void onResponse(@NonNull Call<AppointmentListResponse> call, @NonNull Response<AppointmentListResponse> response) {
                if (response.body() != null) {
                    List<AppointmentData> result = response.body().getAppointmentListData();

                    if (result == null) return;
                    else if (!isFirstPage) myAppointmentAdapter.addAll(result);
                    else myAppointmentAdapter.setList(result);

                    isLoading = false;
                    isLastPage = currentPage == response.body().getPagination().getMaxPage();
                }
            }

            @Override
            public void onFailure(@NonNull Call<AppointmentListResponse> call, @NonNull Throwable t) {
                CommonUtils.showToastShort(context, getResources().getString(R.string.failure));
            }
        });

    }*/
    private void setOrderAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
        binding.rvMyOrders.setLayoutManager(layoutManager);
        binding.rvMyOrders.hasFixedSize();
        binding.rvMyOrders.setItemAnimator(new DefaultItemAnimator());
        myOrderAdapter = new MyOrderAdapter(activity, myOrderDataList);
        binding.rvMyOrders.setAdapter(myOrderAdapter);
    }

    private void setLabAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
        binding.rvMyOrders.setLayoutManager(layoutManager);
        binding.rvMyOrders.hasFixedSize();
        binding.rvMyOrders.setItemAnimator(new DefaultItemAnimator());
        mylabTestAdapter = new MyLabTestAdapter(activity, myTestDataList);
        binding.rvMyOrders.setAdapter(mylabTestAdapter);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                finish();
                break;
            default:
                break;
        }
    }


    private void getMyAppointments() {
        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                 api = RequestController.createService(context, Api.class);
                api.getAllApointment(SharedPref.getStringPreferences(context, AppConstant.USER_ID),currentPage).enqueue(new Callback<AppointmentListResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<AppointmentListResponse> call, @NonNull Response<AppointmentListResponse> response) {
                        binding.swipeRefreshingLayout.setRefreshing(false);
                        ProgressDialogUtils.dismiss();
                        if (response.body() != null) {
                            if (response.body().getStatus() == 1) {
                                CommonUtils.showToastShort(context, response.body().getMessage());
                                if (response.body().getAppointmentListData() != null
                                        && !response.body().getAppointmentListData().isEmpty()) {
                                    setEmptyLayout(false, "Appointment");
                                    appointmentData.clear();
                                    appointmentData.addAll(response.body().getAppointmentListData());
                                } else {
                                    setEmptyLayout(true, "Appointment");
                                    appointmentData.clear();
                                }

                            } else {
                                setEmptyLayout(true, "Appointment");
                                appointmentData.clear();
                                CommonUtils.showToastShort(context, response.body().getMessage());
                            }
                        } else {
                            CommonUtils.showToastShort(context, getResources().getString(R.string.no_response));
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<AppointmentListResponse> call, @NonNull Throwable t) {
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

    private void getMyLabTest() {
        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                 api = RequestController.createService(context, Api.class);
                api.getMyTestList(SharedPref.getStringPreferences(context, AppConstant.USER_ID),currentPage)
                        .enqueue(new Callback<MyOrderListResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<MyOrderListResponse> call, @NonNull Response<MyOrderListResponse> response) {
                        binding.swipeRefreshingLayout.setRefreshing(false);
                        ProgressDialogUtils.dismiss();
                        if (response.body() != null) {
                            if (response.body().getStatus() == 1) {
                                CommonUtils.showToastShort(context, response.body().getMessage());
                                if (response.body().getMyOrderData() != null && !response.body().getMyOrderData().isEmpty()) {
                                    setEmptyLayout(false, "LabTest");
                                    myTestDataList.clear();
                                    myTestDataList.addAll(response.body().getMyOrderData());
                                    mylabTestAdapter.notifyDataSetChanged();
                                } else {
                                    setEmptyLayout(true, "LabTest");
                                    myTestDataList.clear();
                                    mylabTestAdapter.notifyDataSetChanged();
                                }

                            } else {
                                setEmptyLayout(true, "LabTest");
                                myTestDataList.clear();
                                mylabTestAdapter.notifyDataSetChanged();
                                CommonUtils.showToastShort(context, response.body().getMessage());
                            }
                        } else {
                            CommonUtils.showToastShort(context, getResources().getString(R.string.no_response));
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<MyOrderListResponse> call, @NonNull Throwable t) {
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

    private void getMyOrders() {
        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                 api = RequestController.createService(context, Api.class);
                api.getMyOrderList("54"/*SharedPref.getStringPreferences(context, AppConstant.USER_ID)*/,currentPage)
                        .enqueue(new Callback<MyOrderListResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<MyOrderListResponse> call, @NonNull Response<MyOrderListResponse> response) {
                        binding.swipeRefreshingLayout.setRefreshing(false);
                        ProgressDialogUtils.dismiss();
                        if (response.body() != null) {
                            if (response.body().getStatus() == 1) {
                                CommonUtils.showToastShort(context, response.body().getMessage());
                                if (response.body().getMyOrderData() != null && !response.body().getMyOrderData().isEmpty()) {
                                    setEmptyLayout(false, "Order");
                                    myOrderDataList.clear();
                                    myOrderDataList.addAll(response.body().getMyOrderData());
                                   // myOrderAdapter.notifyDataSetChanged();
                                } else {
                                    setEmptyLayout(true, "Order");
                                    myOrderDataList.clear();
                                  //  myOrderAdapter.notifyDataSetChanged();
                                }

                                setOrderAdapter();

                            } else {
                                setEmptyLayout(true, "Order");
                                myOrderDataList.clear();
                               // myOrderAdapter.notifyDataSetChanged();
                                CommonUtils.showToastShort(context, response.body().getMessage());
                            }
                        } else {
                            CommonUtils.showToastShort(context, getResources().getString(R.string.no_response));
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<MyOrderListResponse> call, @NonNull Throwable t) {
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
