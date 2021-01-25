package com.drugvilla.in.ui.product;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.drugvilla.in.R;
import com.drugvilla.in.adapter.product.CategoriesViewAdapter;
import com.drugvilla.in.databinding.ActivityAllProductCategoryBinding;
import com.drugvilla.in.model.Document;
import com.drugvilla.in.model.product.category.ProductCategoryData;
import com.drugvilla.in.model.product.category.ProductCategoryResponse;
import com.drugvilla.in.model.product.productdetail.ProductResponse;
import com.drugvilla.in.service.Api;
import com.drugvilla.in.service.BaseCallback;
import com.drugvilla.in.service.BaseResponse;
import com.drugvilla.in.service.RequestController;
import com.drugvilla.in.utils.CommonUtils;
import com.drugvilla.in.utils.ProgressDialogUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class AllProductCategory extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    private Activity activity;
    private ActivityAllProductCategoryBinding binding;
    private List<ProductCategoryData> productCategoryDataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_all_product_category);
        context = AllProductCategory.this;
        activity = AllProductCategory.this;
        setToolbar();

        binding.swipeRefreshingLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAllProductCategoryApi();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        getAllProductCategoryApi();
        setAllCategories();
    }


    private void setAllCategories() {
        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
        binding.rvCategories.setLayoutManager(gridLayoutManager);
        binding.rvCategories.hasFixedSize();
        binding.rvCategories.setItemAnimator(new DefaultItemAnimator());

        CategoriesViewAdapter categoriesViewAdapter = new CategoriesViewAdapter(activity, productCategoryDataList);
        binding.rvCategories.setAdapter(categoriesViewAdapter);
    }


    private void setToolbar() {
        binding.menubar.tvTitle.setVisibility(View.VISIBLE);
        binding.menubar.tvTitle.setText(R.string.all_categories);
        binding.menubar.ivBack.setImageResource(R.drawable.ic_keyboard_backspace_black_24dp);
        binding.menubar.ivBack.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.ivBack) {
            finish();
        }
    }

    private void getAllProductCategoryApi() {
        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                Api api = RequestController.createService(context, Api.class);
                api.getAllProductCategory().enqueue(new BaseCallback<ProductCategoryResponse>(context) {
                    @Override
                    public void onSuccess(ProductCategoryResponse response) {
                        ProgressDialogUtils.dismiss();
                        binding.swipeRefreshingLayout.setRefreshing(false);
                        if (response != null) {
                            if (response.getStatus() == 1) {
                                CommonUtils.showToastShort(context, response.getMessage());

                                if (response.getProductCategoryDataList() != null
                                        && !response.getProductCategoryDataList().isEmpty()) {
                                    productCategoryDataList.clear();
                                    productCategoryDataList.addAll(response.getProductCategoryDataList());
                                } else {
                                    productCategoryDataList.clear();
                                }

                            } else {
                                CommonUtils.showToastShort(context, response.getMessage());
                            }
                        }
                    }

                    @Override
                    public void onFail(Call<ProductCategoryResponse> call, BaseResponse baseResponse) {
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

