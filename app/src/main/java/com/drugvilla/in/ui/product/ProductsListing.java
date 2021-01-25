package com.drugvilla.in.ui.product;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;


import com.drugvilla.in.R;
import com.drugvilla.in.adapter.product.sortFilter.SortingListAdapter;
import com.drugvilla.in.adapter.product.BrandAdapter;
import com.drugvilla.in.adapter.product.ProductListingAdapter;
import com.drugvilla.in.databinding.ActivityProductsListingBinding;
import com.drugvilla.in.databinding.BottomSortingListBinding;
import com.drugvilla.in.listener.OnSelectedListener;
import com.drugvilla.in.model.Document;
import com.drugvilla.in.model.product.ProductListingResponse;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;

public class ProductsListing extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    private Activity activity;
    private ActivityProductsListingBinding binding;
    private String from = " ";
    private LinearLayoutManager layoutManager;
    private BottomSheetDialog dialog;
    private List<Document> data = new ArrayList<>();
    private final int[] brandImages = {R.drawable.brand11, R.drawable.brand22, R.drawable.brand33, R.drawable.brand22, R.drawable.brand33, R.drawable.brand11
            , R.drawable.brand22, R.drawable.brand33, R.drawable.brand11, R.drawable.brand22, R.drawable.brand11, R.drawable.brand33};
    private final String[] brandName = {"Dabur", "Himalaya", "Ayur", "Godrej", "Nivea", "Bournvita", "Dabur", "Himalaya", "Ayur", "Godrej", "Nivea", "Bournvita"};


    // product listing
    private final int[] listingImages = {R.drawable.brand2, R.drawable.brand1, R.drawable.product3, R.drawable.product4, R.drawable.brand6, R.drawable.brand5,
            R.drawable.brand4, R.drawable.brand3, R.drawable.product1, R.drawable.product2};
    private final String[] listingNames = {"Fairness Creams", "Himalaya Shampoo", "Health Drinks", "Dabur Chyanwanprash 1kg (Get Dabur Honey 50g Free)", "Fairness Creams", "Himalaya Shampoo",
            "Dabur", "Himalaya", "Ayur", "Godrej"};
    private final String[] listingMRP = {"1600", "400", "750", "1100", "500", "1600", "400", "750", "1100", "500"};
    private final String[] listingPrice = {"100", "200", "650", "1000", "300", "1340", "340", "600", "890", "360"};
    private final String[] listingDiscount = {"20% off", "22% off", "20% off", "15% off", "20% off", "20% off", "18% off", "30% off", "12% off", "20% off"};
    private List<Document> listProducts = new ArrayList<>();

    // sorting
    private List<Document> listsortingtype = new ArrayList<>();
    private final String[] sortingType = {"Popularity", "Price High to Low", "Price Low to High", "Discount"};
    private final int[] sortingImage = {R.drawable.sort1, R.drawable.sort2, R.drawable.sort3, R.drawable.sort4};

    private List<SortingData> sortingDataList = new ArrayList<>();
    private List<ProductData> productDataList = new ArrayList<>();
    private String productSubCategoryId = " ", selectedSortingId = " ", productCategoryId = " ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_products_listing);
        context = ProductsListing.this;
        activity = ProductsListing.this;
        getData();
        setToolbar();

        /*//  from view all brands
        if (from.equalsIgnoreCase(AppConstant.TYPE_BRAND)) {
            binding.menubar.tvTitle.setText(R.string.all_brands);
            binding.rvBrands.setVisibility(View.VISIBLE);
            binding.llSortFilter.setVisibility(View.GONE);
            setBrand();
        }
        //  from view all products
        else *//*if (from.equalsIgnoreCase(AppConstant.TYPE_PRODUCT))*//* {
            binding.menubar.tvTitle.setText(R.string.all_products);
            binding.llProductListing.setVisibility(View.VISIBLE);
            getProductListing("");
            setListingProducts();

        }*/
        binding.swipeRefreshingLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (from.equalsIgnoreCase(AppConstant.TYPE_BRAND)) {
                    setBrand();
                } else /*if (from.equalsIgnoreCase(AppConstant.TYPE_PRODUCT))*/ {
                    getProductListing("");
                }

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        getSortingListApi();
        //  from view all brands
        if (from.equalsIgnoreCase(AppConstant.TYPE_BRAND)) {
            binding.menubar.tvTitle.setText(R.string.all_brands);
            binding.rvBrands.setVisibility(View.VISIBLE);
            binding.llSortFilter.setVisibility(View.GONE);
            setBrand();
        }
        //  from view all products
        else /*if (from.equalsIgnoreCase(AppConstant.TYPE_PRODUCT))*/ {
            binding.menubar.tvTitle.setText(R.string.all_products);
            binding.llProductListing.setVisibility(View.VISIBLE);
            getProductListing("");
            setListingProducts();

        }
    }

    private void getData() {
        Bundle bundle = new Bundle();
        bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.getString(AppConstant.FROM) != null && !Objects.requireNonNull(bundle.getString(AppConstant.FROM)).isEmpty()) {
                from = bundle.getString(AppConstant.FROM);
            }

            if (bundle.getString("PRODUCT_SUB_CATEGORY_ID") != null && !Objects.requireNonNull(bundle.getString("PRODUCT_SUB_CATEGORY_ID")).isEmpty()) {
                productSubCategoryId = bundle.getString("PRODUCT_SUB_CATEGORY_ID");
            }
        }
    }

    private void setToolbar() {
        binding.menubar.tvTitle.setVisibility(View.VISIBLE);
        binding.menubar.ivBack.setImageResource(R.drawable.ic_keyboard_backspace_black_24dp);
        binding.menubar.ivBack.setOnClickListener(this);
        binding.menubar.ivRight.setVisibility(View.VISIBLE);
        binding.menubar.ivRight.setOnClickListener(this);
        binding.menubar.ivRight.setBadgeValue(3);


        binding.btnGoToCart.setOnClickListener(this);
        binding.llSort.setOnClickListener(this);
        binding.llFilter.setOnClickListener(this);


    }


    private void setBrand() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 3);
        binding.rvBrands.setLayoutManager(gridLayoutManager);
        binding.rvBrands.hasFixedSize();
        binding.rvBrands.setItemAnimator(new DefaultItemAnimator());
        data.clear();
        data = PrepareDataMessage5();
        // top brands
     //   BrandAdapter brandAdapter = new BrandAdapter(activity, data);
      //  binding.rvBrands.setAdapter(brandAdapter);
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

    public void showBottomSheetDialog() {
        final BottomSortingListBinding sortingListBinding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.bottom_sorting_list, null, false);
        final BottomSheetDialog dialog = DialogUtils.createBottomDialog(context, sortingListBinding.getRoot());
        dialog.setCancelable(false);

        sortingListBinding.ivCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        setSortingListing(sortingListBinding.rvSortingType);
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
                // TODO : apply api for sorting with position
                selectedSortingId = sortingDataList.get(position).getSortingId();
                getProductListing("SORT");
            }
        });
        recyclerView.setAdapter(sortingListAdapter);
    }

    private void getProductListing(final String type) {
        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                Api api = RequestController.createService(context, Api.class);
                HashMap<String, String> map = new HashMap<>();
                map.put("user_id", SharedPref.getStringPreferences(context, AppConstant.USER_ID));
                map.put("sub_category_id", productSubCategoryId);
                if (type.equalsIgnoreCase("SORT")) {
                    map.put("sort", selectedSortingId);
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
        ProductListingAdapter productListingAdapter = new ProductListingAdapter(activity,productDataList /*listProducts*/, new OnSelectedListener() {
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
                                        layoutParams.setMargins(0, 30, 0, 100);
                                        binding.llProductListing.setLayoutParams(layoutParams);
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
