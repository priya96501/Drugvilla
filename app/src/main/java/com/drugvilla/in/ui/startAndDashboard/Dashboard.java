package com.drugvilla.in.ui.startAndDashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;

import com.drugvilla.in.R;
import com.drugvilla.in.adapter.dummy.DashboardAdapter;
import com.drugvilla.in.adapter.product.AllCategoryAdapter;
import com.drugvilla.in.adapter.product.CategoryAdapter;
import com.drugvilla.in.adapter.slidder.AndroidImageAdapter;
import com.drugvilla.in.adapter.slidder.TextAdapter;
import com.drugvilla.in.databinding.ActivityDashboardBinding;
import com.drugvilla.in.databinding.BottomCallToOrderBinding;
import com.drugvilla.in.databinding.DialogBinding;
import com.drugvilla.in.listener.OnSelectedTypeListener;
import com.drugvilla.in.model.Document;
import com.drugvilla.in.model.dashboard.BannerData;
import com.drugvilla.in.model.dashboard.BannerResponse;
import com.drugvilla.in.model.dashboard.CategoryData;
import com.drugvilla.in.model.dashboard.CategoryResponse;
import com.drugvilla.in.model.dashboard.HomeData;
import com.drugvilla.in.model.dashboard.HomeResponse;
import com.drugvilla.in.model.dashboard.HomeSubData;
import com.drugvilla.in.model.dashboard.RedirectUrl;
import com.drugvilla.in.service.Api;
import com.drugvilla.in.service.BaseCallback;
import com.drugvilla.in.service.BaseResponse;
import com.drugvilla.in.service.RequestController;
import com.drugvilla.in.ui.others.SearchActivity;
import com.drugvilla.in.ui.authentication.Login;
import com.drugvilla.in.ui.blog.AllBlogs;
import com.drugvilla.in.ui.blog.CategoryWiseArticle;
import com.drugvilla.in.ui.doctors.DoctorCategory;
import com.drugvilla.in.ui.labs.LabDashboard;
import com.drugvilla.in.ui.labs.ListingActivity;
import com.drugvilla.in.ui.offer.Offer;
import com.drugvilla.in.ui.order.Cart;
import com.drugvilla.in.ui.order.MyOrder;
import com.drugvilla.in.ui.others.ContactUs;
import com.drugvilla.in.ui.others.LegalInfoOption;
import com.drugvilla.in.ui.others.Redirect;
import com.drugvilla.in.ui.prescription.Upload;
import com.drugvilla.in.ui.product.ProductDashboard;
import com.drugvilla.in.ui.product.ProductsListing;
import com.drugvilla.in.ui.reminders.Reminders;
import com.drugvilla.in.ui.user.Profile;
import com.drugvilla.in.ui.user.RefferEarn;
import com.drugvilla.in.ui.user.Wallet;
import com.drugvilla.in.ui.user.Wishlist;
import com.drugvilla.in.utils.ActivityController;
import com.drugvilla.in.utils.AppConstant;
import com.drugvilla.in.utils.CommonUtils;
import com.drugvilla.in.utils.DialogUtils;
import com.drugvilla.in.utils.ProgressDialogUtils;
import com.drugvilla.in.utils.SharedPref;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;

public class Dashboard extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    private Context context;
    private Activity activity;
    private ActivityDashboardBinding binding;
    int flag = 2;
    private Dialog dialog;
    private String from = " ";
    private GoogleApiClient googleApiClient;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private LinearLayoutManager linearLayoutManager;
    private GridLayoutManager gridLayoutManager;

    private List<BannerData> bannerDataList = new ArrayList<>();
    private List<CategoryData> categoryData = new ArrayList<>();
    private List<HomeData> homeData = new ArrayList<>();
    private RedirectUrl redirectUrl;
    private CategoryAdapter categoryAdapter;
    private AllCategoryAdapter allCategoryAdapter;

    // bottom slidder
    List<Document> bottomTextData = new ArrayList<>();
    private final int[] myText = new int[]{R.string.text1, R.string.text2, R.string.text3};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard);
        context = Dashboard.this;
        activity = Dashboard.this;
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        getData();
        if (from.equalsIgnoreCase(AppConstant.WITHOUT_LOGIN)) {
            binding.drawer.llLogout.setVisibility(View.GONE);
            binding.drawer.llLogin.setVisibility(View.VISIBLE);
        } else {
            binding.drawer.llLogin.setVisibility(View.GONE);
            binding.drawer.llLogout.setVisibility(View.VISIBLE);
        }

        // setting bottom data
        bottomTextData = populateList2();
        setBottomSlidderData(bottomTextData);

        setToolbar();
        setNavigationDrawer();
        setListeners();

        binding.swipeRefreshingLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getHomePageData();
                setAllData();
                setCategoryAdapter();
                setBannerData(bannerDataList);
            }
        });

    }

    @Override
    protected void onResume() {
        // getting all api's data
        getHomePageData();
        // setting all data in main recyclerview
        setAllData();
        setCategoryAdapter();
        setBannerData(bannerDataList);
        super.onResume();
    }


    private void setNavigationDrawer() {
        binding.drawer.llWallet.setOnClickListener(this);
        binding.drawer.llReferEarn.setOnClickListener(this);
        binding.drawer.llAboutUs.setOnClickListener(this);
        binding.drawer.llContactUs.setOnClickListener(this);
        binding.drawer.llLegalInfo.setOnClickListener(this);
        binding.drawer.llLogout.setOnClickListener(this);
        binding.drawer.llFAQ.setOnClickListener(this);
        binding.drawer.llViewAllBlog.setOnClickListener(this);
        binding.drawer.llReminders.setOnClickListener(this);
        binding.drawer.llOrder.setOnClickListener(this);
        binding.drawer.llBookLabTest.setOnClickListener(this);
        binding.drawer.llFindDoctors.setOnClickListener(this);
        binding.drawer.llConsultDoctors.setOnClickListener(this);
        binding.drawer.llOffers.setOnClickListener(this);
        binding.drawer.llAccount.setOnClickListener(this);
        binding.drawer.llMyOrders.setOnClickListener(this);
        binding.drawer.llMyLabTest.setOnClickListener(this);
        binding.drawer.llMyAppointments.setOnClickListener(this);
        binding.drawer.llLogin.setOnClickListener(this);

    }

    private void setToolbar() {
        binding.menuBar.tvTitle.setVisibility(View.VISIBLE);
        binding.menuBar.ivRight.setVisibility(View.VISIBLE);
        binding.menuBar.ivSecond.setVisibility(View.VISIBLE);
        binding.menuBar.ivRight.setOnClickListener(this);
        binding.menuBar.ivSecond.setOnClickListener(this);
        binding.menuBar.tvTitle.setText(getResources().getString(R.string.drugvilla));
        binding.menuBar.ivRight.setBadgeValue(3);
        if (binding.menuBar.ivRight.getBadgeValue() <= 0) {
            binding.menuBar.ivRight.visibleBadge(false);
        }
    }

    private void setListeners() {
        binding.btnUploadPrescription.setOnClickListener(this);
        binding.btnOrderNow.setOnClickListener(this);
        binding.llSearch.setOnClickListener(this);
        binding.llFindDoctors.setOnClickListener(this);
        binding.llBookLabTest.setOnClickListener(this);
        binding.llHealthProducts.setOnClickListener(this);
        binding.llCallToOrder.setOnClickListener(this);
        binding.llViewMore.setOnClickListener(this);
        binding.menuBar.ivBack.setOnClickListener(this);
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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            // toolbar items
            case R.id.ivBack:
                binding.drawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.ivRight:
                ActivityController.startActivity(context, Cart.class, AppConstant.TYPE_PRODUCT_CART);
                break;
            case R.id.ivSecond:
                ActivityController.startActivity(context, Wishlist.class);
                break;

            //dashboard items
            case R.id.llViewMore:
                if (flag == 1) {
                    setCategoryAdapter();
                    binding.tvViewmore.setText(R.string.view_more);
                    binding.ivView.setImageDrawable(getResources().getDrawable(R.drawable.down_arrow));
                    flag = 2;
                } else if (flag == 2) {
                    setViewMore();
                    binding.tvViewmore.setText(R.string.viewless);
                    binding.ivView.setImageDrawable(getResources().getDrawable(R.drawable.up_arrow));
                    flag = 1;
                }
                break;
            case R.id.llSearch:
                ActivityController.startActivity(context, SearchActivity.class, AppConstant.MEDICINES);
                break;
            case R.id.llCallToOrder:
                showCallToOrderDialog("Call to order", "Call us on +8588883811 and order your\nMedicines/Products.");
                break;
            case R.id.llHealthProducts:
                ActivityController.startActivity(context, ProductDashboard.class);
                break;
            case R.id.btnOrderNow:
                ActivityController.startActivity(context, SearchActivity.class, AppConstant.MEDICINES);
                break;
            case R.id.btnUploadPrescription:
                ActivityController.startActivity(context, Upload.class, AppConstant.ORDER_MEDICINES_FROM_PRESCRIPTION);
                break;

            // Navigation drawer items
            case R.id.llLogout:
                openLogoutDialog("Logout", "Are you sure you want to logout?");
                break;
            case R.id.llLogin:
                ActivityController.startActivity(context, Login.class);
                break;
            case R.id.llContactUs:
                ActivityController.startActivity(context, ContactUs.class);
                break;
            case R.id.llFAQ:
                ActivityController.startActivity(context, Redirect.class, AppConstant.FAQ);
                break;
            case R.id.llAboutUs:
                ActivityController.startActivity(context, Redirect.class, AppConstant.ABOUT_US);
                break;
            case R.id.llLegalInfo:
                ActivityController.startActivity(context, LegalInfoOption.class);
                break;
            case R.id.llMyAppointments:
                getNextScreenSelection(MyOrder.class, true, AppConstant.MY_APPOINTMENT);
                break;
            case R.id.llMyOrders:
                getNextScreenSelection(MyOrder.class, true, AppConstant.MY_ORDERS);
                break;
            case R.id.llMyLabTest:
                getNextScreenSelection(MyOrder.class, true, AppConstant.MY_LAB_TESTS);
                break;
            case R.id.llOffers:
                getNextScreenSelection(Offer.class, false, "");
                break;
            case R.id.llReferEarn:
                getNextScreenSelection(RefferEarn.class, false, "");
                break;
            case R.id.llAccount:
                getNextScreenSelection(Profile.class, false, "");
                break;
            case R.id.llWallet:
                getNextScreenSelection(Wallet.class, false, "");
                break;
            case R.id.llViewAllBlog:
                ActivityController.startActivity(context, AllBlogs.class);
                break;
            case R.id.llBookLabTest:
                ActivityController.startActivity(context, LabDashboard.class);
                break;
            case R.id.llOrder:
                ActivityController.startActivity(context, Upload.class, AppConstant.ORDER_MEDICINES_FROM_PRESCRIPTION);
                // ActivityController.startActivity();(context, SearchActivity.class, AppConstant.MEDICINES);
                break;
            case R.id.llReminders:
                getNextScreenSelection(Reminders.class, false, "");
                break;
            case R.id.llFindDoctors:
                ActivityController.startActivity(context, DoctorCategory.class);
                break;
            default:
                break;
        }

    }

    // next screen selection on basis of user login or not
    private void getNextScreenSelection(Class className, boolean isType, String type) {
        if (SharedPref.getBooleanPreferences(context, AppConstant.IS_LOGIN)) {
            if (isType) {
                ActivityController.startActivity(context, className, type);
            } else {
                ActivityController.startActivity(context, className);
            }
        } else {
            CommonUtils.chooseUploadOption(context);
        }
    }

    // call to order dialog
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
                CommonUtils.openDialer(context, "9878987678");
            }
        });
    }

    // logout dialog
    private void openLogoutDialog(String heading, String desc) {
        final DialogBinding logoutDialogBinding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.dialog, null, false);
        dialog = DialogUtils.createDialog(context, logoutDialogBinding.getRoot(), 0);
        dialog.setCancelable(false);
        logoutDialogBinding.tvHeading.setText(heading);
        logoutDialogBinding.tvDescription.setText(desc);
        logoutDialogBinding.tvNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        logoutDialogBinding.tvYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToLogin();
                //  userLogout();
            }
        });
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

    private void setBottomSlidderData(List<Document> list) {
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

    // setting banner data
    private void setBannerData(List<BannerData> list) {
      /*  binding.pager.setAdapter(new AndroidImageAdapter(this, list));
        binding.indicator.setViewPager(binding.pager);
        NUM_PAGES = list.size();
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
        }, 1000, 1000);
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
*/
    }

    private void goToLogin() {
        SharedPref.clearPrefs(context);
        ActivityController.startActivity(activity, Login.class, true, true);
        dialog.dismiss();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    private void getCategoryDataApi() {
        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                Api api = RequestController.createService(context, Api.class);
                api.getCategory("product").enqueue(new BaseCallback<CategoryResponse>(context) {
                    @Override
                    public void onSuccess(CategoryResponse response) {
                        ProgressDialogUtils.dismiss();
                        if (response != null) {
                            if (response.getStatus() == 1) {
                                CommonUtils.showToastShort(context, response.getMessage());
                                if (response.getCategoryDataList() != null && !response.getCategoryDataList().isEmpty()) {
                                    categoryData.clear();
                                    categoryData.addAll(response.getCategoryDataList());
                                } else {
                                    categoryData.clear();
                                }

                            } else {
                                categoryData.clear();
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

    private void getBannerDataApi() {
        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                Api api = RequestController.createService(context, Api.class);
                api.getBanners("all").enqueue(new BaseCallback<BannerResponse>(context) {
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

    public void getHomePageData() {
        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                Api api = RequestController.createService(context, Api.class);
                api.getHomePageData().enqueue(new BaseCallback<HomeResponse>(context) {
                    @Override
                    public void onSuccess(HomeResponse response) {
                        binding.swipeRefreshingLayout.setRefreshing(false);
                        ProgressDialogUtils.dismiss();
                        if (response != null) {
                            if (response.getStatus() == 1) {
                                CommonUtils.showToastShort(context, response.getMessage());


                                if (response.getBannerData() != null && !response.getBannerData().isEmpty()) {
                                    bannerDataList.clear();
                                    bannerDataList.addAll(response.getBannerData());
                                } else {
                                    bannerDataList.clear();
                                }


                                if (response.getCategoryData() != null && !response.getCategoryData().isEmpty()) {
                                    categoryData.clear();
                                    categoryData.addAll(response.getCategoryData());
                                } else {
                                    categoryData.clear();
                                }


                                if (response.getData() != null && !response.getData().isEmpty()) {
                                    homeData.clear();
                                    homeData.addAll(response.getData());
                                } else {
                                    homeData.clear();
                                }

                                //  redirectUrl = response.get();
                                SharedPref.saveStringPreference(context, AppConstant.FAQ, response.getFaqUrl());
                                SharedPref.saveStringPreference(context, AppConstant.PRIVACY_POLICY, response.getPrivacyPolicyUrl());
                                SharedPref.saveStringPreference(context, AppConstant.TERMS_CONDITIONS, response.getTermsConditionsUrl());
                                SharedPref.saveStringPreference(context, AppConstant.ABOUT_US, response.getAboutUsUrl());

                            } else {
                                homeData.clear();
                                categoryData.clear();
                                bannerDataList.clear();
                                CommonUtils.showToastShort(context, response.getMessage());
                            }
                        } else {
                            CommonUtils.showToastShort(context, getResources().getString(R.string.no_response));
                        }
                    }

                    @Override
                    public void onFail(Call<HomeResponse> call, BaseResponse baseResponse) {
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
        if (categoryData.size() > 3) {
            binding.llViewMore.setVisibility(View.VISIBLE);
        }
        gridLayoutManager = new GridLayoutManager(context, 3);
        binding.rvProductCategories.setLayoutManager(gridLayoutManager);
        binding.rvProductCategories.hasFixedSize();
        categoryAdapter = new CategoryAdapter(activity, categoryData, AppConstant.TYPE_PRODUCT_CATEGORY);
        binding.rvProductCategories.setAdapter(categoryAdapter);
    }

    private void setViewMore() {
        gridLayoutManager = new GridLayoutManager(context, 3, RecyclerView.VERTICAL, false);
        binding.rvProductCategories.setLayoutManager(gridLayoutManager);
        binding.rvProductCategories.hasFixedSize();
        allCategoryAdapter = new AllCategoryAdapter(activity, categoryData, AppConstant.TYPE_PRODUCT_CATEGORY);
        binding.rvProductCategories.setAdapter(allCategoryAdapter);
    }

    private void setAllData() {
        linearLayoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
        binding.rvDummy.setLayoutManager(linearLayoutManager);
        binding.rvDummy.hasFixedSize();
        DashboardAdapter dashboardAdapter = new DashboardAdapter(context, homeData, new OnSelectedTypeListener() {
            @Override
            public void onClick(View view, int position, String type) {
                switch (type) {
                    case "BRAND":
                        ActivityController.startActivity(context, ProductsListing.class, AppConstant.TYPE_BRAND);
                        break;
                    case "ARTICLE":
                        ActivityController.startActivity(context, CategoryWiseArticle.class);
                        break;
                    case "PRODUCT":
                        ActivityController.startActivity(context, ProductsListing.class, AppConstant.TYPE_PRODUCT);
                        break;
                    case "HEALTH_CONCERN":

                        break;
                    case "PACKAGE":
                        ActivityController.startActivity(context, ListingActivity.class, AppConstant.TYPE_ALL_HEALTH_PACKAGE);
                        break;
                    default:
                        break;
                }

            }
        });
        binding.rvDummy.setAdapter(dashboardAdapter);
    }

}
