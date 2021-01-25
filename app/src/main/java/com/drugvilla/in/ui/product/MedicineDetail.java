package com.drugvilla.in.ui.product;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;

import com.drugvilla.in.R;
import com.drugvilla.in.adapter.product.SubstituteAdapter;
import com.drugvilla.in.adapter.slidder.AndroidImageAdapter;
import com.drugvilla.in.adapter.slidder.ProductImageAdapter;
import com.drugvilla.in.databinding.ActivityMedicineDetailBinding;
import com.drugvilla.in.databinding.BottomCheckPincodeBinding;
import com.drugvilla.in.model.Document;
import com.drugvilla.in.model.product.medicinedetail.MedicineResponse;
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

public class MedicineDetail extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    private ActivityMedicineDetailBinding binding;
    int minteger = 0;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    boolean expand = false;
    private int flagBottom = 0;
    private int flagTop = 0;
    private int maxDistance, movement;
    private float alphaFactor;
    private String product_id = "";
    private boolean wishlisted = false;

    // top slidder
    List<Document> productData = new ArrayList<>();
    private final int[] myImageList = new int[]{R.drawable.med1, R.drawable.med2, R.drawable.med3};

    private List<ProductData> substituteDataList = new ArrayList<>();
    private SubstituteAdapter substituteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_medicine_detail);
        context = MedicineDetail.this;
        getData();
        setToolbar();
        setViews();
        // setting product images data
       /* productData = populateList();
        setProductImages(productData);*/

    }

    @Override
    protected void onResume() {
        super.onResume();
        getMedicineDetail();
        setSubstitutes();

    }


    private void getData() {
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.getStringExtra("PRODUCT_ID") != null && !intent.getStringExtra("PRODUCT_ID").isEmpty()) {
                product_id = intent.getStringExtra("PRODUCT_ID");
            }
        }
    }


    private void setViews() {
        binding.btnMinus.setOnClickListener(this);
        binding.btnPlus.setOnClickListener(this);
        binding.tvShowMore.setOnClickListener(this);
        binding.tvCheck.setOnClickListener(this);
        binding.btnAdd.setOnClickListener(this);
        binding.btnAddtocart.setOnClickListener(this);
        binding.tvRead.setOnClickListener(this);
        binding.tvFindSubstitutes.setOnClickListener(this);
        binding.btnWishlist.setOnClickListener(this);
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

    private void setToolbar() {
        binding.menuBar.tvTitle.setVisibility(View.VISIBLE);
        binding.menuBar.ivRight.setVisibility(View.VISIBLE);
        binding.menuBar.ivSecond.setVisibility(View.VISIBLE);
        binding.menuBar.tvTitle.setText(getResources().getString(R.string.medicine_detail));
        binding.menuBar.ivBack.setImageResource(R.drawable.ic_keyboard_backspace_black_24dp);
        binding.menuBar.ivBack.setOnClickListener(this);
        binding.menuBar.ivRight.setOnClickListener(this);
        binding.menuBar.ivSecond.setOnClickListener(this);
        binding.menuBar.ivRight.setBadgeValue(2);
    }

    private void display(int number) {
        binding.tvCount.setText("" + number);
    }

    // setting banners
    private ArrayList<Document> populateList() {
        ArrayList<Document> list = new ArrayList<>();
        for (int i1 : myImageList) {
            Document imageModel = new Document();
            imageModel.setImage(i1);
            list.add(imageModel);
        }
        return list;
    }

    private void setProductImages(ArrayList<String> list) {
        ProductImageAdapter adapter = new ProductImageAdapter(this, list);
        binding.pager.setAdapter(adapter);
        binding.indicator.setViewPager(binding.pager);
        NUM_PAGES = list.size();
        // Auto start of viewpager
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
            case R.id.btnAdd:
                if (flagTop == 0) {
                    addToCartApi();
                   /* binding.btnAdd.setText(getResources().getString(R.string.go_to_cart));
                    binding.llDetails.setAlpha(alphaFactor);
                    flagTop = 1;
                    binding.btnAddtocart.setText(getResources().getString(R.string.go_to_cart));
                    flagBottom = 1;*/
                } else {
                    ActivityController.startActivity(context, Cart.class, AppConstant.TYPE_PRODUCT_CART);
                }
                break;
            case R.id.btnWishlist:
                if (!wishlisted) {
                    addToWishlistApi();
                }
                break;
            case R.id.btnAddtocart:
                if (flagBottom == 0) {
                    addToCartApi();
                  /*  binding.btnAddtocart.setText(getResources().getString(R.string.go_to_cart));
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
            case R.id.tvRead:
                focusOnView(false);
                break;
            case R.id.tvFindSubstitutes:
                focusOnView(true);
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
                if (Objects.requireNonNull(checkPincodeBinding.etPincode.getText()).toString().isEmpty()) {
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

    private void getMedicineDetail() {
        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                Api api = RequestController.createService(context, Api.class);
                api.getMedicineDetail(product_id).enqueue(new BaseCallback<MedicineResponse>(context) {
                    @Override
                    public void onSuccess(MedicineResponse response) {
                        ProgressDialogUtils.dismiss();
                        if (response != null) {
                            if (response.getStatus() == 1) {
                                CommonUtils.showToastShort(context, response.getMessage());
                                com.drugvilla.in.model.product.medicinedetail.MedicineDetail medicineDetail = response.getMedicineData();
                                setData(medicineDetail);

                                if (medicineDetail.getSubstitutesData() != null
                                        && !medicineDetail.getSubstitutesData().isEmpty()) {
                                    binding.llSimilarProducts.setVisibility(View.VISIBLE);
                                    substituteDataList.clear();
                                    substituteDataList.addAll(medicineDetail.getSubstitutesData());
                                    // substituteAdapter.notifyDataSetChanged();
                                } else {
                                    substituteDataList.clear();
                                    //  substituteAdapter.notifyDataSetChanged();
                                }

                            } else {
                                CommonUtils.showToastShort(context, response.getMessage());
                            }
                        }
                    }

                    @Override
                    public void onFail(Call<MedicineResponse> call, BaseResponse baseResponse) {
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

    private void setData(com.drugvilla.in.model.product.medicinedetail.MedicineDetail medicineDetail) {
        binding.tvMRP.setPaintFlags(binding.tvMRP.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        binding.tvMRP2.setPaintFlags(binding.tvMRP.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        // round image for banner
        CommonUtils.setRoundImage(context, binding.ivBanner, medicineDetail.getBanner());

        binding.tvProductName.setText(medicineDetail.getName());
        binding.tvRating.setText(medicineDetail.getRating());
        binding.tvSaltComposition.setText(medicineDetail.getSaltComposition());
        binding.tvquantity.setText(medicineDetail.getQuantity());
        binding.tvBy.setText(medicineDetail.getProvidedBy());
        binding.tvMRP.setText(medicineDetail.getMrp());
        binding.tvMRP2.setText(medicineDetail.getMrp());
        binding.tvOff.setText(medicineDetail.getDiscount());
        binding.tvOff2.setText(medicineDetail.getDiscount());
        binding.tvAmount.setText(medicineDetail.getAmount());
        binding.tvAmount2.setText(medicineDetail.getAmount());
        binding.tvProductDetails.setText(medicineDetail.getDescription());
        binding.tvAddress.setText(medicineDetail.getSupplierAddress());
        // binding.tvCategory.setText(medicineDetail.getCategory());
        if (medicineDetail.getBanner() != null && !medicineDetail.getBanner().isEmpty()) {
            Picasso.with(context).load(medicineDetail.getBanner()).error(R.drawable.type_otc).placeholder(R.drawable.type_otc).into(binding.ivBanner);
        }
        if (medicineDetail.getOffer() != null && !medicineDetail.getOffer().isEmpty()) {
            binding.llOffer.setVisibility(View.VISIBLE);
            binding.tvDiscount1.setText(medicineDetail.getOffer().toString()
                    .replace("[", "").replace("]", "").replace(",", "\n"));
        }

        if (medicineDetail.getImages() != null && !medicineDetail.getImages().isEmpty()) {
            setProductImages(medicineDetail.getImages());
        }
    }

    private void setSubstitutes() {
        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
        binding.rvSubstitutes.setLayoutManager(gridLayoutManager);
        binding.rvSubstitutes.hasFixedSize();
        binding.rvSubstitutes.setItemAnimator(new DefaultItemAnimator());
        substituteAdapter = new SubstituteAdapter(context, substituteDataList);
        binding.rvSubstitutes.setAdapter(substituteAdapter);
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
