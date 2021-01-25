package com.drugvilla.in.ui.product;

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
import android.view.View;
import android.widget.LinearLayout;

import com.drugvilla.in.R;
import com.drugvilla.in.adapter.product.CategoryAdapter;
import com.drugvilla.in.adapter.product.ProductAdapter;
import com.drugvilla.in.adapter.slidder.AndroidImageAdapter;
import com.drugvilla.in.adapter.slidder.TextAdapter;
import com.drugvilla.in.adapter.product.AllCategoryAdapter;
import com.drugvilla.in.adapter.product.HealthConcernAdapter;
import com.drugvilla.in.adapter.product.ProductListingAdapter;
import com.drugvilla.in.databinding.ActivityProductDashboardBinding;
import com.drugvilla.in.listener.OnSelectedListener;
import com.drugvilla.in.model.Document;
import com.drugvilla.in.model.dashboard.BannerData;
import com.drugvilla.in.model.dashboard.BannerResponse;
import com.drugvilla.in.model.dashboard.CategoryData;
import com.drugvilla.in.model.dashboard.CategoryResponse;
import com.drugvilla.in.service.Api;
import com.drugvilla.in.service.BaseCallback;
import com.drugvilla.in.service.BaseResponse;
import com.drugvilla.in.service.RequestController;
import com.drugvilla.in.ui.others.SearchActivity;
import com.drugvilla.in.ui.order.Cart;
import com.drugvilla.in.ui.user.Wishlist;
import com.drugvilla.in.utils.ActivityController;
import com.drugvilla.in.utils.AppConstant;
import com.drugvilla.in.utils.CommonUtils;
import com.drugvilla.in.utils.ProgressDialogUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;

public class ProductDashboard extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    private Activity activity;
    private ActivityProductDashboardBinding binding;
    private final int[] myImageList = new int[]{R.drawable.b5, R.drawable.b2, R.drawable.b3};
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    List<Document> bannerData = new ArrayList<>();
    // bottom slidder
    List<Document> bottomTextData = new ArrayList<>();
    private final int[] myText = new int[]{R.string.text1, R.string.text2, R.string.text3};


    // product listing for exclusive deals
    private final int[] listingImages = {R.drawable.brand2, R.drawable.brand1, R.drawable.product3, R.drawable.product4, R.drawable.brand6, R.drawable.brand5,
            R.drawable.brand4, R.drawable.brand3, R.drawable.product1, R.drawable.product2};
    private final String[] listingNames = {"Fairness Creams", "Himalaya Shampoo", "Health Drinks", "Chawanprash", "Fairness Creams", "Himalaya Shampoo",
            "Dabur", "Himalaya", "Ayur", "Godrej"};
    private final String[] listingMRP = {"1600", "400", "750", "1100", "500", "1600", "400", "750", "1100", "500"};
    private final String[] listingPrice = {"100", "200", "650", "1000", "300", "1340", "340", "600", "890", "360"};
    private final String[] listingDiscount = {"20% off", "22% off", "20% off", "15% off", "20% off", "20% off", "18% off", "30% off", "12% off", "20% off"};
    private List<Document> listProducts = new ArrayList<>();

    private final int[] concernImage = {R.drawable.concern1, R.drawable.concern3, R.drawable.concern2, R.drawable.concern5, R.drawable.concern6, R.drawable.concern4};
    private final String[] concernName = {"Stomach Pain", "Bone and joint pain", "Mental care", "Weight Management", "Beauty Care ", "Lungs Care"};
    // top products
    private final int[] productImages = {R.drawable.product1, R.drawable.product2, R.drawable.product3, R.drawable.product4, R.drawable.product1, R.drawable.product2};
    private final String[] productName = {"Fairness Creams", "Himalaya Shampoo", "Health Drinks", "Chawanprash", "Fairness Creams", "Himalaya Shampoo"};


    // product categories

    private List<Document> listCategories = new ArrayList<>();
    private List<Document> data3 = new ArrayList<>();
    private List<Document> topSellingList = new ArrayList<>();
    private final int[] category = {R.drawable.cat1, R.drawable.cat5, R.drawable.cat4, R.drawable.cat6, R.drawable.cat7, R.drawable.lab_cat7, R.drawable.lab_cat9, R.drawable.lab_cat10};
    private final String[] categoryName = {"Women \nHealth", "Ayurveda", "Sr. citizen \nCheckup", "Health care", "House \nhold", "Pregnancy", "Skin", "Nutrition"};


    private ArrayList<BannerData> bannerDataList = new ArrayList<>();
    private ArrayList<CategoryData> categoryDataList = new ArrayList<>();
    private AllCategoryAdapter allCategoryAdapter;
    private CategoryAdapter categoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_product_dashboard);
        context = ProductDashboard.this;
        activity = ProductDashboard.this;

        setToolbar();
        setListener();

        // setting bottom data
        bottomTextData = populateList2();
        setSliderViews2(bottomTextData);

        setExclusiveDeals();
        setHealthConcern();
        setAdapter();
        binding.swipeRefreshingLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                getBannerDataApi();
                getCategoryDataApi();

                // TODO : remove static views
                setExclusiveDeals();
                setHealthConcern();
                setAdapter();
                binding.swipeRefreshingLayout.setRefreshing(false);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        getBannerDataApi();
        setSliderViews(bannerDataList);

        getCategoryDataApi();
        setCategories();

    }

    private void setAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
        binding.rvTopProducts.setLayoutManager(layoutManager);
        binding.rvTopProducts.hasFixedSize();
        topSellingList.clear();
        topSellingList = PrepareDataMessage3();
        //ProductAdapter productAdapter = new ProductAdapter(context, topSellingList, AppConstant.TYPE_PRODUCT);
       // productAdapter.showRating(true);
      //  binding.rvTopProducts.setAdapter(productAdapter);
    }

    private void setHealthConcern() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
        binding.rvHealthConcern.setLayoutManager(layoutManager);
        binding.rvHealthConcern.hasFixedSize();
        binding.rvHealthConcern.setItemAnimator(new DefaultItemAnimator());
        data3.clear();
        data3 = PrepareDataMessage2();
        // popular health concern(shop by concern)
      //  HealthConcernAdapter healthConcernAdapter = new HealthConcernAdapter(context, data3, AppConstant.HEALTH_CONCERN_ROUND);
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

    private List<Document> PrepareDataMessage3() {

        List<Document> data = new ArrayList<>();

        for (int i = 0; i < productImages.length; i++) {

            Document document = new Document();

            document.setImage(productImages[i]);
            document.setText(productName[i]);
            data.add(document);

        }

        data3.addAll(data);

        return data;

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

        binding.pager2.setAdapter(new TextAdapter(this, list,AppConstant.TYPE_TEXT));
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


    private void setExclusiveDeals() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
        binding.rvDeals.setLayoutManager(layoutManager);
        binding.rvDeals.hasFixedSize();
        binding.rvDeals.setItemAnimator(new DefaultItemAnimator());
        listProducts.clear();
        listProducts = PrepareProductMessage();
       /* ProductListingAdapter productListingAdapter = new ProductListingAdapter(activity, listProducts, new OnSelectedListener() {
            @Override
            public void onClick(View view, int position) {
                binding.llBottomView.setVisibility(View.VISIBLE);

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(0, 0, 0, 120);
                binding.llslidders.setLayoutParams(layoutParams);
            }
        }, AppConstant.FROM_PRODUCT);
        binding.rvDeals.setAdapter(productListingAdapter);*/
    }

    private List<Document> PrepareProductMessage() {

        List<Document> data3 = new ArrayList<>();

        for (int i = 0; i < listingImages.length; i++) {

            Document document = new Document();

            document.setImage(listingImages[i]);
            document.setText(listingNames[i]);
            document.setText2(listingPrice[i]);
            document.setText3(listingDiscount[i]);
            document.setText4(listingMRP[i]);
            data3.add(document);

        }

        listProducts.addAll(data3);

        return data3;

    }

    private void setListener() {
        binding.tvViewAllDeal.setOnClickListener(this);
        binding.tvViewAllCategories.setOnClickListener(this);
        binding.btnGoToCart.setOnClickListener(this);
        binding.llSearch.setOnClickListener(this);
        binding.tvViewAllTopProducts.setOnClickListener(this);

    }

    private void setToolbar() {
        binding.menuBar.ivBack.setImageResource(R.drawable.ic_keyboard_backspace_black_24dp);
        binding.menuBar.tvTitle.setVisibility(View.VISIBLE);
        binding.menuBar.ivRight.setVisibility(View.VISIBLE);
        binding.menuBar.ivSecond.setVisibility(View.VISIBLE);
        binding.menuBar.tvTitle.setText(R.string.products);
        binding.menuBar.ivBack.setOnClickListener(this);
        binding.menuBar.ivRight.setOnClickListener(this);
        binding.menuBar.ivSecond.setOnClickListener(this);
        binding.menuBar.ivRight.setBadgeValue(3);
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                finish();
                break;
            case R.id.llSearch:
                ActivityController.startActivity(context,SearchActivity.class, AppConstant.TYPE_PRODUCT);
                break;
            case R.id.btnGoToCart:
                ActivityController.startActivity(context,Cart.class, AppConstant.TYPE_PRODUCT_CART);
                break;
            case R.id.tvViewAllDeal:
                ActivityController.startActivity(context,ProductsListing.class, AppConstant.TYPE_PRODUCT);
                break;
            case R.id.tvViewAllCategories:
                ActivityController.startActivity(context, AllProductCategory.class);
                break;
            case R.id.ivRight:
                ActivityController.startActivity(context,Cart.class, AppConstant.TYPE_PRODUCT_CART);
                break;
            case R.id.ivSecond:
                ActivityController.startActivity(context, Wishlist.class);
                break;
            case R.id.tvViewAllTopProducts:
                ActivityController.startActivity(context,ProductsListing.class, AppConstant.TYPE_PRODUCT);
                break;
            default:
                break;
        }
    }


    private void getBannerDataApi() {
        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                Api api = RequestController.createService(context, Api.class);
                api.getBanners("product-medicine").enqueue(new BaseCallback<BannerResponse>(context) {
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
                api.getCategory("product").enqueue(new BaseCallback<CategoryResponse>(context) {
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
    private void setCategories() {
        GridLayoutManager layoutManager = new GridLayoutManager(context, 4);
        binding.rvCategories.setLayoutManager(layoutManager);
        binding.rvCategories.hasFixedSize();
         allCategoryAdapter = new AllCategoryAdapter(activity, categoryDataList, AppConstant.TYPE_PRODUCT_MIX_CATEGORY);
        binding.rvCategories.setAdapter(allCategoryAdapter);
    }
}
