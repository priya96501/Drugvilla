package com.drugvilla.in.ui.doctors;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.drugvilla.in.R;
import com.drugvilla.in.adapter.doctors.DoctorCategoryAdapter;
import com.drugvilla.in.databinding.ActivityDoctorCategoryBinding;
import com.drugvilla.in.model.doctor.DoctorCategoryData;
import com.drugvilla.in.model.doctor.DoctorCategoryResponse;
import com.drugvilla.in.service.Api;
import com.drugvilla.in.service.BaseCallback;
import com.drugvilla.in.service.BaseResponse;
import com.drugvilla.in.service.RequestController;
import com.drugvilla.in.ui.others.SearchActivity;
import com.drugvilla.in.utils.ActivityController;
import com.drugvilla.in.utils.AppConstant;
import com.drugvilla.in.utils.CommonUtils;
import com.drugvilla.in.utils.ProgressDialogUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class DoctorCategory extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    private ActivityDoctorCategoryBinding binding;
    private List<DoctorCategoryData> doctorCategoryDataList = new ArrayList<>();
    private DoctorCategoryAdapter doctorCategoryAdapter;

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
        binding = DataBindingUtil.setContentView(this, R.layout.activity_doctor_category);
        context = DoctorCategory.this;
        setToolbar();
        setListener();

        binding.swipeRefreshingLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDoctorCategory();

            }
        });
    }

    @Override
    protected void onResume() {
        getDoctorCategory();
        setAdapter();
        super.onResume();
    }


    private void setAdapter() {
        final GridLayoutManager layoutManager = new GridLayoutManager(context, 3);
        binding.rvCategories.setLayoutManager(layoutManager);
        binding.rvCategories.hasFixedSize();
        binding.rvCategories.setItemAnimator(new DefaultItemAnimator());
        doctorCategoryDataList.clear();
        doctorCategoryAdapter = new DoctorCategoryAdapter(context, doctorCategoryDataList);
        binding.rvCategories.setAdapter(doctorCategoryAdapter);
      /*  binding.rvCategories.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
   /* private void loadMoreItems(final boolean isFirstPage) {
        // change loading state
        isLoading = true;
        currentPage = currentPage + 1;
        api.getDoctorCategory(currentPage)
                .enqueue(new Callback<DoctorCategoryResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<DoctorCategoryResponse> call, @NonNull Response<DoctorCategoryResponse> response) {
                        if (response.body() != null) {
                            List<DoctorCategoryData> result = response.body().getDoctorCategoryDataList();

                            if (result == null) return;
                            else if (!isFirstPage) doctorCategoryAdapter.addAll(result);
                            else doctorCategoryAdapter.setList(result);

                            isLoading = false;
                            isLastPage = currentPage == response.body().getPagination().getMaxPage();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<DoctorCategoryResponse> call, @NonNull Throwable t) {
                        CommonUtils.showToastShort(context, getResources().getString(R.string.failure));
                    }
                });

    }*/

    private void setListener() {
        binding.llSearch.setOnClickListener(this);
    }

    private void setToolbar() {
        binding.menubar.tvTitle.setVisibility(View.VISIBLE);
        binding.menubar.tvTitle.setText(getResources().getString(R.string.find_doctors));
        binding.menubar.ivBack.setImageResource(R.drawable.ic_keyboard_backspace_black_24dp);
        binding.menubar.ivBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                finish();
                break;
            case R.id.llSearch:
                ActivityController.startActivity(context, SearchActivity.class, AppConstant.DOCTORS);
                break;
            default:
                break;
        }
    }

    private void getDoctorCategory() {
        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                Api api = RequestController.createService(context, Api.class);
                api.getDoctorCategory(currentPage)
                        .enqueue(new BaseCallback<DoctorCategoryResponse>(context) {
                            @Override
                            public void onSuccess(DoctorCategoryResponse response) {
                                binding.swipeRefreshingLayout.setRefreshing(false);
                                ProgressDialogUtils.dismiss();
                                if (response != null) {
                                    if (response.getStatus() == 1) {
                                        CommonUtils.showToastShort(context, response.getMessage());

                                        if (response.getDoctorCategoryDataList() != null && !response.getDoctorCategoryDataList().isEmpty()) {
                                            doctorCategoryDataList.clear();
                                            doctorCategoryDataList.addAll(response.getDoctorCategoryDataList());
                                            doctorCategoryAdapter.notifyDataSetChanged();
                                        } else {
                                            doctorCategoryDataList.clear();
                                            doctorCategoryAdapter.notifyDataSetChanged();
                                        }

                                    } else {
                                        doctorCategoryDataList.clear();
                                        doctorCategoryAdapter.notifyDataSetChanged();
                                        CommonUtils.showToastShort(context, response.getMessage());
                                    }
                                } else {
                                    CommonUtils.showToastShort(context, getResources().getString(R.string.no_response));
                                }
                            }

                            @Override
                            public void onFail(Call<DoctorCategoryResponse> call, BaseResponse baseResponse) {
                                ProgressDialogUtils.dismiss();
                                CommonUtils.showToastShort(context, getResources().getString(R.string.failure));
                            }
                        });
                      /*  .enqueue(new Callback<DoctorCategoryResponse>() {
                            @Override
                            public void onResponse(@NonNull Call<DoctorCategoryResponse> call, @NonNull Response<DoctorCategoryResponse> response) {
                                binding.swipeRefreshingLayout.setRefreshing(false);
                                ProgressDialogUtils.dismiss();
                                if (response.body() != null) {
                                    if (response.body().getStatus() == 1) {
                                        CommonUtils.showToastShort(context, response.body().getMessage());

                                        if (response.body().getDoctorCategoryDataList() != null && !response.body().getDoctorCategoryDataList().isEmpty()) {
                                            doctorCategoryDataList.clear();
                                            doctorCategoryDataList.addAll(response.body().getDoctorCategoryDataList());
                                            doctorCategoryAdapter.notifyDataSetChanged();
                                        } else {
                                            doctorCategoryDataList.clear();
                                            doctorCategoryAdapter.notifyDataSetChanged();
                                        }

                                    } else {
                                        doctorCategoryDataList.clear();
                                        doctorCategoryAdapter.notifyDataSetChanged();
                                        CommonUtils.showToastShort(context, response.body().getMessage());
                                    }
                                } else {
                                    CommonUtils.showToastShort(context, getResources().getString(R.string.no_response));
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<DoctorCategoryResponse> call, @NonNull Throwable t) {
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
