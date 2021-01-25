package com.drugvilla.in.ui.order.orderDetail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.drugvilla.in.R;
import com.drugvilla.in.adapter.blog.MyAdapter;
import com.drugvilla.in.databinding.ActivityOrderDetailsBinding;
import com.drugvilla.in.listener.FragmentSelectionListener;
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
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

import retrofit2.Call;

public class OrderDetails extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    private ActivityOrderDetailsBinding binding;
    private String from = " ", order_ID;
    private OrderSummaryFragment orderFragment;
    private OrderItemsFragment Itemfragment;
    private MyOrderData myOrderData;
    private MyTestData myTestData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order_details);
        context = OrderDetails.this;
        getData();
        setToolbar();
        setViews();
        setTabLayout();

    }

    private void setViews() {
        if (from.equalsIgnoreCase(AppConstant.TYPE_LABS)) {
          //  getMyLabDetail();
        } else {
           // getMyOrderDetail();
        }
    }

    private void getData() {
        Bundle bundle = new Bundle();
        bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.getString(AppConstant.FROM) != null && !Objects.requireNonNull(bundle.getString(AppConstant.FROM)).isEmpty()) {
                from = bundle.getString(AppConstant.FROM);
            }
            if (bundle.getString("ORDER_ID") != null && !Objects.requireNonNull(bundle.getString("ORDER_ID")).isEmpty()) {
                order_ID = bundle.getString("ORDER_ID");
            }
        }
    }

    private void setToolbar() {
        binding.menubar.tvTitle.setVisibility(View.VISIBLE);
        binding.menubar.tvTitle.setText(R.string.order_details);
        binding.menubar.ivBack.setImageResource(R.drawable.ic_keyboard_backspace_black_24dp);
        binding.menubar.ivBack.setOnClickListener(this);

    }

    private void setTabLayout() {
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Summary"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Items"));
        MyAdapter adapter = new MyAdapter(this, getSupportFragmentManager(), binding.tabLayout.getTabCount(), new FragmentSelectionListener() {
            @Override
            public Fragment onClick(int position) {
                switch (position) {
                    case 0:
                        if (from.equalsIgnoreCase(AppConstant.TYPE_LABS)) {
                            orderFragment = new OrderSummaryFragment(context, AppConstant.TYPE_LABS, order_ID);
                            return orderFragment;
                        } else if (from.equalsIgnoreCase(AppConstant.ORDER_MEDICINES_FROM_PRESCRIPTION)) {
                            orderFragment = new OrderSummaryFragment(context, AppConstant.ORDER_MEDICINES_FROM_PRESCRIPTION, order_ID);
                            return orderFragment;
                        } else if (from.equalsIgnoreCase(AppConstant.BOOK_LAB_TEST_FROM_PRESCRIPTION)) {
                            orderFragment = new OrderSummaryFragment(context, AppConstant.BOOK_LAB_TEST_FROM_PRESCRIPTION, order_ID);
                            return orderFragment;
                        } else {
                            orderFragment = new OrderSummaryFragment(context, AppConstant.TYPE_PRODUCT, order_ID);
                            return orderFragment;
                        }

                    case 1:

                        if (from.equalsIgnoreCase(AppConstant.TYPE_LABS)) {
                            Itemfragment = new OrderItemsFragment(context, AppConstant.TYPE_LABS,order_ID);
                            return Itemfragment;
                        } else if (from.equalsIgnoreCase(AppConstant.ORDER_MEDICINES_FROM_PRESCRIPTION)) {
                            Itemfragment = new OrderItemsFragment(context, AppConstant.ORDER_MEDICINES_FROM_PRESCRIPTION,order_ID);
                            return Itemfragment;
                        } else if (from.equalsIgnoreCase(AppConstant.BOOK_LAB_TEST_FROM_PRESCRIPTION)) {
                            Itemfragment = new OrderItemsFragment(context, AppConstant.BOOK_LAB_TEST_FROM_PRESCRIPTION,order_ID);
                            return Itemfragment;
                        } else {
                            Itemfragment = new OrderItemsFragment(context, AppConstant.TYPE_PRODUCT,order_ID);
                            return Itemfragment;

                        }

                    default:
                        return null;
                }

            }
        });
        binding.viewPager.setAdapter(adapter);
        binding.viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(binding.tabLayout));
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.ivBack) {
            finish();
        }
    }

    private void getMyLabDetail() {
        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                Api api = RequestController.createService(context, Api.class);
                api.getMyTestDetail(SharedPref.getStringPreferences(context, AppConstant.USER_ID), order_ID)
                        .enqueue(new BaseCallback<MyTestDetailResponse>(context) {
                    @Override
                    public void onSuccess(MyTestDetailResponse response) {
                        ProgressDialogUtils.dismiss();
                        if (response != null) {
                            if (response.getStatus() == 1) {
                                CommonUtils.showToastShort(context, response.getMessage());
                                if (response.getMyTestData() != null) {
                                    myTestData = response.getMyTestData();
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

    private void getMyOrderDetail() {
        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                Api api = RequestController.createService(context, Api.class);
                api.getMyOrderDetail(SharedPref.getStringPreferences(context, AppConstant.USER_ID), order_ID).enqueue(new BaseCallback<MyOrderDetailResponse>(context) {
                    @Override
                    public void onSuccess(MyOrderDetailResponse response) {
                        ProgressDialogUtils.dismiss();
                        if (response != null) {
                            if (response.getStatus() == 1) {
                                CommonUtils.showToastShort(context, response.getMessage());
                                if (response.getMyOrderData() != null) {
                                    myOrderData = response.getMyOrderData();
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
}
