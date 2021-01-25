package com.drugvilla.in.ui.doctors;

import androidx.annotation.NonNull;
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

import com.drugvilla.in.R;
import com.drugvilla.in.adapter.doctors.DoctorCategoryAdapter;
import com.drugvilla.in.adapter.doctors.FindDoctorAdapter;
import com.drugvilla.in.databinding.ActivityDoctorListingBinding;
import com.drugvilla.in.model.Document;
import com.drugvilla.in.model.doctor.DoctorCategoryData;
import com.drugvilla.in.model.doctor.DoctorCategoryResponse;
import com.drugvilla.in.model.doctor.DoctorData;
import com.drugvilla.in.model.doctor.DoctorListResponse;
import com.drugvilla.in.service.Api;
import com.drugvilla.in.service.BaseCallback;
import com.drugvilla.in.service.BaseResponse;
import com.drugvilla.in.service.RequestController;
import com.drugvilla.in.utils.AppConstant;
import com.drugvilla.in.utils.CommonUtils;
import com.drugvilla.in.utils.ProgressDialogUtils;
import com.drugvilla.in.utils.SharedPref;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoctorListing extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    private ActivityDoctorListingBinding binding;
    private List<DoctorData> doctorDataList = new ArrayList<>();
    private FindDoctorAdapter doctorAdapter;
    private String doctor_Category_ID;

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
        binding = DataBindingUtil.setContentView(this, R.layout.activity_doctor_listing);
        context = DoctorListing.this;
        getData();
        setToolbar();
        binding.swipeRefreshingLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDoctorListing();
            }
        });
    }

    private void getData() {
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.getStringExtra("Doctor_Category_ID") != null && !intent.getStringExtra("Doctor_Category_ID").isEmpty()) {
                doctor_Category_ID = intent.getStringExtra("Doctor_Category_ID");
            }
        }
    }

    @Override
    protected void onResume() {
        getDoctorListing();
        setAdapter();
        super.onResume();
    }

    private void setAdapter() {
        final LinearLayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
        binding.rvDoctors.setLayoutManager(layoutManager);
        binding.rvDoctors.hasFixedSize();
        binding.rvDoctors.setItemAnimator(new DefaultItemAnimator());
        doctorDataList.clear();
        doctorAdapter = new FindDoctorAdapter(context, doctorDataList);
        binding.rvDoctors.setAdapter(doctorAdapter);
       /* binding.rvDoctors.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

    /*   private void loadMoreItems(final boolean isFirstPage) {
           // change loading state
           isLoading = true;
           currentPage = currentPage + 1;
           api.getDoctorList( doctor_Category_ID,currentPage)
                   .enqueue(new Callback<DoctorListResponse>() {
                       @Override
                       public void onResponse(@NonNull Call<DoctorListResponse> call, @NonNull Response<DoctorListResponse> response) {
                           if (response.body() != null) {
                               List<DoctorData> result = response.body().getDoctorDataList();

                               if (result == null) return;
                               else if (!isFirstPage) doctorAdapter.addAll(result);
                               else doctorAdapter.setList(result);

                               isLoading = false;
                               isLastPage = currentPage == response.body().getPagination().getMaxPage();
                           }
                       }

                       @Override
                       public void onFailure(@NonNull Call<DoctorListResponse> call, @NonNull Throwable t) {
                           CommonUtils.showToastShort(context, getResources().getString(R.string.failure));
                       }
                   });

       }*/
    private void setToolbar() {
        binding.menubar.tvTitle.setVisibility(View.VISIBLE);
        //binding.menubar.tvTitle.setText(R.string.add_address);
        binding.menubar.ivBack.setImageResource(R.drawable.ic_keyboard_backspace_black_24dp);
        binding.menubar.ivBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.ivBack) {
            finish();
        }
    }

    private void getDoctorListing() {
        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                api = RequestController.createService(context, Api.class);
                api.getDoctorList(doctor_Category_ID, currentPage)
                        .enqueue(new BaseCallback<DoctorListResponse>(context) {
                            @Override
                            public void onSuccess(DoctorListResponse response) {
                                binding.swipeRefreshingLayout.setRefreshing(false);
                                ProgressDialogUtils.dismiss();
                                if (response != null) {
                                    if (response.getStatus() == 1) {
                                        CommonUtils.showToastShort(context, response.getMessage());

                                        if (response.getDoctorDataList() != null && !response.getDoctorDataList().isEmpty()) {
                                            doctorDataList.clear();
                                            doctorDataList.addAll(response.getDoctorDataList());
                                            doctorAdapter.notifyDataSetChanged();
                                        } else {
                                            doctorDataList.clear();
                                            doctorAdapter.notifyDataSetChanged();
                                        }
                                    } else {
                                        CommonUtils.showToastShort(context, response.getMessage());
                                    }
                                } else {
                                    CommonUtils.showToastShort(context, getResources().getString(R.string.no_response));
                                }
                            }

                            @Override
                            public void onFail(Call<DoctorListResponse> call, BaseResponse baseResponse) {
                                ProgressDialogUtils.dismiss();
                                CommonUtils.showToastShort(context, getResources().getString(R.string.failure));
                            }
                        });
                     /*   .enqueue(new Callback<DoctorListResponse>() {
                            @Override
                            public void onResponse(@NonNull Call<DoctorListResponse> call, @NonNull Response<DoctorListResponse> response) {
                                binding.swipeRefreshingLayout.setRefreshing(false);
                                ProgressDialogUtils.dismiss();
                                if (response.body() != null) {
                                    if (response.body().getStatus() == 1) {
                                        CommonUtils.showToastShort(context, response.body().getMessage());

                                        if (response.body().getDoctorDataList() != null && !response.body().getDoctorDataList().isEmpty()) {
                                            doctorDataList.clear();
                                            doctorDataList.addAll(response.body().getDoctorDataList());
                                            doctorAdapter.notifyDataSetChanged();
                                        } else {
                                            doctorDataList.clear();
                                            doctorAdapter.notifyDataSetChanged();
                                        }
                                    } else {
                                        CommonUtils.showToastShort(context, response.body().getMessage());
                                    }
                                } else {
                                    CommonUtils.showToastShort(context, getResources().getString(R.string.no_response));
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<DoctorListResponse> call, @NonNull Throwable t) {
                                ProgressDialogUtils.dismiss();
                                CommonUtils.showToastShort(context, getResources().getString(R.string.failure));
                            }
                        });*/
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            CommonUtils.showToastShort(context, getResources().getString(R.string.no_internet));
        }
    }
}
