package com.drugvilla.in.ui.labs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.opengl.Visibility;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.drugvilla.in.R;
import com.drugvilla.in.adapter.doctors.DoctorReviewAdapter;
import com.drugvilla.in.adapter.labs.CertificationAdapter;

import com.drugvilla.in.databinding.ActivityPackageDetailBinding;
import com.drugvilla.in.model.ReviewData;
import com.drugvilla.in.model.lab.LabCertification;
import com.drugvilla.in.model.lab.packages.PackageDetail;
import com.drugvilla.in.model.lab.packages.PackageResponse;

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

public class PackageDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    private ActivityPackageDetailBinding binding;
    private int flag = 1;
    private int addFlag = 0;
    boolean expand = false;
    private String package_id="";
    private List<LabCertification> labCertificationList = new ArrayList<>();
    private List<ReviewData> reviewDataList = new ArrayList<>();
    private CertificationAdapter certificationAdapter;
    private DoctorReviewAdapter reviewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_package_detail);
        context = PackageDetailActivity.this;
        getData();
        setToolbar();
        setListener();

        getPackageDetailApi();
        setCertificationAdapter();
        setreviewAdapter();
    }


    private void getData() {
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.getStringExtra("PACKAGE_ID") != null && !intent.getStringExtra("PACKAGE_ID").isEmpty()) {
                package_id = intent.getStringExtra("PACKAGE_ID");
            }
        }
    }

    private void setListener() {
        binding.description.setOnClickListener(this);
        binding.requirements.setOnClickListener(this);
        binding.testIncluded.setOnClickListener(this);
        binding.btnAdd.setOnClickListener(this);
        binding.btnGoToCart.setOnClickListener(this);
        binding.tvShowMore.setOnClickListener(this);
        binding.viewIncludedTest.setOnClickListener(this);
        binding.tvDescription.setVisibility(View.VISIBLE);
        binding.tvMRP.setPaintFlags(binding.tvMRP.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        setselectedBackground(binding.description);
    }

    private void setToolbar() {
        binding.menuBar.tvTitle.setVisibility(View.VISIBLE);
        binding.menuBar.ivRight.setVisibility(View.VISIBLE);
        binding.menuBar.tvTitle.setText(getResources().getString(R.string.package_details));
        binding.menuBar.ivBack.setImageResource(R.drawable.ic_keyboard_backspace_black_24dp);
        binding.menuBar.ivBack.setOnClickListener(this);
        binding.menuBar.ivRight.setOnClickListener(this);
        binding.menuBar.ivRight.setBadgeValue(3);
    }

    private void setreviewAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
        binding.rvReview.setLayoutManager(layoutManager);
        binding.rvReview.hasFixedSize();
        binding.rvReview.setItemAnimator(new DefaultItemAnimator());
        reviewAdapter = new DoctorReviewAdapter(context, reviewDataList, AppConstant.TYPE_LABS);
        binding.rvReview.setAdapter(reviewAdapter);
        CommonUtils.setListCount(reviewDataList, binding.tvLabReviews, "Lab Reviews ");

    }

    private void setCertificationAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
        binding.rvCertifications.setLayoutManager(layoutManager);
        binding.rvCertifications.hasFixedSize();
        certificationAdapter = new CertificationAdapter(context, labCertificationList);
        binding.rvCertifications.setAdapter(certificationAdapter);
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

    private void setViews(int visibility1, int visibility2, int visibility3) {
        binding.tvDescription.setVisibility(visibility1);
        binding.tvRequirements.setVisibility(visibility2);
        binding.TvTestIncluded.setVisibility(visibility3);
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
            case R.id.viewIncludedTest:
                flag = 3;
                setselectedBackground(binding.testIncluded);
                setUnselectedBackground(binding.requirements, binding.description);
                setViews(View.GONE, View.GONE, View.VISIBLE);
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
                    binding.llLabReviews.setLayoutParams(layoutParams);
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
                //  setRequirementAdapter(names2);
                setViews(View.GONE, View.VISIBLE, View.GONE);
                break;
            case R.id.test_included:
                flag = 3;
                setselectedBackground(binding.testIncluded);
                setUnselectedBackground(binding.requirements, binding.description);
                //setRequirementAdapter(tests2);
                setViews(View.GONE, View.GONE, View.VISIBLE);
                break;
            case R.id.tvShowMore:
                if (!expand) {
                    expand = true;
                    binding.tvLabDescription.setMaxLines(Integer.MAX_VALUE);
                    binding.tvShowMore.setText(getResources().getString(R.string.show_less));
                } else {
                    binding.tvLabDescription.setMaxLines(4);
                    binding.tvShowMore.setText(getResources().getString(R.string.show_more));
                    expand = false;
                }
                break;
            default:
                break;
        }
    }


    private void getPackageDetailApi() {
        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                Api api = RequestController.createService(context, Api.class);
                api.getPackageDetail(package_id).enqueue(new BaseCallback<PackageResponse>(context) {
                    @Override
                    public void onSuccess(PackageResponse response) {
                        ProgressDialogUtils.dismiss();
                        if (response != null) {
                            if (response.getStatus() == 1) {
                                CommonUtils.showToastShort(context, response.getMessage());
                                PackageDetail packagesData = response.getPackageDetail();
                                setData(packagesData);

                                if (packagesData.getLabCertificationList() != null
                                        && !packagesData.getLabCertificationList().isEmpty()) {
                                    labCertificationList.clear();
                                    labCertificationList.addAll(packagesData.getLabCertificationList());
                                    certificationAdapter.notifyDataSetChanged();
                                } else {
                                    labCertificationList.clear();
                                    certificationAdapter.notifyDataSetChanged();
                                }

                                if (packagesData.getLabReviewList() != null
                                        && !packagesData.getLabReviewList().isEmpty()) {
                                    reviewDataList.clear();
                                    reviewDataList.addAll(packagesData.getLabReviewList());
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
                    public void onFail(Call<PackageResponse> call, BaseResponse baseResponse) {
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

    private void setData(PackageDetail packagesData) {
        binding.tvTitle.setText(packagesData.getPackage_name());
        String test_count = String.format("%02d", packagesData.getTestIncludedList().size());
        binding.tvIncludeTest.setText("Includes " + test_count + " tests");
        binding.tvBy.setText(packagesData.getProvided_by_labs());
        //binding.tvCertifications.setText(packagesData.getl());
        binding.tvAmount.setText(packagesData.getAmount());
        binding.tvOff.setText(packagesData.getDiscount());
        binding.tvRating.setText(packagesData.getRating());
        binding.tvDescription.setText(packagesData.getDescription());
        binding.tvRequirements.setText(packagesData.getRequirement());
        binding.tvRating2.setText(packagesData.getLab_rating());
        binding.tvLabName.setText(packagesData.getLab_name());
        binding.tvLocation.setText(packagesData.getAddress());
        if (packagesData.getLab_image() != null && !packagesData.getLab_image().isEmpty()) {
            Picasso.with(context)
                    .load(packagesData.getLab_image())
                    .error(R.color.transparent)
                    .placeholder(R.color.transparent)
                    .into(binding.ivLabImage);
        }
        if (packagesData.getTestIncludedList() != null && !packagesData.getTestIncludedList().isEmpty()) {
            binding.TvTestIncluded.setText(packagesData.getTestIncludedList().toString()
                    .replace("[", "").replace("]", "").replace(",", "\n"));
        }
    }
}
