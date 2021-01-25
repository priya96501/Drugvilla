package com.drugvilla.in.ui.blog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.drugvilla.in.R;
import com.drugvilla.in.adapter.blog.ArticleAdapter;
import com.drugvilla.in.databinding.ActivityCategoryWiseArticleBinding;
import com.drugvilla.in.model.Document;
import com.drugvilla.in.model.blog.BlogData;
import com.drugvilla.in.model.blog.BlogResponse;
import com.drugvilla.in.model.dashboard.CategoryData;
import com.drugvilla.in.model.dashboard.CategoryResponse;
import com.drugvilla.in.service.Api;
import com.drugvilla.in.service.BaseCallback;
import com.drugvilla.in.service.BaseResponse;
import com.drugvilla.in.service.RequestController;
import com.drugvilla.in.utils.CommonUtils;
import com.drugvilla.in.utils.ProgressDialogUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class CategoryWiseArticle extends AppCompatActivity implements View.OnClickListener {
    private ActivityCategoryWiseArticleBinding binding;
    public ArticleAdapter adapter;
    private Context context;
    private List<Document> listData = new ArrayList<>();
    private final int[] image = {R.drawable.article1, R.drawable.article3, R.drawable.article2, R.drawable.article1, R.drawable.article3};
    private final String[] title = {"T-Ball Should Consider These 4 Changes!", "How Long Does Marijuana Stay In Your Body?",
            "What 5 Years of Bodybuilding Taught Me About Tackling Mondays",
            "Some Reasons Why Sugarcane Juice Could Be Your Ideal Summer Drink", "Good,, Bad, and Trans Fats and the Health Risk They Pose"};
    private final String[] description = {"The first thing to understand is that hyperhidrosis or sweating is a condition in which a person sweats too much and suddenly. People with hyperhidrosis seem to have overactive sweat glands.",
            "Getting and keeping a healthy brain is vital to longevity and good living. Here are some simple checks to ensure that your brain is operating at its best.",
            "The first thing to understand is that hyperhidrosis or sweating is a condition in which a person sweats too much and suddenly. People with hyperhidrosis seem to have overactive sweat glands.",
            "Little League baseball and softball coaches usually run adequate but inefficient batting practice sessions. Learn to increase the repetitions of everyone on the team.",
            "Getting and keeping a healthy brain is vital to longevity and good living. Here are some simple checks to ensure that your brain is operating at its best."};

    private List<BlogData> blogDataList = new ArrayList<>();
    private String blogCategoryId="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_category_wise_article);
        context = CategoryWiseArticle.this;
        getData();
        getCategoryWiseBlogs();
        setToolbar();
        setAdapter();

        binding.swipeRefreshingLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getCategoryWiseBlogs();
                setAdapter();
            }
        });
    }

    private void getData() {
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.getStringExtra("BLOG_CATEGORY_ID") != null && !intent.getStringExtra("BLOG_CATEGORY_ID").isEmpty()) {
                blogCategoryId = intent.getStringExtra("BLOG_CATEGORY_ID");
            }
        }
    }

    private void setToolbar() {
        binding.menubar.tvTitle.setVisibility(View.VISIBLE);
        binding.menubar.tvTitle.setText(getResources().getString(R.string.category_name));
        binding.menubar.ivBack.setImageResource(R.drawable.ic_keyboard_backspace_black_24dp);
        binding.menubar.ivBack.setOnClickListener(this);
    }

    private void setAdapter() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
        binding.rvArticles.setLayoutManager(linearLayoutManager);
        binding.rvArticles.hasFixedSize();
        binding.rvArticles.setItemAnimator(new DefaultItemAnimator());
        adapter = new ArticleAdapter(context, blogDataList);
        binding.rvArticles.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.ivBack) {
            finish();
        }
    }

    private void getCategoryWiseBlogs() {
        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                Api api = RequestController.createService(context, Api.class);
                api.getCategoryWiseBlog(blogCategoryId).enqueue(new BaseCallback<BlogResponse>(context) {
                    @Override
                    public void onSuccess(BlogResponse response) {
                        ProgressDialogUtils.dismiss();
                        if (response != null) {
                            if (response.getStatus() == 1) {
                                CommonUtils.showToastShort(context, response.getMessage());
                                if (response.getBlogDataList() != null && !response.getBlogDataList().isEmpty()) {
                                    blogDataList.clear();
                                    blogDataList.addAll(response.getBlogDataList());
                                    adapter.notifyDataSetChanged();
                                } else {
                                    blogDataList.clear();
                                    adapter.notifyDataSetChanged();
                                }

                            } else {
                                CommonUtils.showToastShort(context, response.getMessage());
                            }
                        }
                    }

                    @Override
                    public void onFail(Call<BlogResponse> call, BaseResponse baseResponse) {
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
