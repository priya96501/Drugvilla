package com.drugvilla.in.ui.blog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.drugvilla.in.R;
import com.drugvilla.in.adapter.blog.PopularArticleAdapter;
import com.drugvilla.in.adapter.blog.comment.CommentAdapter;
import com.drugvilla.in.databinding.ActivityArticleDescriptionBinding;
import com.drugvilla.in.databinding.DialogBinding;
import com.drugvilla.in.databinding.DialogLabbookingSelectionBinding;
import com.drugvilla.in.databinding.DialogLeaveCommentBinding;
import com.drugvilla.in.listener.CommentListener;
import com.drugvilla.in.listener.OnSelectedListener;
import com.drugvilla.in.model.Document;
import com.drugvilla.in.model.blog.BlogData;
import com.drugvilla.in.model.blog.BlogResponse;
import com.drugvilla.in.model.blog.blogDetail.BlogDetail;
import com.drugvilla.in.model.blog.blogDetail.BlogDetailResponse;
import com.drugvilla.in.model.blog.comments.CommentResponse;
import com.drugvilla.in.model.blog.comments.CommentsData;
import com.drugvilla.in.model.dashboard.HomeSubData;
import com.drugvilla.in.service.Api;
import com.drugvilla.in.service.BaseCallback;
import com.drugvilla.in.service.BaseResponse;
import com.drugvilla.in.service.RequestController;
import com.drugvilla.in.utils.AppConstant;
import com.drugvilla.in.utils.CommonUtils;
import com.drugvilla.in.utils.DialogUtils;
import com.drugvilla.in.utils.ProgressDialogUtils;
import com.drugvilla.in.utils.RegexUtils;
import com.drugvilla.in.utils.SharedPref;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;

public class ArticleDescription extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    private ActivityArticleDescriptionBinding binding;
    private boolean like = false;
    // articles
    private final int[] article = {R.drawable.article1, R.drawable.article2, R.drawable.article3, R.drawable.article1, R.drawable.article2, R.drawable.article3};
    private final String[] articleTitle = {"How does exercise support health later in life?", "How does exercise support health later in life?", "The Mental Health Benefits of Exercise", "How does exercise support health later in life?", "How does exercise support health later in life?", "The Mental Health Benefits of Exercise"};
    private final String[] name = {"Amit shrivastav", "Rahul Mahajan", "Neha Dubbey"};
    private final String[] comment = {"It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout."
            , "It is a long established fact that a reader will be distracted by the readable.",
            " content of a page when looking at its layout."};
    private final String[] dateTime = {"22 jan 2019, 09:00 AM", "22 jan 2019, 09:00 AM", "22 jan 2019, 09:00 AM"};
    private final int[] user = {R.drawable.doc4, R.drawable.doc3, R.drawable.doc2};
    private List<Document> listData3 = new ArrayList<>();
    private List<Document> listData = new ArrayList<>();

    private boolean expandable, expand;
    private Dialog dialog;
    private String blogId = "", blog_URL = " ";
    //  private List<BlogData> blogDataList = new ArrayList<>();
    private List<HomeSubData> blogDataList = new ArrayList<>();
    private PopularArticleAdapter articleAdapter;
    private List<CommentsData> commentsDataList = new ArrayList<>();
    private CommentAdapter commentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_article_description);
        context = ArticleDescription.this;
        getData();
        setListener();
        setToolbar();
        getArticleDescriptionApi();
        getArticleCommentsApi();
        setRelatedArticleAdapter();
        setCommentsAdapter();

    }


    private void getData() {
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.getStringExtra("BLOG_ID") != null && !intent.getStringExtra("BLOG_ID").isEmpty()) {
                blogId = intent.getStringExtra("BLOG_ID");
            }
        }
    }

    private void setToolbar() {
        binding.menuBar.tvTitle.setVisibility(View.VISIBLE);
        binding.menuBar.tvTitle.setText(getResources().getString(R.string.article_detail));
        binding.menuBar.ivBack.setImageResource(R.drawable.ic_keyboard_backspace_black_24dp);
        binding.menuBar.ivBack.setOnClickListener(this);
    }

    private void setListener() {
        binding.ivShare.setOnClickListener(this);
        binding.ivFavourite.setOnClickListener(this);
        binding.expand.setOnClickListener(this);
        binding.tvLeaveComment.setOnClickListener(this);
        binding.ArticleRating.setRating(3);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                finish();
                break;
            case R.id.tvLeaveComment:
                showBottomSheetDialog(0, false, false);
                break;
            case R.id.ivShare:
                if (blog_URL != null && !blog_URL.isEmpty())
                    CommonUtils.shareData(context, blog_URL);
                break;
            case R.id.ivFavourite:
                if (!like) {
                    like = true;
                    binding.ivFavourite.setImageResource(R.drawable.ic_fav);
                    addBlogToFavourite();
                } else {
                    binding.ivFavourite.setImageResource(R.drawable.favourite);
                    like = false;
                }
                break;
            case R.id.expand:
                if (!expand) {
                    expand = true;
                    ObjectAnimator animation = ObjectAnimator.ofInt(binding.tvDescription, "maxLines", 400);
                    animation.setDuration(100).start();
                    binding.expand.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.up_arrow));
                } else {
                    expand = false;
                    ObjectAnimator animation = ObjectAnimator.ofInt(binding.tvDescription, "maxLines", 10);
                    animation.setDuration(100).start();
                    binding.expand.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.down_arrow));
                }
                break;
            default:
                break;
        }
    }

    // article comment section

    private void getArticleCommentsApi() {
        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                Api api = RequestController.createService(context, Api.class);
                api.getBlogComments(blogId,SharedPref.getStringPreferences(context,AppConstant.USER_ID)).enqueue(new BaseCallback<CommentResponse>(context) {
                    @Override
                    public void onSuccess(CommentResponse response) {
                        ProgressDialogUtils.dismiss();
                        if (response != null) {
                            if (response.getStatus() == 1) {
                                CommonUtils.showToastShort(context, response.getMessage());

                                if (response.getCommentsDataList() != null && !response.getCommentsDataList().isEmpty()) {
                                    binding.llComments.setVisibility(View.VISIBLE);
                                    commentsDataList.clear();
                                    commentsDataList.addAll(response.getCommentsDataList());
                                    commentAdapter.notifyDataSetChanged();
                                } else {
                                    binding.llComments.setVisibility(View.GONE);
                                    commentsDataList.clear();
                                    commentAdapter.notifyDataSetChanged();
                                }

                            } else {
                                CommonUtils.showToastShort(context, response.getMessage());
                            }
                        }
                    }

                    @Override
                    public void onFail(Call<CommentResponse> call, BaseResponse baseResponse) {
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

    private void setCommentsAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
        binding.rvComments.setLayoutManager(layoutManager);
        binding.rvComments.hasFixedSize();
        binding.rvComments.setItemAnimator(new DefaultItemAnimator());
        commentAdapter = new CommentAdapter(context, commentsDataList, new CommentListener() {
            @Override
            public void onClick(MenuItem view, int position) {
                switch (view.getItemId()) {
                    case R.id.delete:
                        openDeleteCommentDialog(position);
                        break;
                    case R.id.edit:
                        showBottomSheetDialog(position, true, false);
                        break;
                }
            }
        }, new OnSelectedListener() {
            @Override
            public void onClick(View view, int position) {
                showBottomSheetDialog(position, false, true);
            }
        });
        binding.rvComments.setAdapter(commentAdapter);
    }

    public void showBottomSheetDialog(final int position, final boolean valueEditComment, final boolean valueReply) {
        final DialogLeaveCommentBinding comentBinding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.dialog_leave_comment, null, false);
        final BottomSheetDialog dialog = DialogUtils.createBottomDialog(context, comentBinding.getRoot());
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        comentBinding.etEmail.setText(getResources().getString(R.string.company_care_email));
        comentBinding.etname.setText(getResources().getString(R.string.dummy_username));

        if (valueEditComment) {
            Objects.requireNonNull(comentBinding.ratingBar).setVisibility(View.GONE);
            Objects.requireNonNull(comentBinding.tvHeading).setText(getResources().getString(R.string.edit_comment));
            Objects.requireNonNull(comentBinding.btnPostComment).setText(getResources().getString(R.string.edit));
            Objects.requireNonNull(comentBinding.etComment).setText(commentsDataList.get(position).getComment());

        }
        if (valueReply) {
            Objects.requireNonNull(comentBinding.ratingBar).setVisibility(View.GONE);
            Objects.requireNonNull(comentBinding.tvHeading).setText("reply to " + commentsDataList.get(position).getName());
            Objects.requireNonNull(comentBinding.btnPostComment).setText(getResources().getString(R.string.post_reply));
        }

        comentBinding.btnPostComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isValid()) {
                    if (valueEditComment) {
                        dialog.dismiss();
                        editCommentApi(commentsDataList.get(position).getComment_id(),
                                comentBinding.etname.getText().toString(), comentBinding.etEmail.getText().toString(), comentBinding.etComment.getText().toString());
                    } else if (valueReply) {
                        dialog.dismiss();
                        replyOnCommentApi(commentsDataList.get(position).getComment_id(),
                                comentBinding.etname.getText().toString(), comentBinding.etEmail.getText().toString(), comentBinding.etComment.getText().toString(),
                                commentsDataList.get(position).getName());

                    } else {
                        dialog.dismiss();
                        addCommentApi(comentBinding.etname.getText().toString(), comentBinding.etEmail.getText().toString(), comentBinding.etComment.getText().toString());
                    }

                }
            }

            private boolean isValid() {
                if (Objects.requireNonNull(comentBinding.etname.getText()).toString().isEmpty()) {
                    CommonUtils.setErrorMessage(comentBinding.etname, Objects.requireNonNull(comentBinding.tvNameError), getResources().getString(R.string.empty_name));
                } else if (comentBinding.etname.getText().toString().length() < 2) {
                    CommonUtils.setErrorMessage(comentBinding.etname, Objects.requireNonNull(comentBinding.tvNameError), getResources().getString(R.string.invalid_name));
                } else if (Objects.requireNonNull(comentBinding.etEmail.getText()).toString().isEmpty()) {
                    CommonUtils.setErrorMessage(comentBinding.etEmail, Objects.requireNonNull(comentBinding.tvEmailError), getResources().getString(R.string.empty_email));
                } else if (!RegexUtils.isEmailValid(comentBinding.etEmail.getText().toString().trim())) {
                    CommonUtils.setErrorMessage(comentBinding.etEmail, Objects.requireNonNull(comentBinding.tvEmailError), getResources().getString(R.string.invalid_email));
                } else if (Objects.requireNonNull(comentBinding.etComment.getText()).toString().isEmpty()) {
                    CommonUtils.setErrorMessage(comentBinding.etComment, Objects.requireNonNull(comentBinding.tvCommentError), getResources().getString(R.string.empty_comment));
                } else if (comentBinding.etComment.getText().toString().length() < 5) {
                    CommonUtils.setErrorMessage(comentBinding.etComment, Objects.requireNonNull(comentBinding.tvCommentError), getResources().getString(R.string.invalid_comment));
                } else {
                    return true;
                }
                return false;
            }
        });
    }

    private void addCommentApi(String name, String email, String comment) {
        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                Api api = RequestController.createService(context, Api.class);
                api.addComment(SharedPref.getStringPreferences(context, AppConstant.USER_ID), blogId, name, email, comment)
                        .enqueue(new BaseCallback<BaseResponse>(context) {
                            @Override
                            public void onSuccess(BaseResponse response) {
                                ProgressDialogUtils.dismiss();
                                if (response != null) {
                                    if (response.getStatus() == 1) {
                                        CommonUtils.showToastShort(context, response.getMessage());
                                        getArticleCommentsApi();
                                    } else {

                                        CommonUtils.showToastShort(context, response.getMessage());
                                    }
                                } else {
                                    CommonUtils.showToastShort(context, getResources().getString(R.string.no_response));
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

    private void openDeleteCommentDialog(final int position) {
        final DialogBinding dialogBinding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.dialog, null, false);
        dialog = DialogUtils.createDialog(context, dialogBinding.getRoot(), 0);
        dialog.setCancelable(false);
        dialogBinding.tvHeading.setText(getResources().getString(R.string.delete_comment));
        dialogBinding.tvYes.setText(getResources().getString(R.string.delete));
        dialogBinding.tvNo.setText(getResources().getString(R.string.cancel));
        dialogBinding.tvDescription.setText(getResources().getString(R.string.delete_comment_permanentaly));
        dialogBinding.tvNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialogBinding.tvYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                deleteCommentApi(commentsDataList.get(position).getComment_id());
            }
        });
    }

    private void deleteCommentApi(String comment_id) {
        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                Api api = RequestController.createService(context, Api.class);
                api.deleteComment(SharedPref.getStringPreferences(context, AppConstant.USER_ID), blogId, comment_id)
                        .enqueue(new BaseCallback<BaseResponse>(context) {
                            @Override
                            public void onSuccess(BaseResponse response) {
                                ProgressDialogUtils.dismiss();
                                if (response != null) {
                                    if (response.getStatus() == 1) {
                                        CommonUtils.showToastShort(context, response.getMessage());
                                        getArticleCommentsApi();
                                        //commentAdapter.notifyDataSetChanged();
                                    } else {

                                        CommonUtils.showToastShort(context, response.getMessage());
                                    }
                                } else {
                                    CommonUtils.showToastShort(context, getResources().getString(R.string.no_response));
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

    private void editCommentApi(String comment_id, String name, String email, String comment) {
        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                Api api = RequestController.createService(context, Api.class);
                api.updateComment(SharedPref.getStringPreferences(context, AppConstant.USER_ID), blogId, comment_id, name, email, comment)
                        .enqueue(new BaseCallback<BaseResponse>(context) {
                            @Override
                            public void onSuccess(BaseResponse response) {
                                ProgressDialogUtils.dismiss();
                                if (response != null) {
                                    if (response.getStatus() == 1) {
                                        CommonUtils.showToastShort(context, response.getMessage());
                                        getArticleCommentsApi();
                                    } else {

                                        CommonUtils.showToastShort(context, response.getMessage());
                                    }
                                } else {
                                    CommonUtils.showToastShort(context, getResources().getString(R.string.no_response));
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

    private void replyOnCommentApi(String comment_id, String name, String email, String comment, String replyTo) {
        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                Api api = RequestController.createService(context, Api.class);
                api.replyOnComment(SharedPref.getStringPreferences(context, AppConstant.USER_ID), blogId, name, email, comment, comment_id, replyTo)
                        .enqueue(new BaseCallback<BaseResponse>(context) {
                            @Override
                            public void onSuccess(BaseResponse response) {
                                ProgressDialogUtils.dismiss();
                                if (response != null) {
                                    if (response.getStatus() == 1) {
                                        CommonUtils.showToastShort(context, response.getMessage());
                                        getArticleCommentsApi();
                                        //commentAdapter.notifyDataSetChanged();
                                    } else {

                                        CommonUtils.showToastShort(context, response.getMessage());
                                    }
                                } else {
                                    CommonUtils.showToastShort(context, getResources().getString(R.string.no_response));
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


    // article description section

    private void getArticleDescriptionApi() {
        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                Api api = RequestController.createService(context, Api.class);
                api.getBlogDetail(blogId).enqueue(new BaseCallback<BlogDetailResponse>(context) {
                    @Override
                    public void onSuccess(BlogDetailResponse response) {
                        ProgressDialogUtils.dismiss();
                        if (response != null) {
                            if (response.getStatus() == 1) {
                                CommonUtils.showToastShort(context, response.getMessage());
                                BlogDetail blogDetail = response.getBlogDetail();
                                blog_URL = blogDetail.getUrl();
                                setArticleDetail(blogDetail);
                                if (blogDetail.getRelatedBlogDataList() != null && !blogDetail.getRelatedBlogDataList().isEmpty()) {
                                    binding.llHealthArticles.setVisibility(View.VISIBLE);
                                    blogDataList.clear();
                                    blogDataList.addAll(blogDetail.getRelatedBlogDataList());
                                    articleAdapter.notifyDataSetChanged();
                                } else {
                                    binding.llHealthArticles.setVisibility(View.GONE);
                                    blogDataList.clear();
                                    articleAdapter.notifyDataSetChanged();
                                }

                            } else {
                                CommonUtils.showToastShort(context, response.getMessage());
                            }
                        }
                    }

                    @Override
                    public void onFail(Call<BlogDetailResponse> call, BaseResponse baseResponse) {
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

    private void setRelatedArticleAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
        binding.rvArticles.setLayoutManager(layoutManager);
        binding.rvArticles.hasFixedSize();
        binding.rvArticles.setItemAnimator(new DefaultItemAnimator());
        articleAdapter = new PopularArticleAdapter(context, blogDataList);
        binding.rvArticles.setAdapter(articleAdapter);
    }

    private void setArticleDetail(BlogDetail blogDetail) {
        binding.tvdate.setText(blogDetail.getDate());
        binding.tvTitle.setText(blogDetail.getTitle());
        binding.tvDescription.setText(blogDetail.getDescription());
        setDescription();
        binding.tvPostedBy.setText(blogDetail.getBlogBy());
        binding.ArticleRating.setRating(Float.parseFloat(blogDetail.getRating()));
    }

    private void setDescription() {
        binding.tvDescription.setLines(10);
        binding.tvDescription.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (binding.tvDescription.getLineCount() > 10) {
                    binding.expand.setVisibility(View.VISIBLE);
                    if (expandable) {
                        expandable = false;
                        if (binding.tvDescription.getLineCount() > 10) {
                            binding.expand.setVisibility(View.VISIBLE);
                            ObjectAnimator animation = ObjectAnimator.ofInt(binding.tvDescription, "maxLines", 10);
                            animation.setDuration(0).start();
                        }
                    }
                } else {
                    binding.expand.setVisibility(View.GONE);
                }
            }
        });
    }

    private void addBlogToFavourite() {
        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                Api api = RequestController.createService(context, Api.class);
                api.addFavouriteBlog(SharedPref.getStringPreferences(context, AppConstant.USER_ID), blogId)
                        .enqueue(new BaseCallback<BaseResponse>(context) {
                            @Override
                            public void onSuccess(BaseResponse response) {
                                ProgressDialogUtils.dismiss();
                                if (response != null) {
                                    if (response.getStatus() == 1) {
                                        CommonUtils.showToastShort(context, response.getMessage());
                                    } else {

                                        CommonUtils.showToastShort(context, response.getMessage());
                                    }
                                } else {
                                    CommonUtils.showToastShort(context, getResources().getString(R.string.no_response));
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

