package com.drugvilla.in.ui.product;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.drugvilla.in.R;
import com.drugvilla.in.adapter.product.sortFilter.SortingListAdapter;
import com.drugvilla.in.adapter.product.AllCategoryAdapter;
import com.drugvilla.in.adapter.product.ProductAdapter;
import com.drugvilla.in.adapter.product.ProductListingAdapter;
import com.drugvilla.in.databinding.ActivityProductMenuBinding;
import com.drugvilla.in.databinding.BottomCheckPincodeBinding;
import com.drugvilla.in.databinding.BottomSortingListBinding;
import com.drugvilla.in.listener.OnSelectedListener;
import com.drugvilla.in.model.Document;
import com.drugvilla.in.model.dashboard.CategoryData;
import com.drugvilla.in.model.product.ProductCategoryDetailPageData;
import com.drugvilla.in.model.product.ProductCategoryDetailPageResponse;
import com.drugvilla.in.model.product.ProductListingResponse;
import com.drugvilla.in.model.product.category.ProductCategoryResponse;
import com.drugvilla.in.model.product.category.ProductSubCategoryData;
import com.drugvilla.in.model.product.productdetail.ProductData;
import com.drugvilla.in.model.product.sort.SortingData;
import com.drugvilla.in.model.product.sort.SortingResponse;
import com.drugvilla.in.service.Api;
import com.drugvilla.in.service.BaseCallback;
import com.drugvilla.in.service.BaseResponse;
import com.drugvilla.in.service.RequestController;
import com.drugvilla.in.ui.order.Cart;
import com.drugvilla.in.utils.ActivityController;
import com.drugvilla.in.utils.AppConstant;
import com.drugvilla.in.utils.CommonUtils;
import com.drugvilla.in.utils.DialogUtils;
import com.drugvilla.in.utils.ProgressDialogUtils;
import com.drugvilla.in.utils.SharedPref;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;

public class ProductMenu extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    private Activity activity;
    private ActivityProductMenuBinding binding;
    private LinearLayoutManager layoutManager;
    private String from = " ", productCategoryId = " ", selectedSortingId = " ";


    // sub categories
    private final int[] productSubCategory = {R.drawable.cat1, R.drawable.cat2, R.drawable.cat3, R.drawable.cat4, R.drawable.cat5, R.drawable.cat6, R.drawable.cat7};
    private final String[] SubcategoryName = {"Women Care", "Men Care", "Baby Care", "Sr. citizen", "Ayurveda", "Healthcare", "House hold"};
    private List<Document> listData = new ArrayList<>();


    // top products of brand
    private final int[] productImages = {R.drawable.product1, R.drawable.product2, R.drawable.product3};
    private final String[] productName = {"Fairness Creams", "Himalaya Shampoo", "Health Drinks"};
    private List<Document> listTopProducts = new ArrayList<>();


    // product listing
    private final int[] listingImages = {R.drawable.brand2, R.drawable.brand1, R.drawable.product3, R.drawable.product4, R.drawable.brand6, R.drawable.brand5,
            R.drawable.brand4, R.drawable.brand3, R.drawable.product1, R.drawable.product2};
    private final String[] listingNames = {"Fairness Creams", "Himalaya Shampoo", "Health Drinks", "Chawanprash", "Fairness Creams", "Himalaya Shampoo",
            "Dabur", "Himalaya", "Ayur", "Godrej"};
    private final String[] listingMRP = {"1600", "400", "750", "1100", "500", "1600", "400", "750", "1100", "500"};
    private final String[] listingPrice = {"100", "200", "650", "1000", "300", "1340", "340", "600", "890", "360"};
    private final String[] listingDiscount = {"20% off", "22% off", "20% off", "15% off", "20% off", "20% off", "18% off", "30% off", "12% off", "20% off"};
    private List<Document> listProducts = new ArrayList<>();

    // sorting
    private List<Document> listsortingtype = new ArrayList<>();
    private final String[] sortingType = {"Popularity", "Price High to Low", "Price Low to High", "Discount"};
    private final int[] sortingImage = {R.drawable.sort1, R.drawable.sort2, R.drawable.sort3, R.drawable.sort4};
    private BottomSheetDialog dialog;

    private List<CategoryData> subCategoryDataList = new ArrayList<>();
    private List<SortingData> sortingDataList = new ArrayList<>();
    private List<ProductData> productDataList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_product_menu);
        context = ProductMenu.this;
        activity = ProductMenu.this;
        getData();
        setListener();
        setData();

        // CommonUtils.setImageRound(context, binding.ivType, R.drawable.b1);

        binding.swipeRefreshingLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getProductSubCategoryApi();
                getSortingListApi();
                getProductListing("");
                setListingProducts();
            }
        });

    }

    @Override
    protected void onResume() {
        getData();
        setListener();
        setData();
        getProductSubCategoryApi();
        getSortingListApi();
        getProductListing("");
        setListingProducts();
        super.onResume();
    }

    private void getData() {
        Bundle bundle = new Bundle();
        bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.getString(AppConstant.FROM) != null && !Objects.requireNonNull(bundle.getString(AppConstant.FROM)).isEmpty()) {
                from = bundle.getString(AppConstant.FROM);
            }
            if (bundle.getString("PRODUCT_CATEGORY_ID") != null && !Objects.requireNonNull(bundle.getString("PRODUCT_CATEGORY_ID")).isEmpty()) {
                productCategoryId = bundle.getString("PRODUCT_CATEGORY_ID");
            }
        }
    }

    private void setListener() {
        binding.menuBar.ivBack.setImageResource(R.drawable.ic_keyboard_backspace_black_24dp);
        binding.menuBar.ivBack.setOnClickListener(this);
        binding.menuBar.ivRight.setVisibility(View.VISIBLE);
        binding.menuBar.ivRight.setOnClickListener(this);
        binding.menuBar.ivRight.setBadgeValue(3);
        binding.llFilter.setOnClickListener(this);
        binding.tvViewAllTopBrandProducts.setOnClickListener(this);
        binding.btnGoToCart.setOnClickListener(this);
        binding.llSort.setOnClickListener(this);

    }

    private void setData() {
        if (from.equalsIgnoreCase(AppConstant.TYPE_BRAND)) {
            binding.menuBar.tvTitle.setVisibility(View.VISIBLE);
            binding.menuBar.tvTitle.setText(R.string.brand_name);
            binding.llSubCategory.setVisibility(View.GONE);
            setTopProductsOfBrand();

        } else if (from.equalsIgnoreCase(AppConstant.TYPE_PRODUCT_CATEGORY)) {
            binding.llBrandTopProducts.setVisibility(View.GONE);
            getProductSubCategoryApi();
            setProductSubCategory();

        } else {
            binding.menuBar.tvTitle.setVisibility(View.VISIBLE);
            binding.menuBar.tvTitle.setText(R.string.concern_name);
            binding.ivType.setImageResource(R.drawable.concern1);
            binding.llBrandTopProducts.setVisibility(View.GONE);
            binding.llSubCategory.setVisibility(View.GONE);

        }
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                finish();
                break;
            case R.id.ivRight:
                ActivityController.startActivity(context, Cart.class, AppConstant.TYPE_PRODUCT_CART);

                break;
            case R.id.btnGoToCart:
                ActivityController.startActivity(context, Cart.class, AppConstant.TYPE_PRODUCT_CART);

                break;

            case R.id.tvViewAllTopBrandProducts:
                ActivityController.startActivity(context, ProductsListing.class, AppConstant.TYPE_PRODUCT);

                break;
            case R.id.llSort:
                showBottomSheetDialog();
                break;
            case R.id.llFilter:
                ActivityController.startActivity(context, FilterActivity.class);
                break;
            default:
                break;
        }
    }

    public void showBottomSheetDialog() {
        final BottomSortingListBinding sortingListBinding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.bottom_sorting_list, null, false);
        final BottomSheetDialog dialog = DialogUtils.createBottomDialog(context, sortingListBinding.getRoot());
        dialog.setCancelable(false);
        setSortingListing(sortingListBinding.rvSortingType);
        sortingListBinding.ivCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private List<Document> PrepareDataMessage2() {

        List<Document> data = new ArrayList<>();

        for (int i = 0; i < productImages.length; i++) {

            Document document = new Document();

            document.setImage(productImages[i]);
            document.setText(productName[i]);
            data.add(document);

        }

        listTopProducts.addAll(data);
        return data;

    }

    private void setTopProductsOfBrand() {
        layoutManager = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
        binding.rvBrandTopProducts.setLayoutManager(layoutManager);
        binding.rvBrandTopProducts.hasFixedSize();
        binding.rvBrandTopProducts.setItemAnimator(new DefaultItemAnimator());
        listTopProducts.clear();
        listTopProducts = PrepareDataMessage2();
      //  ProductAdapter productAdapter = new ProductAdapter(context, listTopProducts, AppConstant.TYPE_PRODUCT);
     //   binding.rvBrandTopProducts.setAdapter(productAdapter);
    }


    private void getProductSubCategoryApi() {

        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                Api api = RequestController.createService(context, Api.class);
                api.getProductCategoryDetailData(productCategoryId).
                        enqueue(new BaseCallback<ProductCategoryDetailPageResponse>(context) {
                            @Override
                            public void onSuccess(ProductCategoryDetailPageResponse response) {
                                ProgressDialogUtils.dismiss();
                                binding.swipeRefreshingLayout.setRefreshing(false);
                                if (response != null) {
                                    if (response.getStatus() == 1) {
                                        CommonUtils.showToastShort(context, response.getMessage());
                                        ProductCategoryDetailPageData data = response.getProductData();
                                        binding.menuBar.tvTitle.setVisibility(View.VISIBLE);
                                        binding.menuBar.tvTitle.setText(data.getCategoryName());
                                        if (data.getCategoryBanner() != null && !data.getCategoryBanner().isEmpty()) {
                                            Picasso.with(context).load(data.getCategoryBanner())
                                                    .placeholder(R.drawable.gallery)
                                                    .error(R.drawable.gallery)
                                                    .into(binding.ivType);
                                        }

                                        if (data.getProductSubCategoryDataList() != null
                                                && !data.getProductSubCategoryDataList().isEmpty()) {
                                            subCategoryDataList.clear();
                                            subCategoryDataList.addAll(data.getProductSubCategoryDataList());
                                        } else {
                                            subCategoryDataList.clear();
                                        }

                                    } else {
                                        CommonUtils.showToastShort(context, response.getMessage());
                                    }
                                }
                            }

                            @Override
                            public void onFail(Call<ProductCategoryDetailPageResponse> call, BaseResponse baseResponse) {
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

    private void setProductSubCategory() {
        layoutManager = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
        binding.rvSubCategories.setLayoutManager(layoutManager);
        binding.rvSubCategories.hasFixedSize();
        AllCategoryAdapter subCategory = new AllCategoryAdapter(activity, /*listData*/ subCategoryDataList, AppConstant.TYPE_SUB_PRODUCT_CATEGORY);
        binding.rvSubCategories.setAdapter(subCategory);
    }

    private void getProductListing(final String type) {
        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                Api api = RequestController.createService(context, Api.class);
                HashMap<String, String> map = new HashMap<>();
                map.put("user_id", SharedPref.getStringPreferences(context, AppConstant.USER_ID));
                map.put("category_id", productCategoryId);
                if (type.equalsIgnoreCase("SORT")) {
                    map.put("sort"," " /*selectedSortingId*/);
                }

                api.getProductListing(map).enqueue(new BaseCallback<ProductListingResponse>(context) {
                    @Override
                    public void onSuccess(ProductListingResponse response) {
                        ProgressDialogUtils.dismiss();
                        binding.swipeRefreshingLayout.setRefreshing(false);
                        if (response != null) {
                            if (response.getStatus() == 1) {
                                CommonUtils.showToastShort(context, response.getMessage());

                                if (response.getProductDataList() != null
                                        && !response.getProductDataList().isEmpty()) {
                                    productDataList.clear();
                                    productDataList.addAll(response.getProductDataList());
                                } else {
                                    productDataList.clear();
                                }

                            } else {
                                CommonUtils.showToastShort(context, response.getMessage());
                            }
                        }
                    }

                    @Override
                    public void onFail(Call<ProductListingResponse> call, BaseResponse baseResponse) {
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

    private void setListingProducts() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2);
        binding.rvProductListing.setLayoutManager(gridLayoutManager);
        binding.rvProductListing.hasFixedSize();
        binding.rvProductListing.setItemAnimator(new DefaultItemAnimator());
        listProducts.clear();
        listProducts = PrepareProductMessage();
        ProductListingAdapter productListingAdapter = new ProductListingAdapter(activity, productDataList/*listProducts*/,
                new OnSelectedListener() {
                    @Override
                    public void onClick(View view, int position) {
                        switch (view.getId()) {
                            case R.id.btnAdd:
                                addToCartApi(productDataList.get(position).getProductId());
                                break;
                            default:
                                break;
                        }
                    }
                }, AppConstant.FROM_PRODUCT);
        binding.rvProductListing.setAdapter(productListingAdapter);
    }

    private void getSortingListApi() {
        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                Api api = RequestController.createService(context, Api.class);
                api.getSortingList().enqueue(new BaseCallback<SortingResponse>(context) {
                    @Override
                    public void onSuccess(SortingResponse response) {
                        ProgressDialogUtils.dismiss();
                        binding.swipeRefreshingLayout.setRefreshing(false);
                        if (response != null) {
                            if (response.getStatus() == 1) {
                                CommonUtils.showToastShort(context, response.getMessage());

                                if (response.getSortingDataList() != null
                                        && !response.getSortingDataList().isEmpty()) {
                                    sortingDataList.clear();
                                    sortingDataList.addAll(response.getSortingDataList());
                                } else {
                                    sortingDataList.clear();
                                }

                            } else {
                                CommonUtils.showToastShort(context, response.getMessage());
                            }
                        }
                    }

                    @Override
                    public void onFail(Call<SortingResponse> call, BaseResponse baseResponse) {
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

    private void setSortingListing(RecyclerView recyclerView) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.hasFixedSize();
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        SortingListAdapter sortingListAdapter = new SortingListAdapter(context, sortingDataList, new OnSelectedListener() {
            @Override
            public void onClick(View view, int position) {
                dialog.dismiss();
                // TODO : apply api for sorting selection with position
               // selectedSortingId = sortingDataList.get(position).getSortingId();
                getProductListing("SORT");
            }
        });
        recyclerView.setAdapter(sortingListAdapter);
    }

    private void addToCartApi(String productId) {
        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                Api api = RequestController.createService(context, Api.class);
                api.addToCart(SharedPref.getStringPreferences(context, AppConstant.USER_ID), productId, "1")
                        .enqueue(new BaseCallback<BaseResponse>(context) {
                            @Override
                            public void onSuccess(BaseResponse response) {
                                ProgressDialogUtils.dismiss();
                                if (response != null) {
                                    if (response.getStatus() == 1) {
                                        CommonUtils.showToastShort(context, response.getMessage());
                                        binding.llBottomView.setVisibility(View.VISIBLE);
                                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                        layoutParams.setMargins(0, 0, 0, 110);
                                        binding.llAllProducts.setLayoutParams(layoutParams);
                                    }
                                }
                            }

                            @Override
                            public void onFail(Call<BaseResponse> call, BaseResponse baseResponse) {
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
