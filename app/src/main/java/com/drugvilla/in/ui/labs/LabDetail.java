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
import com.drugvilla.in.adapter.doctors.DoctorReviewAdapter;
import com.drugvilla.in.adapter.labs.CertificationAdapter;
import com.drugvilla.in.adapter.labs.PackageListingAdapter;
import com.drugvilla.in.adapter.labs.TestListingAdapter;
import com.drugvilla.in.databinding.ActivityLabDetailBinding;
import com.drugvilla.in.listener.OnSelectedListener;
import com.drugvilla.in.model.Document;
import com.drugvilla.in.model.ReviewData;
import com.drugvilla.in.model.lab.LabCertification;
import com.drugvilla.in.model.lab.labs.LabResponse;
import com.drugvilla.in.model.lab.packages.PackageData;
import com.drugvilla.in.model.lab.test.TestData;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class LabDetail extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    private ActivityLabDetailBinding binding;
    private List<Document> data = new ArrayList<>();
    private List<Document> certificationData = new ArrayList<>();
    private final String[] name = {"Pankaj Kumar", "Tarun Chaturvedi"};
    private List<Document> listData = new ArrayList<>();
    boolean expand = false;
    // lab tests
    private final String[] testName = {"ACE (Absolute Eosinophil Count)", "Thyroid Profile", "Liver Function Test", "Beta Hcg/Hcg Beta Subunit"
    };
    private final String[] testBy = {"By Delhi Path Lab", "By Sure path labs pvt ltd", "By Thyrocare Laboratories Ltd.", "By Delhi Path Lab"
    };
    private final String[] testPrice = {"1600", "400", "750", "500"};
    // health packages
    private final String[] packageName = {"Advance Health Care", "Healthy Heart", "Complete Care"};
    private final String[] packageBy = {"By Delhi Path Lab", "By Delhi Path Lab", "By Sure path labs pvt ltd"};
    private final String[] packagePrice = {"1600", "400", "750"};
    private final String[] tvCertification = {"ISO", "NABL", "NABH"};
    private final int[] ivCertification = {R.drawable.certification2, R.drawable.certification1, R.drawable.certification3};

    private String lab_id;
    private List<LabCertification> labCertificationList = new ArrayList<>();
    private List<ReviewData> reviewDataList = new ArrayList<>();
    private List<TestData> testDataList = new ArrayList<>();
    private List<PackageData> packageDataList = new ArrayList<>();
    private CertificationAdapter certificationAdapter;
    private DoctorReviewAdapter reviewAdapter;
    private TestListingAdapter testListingAdapter;
    private PackageListingAdapter packageListingAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_lab_detail);
        context = LabDetail.this;
        getData();
        setToolbar();
        setViews();



        binding.swipeRefreshingLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getLabData();
                binding.swipeRefreshingLayout.setRefreshing(false);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getLabData();
        setTestAdapter();
        setPackageAdapter();
        setCertificationAdapter();
        setreviewAdapter();
    }

    private void getData() {
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.getStringExtra("LAB_ID") != null && !intent.getStringExtra("LAB_ID").isEmpty()) {
                lab_id = intent.getStringExtra("LAB_ID");
            }
        }
    }

    private void setViews() {
        binding.tvShowMore.setOnClickListener(this);
        binding.tvPackages.setOnClickListener(this);
        binding.tvTests.setOnClickListener(this);
        binding.btnGoToCart.setOnClickListener(this);
        binding.view1.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        setTestAdapter();
    }

    private void setToolbar() {
        binding.menubar.tvTitle.setVisibility(View.VISIBLE);
        binding.menubar.ivRight.setVisibility(View.VISIBLE);
        binding.menubar.tvTitle.setText(getResources().getString(R.string.lab_details));
        binding.menubar.ivBack.setImageResource(R.drawable.ic_keyboard_backspace_black_24dp);
        binding.menubar.ivBack.setOnClickListener(this);
        binding.menubar.ivRight.setOnClickListener(this);
        binding.menubar.ivRight.setBadgeValue(3);
    }

    private void setTestAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
        binding.rvListing.setLayoutManager(layoutManager);
        binding.rvListing.hasFixedSize();
        testListingAdapter = new TestListingAdapter(context, testDataList, new OnSelectedListener() {
            @Override
            public void onClick(View view, int position) {
                setBottomView();
            }
        });
        binding.rvListing.setAdapter(testListingAdapter);
    }

    private void setPackageAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
        binding.rvListing.setLayoutManager(layoutManager);
        binding.rvListing.hasFixedSize();
        packageListingAdapter = new PackageListingAdapter(context, packageDataList, new OnSelectedListener() {
            @Override
            public void onClick(View view, int position) {
                setBottomView();
            }
        });
        binding.rvListing.setAdapter(packageListingAdapter);
    }


    private void setreviewAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
        binding.rvReview.setLayoutManager(layoutManager);
        binding.rvReview.hasFixedSize();
        binding.rvReview.setItemAnimator(new DefaultItemAnimator());
        reviewAdapter = new DoctorReviewAdapter(context, reviewDataList, AppConstant.TYPE_LABS);
        binding.rvReview.setAdapter(reviewAdapter);
        CommonUtils.setListCount(reviewDataList, binding.tvLabReviewsCount, "Lab Reviews ");

    }


    private void setCertificationAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
        binding.rvCertifications.setLayoutManager(layoutManager);
        binding.rvCertifications.hasFixedSize();
        certificationAdapter = new CertificationAdapter(context, labCertificationList);
        binding.rvCertifications.setAdapter(certificationAdapter);
    }

    private void setBottomView() {
        binding.llBottomView.setVisibility(View.VISIBLE);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, 0, 90);
        binding.rvReview.setLayoutParams(layoutParams);
    }

    private void setViewColor(View selected, View unSelected, TextView tvSelected, TextView tvUnselected) {
        selected.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        unSelected.setBackgroundColor(getResources().getColor(R.color.color_cccccc));
        tvSelected.setTextColor(getResources().getColor(R.color.colorAccent));
        tvUnselected.setTextColor(getResources().getColor(R.color.colorBlack));

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivRight:
                ActivityController.startActivity(context, Cart.class, AppConstant.TYPE_LAB_CART);
                break;
            case R.id.ivBack:
                finish();
                break;
            case R.id.tvTests:
                setViewColor(binding.view1, binding.view2, binding.tvTests, binding.tvPackages);
                setTestAdapter();
                break;
            case R.id.tvPackages:
                setViewColor(binding.view2, binding.view1, binding.tvPackages, binding.tvTests);
                setPackageAdapter();
                break;
            case R.id.btnGoToCart:
                ActivityController.startActivity(context, Cart.class, AppConstant.TYPE_LAB_CART);
                break;
            case R.id.tvShowMore:
                if (!expand) {
                    expand = true;
                    binding.tvDescription.setMaxLines(Integer.MAX_VALUE);
                    binding.tvShowMore.setText(getResources().getString(R.string.show_less));
                } else {
                    binding.tvDescription.setMaxLines(6);
                    binding.tvShowMore.setText(getResources().getString(R.string.show_more));
                    expand = false;
                }
                break;
            default:
                break;
        }
    }


    private void getLabData() {

        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                Api api = RequestController.createService(context, Api.class);
                api.getLabDetail(lab_id).enqueue(new BaseCallback<LabResponse>(context) {
                    @Override
                    public void onSuccess(LabResponse response) {
                        binding.swipeRefreshingLayout.setRefreshing(false);
                        ProgressDialogUtils.dismiss();
                        if (response != null) {
                            if (response.getStatus() == 1) {
                                CommonUtils.showToastShort(context, response.getMessage());

                                com.drugvilla.in.model.lab.labs.LabDetail labDetail = response.getLabDetail();
                                setData(labDetail);

                                if (labDetail.getTestProvidedList() != null
                                        && !labDetail.getTestProvidedList().isEmpty()) {
                                    testDataList.clear();
                                    testDataList.addAll(labDetail.getTestProvidedList());
                                    testListingAdapter.notifyDataSetChanged();
                                } else {
                                    testDataList.clear();
                                    testListingAdapter.notifyDataSetChanged();
                                }

                                if (labDetail.getPackageProvidedList() != null
                                        && !labDetail.getPackageProvidedList().isEmpty()) {
                                    packageDataList.clear();
                                    packageDataList.addAll(labDetail.getPackageProvidedList());
                                    packageListingAdapter.notifyDataSetChanged();
                                } else {
                                    packageDataList.clear();
                                    packageListingAdapter.notifyDataSetChanged();
                                }

                                if (labDetail.getLabCertificationList() != null
                                        && !labDetail.getLabCertificationList().isEmpty()) {
                                    labCertificationList.clear();
                                    labCertificationList.addAll(labDetail.getLabCertificationList());
                                    certificationAdapter.notifyDataSetChanged();
                                } else {
                                    labCertificationList.clear();
                                    certificationAdapter.notifyDataSetChanged();
                                }

                                if (labDetail.getLabReviewList() != null
                                        && !labDetail.getLabReviewList().isEmpty()) {
                                    reviewDataList.clear();
                                    reviewDataList.addAll(labDetail.getLabReviewList());
                                    reviewAdapter.notifyDataSetChanged();
                                } else {
                                    reviewDataList.clear();
                                    reviewAdapter.notifyDataSetChanged();
                                }

                            } else {
                                CommonUtils.showToastShort(context, response.getMessage());
                            }
                        }
                    }

                    @Override
                    public void onFail(Call<LabResponse> call, BaseResponse baseResponse) {
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

    private void setData(com.drugvilla.in.model.lab.labs.LabDetail labDetail) {
        binding.tvLabName.setText(labDetail.getLab_name());
        binding.tvLocation.setText(labDetail.getAddress());
        binding.tvRating.setText(labDetail.getLab_rating());
        binding.tvDescription.setText(labDetail.getLab_description());
        if (labDetail.getLab_image() != null && !labDetail.getLab_image().isEmpty()) {
            Picasso.with(context)
                    .load(labDetail.getLab_image())
                    .error(R.color.transparent)
                    .placeholder(R.color.transparent)
                    .into(binding.ivLabImage);
        }
    }

}

