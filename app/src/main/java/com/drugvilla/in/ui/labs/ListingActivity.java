package com.drugvilla.in.ui.labs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.drugvilla.in.R;
import com.drugvilla.in.adapter.labs.LabsAdapter;
import com.drugvilla.in.adapter.labs.PackageListingAdapter;
import com.drugvilla.in.adapter.labs.TestListingAdapter;
import com.drugvilla.in.databinding.ActivityLabsListingBinding;
import com.drugvilla.in.listener.OnSelectedListener;
import com.drugvilla.in.model.Document;
import com.drugvilla.in.model.doctor.DoctorData;
import com.drugvilla.in.model.doctor.DoctorListResponse;
import com.drugvilla.in.model.lab.labs.LabData;
import com.drugvilla.in.model.lab.labs.LabListingResponse;
import com.drugvilla.in.model.lab.packages.PackageData;
import com.drugvilla.in.model.lab.packages.PackageListResponse;
import com.drugvilla.in.model.lab.test.TestData;
import com.drugvilla.in.model.lab.test.TestListingResponse;
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
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListingActivity extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    private String from = " ";
    private int bottomFlag = 0;
    private ActivityLabsListingBinding binding;
    private List<LabData> labDataList = new ArrayList<>();
    private List<TestData> testDataList = new ArrayList<>();
    private List<PackageData> packageDataList = new ArrayList<>();
    private PackageListingAdapter packageListingAdapter;
    private TestListingAdapter testListingAdapter;
    private LabsAdapter labsAdapter;
    private LinearLayoutManager layoutManager;

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
        binding = DataBindingUtil.setContentView(this, R.layout.activity_labs_listing);
        context = ListingActivity.this;
        getData();
        setToolbar();
        setViews();

        binding.swipeRefreshingLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (from.equalsIgnoreCase(AppConstant.TYPE_ALL_HEALTH_PACKAGE)) {
                    getAllPackages();
                } else if (from.equalsIgnoreCase(AppConstant.TYPE_ALL_LABS)) {
                    getAllLabs();
                } else {
                    getAllTests();
                }

            }
        });

    }

    @Override
    protected void onResume() {

        if (from.equalsIgnoreCase(AppConstant.TYPE_ALL_HEALTH_PACKAGE)) {
            getAllPackages();
            setPackageAdapter();
        } else if (from.equalsIgnoreCase(AppConstant.TYPE_ALL_LABS)) {
            getAllLabs();
            setLabAdapter();
        } else {
            getAllTests();
            setTestAdapter();
        }
        super.onResume();
    }


    private void setViews() {
        if (from.equalsIgnoreCase(AppConstant.TYPE_ALL_HEALTH_PACKAGE)) {
            binding.menuBar.tvTitle.setText(R.string.health_packages);
            binding.etsearch.setHint("Search for Packages");
        } else if (from.equalsIgnoreCase(AppConstant.TYPE_ALL_LABS)) {
            binding.menuBar.tvTitle.setText(R.string.all_labs);
        } else {
            binding.menuBar.tvTitle.setText(R.string.all_lab_tests);
            binding.etsearch.setHint("Search for Tests");
        }

        binding.btnGoToCart.setOnClickListener(this);
        binding.llSearch.setOnClickListener(this);
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
        binding.menuBar.tvTitle.setVisibility(View.VISIBLE);
        binding.menuBar.ivRight.setVisibility(View.VISIBLE);
        binding.menuBar.ivBack.setImageResource(R.drawable.ic_keyboard_backspace_black_24dp);
        binding.menuBar.ivBack.setOnClickListener(this);
        binding.menuBar.ivRight.setOnClickListener(this);
        binding.menuBar.ivRight.setBadgeValue(3);
    }

    private void setLabAdapter() {
        layoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
        binding.rvListing.setLayoutManager(layoutManager);
        binding.rvListing.hasFixedSize();
        labsAdapter = new LabsAdapter(context, labDataList);
        binding.rvListing.setAdapter(labsAdapter);
       // addPaginationOnScrolling("LABS");
    }

    private void setPackageAdapter() {
        layoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
        binding.rvListing.setLayoutManager(layoutManager);
        binding.rvListing.hasFixedSize();
        packageListingAdapter = new PackageListingAdapter(context, packageDataList, new OnSelectedListener() {
            @Override
            public void onClick(View view, int position) {
                setBottomView(AppConstant.TYPE_ALL_HEALTH_PACKAGE);
                bottomFlag = 1;
            }
        });
        binding.rvListing.setAdapter(packageListingAdapter);
       // addPaginationOnScrolling("PACKAGE");
    }


    private void setTestAdapter() {
        layoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
        binding.rvListing.setLayoutManager(layoutManager);
        binding.rvListing.hasFixedSize();
        testListingAdapter = new TestListingAdapter(context, testDataList, new OnSelectedListener() {
            @Override
            public void onClick(View view, int position) {
                setBottomView(AppConstant.TYPE_ALL_LAB_TESTS);
                bottomFlag = 2;
            }
        });
        binding.rvListing.setAdapter(testListingAdapter);
       // addPaginationOnScrolling("TEST");
    }

   /* private void addPaginationOnScrolling(final String type) {
        binding.rvListing.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

                if (shouldLoadMore) loadMoreItems(false, type);
            }
        });

        // load the first page
        loadMoreItems(true, type);
    }

    private void loadMoreItems(final boolean isFirstPage, final String type) {
        // change loading state
        isLoading = true;
        currentPage = currentPage + 1;
        if (type.equalsIgnoreCase("LABS")) {
            api.getAllLabs(currentPage).enqueue(new BaseCallback<LabListingResponse>(context) {
                @Override
                public void onSuccess(LabListingResponse response) {
                    if (response != null) {
                        List<LabData> result = response.getLabDataList();

                        if (result == null) return;
                        else if (!isFirstPage) labsAdapter.addAll(result);
                        else labsAdapter.setList(result);

                        isLoading = false;
                        isLastPage = currentPage == response.getPagination().getMaxPage();
                    }
                }

                @Override
                public void onFail(Call<LabListingResponse> call, BaseResponse baseResponse) {
                    CommonUtils.showToastShort(context, getResources().getString(R.string.failure));
                }
            });
        } else if (type.equalsIgnoreCase("PACKAGE")) {
            api.getAllPackage(currentPage).enqueue(new BaseCallback<PackageListResponse>(context) {
                @Override
                public void onSuccess(PackageListResponse response) {
                    if (response != null) {
                        List<PackageData> result = response.getPackageDataList();

                        if (result == null) return;
                        else if (!isFirstPage) packageListingAdapter.addAll(result);
                        else packageListingAdapter.setList(result);

                        isLoading = false;
                        isLastPage = currentPage == response.getPagination().getMaxPage();
                    }
                }

                @Override
                public void onFail(Call<PackageListResponse> call, BaseResponse baseResponse) {
                    CommonUtils.showToastShort(context, getResources().getString(R.string.failure));
                }
            });
        } else {
            api.getAllTest(currentPage).enqueue(new BaseCallback<TestListingResponse>(context) {
                @Override
                public void onSuccess(TestListingResponse response) {
                    if (response != null) {
                        List<TestData> result = response.getTestDataList();

                        if (result == null) return;
                        else if (!isFirstPage) testListingAdapter.addAll(result);
                        else testListingAdapter.setList(result);

                        isLoading = false;
                        isLastPage = currentPage == response.getPagination().getMaxPage();
                    }
                }

                @Override
                public void onFail(Call<TestListingResponse> call, BaseResponse baseResponse) {
                    CommonUtils.showToastShort(context, getResources().getString(R.string.failure));
                }
            });
        }


    }
*/
    private void setBottomView(String type) {
        binding.llBottomView.setVisibility(View.VISIBLE);
        if (type.equalsIgnoreCase(AppConstant.TYPE_ALL_HEALTH_PACKAGE)) {
            binding.btnGoToCart.setText(getResources().getString(R.string.go_to_cart));
        } else {
            binding.btnGoToCart.setText(getResources().getString(R.string.select_lab));
        }
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, 0, 120);
        binding.llLabsListing.setLayoutParams(layoutParams);
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
            case R.id.btnGoToCart:
                if (bottomFlag == 1) {
                    ActivityController.startActivity(context, Cart.class, AppConstant.TYPE_LAB_CART);
                } else if (bottomFlag == 2) {
                    ActivityController.startActivity(context, SelectLab.class, AppConstant.TYPE_LAB_CART);
                }
                break;
            default:
                break;
        }
    }

    private void getAllTests() {
        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                api = RequestController.createService(context, Api.class);
                api.getAllTest(currentPage).enqueue(new BaseCallback<TestListingResponse>(context) {
                    @Override
                    public void onSuccess(TestListingResponse response) {
                        binding.swipeRefreshingLayout.setRefreshing(false);
                        ProgressDialogUtils.dismiss();
                        if (response != null) {
                            if (response.getStatus() == 1) {
                                CommonUtils.showToastShort(context, response.getMessage());

                                if (response.getTestDataList() != null && !response.getTestDataList().isEmpty()) {
                                    testDataList.clear();
                                    testDataList.addAll(response.getTestDataList());
                                    testListingAdapter.notifyDataSetChanged();
                                } else {
                                    testDataList.clear();
                                    testListingAdapter.notifyDataSetChanged();
                                }

                            } else {
                                testDataList.clear();
                                testListingAdapter.notifyDataSetChanged();
                                CommonUtils.showToastShort(context, response.getMessage());
                            }
                        }
                    }

                    @Override
                    public void onFail(Call<TestListingResponse> call, BaseResponse baseResponse) {
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

    private void getAllLabs() {
        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                api = RequestController.createService(context, Api.class);
                api.getAllLabs(currentPage).enqueue(new BaseCallback<LabListingResponse>(context) {
                    @Override
                    public void onSuccess(LabListingResponse response) {

                        binding.swipeRefreshingLayout.setRefreshing(false);
                        ProgressDialogUtils.dismiss();
                        if (response != null) {
                            if (response.getStatus() == 1) {
                                CommonUtils.showToastShort(context, response.getMessage());

                                if (response.getLabDataList() != null && !response.getLabDataList().isEmpty()) {
                                    labDataList.clear();
                                    labDataList.addAll(response.getLabDataList());
                                    labsAdapter.notifyDataSetChanged();
                                } else {
                                    labDataList.clear();
                                    labsAdapter.notifyDataSetChanged();
                                }

                            } else {
                                labDataList.clear();
                                labsAdapter.notifyDataSetChanged();
                                CommonUtils.showToastShort(context, response.getMessage());
                            }
                        }
                    }

                    @Override
                    public void onFail(Call<LabListingResponse> call, BaseResponse baseResponse) {
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

    private void getAllPackages() {
        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                api = RequestController.createService(context, Api.class);
                api.getAllPackage(currentPage).enqueue(new BaseCallback<PackageListResponse>(context) {
                    @Override
                    public void onSuccess(PackageListResponse response) {

                        binding.swipeRefreshingLayout.setRefreshing(false);
                        ProgressDialogUtils.dismiss();
                        if (response != null) {
                            if (response.getStatus() == 1) {
                                CommonUtils.showToastShort(context, response.getMessage());

                                if (response.getPackageDataList() != null && !response.getPackageDataList().isEmpty()) {
                                    packageDataList.clear();
                                    packageDataList.addAll(response.getPackageDataList());
                                    packageListingAdapter.notifyDataSetChanged();
                                } else {
                                    packageDataList.clear();
                                    packageListingAdapter.notifyDataSetChanged();
                                }

                            } else {
                                packageDataList.clear();
                                packageListingAdapter.notifyDataSetChanged();
                                CommonUtils.showToastShort(context, response.getMessage());
                            }
                        } else {
                            CommonUtils.showToastShort(context, getResources().getString(R.string.no_response));
                        }
                    }

                    @Override
                    public void onFail(Call<PackageListResponse> call, BaseResponse baseResponse) {
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
