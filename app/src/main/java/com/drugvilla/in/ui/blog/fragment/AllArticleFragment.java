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
import com.drugvilla.in.databinding.FragmentAllArticleBinding;
import com.drugvilla.in.model.Document;
import com.drugvilla.in.model.blog.BlogData;
import com.drugvilla.in.model.blog.BlogResponse;
import com.drugvilla.in.service.Api;
import com.drugvilla.in.service.BaseCallback;
import com.drugvilla.in.service.BaseResponse;
import com.drugvilla.in.service.RequestController;
import com.drugvilla.in.utils.CommonUtils;
import com.drugvilla.in.utils.ProgressDialogUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class AllArticleFragment extends Fragment {
    private FragmentAllArticleBinding binding;
    public ArticleAdapter adapter;
    private List<Document> listData = new ArrayList<>();
    private List<BlogData> blogDataList = new ArrayList<>();
    private final int[] image = {R.drawable.article1, R.drawable.article3, R.drawable.article2, R.drawable.article1, R.drawable.article3};
    private final String[] title = {"T-Ball Should Consider These 4 Changes!", "How Long Does Marijuana Stay In Your Body?",
            "What 5 Years of Bodybuilding Taught Me About Tackling Mondays",
            "Some Reasons Why Sugarcane Juice Could Be Your Ideal Summer Drink", "Good,, Bad, and Trans Fats and the Health Risk They Pose"};
    private final String[] description = {"The first thing to understand is that hyperhidrosis or sweating is a condition in which a person sweats too much and suddenly. People with hyperhidrosis seem to have overactive sweat glands.",
            "Getting and keeping a healthy brain is vital to longevity and good living. Here are some simple checks to ensure that your brain is operating at its best.",
            "The first thing to understand is that hyperhidrosis or sweating is a condition in which a person sweats too much and suddenly. People with hyperhidrosis seem to have overactive sweat glands.",
            "Little League baseball and softball coaches usually run adequate but inefficient batting practice sessions. Learn to increase the repetitions of everyone on the team.",
            "Getting and keeping a healthy brain is vital to longevity and good living. Here are some simple checks to ensure that your brain is operating at its best."};
    public AllArticleFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_all_article, container, false);
        View view = binding.getRoot();
        getAllBlogs();
        setArticleAdapter();
        return view;
    }

    private void getAllBlogs() {
        if (CommonUtils.isOnline(getActivity())) {
            try {
                ProgressDialogUtils.show(getActivity());
                Api api = RequestController.createService(getActivity(), Api.class);
                api.getAllBlogs().enqueue(new BaseCallback<BlogResponse>(getActivity()) {
                    @Override
                    public void onSuccess(BlogResponse response) {
                        ProgressDialogUtils.dismiss();
                        if (response != null) {
                            if (response.getStatus() == 1) {
                                CommonUtils.showToastShort(getActivity(), response.getMessage());
                                if (response.getBlogDataList() != null && !response.getBlogDataList().isEmpty()) {
                                    blogDataList.clear();
                                    blogDataList.addAll(response.getBlogDataList());
                                    adapter.notifyDataSetChanged();
                                } else {
                                    blogDataList.clear();
                                    adapter.notifyDataSetChanged();
                                }

                            } else {
                                CommonUtils.showToastShort(getActivity(), response.getMessage());
                            }
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

    private void setArticleAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        binding.rvArticle.setLayoutManager(layoutManager);
        binding.rvArticle.hasFixedSize();
        binding.rvArticle.setItemAnimator(new DefaultItemAnimator());
        adapter = new ArticleAdapter(getActivity(), blogDataList);
        binding.rvArticle.setAdapter(adapter);
    }
}
