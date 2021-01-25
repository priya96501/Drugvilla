package com.drugvilla.in.ui.product;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.drugvilla.in.R;
import com.drugvilla.in.adapter.labs.CertificationAdapter;
import com.drugvilla.in.adapter.slidder.AndroidImageAdapter;
import com.drugvilla.in.adapter.product.ComboPackAdapter;
import com.drugvilla.in.adapter.product.ProductListingAdapter;
import com.drugvilla.in.adapter.slidder.ProductImageAdapter;
import com.drugvilla.in.databinding.ActivityProductDetailBinding;
import com.drugvilla.in.databinding.BottomCheckPincodeBinding;
import com.drugvilla.in.databinding.DialogBinding;
import com.drugvilla.in.listener.OnSelectedListener;
import com.drugvilla.in.model.Document;
import com.drugvilla.in.model.ReviewData;
import com.drugvilla.in.model.lab.packages.PackageDetail;
import com.drugvilla.in.model.lab.packages.PackageResponse;
import com.drugvilla.in.model.product.productdetail.ProductData;
import com.drugvilla.in.model.product.productdetail.ProductResponse;
import com.drugvilla.in.service.Api;
import com.drugvilla.in.service.BaseCallback;
import com.drugvilla.in.service.BaseResponse;
import com.drugvilla.in.service.RequestController;
import com.drugvilla.in.ui.order.Cart;
import com.drugvilla.in.ui.user.Wishlist;
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

public class ProductDetail extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    private ActivityProductDetailBinding binding;
    private Activity activity;

    int minteger = 0;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    boolean expand = false;

    // top slidder
    List<Document> productData = new ArrayList<>();
    private final int[] myImageList = new int[]{R.drawable.product2, R.drawable.product1, R.drawable.product4};

    private List<ProductData> similarProductList = new ArrayList<>();
    private ProductListingAdapter productListingAdapter;
    private String product_id = "1";
    private int flagBottom = 0;
    private int flagTop = 0;
    private int maxDistance, movement;
    private float alphaFactor;
    private boolean wishlisted = false;

    // product listing
    private final int[] listingImages = {R.drawable.brand2, R.drawable.brand1, R.drawable.product3, R.drawable.product4};
    private final int[] listingImages2 = {R.drawable.brand22, R.drawable.brand11, R.drawable.brand33, R.drawable.product1};
    private final String[] listingNames = {"Pack of 2", "Pack of 3", "Pack of 6", "Pack of 4"};
    private final String[] listingNames2 = {"Fairness Creams", "Himalaya Shampoo", "Health Drinks", "Dabur Chyanwanprash 1kg (Get Dabur Honey 50g Free)"};
    private final String[] listingMRP = {"1600", "400", "750", "1100", "500", "1600", "400", "750", "1100", "500"};
    private final String[] listingPrice = {"100", "200", "650", "1000", "300", "1340", "340", "600", "890", "360"};
    private final String[] listingDiscount = {"20% off", "22% off", "20% off", "15% off", "20% off", "20% off", "18% off", "30% off", "12% off", "20% off"};
    private List<Document> comboList = new ArrayList<>();
    private List<Document> listProducts = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_product_detail);
        context = ProductDetail.this;
        activity = ProductDetail.this;
        getData();
        setToolbar();
        setViews();
    }

    private void getData() {
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.getStringExtra("PRODUCT_ID") != null && !intent.getStringExtra("PRODUCT_ID").isEmpty()) {
                product_id = intent.getStringExtra("PRODUCT_ID");
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getProductDetail();
        setComboPack();
        setSimilarProducts();
    }

    private void setViews() {
        binding.tvShowMore.setOnClickListener(this);
        binding.tvCheck.setOnClickListener(this);
        binding.btnAdd.setOnClickListener(this);
        binding.btnAddtocart.setOnClickListener(this);
        binding.tvRead.setOnClickListener(this);
        binding.btnWishlist.setOnClickListener(this);
        binding.btnMinus.setOnClickListener(this);
        binding.btnPlus.setOnClickListener(this);
        binding.llDetails.setAlpha(0f);

        // showing bottom view on scrolling
        binding.scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {

                /* get the maximum height which we have scroll before performing any action */
                maxDistance = binding.llProductDetail.getHeight();
                /* how much we have scrolled */
                movement = binding.scrollView.getScrollY();
                /*finally calculate the alpha factor and set on the view */
                alphaFactor = ((movement * 1.0f) / (maxDistance - binding.llDetails.getHeight()));
                if (movement >= 0 && movement <= maxDistance) {
                    /*for image parallax with scroll */
                    // binding.llProductDetail.setTranslationY(-movement / 2);
                    /* set visibility */
                    binding.llDetails.setAlpha(alphaFactor);
                }

            }
        });

    }

    private void setComboPack() {
        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
        binding.rvComboPack.setLayoutManager(gridLayoutManager);
        binding.rvComboPack.hasFixedSize();
        binding.rvComboPack.setItemAnimator(new DefaultItemAnimator());
        comboList.clear();
        comboList = PrepareProductMessage();
        ComboPackAdapter comboPackAdapter = new ComboPackAdapter(context, comboList);
        binding.rvComboPack.setAdapter(comboPackAdapter);
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
        comboList.addAll(data3);
        return data3;
    }

    private List<Document> PrepareProductMessage2() {
        List<Document> data3 = new ArrayList<>();
        for (int i = 0; i < listingImages2.length; i++) {
            Document document = new Document();
            document.setImage(listingImages2[i]);
            document.setText(listingNames2[i]);
            document.setText2(listingPrice[i]);
            document.setText3(listingDiscount[i]);
            document.setText4(listingMRP[i]);
            data3.add(document);
        }
        listProducts.addAll(data3);
        return data3;
    }

    private void setToolbar() {
        binding.menuBar.tvTitle.setVisibility(View.VISIBLE);
        binding.menuBar.ivRight.setVisibility(View.VISIBLE);
        binding.menuBar.ivSecond.setVisibility(View.VISIBLE);
        binding.menuBar.tvTitle.setText("");
        binding.menuBar.ivBack.setImageResource(R.drawable.ic_keyboard_backspace_black_24dp);
        binding.menuBar.ivBack.setOnClickListener(this);
        binding.menuBar.ivRight.setOnClickListener(this);
        binding.menuBar.ivSecond.setOnClickListener(this);
        binding.menuBar.ivRight.setBadgeValue(2);
    }

    private void display(int number) {
        binding.tvCount.setText("" + number);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.ivBack:
                finish();
                break;
            case R.id.ivSecond:
                ActivityController.startActivity(context, Wishlist.class);
                break;
            case R.id.ivRight:
                ActivityController.startActivity(context, Cart.class, AppConstant.TYPE_PRODUCT_CART);
                break;
            case R.id.btnWishlist:
                if (!wishlisted) {
                    addToWishlistApi();
                }
                break;
            case R.id.btnAdd:
                if (flagTop == 0) {
                    addToCartApi();
                 /*   binding.btnAdd.setText(getResources().getString(R.string.go_to_cart));
                    binding.llDetails.setAlpha(alphaFactor);
                    flagTop = 1;
                    binding.btnAddtocart.setText(getResources().getString(R.string.go_to_cart));
                    flagBottom = 1;*/
                } else {
                    ActivityController.startActivity(context, Cart.class, AppConstant.TYPE_PRODUCT_CART);
                }
                break;
            case R.id.btnAddtocart:
                if (flagBottom == 0) {
                    addToCartApi();
                   /* binding.btnAddtocart.setText(getResources().getString(R.string.go_to_cart));
                    flagBottom = 1;
                    binding.llDetails.setAlpha(alphaFactor);
                    binding.btnAdd.setText(getResources().getString(R.string.go_to_cart));
                    flagTop = 1;*/
                } else {
                    ActivityController.startActivity(context, Cart.class, AppConstant.TYPE_PRODUCT_CART);
                }
                break;
            case R.id.btnMinus:
                if (!(minteger <= 1)) {
                    minteger = minteger - 1;
                }
                display(minteger);
                break;
            case R.id.btnPlus:
                if (minteger <= 29) {
                    minteger = minteger + 1;
                    display(minteger);
                }
                break;
            case R.id.tvCheck:
                checkPincode();
                break;

            case R.id.tvShowMore:
                if (!expand) {
                    expand = true;
                    binding.tvProductDetails.setMaxLines(Integer.MAX_VALUE);
                    binding.tvShowMore.setText(getResources().getString(R.string.show_less));
                } else {
                    binding.tvProductDetails.setMaxLines(4);
                    binding.tvShowMore.setText(getResources().getString(R.string.show_more));
                    expand = false;
                }
                break;
            case R.id.tvRead:
                focusOnView(false);
                break;
            default:
                break;
        }
    }


    private void focusOnView(final boolean substitute) {
        binding.scrollView.post(new Runnable() {
            @Override
            public void run() {
                if (substitute) {
                    binding.scrollView.scrollTo(0, binding.llSimilarProducts.getTop());
                } else {
                    binding.scrollView.scrollTo(0, binding.llProductDetail.getTop());
                }
            }
        });
    }

    public void checkPincode() {
        final BottomCheckPincodeBinding checkPincodeBinding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.bottom_check_pincode, null, false);
        final BottomSheetDialog dialog = DialogUtils.createBottomDialog(context, checkPincodeBinding.getRoot());
        dialog.setCancelable(false);

        checkPincodeBinding.ivCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        checkPincodeBinding.btnCheckPincode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPincodeBinding.etPincode.getText().toString().isEmpty()) {
                    checkPincodeBinding.etPincode.setError("Pincode cant be empty.");
                } else if (checkPincodeBinding.etPincode.getText().toString().length() < 6) {
                    checkPincodeBinding.etPincode.setError("Invalid pincode.");
                } else {
                    binding.rlCheck.setVisibility(View.GONE);
                    binding.llDeliveryData.setVisibility(View.VISIBLE);
                    dialog.dismiss();
                }
            }
        });
    }

    private void setSimilarProducts() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2);
        binding.rvSimilarProducts.setLayoutManager(gridLayoutManager);
        binding.rvSimilarProducts.hasFixedSize();
        binding.rvSimilarProducts.setItemAnimator(new DefaultItemAnimator());
        listProducts.clear();
        listProducts = PrepareProductMessage2();
        productListingAdapter = new ProductListingAdapter(activity, /*listProducts*/   similarProductList, new OnSelectedListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view, int position) {
                binding.btnAddtocart.setText("Go To Cart");
                flagBottom = 1;
                binding.llDetails.setAlpha(alphaFactor);
            }
        }, AppConstant.FROM_PRODUCT);
        binding.rvSimilarProducts.setAdapter(productListingAdapter);
    }

    private void getProductDetail() {
        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                Api api = RequestController.createService(context, Api.class);
                api.getProductDetail(product_id).enqueue(new BaseCallback<ProductResponse>(context) {
                    @Override
                    public void onSuccess(ProductResponse response) {
                        ProgressDialogUtils.dismiss();
                        if (response != null) {
                            if (response.getStatus() == 1) {
                                CommonUtils.showToastShort(context, response.getMessage());
                                com.drugvilla.in.model.product.productdetail.ProductDetail productDetail = response.getProductData();
                                setData(productDetail);

                                if (productDetail.getSililarProductData() != null
                                        && !productDetail.getSililarProductData().isEmpty()) {
                                    similarProductList.clear();
                                    similarProductList.addAll(productDetail.getSililarProductData());
                                    //  productListingAdapter.notifyDataSetChanged();
                                } else {
                                    similarProductList.clear();
                                    // productListingAdapter.notifyDataSetChanged();
                                }

                            } else {
                                CommonUtils.showToastShort(context, response.getMessage());
                            }
                        }
                    }

                    @Override
                    public void onFail(Call<ProductResponse> call, BaseResponse baseResponse) {
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

    private void setData(com.drugvilla.in.model.product.productdetail.ProductDetail productDetail) {
        binding.tvMRP.setPaintFlags(binding.tvMRP.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        binding.tvMRP2.setPaintFlags(binding.tvMRP.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        // round image for banner
        CommonUtils.setRoundImage(context, binding.ivBanner, productDetail.getBanner());

        binding.tvProductName.setText(productDetail.getName());
        if (productDetail.getRating() != null && !productDetail.getRating().isEmpty()) {
            binding.llrating.setVisibility(View.VISIBLE);
            binding.tvRating.setText(productDetail.getRating());
        }
        binding.tvBy.setText(productDetail.getProvidedBy());
        binding.tvMRP.setText(productDetail.getMrp());
        binding.tvMRP2.setText(productDetail.getMrp());
        binding.tvOff.setText(productDetail.getDiscount());
        binding.tvOff2.setText(productDetail.getDiscount());
        binding.tvAmount.setText(productDetail.getAmount());
        binding.tvAmount2.setText(productDetail.getAmount());
        binding.tvProductDetails.setText(productDetail.getDescription());
        binding.tvAddress.setText(productDetail.getSupplierAddress());
        // binding.tvCategory.setText(productDetail.getCategory());
        if (productDetail.getBanner() != null && !productDetail.getBanner().isEmpty()) {
            Picasso.with(context).load(productDetail.getBanner()).error(R.drawable.type_otc).placeholder(R.drawable.type_otc).into(binding.ivBanner);
        }
        if (productDetail.getOffer() != null && !productDetail.getOffer().isEmpty()) {
            binding.llOffer.setVisibility(View.VISIBLE);
            binding.tvDiscount1.setText(productDetail.getOffer().toString()
                    .replace("[", "").replace("]", "").replace(",", "\n"));
        }

        if (productDetail.getImages() != null && !productDetail.getImages().isEmpty()) {
            setProductImages(productDetail.getImages());
        }

    }

    private void setProductImages(ArrayList<String> list) {

        ProductImageAdapter adapter = new ProductImageAdapter(this, list);
        binding.pager.setAdapter(adapter);
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

    private void addToCartApi() {
        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                Api api = RequestController.createService(context, Api.class);
                api.addToCart(SharedPref.getStringPreferences(context, AppConstant.USER_ID), product_id, binding.tvCount.getText().toString())
                        .enqueue(new BaseCallback<BaseResponse>(context) {
                            @Override
                            public void onSuccess(BaseResponse response) {
                                ProgressDialogUtils.dismiss();
                                if (response != null) {
                                    if (response.getStatus() == 1) {
                                        CommonUtils.showToastShort(context, response.getMessage());
                                        binding.btnAdd.setText(getResources().getString(R.string.go_to_cart));
                                        binding.llDetails.setAlpha(alphaFactor);
                                        flagTop = 1;
                                        binding.btnAddtocart.setText(getResources().getString(R.string.go_to_cart));
                                        flagBottom = 1;
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
    private void addToWishlistApi() {
        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                Api api = RequestController.createService(context, Api.class);
                api.addToWishlist(SharedPref.getStringPreferences(context, AppConstant.USER_ID), product_id)
                        .enqueue(new BaseCallback<BaseResponse>(context) {
                            @Override
                            public void onSuccess(BaseResponse response) {
                                ProgressDialogUtils.dismiss();
                                if (response != null) {
                                    if (response.getStatus() == 1) {
                                        CommonUtils.showToastShort(context, response.getMessage());
                                        binding.btnWishlist.setText(getResources().getString(R.string.wishlisted));
                                        wishlisted = true;
                                    } else {
                                        CommonUtils.showToastShort(context, response.getMessage());
                                    }
                                } else {
                                    CommonUtils.showToastShort(context, context.getResources().getString(R.string.no_response));
                                }
                            }

                            @Override
                            public void onFail(Call<BaseResponse> call, BaseResponse baseResponse) {
                                ProgressDialogUtils.dismiss();
                                CommonUtils.showToastShort(context, context.getResources().getString(R.string.failure));
                            }
                        });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            CommonUtils.showToastShort(context, context.getResources().getString(R.string.no_internet));
        }
    }
}
