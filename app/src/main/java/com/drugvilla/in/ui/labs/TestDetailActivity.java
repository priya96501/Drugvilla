package com.drugvilla.in.ui.labs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.drugvilla.in.R;
import com.drugvilla.in.adapter.labs.SelectedLabAdapter;
import com.drugvilla.in.databinding.ActivityDetailsBinding;
import com.drugvilla.in.listener.OnCheckSelectedListener;
import com.drugvilla.in.model.Document;
import com.drugvilla.in.model.lab.labs.LabData;
import com.drugvilla.in.model.lab.test.TestDetail;
import com.drugvilla.in.model.lab.test.TestResponse;
import com.drugvilla.in.service.Api;
import com.drugvilla.in.service.BaseCallback;
import com.drugvilla.in.service.BaseResponse;
import com.drugvilla.in.service.RequestController;
import com.drugvilla.in.ui.order.Cart;
import com.drugvilla.in.utils.ActivityController;
import com.drugvilla.in.utils.AppConstant;
import com.drugvilla.in.utils.CommonUtils;
import com.drugvilla.in.utils.ProgressDialogUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class TestDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    private String from = " ";
    private ActivityDetailsBinding binding;
    private int flag = 1;
    private int addFlag = 0;
    private List<Document> packagesData = new ArrayList<>();
    private List<Document> listData = new ArrayList<>();
    // test requirements and test included
    private final String[] names = {"Fasting required", "Blood test sample"};
    private final String[] tests = {"T3", "T4", "TSH"};

    private String test_id;
    private List<LabData> providedByLabList = new ArrayList<>();
    private SelectedLabAdapter selectedLabAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_details);
        context = TestDetailActivity.this;
        getData();
        setToolbar();
        setListener();

        binding.swipeRefreshingLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getTestDetail();
            }

        });
    }


    @Override
    protected void onResume() {
        getTestDetail();
        setProvidedByLabsAdapter();
        super.onResume();
    }

    private void getData() {
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.getStringExtra("TEST_ID") != null && !intent.getStringExtra("TEST_ID").isEmpty()) {
                test_id = intent.getStringExtra("TEST_ID");
            }
        }
    }

    private void setProvidedByLabsAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
        binding.rvLabs.setLayoutManager(layoutManager);
        binding.rvLabs.hasFixedSize();
        binding.rvLabs.setItemAnimator(new DefaultItemAnimator());

        selectedLabAdapter = new SelectedLabAdapter(context, providedByLabList, new OnCheckSelectedListener() {
            @Override
            public void onClick(View view, int position, boolean selected) {
                if (providedByLabList.get(position).isSelected()) {
                    providedByLabList.get(position).setSelected(false);
                } else {
                    for (int i = 0; i < providedByLabList.size(); i++) {
                        providedByLabList.get(i).setSelected(false);
                    }
                    providedByLabList.get(position).setSelected(true);
                }
                selectedLabAdapter.notifyDataSetChanged();
                binding.llBottomView.setVisibility(View.VISIBLE);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(0, 0, 0, 80);
                binding.llProvidedByLabs.setLayoutParams(layoutParams);
                addFlag = 1;
                binding.btnAdd.setText(getResources().getString(R.string.go_to_cart));
                // TODO : by default one lab is already selected with lowest pricing ,
                //  if user want to change lab then click on select lab button and then that particular lab will be selected and detail of that selected lab will be updated at the top of the screen

            }
        }
        );
        binding.rvLabs.setAdapter(selectedLabAdapter);
    }


    private void setListener() {
        binding.description.setOnClickListener(this);
        binding.requirements.setOnClickListener(this);
        binding.testIncluded.setOnClickListener(this);
        binding.btnAdd.setOnClickListener(this);
        binding.btnGoToCart.setOnClickListener(this);
        binding.tvViewAll.setOnClickListener(this);
        binding.llProvidedBy.setOnClickListener(this);
        setselectedBackground(binding.description);
        binding.tvDescription.setVisibility(View.VISIBLE);
    }

    private void setselectedBackground(TextView textView) {
        textView.setBackground(getResources().getDrawable(R.drawable.back_red_square));
        textView.setTextColor(getResources().getColor(R.color.colorWhite));
    }

    private void setUnselectedBackground(TextView textView1, TextView textView2) {
        textView1.setBackground(getResources().getDrawable(R.drawable.gray_border));
        textView1.setTextColor(getResources().getColor(R.color.colorBlack));
        textView2.setBackground(getResources().getDrawable(R.drawable.gray_border));
        textView2.setTextColor(getResources().getColor(R.color.colorBlack));
    }

    private void setToolbar() {
        binding.menuBar.tvTitle.setVisibility(View.VISIBLE);
        binding.menuBar.ivRight.setVisibility(View.VISIBLE);
        binding.menuBar.tvTitle.setText(getResources().getString(R.string.test_details));
        binding.menuBar.ivBack.setImageResource(R.drawable.ic_keyboard_backspace_black_24dp);
        binding.menuBar.ivBack.setOnClickListener(this);
        binding.menuBar.ivRight.setOnClickListener(this);
        binding.menuBar.ivRight.setBadgeValue(3);
    }

    private void focusOnView() {
        binding.scrollView.post(new Runnable() {
            @Override
            public void run() {
                binding.scrollView.scrollTo(0, binding.llProvidedByLabs.getTop());
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivRight:
                ActivityController.startActivity(context, Cart.class, AppConstant.TYPE_LAB_CART);
                break;
            case R.id.llProvidedBy:
                focusOnView();
                break;
            case R.id.ivBack:
                finish();
                break;
            case R.id.tvViewAll:
                ActivityController.startActivity(context, SelectLab.class, AppConstant.TYPE_LAB_CART);
                break;
            case R.id.btnGoToCart:
                ActivityController.startActivity(context, Cart.class, AppConstant.TYPE_LAB_CART);
                break;
            case R.id.btnAdd:
                if (addFlag == 0) {
                    binding.btnAdd.setText(getResources().getString(R.string.go_to_cart));
                    binding.llBottomView.setVisibility(View.VISIBLE);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    layoutParams.setMargins(0, 0, 0, 90);
                    binding.llProvidedByLabs.setLayoutParams(layoutParams);
                    addFlag = 1;
                } else {
                    ActivityController.startActivity(context, Cart.class, AppConstant.TYPE_LAB_CART);
                    addFlag = 0;
                }
                break;
            case R.id.description:
                flag = 1;
                setselectedBackground(binding.description);
                setUnselectedBackground(binding.requirements, binding.testIncluded);
                setViews(View.VISIBLE, View.GONE, View.GONE);
                break;
            case R.id.requirements:
                flag = 2;
                setselectedBackground(binding.requirements);
                setUnselectedBackground(binding.description, binding.testIncluded);
                setViews(View.GONE, View.VISIBLE, View.GONE);
                break;
            case R.id.test_included:
                flag = 3;
                setselectedBackground(binding.testIncluded);
                setUnselectedBackground(binding.requirements, binding.description);
                setViews(View.GONE, View.GONE, View.VISIBLE);
                break;
            default:
                break;
        }
    }

    private void setViews(int visibility1, int visibility2, int visibility3) {
        binding.tvDescription.setVisibility(visibility1);
        binding.tvRequirements.setVisibility(visibility2);
        binding.TvTestIncluded.setVisibility(visibility3);
    }


    private void getTestDetail() {

        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                Api api = RequestController.createService(context, Api.class);
                api.getTestDetail(test_id).enqueue(new BaseCallback<TestResponse>(context) {
                    @Override
                    public void onSuccess(TestResponse response) {
                        binding.swipeRefreshingLayout.setRefreshing(false);
                        ProgressDialogUtils.dismiss();
                        if (response != null) {
                            if (response.getStatus() == 1) {
                                CommonUtils.showToastShort(context, response.getMessage());

                                if (response.getTestDetail() != null) {
                                    TestDetail testDetail = response.getTestDetail();
                                    setData(testDetail);
                                }

                                if (response.getTestDetail().getProvidedByLabsList() != null
                                        && !response.getTestDetail().getProvidedByLabsList().isEmpty()) {
                                    providedByLabList.clear();
                                    providedByLabList.addAll(response.getTestDetail().getProvidedByLabsList());
                                    selectedLabAdapter.notifyDataSetChanged();
                                } else {
                                    providedByLabList.clear();
                                    selectedLabAdapter.notifyDataSetChanged();
                                }

                            } else {
                                CommonUtils.showToastShort(context, response.getMessage());
                            }
                        }
                    }

                    @Override
                    public void onFail(Call<TestResponse> call, BaseResponse baseResponse) {
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

    private void setData(TestDetail testDetail) {
        binding.tvTitle.setText(testDetail.getTest_name());
        if (testDetail.getProvidedByLabsList() != null && !testDetail.getProvidedByLabsList().isEmpty()) {
            String test_count = String.format("%02d", testDetail.getProvidedByLabsList().size());
            binding.tvProvidedBy.setText("Provided by (" + test_count + ") Labs");
        }
        binding.tvBy.setText(testDetail.getSelected_lab_name());
        binding.tvCertifications.setText(testDetail.getSelected_lab_certification());
        binding.tvAmount.setText(testDetail.getAmount());
        binding.tvOff.setText(testDetail.getDiscount());
        binding.tvRating.setText(testDetail.getRating());
        binding.tvDescription.setText(testDetail.getDescription());
        binding.tvRequirements.setText(testDetail.getRequirement());
        binding.tvRating.setText(testDetail.getRating());
        if (testDetail.getTestIncludedList() != null && !testDetail.getTestIncludedList().isEmpty()) {
            binding.TvTestIncluded.setText(testDetail.getTestIncludedList().toString()
                    .replace("[", "").replace("]", "").replace(",", "\n"));
        }
    }
}
