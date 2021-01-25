package com.drugvilla.in.ui.doctors;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;

import com.drugvilla.in.R;
import com.drugvilla.in.adapter.doctors.DoctorReviewAdapter;
import com.drugvilla.in.databinding.ActivityDoctorProfileBinding;
import com.drugvilla.in.databinding.DialogDoctorRatingBinding;
import com.drugvilla.in.model.Document;
import com.drugvilla.in.model.doctor.DoctorData;
import com.drugvilla.in.model.doctor.DoctorResponse;
import com.drugvilla.in.model.ReviewData;
import com.drugvilla.in.service.Api;
import com.drugvilla.in.service.BaseCallback;
import com.drugvilla.in.service.BaseResponse;
import com.drugvilla.in.service.RequestController;
import com.drugvilla.in.utils.ActivityController;
import com.drugvilla.in.utils.AppConstant;
import com.drugvilla.in.utils.CommonUtils;
import com.drugvilla.in.utils.DialogUtils;
import com.drugvilla.in.utils.ProgressDialogUtils;
import com.drugvilla.in.utils.SharedPref;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoctorProfile extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    private ActivityDoctorProfileBinding binding;
    private String doctor_Id;
    private List<ReviewData> reviewDataList = new ArrayList<>();
    private DoctorReviewAdapter reviewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_doctor_profile);
        context = DoctorProfile.this;
        getData();
        setToolbar();
        getDoctorProfile();
        setAdapter();
    }


    private void getData() {
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.getStringExtra("DOCTOR_ID") != null && !intent.getStringExtra("DOCTOR_ID").isEmpty()) {
                doctor_Id = intent.getStringExtra("DOCTOR_ID");
            }
        }
    }

    private void setAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
        binding.rvReview.setLayoutManager(layoutManager);
        binding.rvReview.hasFixedSize();
        binding.rvReview.setItemAnimator(new DefaultItemAnimator());
        reviewAdapter = new DoctorReviewAdapter(context, reviewDataList, AppConstant.TYPE_DOCTOR_CATEGORY);
        binding.rvReview.setAdapter(reviewAdapter);
    }


    private void setToolbar() {
        binding.menubar.tvTitle.setVisibility(View.VISIBLE);
        binding.menubar.tvTitle.setText(getResources().getString(R.string.doctor_profile));
        binding.menubar.ivBack.setImageResource(R.drawable.ic_keyboard_backspace_black_24dp);
        binding.menubar.ivBack.setOnClickListener(this);
        binding.btnBookAppointment.setOnClickListener(this);
        binding.btnShareExperience.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                finish();
                break;
            case R.id.btnBookAppointment:
                ActivityController.startActivity(context, BookAppointment.class);
                break;
            case R.id.btnShareExperience:
                showBottomSheetDialog();
                break;
            default:
                break;
        }
    }

    public void showBottomSheetDialog() {
        final DialogDoctorRatingBinding doctorRating = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.dialog_doctor_rating, null, false);
        final BottomSheetDialog dialog = DialogUtils.createBottomDialog(context, doctorRating.getRoot());
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        doctorRating.etmessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                doctorRating.tvReviewError.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        doctorRating.btnShareExperience.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Objects.requireNonNull(doctorRating.etmessage.getText()).toString().isEmpty()) {
                    CommonUtils.setErrorMessage(doctorRating.etmessage, Objects.requireNonNull(doctorRating.tvReviewError), getResources().getString(R.string.empty_review));
                } else if (doctorRating.etmessage.getText().toString().length() < 3) {
                    CommonUtils.setErrorMessage(doctorRating.etmessage, Objects.requireNonNull(doctorRating.tvReviewError), getResources().getString(R.string.invalid_review));
                } else {
                    addExperience(doctorRating.ratingBar.getNumStars(), doctorRating.etmessage.getText().toString());
                    dialog.dismiss();
                }

            }
        });
    }

    private void addExperience(int rating, String review) {
        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                Api api = RequestController.createService(context, Api.class);
                api.addDoctorReview(SharedPref.getStringPreferences(context, AppConstant.USER_ID), doctor_Id, rating, review)
                        .enqueue(new Callback<BaseResponse>() {
                            @Override
                            public void onResponse(@NonNull Call<BaseResponse> call, @NonNull Response<BaseResponse> response) {
                                ProgressDialogUtils.dismiss();
                                if (response.body() != null) {
                                    if (response.body().getStatus() == 1) {
                                        CommonUtils.showToastShort(context, response.body().getMessage());
                                    } else {
                                        CommonUtils.showToastShort(context, response.body().getMessage());
                                    }
                                } else {
                                    CommonUtils.showToastShort(context, getResources().getString(R.string.no_response));
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<BaseResponse> call, @NonNull Throwable t) {
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


    private void getDoctorProfile() {
        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                Api api = RequestController.createService(context, Api.class);
                api.getDoctorProfile(doctor_Id)
                        .enqueue(new BaseCallback<DoctorResponse>(context) {
                            @Override
                            public void onSuccess(DoctorResponse response) {
                                ProgressDialogUtils.dismiss();
                                if (response != null) {
                                    if (response.getStatus() == 1) {
                                        CommonUtils.showToastShort(context, response.getMessage());
                                        DoctorData doctorData = response.getDoctorData();
                                        if (doctorData != null) {
                                            setProfileData(doctorData);
                                            if (doctorData.getDoctorFeedbackList() != null && !doctorData.getDoctorFeedbackList().isEmpty()) {
                                                binding.llFeedback.setVisibility(View.VISIBLE);
                                                binding.tvFeedbackFor.setText("Feedback for  " + doctorData.getDoctorName());
                                                reviewDataList.clear();
                                                reviewDataList.addAll(doctorData.getDoctorFeedbackList());
                                                reviewAdapter.notifyDataSetChanged();
                                            } else {
                                                reviewDataList.clear();
                                                reviewAdapter.notifyDataSetChanged();
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
                            public void onFail(Call<DoctorResponse> call, BaseResponse baseResponse) {
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

    private void setProfileData(DoctorData doctorData) {

        binding.tvAwards.setText(doctorData.getDoctorAward());
        binding.tvCategory.setText(doctorData.getDoctorCategoryName());
        binding.tvDoctorName.setText(doctorData.getDoctorName());
        binding.tvExperience.setText(doctorData.getDoctorExperience());
        binding.tvExperienceFull.setText(doctorData.getDoctorExperience());
        binding.tvFees.setText(doctorData.getDoctorFees());
        if (doctorData.getDoctorRating() != null && !doctorData.getDoctorRating().isEmpty()) {
            binding.doctorRating.setRating(Float.parseFloat(doctorData.getDoctorRating()));
        }
        binding.tvLocation.setText(doctorData.getDoctorLocation());
        binding.tvQualification.setText(doctorData.getDoctorQualification());
        binding.tvQualificationFull.setText(doctorData.getDoctorQualification());
        binding.tvRating.setText(doctorData.getDoctorRating());
        binding.tvSpecializations.setText(doctorData.getDoctorSpecializations());
        binding.tvRegistration.setText(doctorData.getDoctorRegistration());
        binding.tvTimings.setText(doctorData.getDoctorTimings());
        if (doctorData.getDoctorProfile() != null && !doctorData.getDoctorProfile().isEmpty()) {
            Picasso.with(context)
                    .load(doctorData.getDoctorProfile())
                    .placeholder(R.drawable.default_user)
                    .error(R.drawable.default_user)
                    .into(binding.ivDoctor);
        }
    }
}
