package com.drugvilla.in.ui.blog.fragment;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.drugvilla.in.R;
import com.drugvilla.in.adapter.blog.BlogCategoryAdapter;
import com.drugvilla.in.databinding.FragmentCategoryBinding;
import com.drugvilla.in.model.Document;
import com.drugvilla.in.model.blog.BlogData;
import com.drugvilla.in.model.blog.blogDetail.BlogDetail;
import com.drugvilla.in.model.dashboard.CategoryData;
import com.drugvilla.in.model.dashboard.CategoryResponse;
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

public class CategoryFragment extends Fragment {
    private FragmentCategoryBinding binding;
    public BlogCategoryAdapter adapter;
    private List<CategoryData> blogCategoryList = new ArrayList<>();

    public CategoryFragment() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_category, container, false);
        View view = binding.getRoot();
        getBlogCategory();
        setCategoryAdapter();
        return view;
    }

    private void setCategoryAdapter() {
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 3, RecyclerView.VERTICAL, false);
        binding.rvCategory.setLayoutManager(layoutManager);
        binding.rvCategory.hasFixedSize();
        binding.rvCategory.setItemAnimator(new DefaultItemAnimator());
        adapter = new BlogCategoryAdapter(getActivity(), blogCategoryList);
        binding.rvCategory.setAdapter(adapter);
    }

    private void getBlogCategory() {
        if (CommonUtils.isOnline(getActivity())) {
            try {
                ProgressDialogUtils.show(getActivity());
                Api api = RequestController.createService(getActivity(), Api.class);
                api.getBlogCategory().enqueue(new BaseCallback<CategoryResponse>(getActivity()) {
                    @Override
                    public void onSuccess(CategoryResponse response) {
                        ProgressDialogUtils.dismiss();
                        if (response != null) {
                            if (response.getStatus() == 1) {
                                CommonUtils.showToastShort(getActivity(), response.getMessage());
                                if (response.getCategoryDataList() != null && !response.getCategoryDataList().isEmpty()) {
                                    blogCategoryList.clear();
                                    blogCategoryList.addAll(response.getCategoryDataList());
                                    adapter.notifyDataSetChanged();
                                } else {
                                    blogCategoryList.clear();
                                    adapter.notifyDataSetChanged();
                                }

                            } else {
                                CommonUtils.showToastShort(getActivity(), response.getMessage());
                            }
                        }
                    }

                    @Override
                    public void onFail(Call<CategoryResponse> call, BaseResponse baseResponse) {
                        ProgressDialogUtils.dismiss();
                        CommonUtils.showToastShort(getActivity(), getResources().getString(R.string.failure));
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            CommonUtils.showToastShort(getActivity(), getResources().getString(R.string.no_internet));
        }
    }

}
