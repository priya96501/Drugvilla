package com.drugvilla.in.ui.startAndDashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.blikoon.qrcodescanner.QrCodeActivity;
import com.drugvilla.in.R;
import com.drugvilla.in.adapter.labs.PopularHealthPackageAdapter;
import com.drugvilla.in.adapter.labs.PopularTestPackageAdapter;
import com.drugvilla.in.adapter.slidder.TextAdapter;
import com.drugvilla.in.adapter.blog.PopularArticleAdapter;
import com.drugvilla.in.adapter.product.AllCategoryAdapter;
import com.drugvilla.in.adapter.product.BrandAdapter;
import com.drugvilla.in.adapter.product.CategoryAdapter;
import com.drugvilla.in.adapter.product.HealthConcernAdapter;
import com.drugvilla.in.adapter.product.ProductAdapter;
import com.drugvilla.in.databinding.ActivityHomeBinding;
import com.drugvilla.in.databinding.BottomCallToOrderBinding;
import com.drugvilla.in.databinding.DialogBinding;
import com.drugvilla.in.databinding.PopupScannerBinding;
import com.drugvilla.in.listener.OnSelectedListener;
import com.drugvilla.in.model.Document;
import com.drugvilla.in.service.Api;
import com.drugvilla.in.service.RequestController;
import com.drugvilla.in.ui.others.SearchActivity;
import com.drugvilla.in.ui.authentication.Login;
import com.drugvilla.in.ui.doctors.DoctorCategory;
import com.drugvilla.in.ui.labs.LabDashboard;
import com.drugvilla.in.ui.labs.ListingActivity;
import com.drugvilla.in.ui.membership.Membership;
import com.drugvilla.in.ui.offer.Offer;
import com.drugvilla.in.ui.order.Cart;
import com.drugvilla.in.ui.blog.AllBlogs;
import com.drugvilla.in.ui.others.ContactUs;
import com.drugvilla.in.ui.others.LegalInfoOption;
import com.drugvilla.in.ui.others.Redirect;
import com.drugvilla.in.ui.prescription.Upload;
import com.drugvilla.in.ui.order.MyOrder;
import com.drugvilla.in.ui.product.ProductDashboard;
import com.drugvilla.in.ui.product.ProductsListing;
import com.drugvilla.in.ui.user.Profile;
import com.drugvilla.in.ui.reminders.Reminders;
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
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Home extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    private Context context;
    private Activity activity;
    private ActivityHomeBinding binding;
    int flag = 2;
    private Dialog dialog;
    private String from = " ";
    private GoogleApiClient googleApiClient;
    private GridLayoutManager gridLayoutManager;
    private LinearLayoutManager layoutManager;

    private static int currentPage = 0;
    private static int NUM_PAGES = 0;

    // top slidder
    List<Document> bannerData = new ArrayList<>();
    private final int[] myImageList = new int[]{R.drawable.banner1, R.drawable.banner2, R.drawable.banner3};

    // bottom slidder
    List<Document> bottomTextData = new ArrayList<>();
    private final int[] myText = new int[]{R.string.text1, R.string.text2, R.string.text3};


    private List<Document> data = new ArrayList<>();
    private List<Document> listDeal = new ArrayList<>();
    private List<Document> listRecommended = new ArrayList<>();
    private List<Document> data2 = new ArrayList<>();
    private List<Document> data3 = new ArrayList<>();
    private List<Document> listData = new ArrayList<>();
    private List<Document> listData3 = new ArrayList<>();
    private List<Document> listTests = new ArrayList<>();

    // shop by categories
    private final int[] productCategory = {R.drawable.category1, R.drawable.category2, R.drawable.category3, R.drawable.category4, R.drawable.category5, R.drawable.category6};
    private final String[] categoryName = {"Ayurveda", "Devices", "Family Care", "Health Care", "Lifestyle", "Personal Care"};


    // top brands
    private final int[] brandImages = {R.drawable.brand11, R.drawable.brand22, R.drawable.brand33, R.drawable.brand11, R.drawable.brand22, R.drawable.brand33};
    private final String[] brandName = {"Dabur", "Himalaya", "Ayur", "Godrej", "Nivea", "Bournvita"};

    // articles
    private final int[] article = {R.drawable.article1, R.drawable.article2, R.drawable.article3, R.drawable.article1, R.drawable.article2, R.drawable.article3};
    private final String[] articleTitle = {"How does exercise support health later in life?",
            "How does exercise support health later in life?",
            "The Mental Health Benefits of Exercise",
            "How does exercise support health later in life?",
            "How does exercise support health later in life?",
            "The Mental Health Benefits of Exercise"};


    // top products
    private final int[] productImages = {R.drawable.product1, R.drawable.product2, R.drawable.product3, R.drawable.product4, R.drawable.product1, R.drawable.product2};
    private final String[] productName = {"Fairness Creams", "Himalaya Shampoo", "Health Drinks", "Chawanprash", "Fairness Creams", "Himalaya Shampoo"};

    // recommended products
    private final int[] recommendedImages = {R.drawable.rc1, R.drawable.med2, R.drawable.med3, R.drawable.med1, R.drawable.med2, R.drawable.rc1};
    private final String[] recommendedName = {"Callaxo-Max", "IMore Gel", "Orlistat Capsules", "Murine-XT", "Murine-300", "Callaxo-Max"};

    // deal of the day
    private final int[] dealImages = {R.drawable.brand1, R.drawable.brand2, R.drawable.brand3, R.drawable.brand4, R.drawable.brand5, R.drawable.brand6};
    private final String[] dealName = {"Dabur", "Himalaya", "Ayur", "Godrej", "Nivea", "Bournvita"};
    // popular test and packages
    private final int[] popularTestImages = {R.drawable.top_package6, R.drawable.top_package1, R.drawable.top_package2, R.drawable.top_package3, R.drawable.top_package4, R.drawable.top_package5};
    private final String[] popularTestName = {"Aarogyam 1.3", "Complete Blood Count", "Diabetes Care", "Full Body Screening", "Kidney Function Test", "Prolactin (PRL)"};


    // popular health concern(shop by concern)
    private final int[] concernImage = {R.drawable.concern1, R.drawable.concern3, R.drawable.concern2, R.drawable.concern5, R.drawable.concern6, R.drawable.concern4};
    private final String[] concernName = {"Stomach\nPain", "Bone and \njoint pain", "Mental\ncare", "Weight \nManagement", "Beauty \nCare ", "Lungs \nCare"};
    // health packages
    private final String[] packageName = {"Advance Health Care", "Healthy Heart", "Complete Care", "Basic Health Care", "Diabetes Care Package"};
    private final String[] packageBy = {"By Delhi Path Lab", "By Delhi Path Lab", "By Sure path labs pvt ltd", "By Thyrocare Laboratories Ltd.", "By Delhi Path Lab"};
    private final String[] packagePrice = {"1600", "400", "750", "1100", "500"};



    private static final int REQUEST_CODE_QR_SCAN = 101;
    private final String LOGTAG = "QRCScanner-MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        context = Home.this;
        activity = Home.this;
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        getData();
        setToolbar();
        setNavigationDrawer();
        setListeners();

        if (from.equalsIgnoreCase(AppConstant.WITHOUT_LOGIN)) {
            binding.drawer.llLogout.setVisibility(View.GONE);
            binding.drawer.llLogin.setVisibility(View.VISIBLE);
        } else {
            binding.drawer.llLogin.setVisibility(View.GONE);
            binding.drawer.llLogout.setVisibility(View.VISIBLE);
        }


        /*// setting banner data
        bannerData = populateList();
        setSliderViews(bannerData);*/

        // setting bottom data
        bottomTextData = populateList2();
        setSliderViews2(bottomTextData);


        // shop by categories
        setProductCategory();

        // articles
        setArticles();

        // health concerns
        setHealthConcern();

        // popular test and packages
        setPopularTestPackages();
        // setPopularPackage();

        // top brands
        setBrand();


        // deal of the day
        setAdapter(listDeal, binding.rvDeals, dealImages, dealName, AppConstant.TYPE_DEAL_OF_THE_DAY);
        // top products
        setAdapter(data2, binding.rvTopProducts, productImages, productName, AppConstant.TYPE_PRODUCT);

        // recommended products
        setAdapter(listRecommended, binding.rvRecommendedProducts, recommendedImages, recommendedName, AppConstant.TYPE_RECOMMENDED_PRODUCT);
        binding.swipeRefreshingLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setProductCategory();
                setArticles();
                setHealthConcern();
                setPopularTestPackages();
                //setPopularPackage();
                setBrand();
                setAdapter(listDeal, binding.rvDeals, dealImages, dealName, AppConstant.TYPE_DEAL_OF_THE_DAY);
                setAdapter(data2, binding.rvTopProducts, productImages, productName, AppConstant.TYPE_PRODUCT);
                setAdapter(listRecommended, binding.rvRecommendedProducts, recommendedImages, recommendedName, AppConstant.TYPE_RECOMMENDED_PRODUCT);
                binding.swipeRefreshingLayout.setRefreshing(false);

            }
        });

    }

    //Getting the scan results
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            Log.d(LOGTAG, "COULD NOT GET A GOOD RESULT.");
            if (data == null)
                return;
            //Getting the passed result
            String result = data.getStringExtra("com.blikoon.qrcodescanner.error_decoding_image");
            if (result != null) {
                AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                alertDialog.setTitle("Scan Error");
                alertDialog.setMessage("QR Code could not be scanned");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
            return;

        }
        if (requestCode == REQUEST_CODE_QR_SCAN) {
            if (data == null)
                return;
            //Getting the passed result
            String result = data.getStringExtra("com.blikoon.qrcodescanner.got_qr_scan_relult");
            Log.d(LOGTAG, "Have scan result in your app activity :" + result);
            AlertDialog alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.setTitle("Scan result");
            alertDialog.setMessage(result);
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();

        }
    }



    private void setPopularTestPackages() {
        layoutManager = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
        binding.rvTopTest.setLayoutManager(layoutManager);
        binding.rvTopTest.hasFixedSize();
        binding.rvTopTest.setItemAnimator(new DefaultItemAnimator());
        listTests.clear();
        listTests = PrepareDataMessage9();
       // PopularTestPackageAdapter popularTestPackageAdapter = new PopularTestPackageAdapter(context, listTests);
        //binding.rvTopTest.setAdapter(popularTestPackageAdapter);

    }

    private List<Document> PrepareDataMessage9() {

        List<Document> dataa = new ArrayList<>();

        for (int i = 0; i < popularTestImages.length; i++) {

            Document document = new Document();

            document.setImage(popularTestImages[i]);
            document.setText(popularTestName[i]);
            dataa.add(document);

        }

        listTests.addAll(dataa);

        return dataa;

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

    private void setAdapter(List<Document> list, RecyclerView recyclerView, int[] image, String[] name, String type) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.hasFixedSize();
        list.clear();
        list = PrepareDataMessage(list, image, name);
     //   ProductAdapter productAdapter = new ProductAdapter(context, list, type);

        if (type.equalsIgnoreCase(AppConstant.TYPE_PRODUCT)) {
          //  productAdapter.showRating(true);
        }
        if (type.equalsIgnoreCase(AppConstant.TYPE_RECOMMENDED_PRODUCT)) {
           // productAdapter.showRating(true);
          //  productAdapter.showOffer(true);
        }
        if (type.equalsIgnoreCase(AppConstant.TYPE_DEAL_OF_THE_DAY)) {
         //   productAdapter.showOffer(true);
        }

        //recyclerView.setAdapter(productAdapter);
    }

    private List<Document> PrepareDataMessage(List<Document> list, int[] image, String[] name) {
        List<Document> data3 = new ArrayList<>();
        for (int i = 0; i < name.length; i++) {
            Document document = new Document();
            document.setImage(image[i]);
            document.setText(name[i]);
            data3.add(document);
        }
        list.addAll(data3);
        return data3;
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


    private void setArticles() {
        layoutManager = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
        binding.rvArticles.setLayoutManager(layoutManager);
        binding.rvArticles.hasFixedSize();
        binding.rvArticles.setItemAnimator(new DefaultItemAnimator());
        listData3.clear();
        listData3 = PrepareDataMessage3();
      //  PopularArticleAdapter articleAdapter = new PopularArticleAdapter(context, listData3);
      //  binding.rvArticles.setAdapter(articleAdapter);
    }


    private void setBrand() {
        layoutManager = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
        binding.rvBrands.setLayoutManager(layoutManager);
        binding.rvBrands.hasFixedSize();
        binding.rvBrands.setItemAnimator(new DefaultItemAnimator());
        data.clear();
        data = PrepareDataMessage5();
       // BrandAdapter brandAdapter = new BrandAdapter(activity, data);
       // binding.rvBrands.setAdapter(brandAdapter);
    }

    private List<Document> PrepareDataMessage5() {

        List<Document> dataa = new ArrayList<>();

        for (int i = 0; i < brandImages.length; i++) {

            Document document = new Document();

            document.setImage(brandImages[i]);
            document.setText(brandName[i]);
            dataa.add(document);

        }

        data.addAll(dataa);

        return dataa;

    }


    private void setHealthConcern() {
        layoutManager = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
        binding.rvHealthConcern.setLayoutManager(layoutManager);
        binding.rvHealthConcern.hasFixedSize();
        binding.rvHealthConcern.setItemAnimator(new DefaultItemAnimator());
        data3.clear();
        data3 = PrepareDataMessage2();
      //  HealthConcernAdapter healthConcernAdapter = new HealthConcernAdapter(context, data3, AppConstant.HEALTH_CONCERN_SQUARE);
      //  binding.rvHealthConcern.setAdapter(healthConcernAdapter);
    }

    private List<Document> PrepareDataMessage2() {

        List<Document> data = new ArrayList<>();

        for (int i = 0; i < concernImage.length; i++) {

            Document document = new Document();

            document.setImage(concernImage[i]);
            document.setText(concernName[i]);
            data.add(document);

        }

        data3.addAll(data);

        return data;

    }

    private void setProductCategory() {

        listData.clear();
        listData = PrepareDataMessage();

        if (listData.size() > 3) {
            binding.llViewMore.setVisibility(View.VISIBLE);

        }
        gridLayoutManager = new GridLayoutManager(context, 3);
        binding.rvProductCategories.setLayoutManager(gridLayoutManager);
        binding.rvProductCategories.hasFixedSize();
       // CategoryAdapter categoryAdapter = new CategoryAdapter(activity, listData, AppConstant.TYPE_PRODUCT_CATEGORY);
      //  binding.rvProductCategories.setAdapter(categoryAdapter);
    }

    private void setViewMore() {
        listData.clear();
        listData = PrepareDataMessage();
        gridLayoutManager = new GridLayoutManager(context, 3, RecyclerView.VERTICAL, false);
        binding.rvProductCategories.setLayoutManager(gridLayoutManager);
        binding.rvProductCategories.hasFixedSize();
       // AllCategoryAdapter allCategoryAdapter = new AllCategoryAdapter(activity, listData, AppConstant.TYPE_PRODUCT_CATEGORY);
       // binding.rvProductCategories.setAdapter(allCategoryAdapter);
    }

    private List<Document> PrepareDataMessage3() {
        List<Document> data3 = new ArrayList<>();
        for (int i = 0; i < article.length; i++) {
            Document document = new Document();
            document.setImage(article[i]);
            document.setText(articleTitle[i]);
            data3.add(document);
        }
        listData3.addAll(data3);
        return data3;
    }


    private List<Document> PrepareDataMessage() {
        List<Document> data = new ArrayList<>();
        for (int i = 0; i < productCategory.length; i++) {
            Document document = new Document();
            document.setImage(productCategory[i]);
            document.setText(categoryName[i]);
            data.add(document);
        }
        listData.addAll(data);
        return data;
    }

    private void setNavigationDrawer() {
        binding.drawer.llScanner.setOnClickListener(this);
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
        binding.llReferEarn.setOnClickListener(this);
        binding.btnUploadPrescription.setOnClickListener(this);
        binding.btnOrderNow.setOnClickListener(this);
        binding.llSearch.setOnClickListener(this);
        binding.llFindDoctors.setOnClickListener(this);
        binding.llBookLabTest.setOnClickListener(this);
        binding.llHealthProducts.setOnClickListener(this);
        binding.llCallToOrder.setOnClickListener(this);
        binding.llViewMore.setOnClickListener(this);
        binding.tvViewAllDeal.setOnClickListener(this);
        binding.tvViewAllTopProducts.setOnClickListener(this);
        binding.tvViewAllRecommendedProducts.setOnClickListener(this);
        binding.tvViewAllTopBrands.setOnClickListener(this);
        binding.tvViewAllTopTest.setOnClickListener(this);
        binding.btnEnroolNow.setOnClickListener(this);
        binding.menuBar.ivBack.setOnClickListener(this);
    }


   /* // setting banners
    private ArrayList<Document> populateList() {
        ArrayList<Document> list = new ArrayList<>();
        for (int i1 : myImageList) {
            Document imageModel = new Document();
            imageModel.setImage(i1);
            list.add(imageModel);
        }
        return list;
    }

    private void setSliderViews(List<Document> list) {
        binding.pager.setAdapter(new AndroidImageAdapter(this, list));
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

    }*/

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llScanner:
                //initiating the qr code scan
                // qrScan.initiateScan();
                openScannerDialog();

                break;
            case R.id.ivBack:
                binding.drawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.llViewMore:
                if (flag == 1) {
                    setProductCategory();
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
            case R.id.llCallToOrder:
                showCallToOrderDialog("Call to order", "Call us on +8588883811 and order your\nMedicines/Products.");
                break;
            case R.id.llLogout:
                openLogoutDialog("Logout", "Are you sure you want to logout?");
                break;
            case R.id.llLogin:
                ActivityController.startActivity(context, Login.class);
                break;
            case R.id.llFindDoctors:
                ActivityController.startActivity(context, DoctorCategory.class);
                break;
            case R.id.btnEnroolNow:
                ActivityController.startActivity(context, Membership.class);
                break;
            case R.id.llHealthProducts:
                ActivityController.startActivity(context, ProductDashboard.class);
                break;
            case R.id.tvViewAllDeal:
                ActivityController.startActivity(context, ProductsListing.class, AppConstant.TYPE_PRODUCT);
                break;
            case R.id.tvViewAllTopBrands:
                ActivityController.startActivity(context, ProductsListing.class, AppConstant.TYPE_BRAND);
                break;
            case R.id.tvViewAllTopProducts:
                ActivityController.startActivity(context, ProductsListing.class, AppConstant.TYPE_PRODUCT);
                break;
            case R.id.tvViewAllTopTest:
                ActivityController.startActivity(context, ListingActivity.class, AppConstant.TYPE_ALL_HEALTH_PACKAGE);
                break;
            case R.id.tvViewAllRecommendedProducts:
                ActivityController.startActivity(context, ProductsListing.class, AppConstant.TYPE_PRODUCT);
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
            case R.id.llReferEarn:
                getNextScreenSelection(RefferEarn.class, false, "");
                break;
            case R.id.llAccount:
                getNextScreenSelection(Profile.class, false, "");
                break;
            case R.id.llContactUs:
                ActivityController.startActivity(context, ContactUs.class);
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
            case R.id.llReminders:
                getNextScreenSelection(Reminders.class, false, "");
                break;
            case R.id.llOffers:
                getNextScreenSelection(Offer.class, false, "");
                break;
            case R.id.ivSecond:
                ActivityController.startActivity(context, Wishlist.class);
                break;
            case R.id.llWallet:
                getNextScreenSelection(Wallet.class, false, "");
                break;
            case R.id.ivRight:
                ActivityController.startActivity(context, Cart.class, AppConstant.TYPE_PRODUCT_CART);
                break;
            case R.id.llSearch:
                ActivityController.startActivity(context, SearchActivity.class, AppConstant.MEDICINES);
                break;
            case R.id.btnOrderNow:
                ActivityController.startActivity(context, SearchActivity.class, AppConstant.MEDICINES);
                break;
            case R.id.btnUploadPrescription:
                getNextScreenSelection(Upload.class, true, AppConstant.ORDER_MEDICINES_FROM_PRESCRIPTION);
                break;
            default:
                break;
        }
    }

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

    private void openScannerDialog() {
        final PopupScannerBinding scannerBinding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.popup_scanner, null, false);
        dialog = DialogUtils.createDialog(context, scannerBinding.getRoot(), 0);
        dialog.setCancelable(true);
        scannerBinding.btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                //Start the qr scan activity
                Intent i = new Intent(context, QrCodeActivity.class);
                startActivityForResult(i, REQUEST_CODE_QR_SCAN);
            }
        });

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

    private void userLogout() {
        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                String userId = SharedPref.getStringPreferences(context, AppConstant.USER_ID);
                Api api = RequestController.createService(context, Api.class);
                api.logout(userId).enqueue(new Callback<com.drugvilla.in.service.BaseResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<com.drugvilla.in.service.BaseResponse> call, @NonNull Response<com.drugvilla.in.service.BaseResponse> response) {
                        ProgressDialogUtils.dismiss();
                        if (response.body() != null) {
                            if (response.body().getStatus() == 1) {

                                String LOGIN_TYPE = SharedPref.getStringPreferences(context, AppConstant.LOGIN_TYPE);
                                if (LOGIN_TYPE.equalsIgnoreCase(AppConstant.TYPE_SOCIAL_LOGIN_GOOGLE)) {
                                    FirebaseAuth.getInstance().signOut();
                                    Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
                                            new ResultCallback<Status>() {
                                                @Override
                                                public void onResult(@NonNull Status status) {
                                                    if (status.isSuccess()) {
                                                        goToLogin();
                                                    } else {
                                                        Toast.makeText(getApplicationContext(), "Session not close", Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            });
                                } else {

                                    goToLogin();
                                }

                            } else {
                                Toast.makeText(context, "Something went wrong.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, "" + R.string.no_response, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<com.drugvilla.in.service.BaseResponse> call, Throwable t) {
                        ProgressDialogUtils.dismiss();
                        Toast.makeText(context, "" + R.string.failure, Toast.LENGTH_SHORT).show();
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(context, "" + R.string.no_internet, Toast.LENGTH_SHORT).show();
        }
    }
}
