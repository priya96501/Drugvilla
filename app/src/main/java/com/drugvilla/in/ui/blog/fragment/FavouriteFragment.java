package com.drugvilla.in.ui.blog.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.drugvilla.in.R;
import com.drugvilla.in.adapter.blog.ArticleAdapter;
import com.drugvilla.in.databinding.FragmentFavouriteBinding;
import com.drugvilla.in.model.blog.BlogData;
import com.drugvilla.in.model.blog.BlogResponse;
import com.drugvilla.in.service.Api;
import com.drugvilla.in.service.BaseCallback;
import com.drugvilla.in.service.BaseResponse;
import com.drugvilla.in.service.RequestController;
import com.drugvilla.in.utils.AppConstant;
import com.drugvilla.in.utils.CommonUtils;
import com.drugvilla.in.utils.ProgressDialogUtils;
import com.drugvilla.in.utils.SharedPref;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class FavouriteFragment extends Fragment {
    private FragmentFavouriteBinding binding;
    private List<BlogData> blogDataList = new ArrayList<>();
    public ArticleAdapter adapter;

    public FavouriteFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_favourite, container, false);
        View view = binding.getRoot();
        getFavouriteBlogs();
        setAdapter();
        return view;
    }

    private void setAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        binding.rvArticle.setLayoutManager(layoutManager);
        binding.rvArticle.hasFixedSize();
        binding.rvArticle.setItemAnimator(new DefaultItemAnimator());
        adapter = new ArticleAdapter(getActivity(), blogDataList);
        binding.rvArticle.setAdapter(adapter);
    }

    private void getFavouriteBlogs() {
        if (CommonUtils.isOnline(getActivity())) {
            try {
                ProgressDialogUtils.show(getActivity());
                Api api = RequestController.createService(getActivity(), Api.class);
                api.getFavouriteBlogs(SharedPref.getStringPreferences(getActivity(), AppConstant.USER_ID))
                        .enqueue(new BaseCallback<BlogResponse>(getActivity()) {
                    @Override
                    public void onSuccess(BlogResponse response) {
                        ProgressDialogUtils.dismiss();
                        if (response != null) {
                            if (response.getStatus() == 1) {
                                CommonUtils.showToastShort(getActivity(), response.getMessage());
                                if (response.getBlogDataList() != null && !response.getBlogDataList().isEmpty()) {
                                    setEmptyLayout(false);
                                    blogDataList.clear();
                                    blogDataList.addAll(response.getBlogDataList());
                                    adapter.notifyDataSetChanged();
                                } else {
                                    setEmptyLayout(true);
                                    blogDataList.clear();
                                    adapter.notifyDataSetChanged();
                                }

                            } else {
                                setEmptyLayout(true);
                                blogDataList.clear();
                                adapter.notifyDataSetChanged();
                                CommonUtils.showToastShort(getActivity(), response.getMessage());
                            }
                        } else {
                            CommonUtils.showToastShort(getActivity(), getResources().getString(R.string.no_response));
                        }
                    }

                    @Override
                    public void onFail(Call<BlogResponse> call, BaseResponse baseResponse) {
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

    private void setEmptyLayout(boolean value) {
        if (value) {
            binding.emptyLayout.llRoot.setVisibility(View.VISIBLE);
            binding.emptyLayout.ivImage.setImageResource(R.drawable.nofavourite);
            binding.emptyLayout.tvHeading.setText(getResources().getString(R.string.no_favourite));
            binding.emptyLayout.tvSubHeading.setVisibility(View.VISIBLE);
            binding.emptyLayout.tvSubHeading.setText(getResources().getString(R.string.no_favourite_content));
            binding.emptyLayout.btnSubmit.setVisibility(View.GONE);
        } else {
            binding.emptyLayout.llRoot.setVisibility(View.GONE);
        }
    }
}
