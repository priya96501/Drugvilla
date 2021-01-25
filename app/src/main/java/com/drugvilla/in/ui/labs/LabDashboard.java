package com.drugvilla.in.ui.labs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.drugvilla.in.R;
import com.drugvilla.in.adapter.slidder.AndroidImageAdapter;
import com.drugvilla.in.adapter.slidder.TextAdapter;
import com.drugvilla.in.adapter.labs.PopularHealthPackageAdapter;
import com.drugvilla.in.adapter.product.AllCategoryAdapter;
import com.drugvilla.in.adapter.product.CategoryAdapter;
import com.drugvilla.in.adapter.product.ProductAdapter;
import com.drugvilla.in.databinding.ActivityLabsBinding;
import com.drugvilla.in.databinding.BottomCallToOrderBinding;
import com.drugvilla.in.listener.OnSelectedListener;
import com.drugvilla.in.model.Document;
import com.drugvilla.in.model.dashboard.BannerData;
import com.drugvilla.in.model.dashboard.BannerResponse;
import com.drugvilla.in.model.dashboard.CategoryData;
import com.drugvilla.in.model.dashboard.CategoryResponse;
import com.drugvilla.in.model.dashboard.HomeSubData;
import com.drugvilla.in.model.dashboard.labDashboard.LabBaseResponse;
import com.drugvilla.in.model.dashboard.labDashboard.LabDashboardData;
import com.drugvilla.in.model.dashboard.labDashboard.LabDashboardResponse;
import com.drugvilla.in.model.lab.SelectLabData;
import com.drugvilla.in.model.lab.SelectLabResponse;
import com.drugvilla.in.model.lab.labs.LabData;
import com.drugvilla.in.model.lab.packages.PackageData;
import com.drugvilla.in.model.lab.test.TestData;
import com.drugvilla.in.service.Api;
import com.drugvilla.in.service.BaseCallback;
import com.drugvilla.in.service.BaseResponse;
import com.drugvilla.in.service.RequestController;
import com.drugvilla.in.ui.others.SearchActivity;
import com.drugvilla.in.ui.order.Cart;
import com.drugvilla.in.ui.prescription.Upload;
import com.drugvilla.in.utils.ActivityController;
import com.drugvilla.in.utils.AppConstant;
import com.drugvilla.in.utils.CommonUtils;
import com.drugvilla.in.utils.DialogUtils;
import com.drugvilla.in.utils.ProgressDialogUtils;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;

public class LabDashboard extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    private Activity activity;
    private ActivityLabsBinding binding;
    int flag = 2;
    private GridLayoutManager gridLayoutManager;

    List<Document> bannerData = new ArrayList<>();
    List<Document> bottomTextData = new ArrayList<>();
    private final int[] myText = new int[]{R.string.text1, R.string.text2, R.string.text3};
    private final int[] myImageList = new int[]{R.drawable.b1, R.drawable.banner2, R.drawable.banner3};
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private List<Document> listData = new ArrayList<>();
    private List<Document> labsdata = new ArrayList<>();
    private List<Document> packagesData = new ArrayList<>();
    private List<Document> popularTestData = new ArrayList<>();

    // checkup categories
    // shop by categories
    private final int[] category = {R.drawable.cat1, R.drawable.cat2, R.drawable.cat3, R.drawable.cat4, R.drawable.lab_cat5, R.drawable.lab_cat6,
            R.drawable.lab_cat7, R.drawable.lab_cat8, R.drawable.lab_cat9, R.drawable.lab_cat10, R.drawable.cat8, R.drawable.lab_cat12};
    private final String[] categoryName = {"Women Health", "Men \nHealth", "Full Body Checkup", "Sr. citizen Checkup", "Diabeties", "Heart", "Pregnancy", "Kidney", "Skin",
            "Bone", "Sexual Wellness", "thyroid"};
    // featured labs
    private final int[] labImages = {R.drawable.lab1, R.drawable.lab2, R.drawable.lab3, R.drawable.lab4, R.drawable.lab1};
    private final String[] labName = {"Delhi Path Lab", "Dr. A. Lalchandani's Pathology Laboratories", "Sure path labs pvt ltd", "Thyrocare Laboratories Ltd.", "SRL Limited"};
    // health packages
    private final String[] packageName = {"Advance Health Care", "Healthy Heart", "Complete Care", "Basic Health Care", "Diabetes Care Package"};
    private final String[] packageBy = {"By Delhi Path Lab", "By Delhi Path Lab", "By Sure path labs pvt ltd", "By Thyrocare Laboratories Ltd.", "By Delhi Path Lab"};
    private final String[] packagePrice = {"1600", "400", "750", "1100", "500"};
    // health packages
    private final String[] testName = {"ACE (Absolute Eosinophil Count)", "Thyroid Profile", "Liver Function Test", "Beta Hcg/Hcg Beta Subunit", "Liver Function Test"};


    private List<BannerData> bannerDataList = new ArrayList<>();
    private List<CategoryData> categoryDataList = new ArrayList<>();
    private List<HomeSubData> labDataList = new ArrayList<>();
    private List<LabDashboardData> packageTestDataList = new ArrayList<>();

    private CategoryAdapter categoryAdapter;
    private AllCategoryAdapter allCategoryAdapter;
    private PopularHealthPackageAdapter adapter;
    private ProductAdapter productAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_labs);
        context = LabDashboard.this;
        activity = LabDashboard.this;
        setToolbar();
        setListener();

        // setting bottom data
        bottomTextData = populateList2();
        setSliderViews2(bottomTextData);

        binding.swipeRefreshingLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getBannerDataApi();
                getCategoryDataApi();
                getTestsApi();
                getPackagesApi();
                getLabsApi();


            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();


        getBannerDataApi();
        setSliderViews(bannerDataList);

        getCategoryDataApi();
        setCategoryAdapter();

        getTestsApi();
        setPopularTest();

        getLabsApi();
        setFeaturedLabs();

        getPackagesApi();
        setPopularPackage();

    }


    private void setListener() {
        binding.llViewMore.setOnClickListener(this);
        binding.llAllHealthPackages.setOnClickListener(this);
        binding.llAlltests.setOnClickListener(this);
        binding.tvViewAllPackages.setOnClickListener(this);
        binding.tvViewAllLabs.setOnClickListener(this);
        binding.btnGoToCart.setOnClickListener(this);
        binding.llSearch.setOnClickListener(this);
        binding.llCallToOrder.setOnClickListener(this);
        binding.btnUploadPrescription.setOnClickListener(this);
        binding.tvViewAllPopularTest.setOnClickListener(this);
        binding.btnSelectLab.setOnClickListener(this);
    }


    private void setBottomView(String type) {
        binding.llBottomView.setVisibility(View.VISIBLE);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, 0, 150);
        binding.llslidders.setLayoutParams(layoutParams);
        if (type.equalsIgnoreCase("test")) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    CommonUtils.goToNextActivity(context, SelectLab.class, AppConstant.TYPE_LAB_CART);
                }
            }, 250);
        } else {
            binding.btnGoToCart.setVisibility(View.VISIBLE);
            binding.btnSelectLab.setVisibility(View.GONE);
        }
    }



    // setting bottom sliders
    private ArrayList<Document> populateList2() {
        ArrayList<Document> list = new ArrayList<>();
        for (int i1 : myText) {
            Document imageModel = new Document();
            imageModel.setText(getResources().getString(i1));
            list.add(imageModel);
        }
        return list;
    }

    private void setSliderViews2(List<Document> list) {
        binding.pager2.setAdapter(new TextAdapter(this, list, AppConstant.TYPE_TEXT));
        binding.indicator2.setViewPager(binding.pager2);
        NUM_PAGES = list.size();
        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                binding.pager2.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 2000, 2000);

        // Pager listener over indicator
        binding.indicator2.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });

    }


    private List<Document> PrepareDataMessage() {
        List<Document> data = new ArrayList<>();
        for (int i = 0; i < category.length; i++) {
            Document document = new Document();
            document.setImage(category[i]);
            document.setText(categoryName[i]);
            data.add(document);
        }
        listData.addAll(data);
        return data;
    }

    private void setToolbar() {
        binding.menuBar.tvTitle.setVisibility(View.VISIBLE);
        binding.menuBar.tvTitle.setText(getResources().getString(R.string.find_labs));
        binding.menuBar.ivBack.setImageResource(R.drawable.ic_keyboard_backspace_black_24dp);
        binding.menuBar.ivBack.setOnClickListener(this);
        binding.menuBar.ivRight.setVisibility(View.VISIBLE);
        binding.menuBar.ivRight.setOnClickListener(this);
        binding.menuBar.ivRight.setBadgeValue(3);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnUploadPrescription:
                ActivityController.startActivity(context, Upload.class, AppConstant.BOOK_LAB_TEST_FROM_PRESCRIPTION);
                // CommonUtils.chooseUploadOption(context, AppConstant.BOOK_LAB_TEST_FROM_PRESCRIPTION);
                break;
            case R.id.ivBack:
                finish();
                break;
            case R.id.llSearch:
                CommonUtils.goToNextActivity(context, SearchActivity.class, AppConstant.TYPE_LABS);
                break;
           /* case R.id.btnSelectLab:
                CommonUtils.goToNextActivity(context, SelectLab.class, AppConstant.TYPE_LAB_CART);
                break;*/

            case R.id.btnGoToCart:
                CommonUtils.goToNextActivity(context, Cart.class, AppConstant.TYPE_LAB_CART);
                break;
            case R.id.ivRight:
                CommonUtils.goToNextActivity(context, Cart.class, AppConstant.TYPE_LAB_CART);
                break;
            case R.id.llAllHealthPackages:
                CommonUtils.goToNextActivity(context, ListingActivity.class, AppConstant.TYPE_ALL_HEALTH_PACKAGE);
                break;
            case R.id.tvViewAllPackages:
                CommonUtils.goToNextActivity(context, ListingActivity.class, AppConstant.TYPE_ALL_HEALTH_PACKAGE);
                break;
            case R.id.tvViewAllLabs:
                CommonUtils.goToNextActivity(context, ListingActivity.class, AppConstant.TYPE_ALL_LABS);
                break;
            case R.id.tvViewAllPopularTest:
                CommonUtils.goToNextActivity(context, ListingActivity.class, AppConstant.TYPE_ALL_LAB_TESTS);
                break;
            case R.id.llAlltests:
                CommonUtils.goToNextActivity(context, ListingActivity.class, AppConstant.TYPE_ALL_LAB_TESTS);
                break;
            case R.id.llViewMore:
                if (flag == 1) {
                    setCategoryAdapter();
                    binding.tvViewmore.setText(getResources().getString(R.string.view_more));
                    binding.ivView.setImageDrawable(getResources().getDrawable(R.drawable.down_arrow));
                    flag = 2;
                } else if (flag == 2) {
                    setViewMore();
                    binding.tvViewmore.setText(getResources().getString(R.string.viewless));
                    binding.ivView.setImageDrawable(getResources().getDrawable(R.drawable.up_arrow));
                    flag = 1;
                }
                break;
            case R.id.llCallToOrder:
                showCallToOrderDialog("Book on Call", "Call us on +8588883811 and book your\nLab Test.");

                break;
            default:
                break;
        }
    }

    private void showCallToOrderDialog(String heading, String desc) {
        final BottomCallToOrderBinding callToOrderBinding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.bottom_call_to_order, null, false);
        final BottomSheetDialog dialog = DialogUtils.createBottomDialog(context, callToOrderBinding.getRoot());
        dialog.setCancelable(false);
        callToOrderBinding.tvTitle.setText(heading);
        callToOrderBinding.tvDescription.setText(desc);
        callToOrderBinding.ivCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        callToOrderBinding.btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtils.openDialer(context, AppConstant.DRUGVILLA_CONTACT_NUMBER);
            }
        });
    }


    private void getBannerDataApi() {
        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                Api api = RequestController.createService(context, Api.class);
                api.getBanners("lab").enqueue(new BaseCallback<BannerResponse>(context) {
                    @Override
                    public void onSuccess(BannerResponse response) {
                        ProgressDialogUtils.dismiss();
                        if (response != null) {
                            if (response.getStatus() == 1) {
                                CommonUtils.showToastShort(context, response.getMessage());

                                if (response.getBannerDataList() != null && !response.getBannerDataList().isEmpty()) {
                                    bannerDataList.clear();
                                    bannerDataList.addAll(response.getBannerDataList());
                                } else {
                                    bannerDataList.clear();

                                }

                            } else {
                                bannerDataList.clear();

                                CommonUtils.showToastShort(context, response.getMessage());
                            }
                        } else {
                            CommonUtils.showToastShort(context, getResources().getString(R.string.no_response));
                        }
                    }

                    @Override
                    public void onFail(Call<BannerResponse> call, BaseResponse baseResponse) {
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
    private void setSliderViews(List<BannerData> bannerDataList) {
        binding.pager.setAdapter(new AndroidImageAdapter(this, bannerDataList));
        binding.indicator.setViewPager(binding.pager);
        NUM_PAGES = bannerDataList.size();
        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                binding.pager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 2000, 2000);

        // Pager listener over indicator
        binding.indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });

    }

    private void getCategoryDataApi() {
        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                Api api = RequestController.createService(context, Api.class);
                api.getCategory("lab").enqueue(new BaseCallback<CategoryResponse>(context) {
                    @Override
                    public void onSuccess(CategoryResponse response) {
                        ProgressDialogUtils.dismiss();
                        if (response != null) {
                            if (response.getStatus() == 1) {
                                CommonUtils.showToastShort(context, response.getMessage());

                                if (response.getCategoryDataList() != null && !response.getCategoryDataList().isEmpty()) {
                                    categoryDataList.clear();
                                    categoryDataList.addAll(response.getCategoryDataList());

                                } else {
                                    categoryDataList.clear();

                                }

                            } else {
                                categoryDataList.clear();

                                CommonUtils.showToastShort(context, response.getMessage());
                            }
                        } else {
                            CommonUtils.showToastShort(context, getResources().getString(R.string.no_response));
                        }
                    }

                    @Override
                    public void onFail(Call<CategoryResponse> call, BaseResponse baseResponse) {
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
    private void setCategoryAdapter() {
        listData.clear();
        listData = PrepareDataMessage();
        if (listData.size() > 4) {
            binding.llViewMore.setVisibility(View.VISIBLE);
        }
        gridLayoutManager = new GridLayoutManager(context, 4);
        binding.rvTestCategory.setLayoutManager(gridLayoutManager);
        binding.rvTestCategory.hasFixedSize();
        categoryAdapter = new CategoryAdapter(activity, categoryDataList, AppConstant.TYPE_LABS_CATEGORY);
        binding.rvTestCategory.setAdapter(categoryAdapter);
    }
    private void setViewMore() {
        listData.clear();
        listData = PrepareDataMessage();
        gridLayoutManager = new GridLayoutManager(context, 4, RecyclerView.VERTICAL, false);
        binding.rvTestCategory.setLayoutManager(gridLayoutManager);
        binding.rvTestCategory.hasFixedSize();
        allCategoryAdapter = new AllCategoryAdapter(activity, categoryDataList, AppConstant.TYPE_LABS_CATEGORY);
        binding.rvTestCategory.setAdapter(allCategoryAdapter);
    }

    private void getTestsApi() {
        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                Api api = RequestController.createService(context, Api.class);
                api.getTests().enqueue(new BaseCallback<LabDashboardResponse>(context) {
                    @Override
                    public void onSuccess(LabDashboardResponse response) {
                        binding.swipeRefreshingLayout.setRefreshing(false);
                        ProgressDialogUtils.dismiss();
                        if (response != null) {
                            if (response.getStatus() == 1) {
                                CommonUtils.showToastShort(context, response.getMessage());
                                if (response.getDataList() != null && !response.getDataList().isEmpty()) {
                                    packageTestDataList.clear();
                                    packageTestDataList.addAll(response.getDataList());
                                    adapter.notifyDataSetChanged();
                                } else {
                                    packageTestDataList.clear();
                                    adapter.notifyDataSetChanged();
                                }

                            } else {
                                packageTestDataList.clear();
                                adapter.notifyDataSetChanged();
                                CommonUtils.showToastShort(context, response.getMessage());
                            }
                        } else {
                            CommonUtils.showToastShort(context, getResources().getString(R.string.no_response));
                        }
                    }

                    @Override
                    public void onFail(Call<LabDashboardResponse> call, BaseResponse baseResponse) {
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
    private void setPopularTest() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
        binding.rvPopularTest.setLayoutManager(layoutManager);
        binding.rvPopularTest.hasFixedSize();
        binding.rvPopularTest.setItemAnimator(new DefaultItemAnimator());
        adapter = new PopularHealthPackageAdapter(context, packageTestDataList, new OnSelectedListener() {
            @Override
            public void onClick(View view, int position) {
                setBottomView("test");
            }
        }, AppConstant.TYPE_ALL_LAB_TESTS);
        binding.rvPopularTest.setAdapter(adapter);
    }

    private void getLabsApi() {
        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                Api api = RequestController.createService(context, Api.class);
                api.getLabs().enqueue(new BaseCallback<LabBaseResponse>(context) {
                    @Override
                    public void onSuccess(LabBaseResponse response) {
                        ProgressDialogUtils.dismiss();
                        if (response != null) {
                            if (response.getStatus() == 1) {
                                CommonUtils.showToastShort(context, response.getMessage());
                                if (response.getLabDataList() != null && !response.getLabDataList().isEmpty()) {
                                    labDataList.clear();
                                    labDataList.addAll(response.getLabDataList());
                                    productAdapter.notifyDataSetChanged();
                                } else {
                                    labDataList.clear();
                                    productAdapter.notifyDataSetChanged();
                                }

                            } else {
                                labDataList.clear();
                                productAdapter.notifyDataSetChanged();
                                CommonUtils.showToastShort(context, response.getMessage());
                            }
                        } else {
                            CommonUtils.showToastShort(context, getResources().getString(R.string.no_response));
                        }
                    }

                    @Override
                    public void onFail(Call<LabBaseResponse> call, BaseResponse baseResponse) {
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
    private void setFeaturedLabs() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
        binding.rvLabs.setLayoutManager(layoutManager);
        binding.rvLabs.hasFixedSize();
        binding.rvLabs.setItemAnimator(new DefaultItemAnimator());
        productAdapter = new ProductAdapter(context, labDataList, AppConstant.TYPE_ALL_LABS);
        productAdapter.showRating(true);
        binding.rvLabs.setAdapter(productAdapter);
    }

    private void getPackagesApi() {
        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                Api api = RequestController.createService(context, Api.class);
                api.getPackages().enqueue(new BaseCallback<LabDashboardResponse>(context) {
                    @Override
                    public void onSuccess(LabDashboardResponse response) {
                        binding.swipeRefreshingLayout.setRefreshing(false);
                        ProgressDialogUtils.dismiss();
                        if (response != null) {
                            if (response.getStatus() == 1) {
                                CommonUtils.showToastShort(context, response.getMessage());
                                if (response.getDataList() != null && !response.getDataList().isEmpty()) {
                                    packageTestDataList.clear();
                                    packageTestDataList.addAll(response.getDataList());
                                    adapter.notifyDataSetChanged();
                                } else {
                                    packageTestDataList.clear();
                                    adapter.notifyDataSetChanged();
                                }

                            } else {
                                packageTestDataList.clear();
                                adapter.notifyDataSetChanged();
                                CommonUtils.showToastShort(context, response.getMessage());
                            }
                        } else {
                            CommonUtils.showToastShort(context, getResources().getString(R.string.no_response));
                        }
                    }

                    @Override
                    public void onFail(Call<LabDashboardResponse> call, BaseResponse baseResponse) {
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
    private void setPopularPackage() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
        binding.rvPackages.setLayoutManager(layoutManager);
        binding.rvPackages.hasFixedSize();
        binding.rvPackages.setItemAnimator(new DefaultItemAnimator());
        adapter = new PopularHealthPackageAdapter(context, packageTestDataList, new OnSelectedListener() {
            @Override
            public void onClick(View view, int position) {
                setBottomView("package");
            }
        }, AppConstant.TYPE_ALL_HEALTH_PACKAGE);
        binding.rvPackages.setAdapter(adapter);
    }

}
