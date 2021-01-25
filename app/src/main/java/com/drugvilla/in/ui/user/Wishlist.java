package com.drugvilla.in.ui.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.drugvilla.in.R;
import com.drugvilla.in.adapter.product.ProductListingAdapter;
import com.drugvilla.in.databinding.ActivityWishlistBinding;
import com.drugvilla.in.listener.OnSelectedListener;
import com.drugvilla.in.model.Document;
import com.drugvilla.in.model.dashboard.BannerData;
import com.drugvilla.in.model.dashboard.BannerResponse;
import com.drugvilla.in.model.product.productdetail.ProductData;
import com.drugvilla.in.model.wishlist.WishlistData;
import com.drugvilla.in.model.wishlist.WishlistResponse;
import com.drugvilla.in.service.Api;
import com.drugvilla.in.service.BaseCallback;
import com.drugvilla.in.service.BaseResponse;
import com.drugvilla.in.service.RequestController;
import com.drugvilla.in.ui.order.Cart;
import com.drugvilla.in.utils.ActivityController;
import com.drugvilla.in.utils.AppConstant;
import com.drugvilla.in.utils.CommonUtils;
import com.drugvilla.in.utils.ProgressDialogUtils;
import com.drugvilla.in.utils.SharedPref;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class Wishlist extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    private Activity activity;
    private ActivityWishlistBinding binding;

    // product listing
    private final int[] listingImages = {R.drawable.brand2, R.drawable.brand1, R.drawable.product3, R.drawable.product4};
    private final String[] listingNames = {"Fairness Creams", "Himalaya Shampoo", "Health Drinks", "Dabur Chyanwanprash 1kg (Get Dabur Honey 50g Free)"};
    private final String[] listingMRP = {"1600", "400", "750", "1100"};
    private final String[] listingPrice = {"100", "200", "650", "1000"};
    private final String[] listingDiscount = {"20% off", "22% off", "20% off", "15% off"};
    private List<Document> listProducts = new ArrayList<>();
    // private List<WishlistData> wishlistDataList = new ArrayList<>();
    private List<ProductData> wishlistDataList = new ArrayList<>();
    private ProductListingAdapter productListingAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_wishlist);
        context = Wishlist.this;
        activity = Wishlist.this;
        setToolbar();

        getWishlist();
        setListingProducts();

        binding.swipeRefreshingLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getWishlist();
                setListingProducts();

            }
        });
    }

   /* @Override
    protected void onResume() {
        super.onResume();
        getWishlist();
        setListingProducts();
    }
*/

    private void setToolbar() {
        binding.menubar.tvTitle.setVisibility(View.VISIBLE);
        binding.menubar.ivRight.setVisibility(View.VISIBLE);
        binding.menubar.tvTitle.setText(R.string.wishlist);
        binding.menubar.ivBack.setImageResource(R.drawable.ic_keyboard_backspace_black_24dp);
        binding.menubar.ivBack.setOnClickListener(this);
        binding.menubar.ivRight.setOnClickListener(this);
        binding.menubar.ivRight.setBadgeValue(3);
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
            default:
                break;
        }
    }

    private void setListingProducts() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2);
        binding.rvItems.setLayoutManager(gridLayoutManager);
        binding.rvItems.hasFixedSize();
       // binding.rvItems.setItemAnimator(new DefaultItemAnimator());
        productListingAdapter = new ProductListingAdapter(activity, wishlistDataList, new OnSelectedListener() {
            @Override
            public void onClick(View view, int position) {
                switch (view.getId()) {
                    case R.id.btnMoveToCart:
                        moveToCart(position);
                        break;
                    case R.id.ivCross:
                        removeFromWishlist(position);
                        break;
                    default:
                        break;
                }
            }
        }, AppConstant.FROM_WISHLIST);
        binding.rvItems.setAdapter(productListingAdapter);
    }

    private void removeFromWishlist(int position) {
        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                Api api = RequestController.createService(context, Api.class);
                api.removeFromWishlist(SharedPref.getStringPreferences(context, AppConstant.USER_ID)
                        , wishlistDataList.get(position).getId())
                        .enqueue(new BaseCallback<BaseResponse>(context) {
                            @Override
                            public void onSuccess(BaseResponse response) {
                                ProgressDialogUtils.dismiss();
                                if (response != null) {
                                    if (response.getStatus() == 1) {
                                        CommonUtils.showToastShort(context, response.getMessage());
                                        getWishlist();
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

    private void moveToCart(int position) {
        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                Api api = RequestController.createService(context, Api.class);
                api.moveToCart(SharedPref.getStringPreferences(context, AppConstant.USER_ID),
                        wishlistDataList.get(position).getId())
                        .enqueue(new BaseCallback<BaseResponse>(context) {
                            @Override
                            public void onSuccess(BaseResponse response) {
                                ProgressDialogUtils.dismiss();
                                if (response != null) {
                                    if (response.getStatus() == 1) {
                                        CommonUtils.showToastShort(context, response.getMessage());
                                        getWishlist();
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


    private void getWishlist() {
        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                Api api = RequestController.createService(context, Api.class);
                api.getWishlist(SharedPref.getStringPreferences(context, AppConstant.USER_ID)).enqueue(new BaseCallback<WishlistResponse>(context) {
                            @Override
                            public void onSuccess(WishlistResponse response) {
                                binding.swipeRefreshingLayout.setRefreshing(false);
                                ProgressDialogUtils.dismiss();
                                if (response != null) {
                                    if (response.getStatus()==1) {
                                        CommonUtils.showToastShort(context, response.getMessage());
                                        if (response.getWishlistDataList() != null && !response.getWishlistDataList().isEmpty()) {
                                            wishlistDataList.clear();
                                            wishlistDataList.addAll(response.getWishlistDataList());
                                        } else {
                                            wishlistDataList.clear();
                                            binding.llNoItems.setVisibility(View.VISIBLE);
                                        }
                                    } else {
                                        wishlistDataList.clear();
                                        CommonUtils.showToastShort(context, response.getMessage());
                                    }
                                } else {
                                    CommonUtils.showToastShort(context, getResources().getString(R.string.no_response));
                                }
                            }

                            @Override
                            public void onFail(Call<WishlistResponse> call, BaseResponse baseResponse) {
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
