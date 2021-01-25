package com.drugvilla.in.adapter.blog;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.drugvilla.in.R;
import com.drugvilla.in.databinding.RowArticleViewBinding;
import com.drugvilla.in.model.Document;
import com.drugvilla.in.model.blog.BlogData;
import com.drugvilla.in.service.Api;
import com.drugvilla.in.service.BaseCallback;
import com.drugvilla.in.service.BaseResponse;
import com.drugvilla.in.service.RequestController;
import com.drugvilla.in.ui.blog.ArticleDescription;
import com.drugvilla.in.ui.blog.CategoryWiseArticle;
import com.drugvilla.in.utils.ActivityController;
import com.drugvilla.in.utils.AppConstant;
import com.drugvilla.in.utils.CommonUtils;
import com.drugvilla.in.utils.ProgressDialogUtils;
import com.drugvilla.in.utils.SharedPref;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.MyViewHolder> {
    private Context context;
    private List<BlogData> dataList;
    private boolean like = false;

    public ArticleAdapter(Context context, List<BlogData> list) {
        this.context = context;
        this.dataList = list;
    }

    @NonNull
    @Override
    public ArticleAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_article_view, parent, false);
        return new ArticleAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ArticleAdapter.MyViewHolder holder, final int position) {

        final BlogData model = dataList.get(position);
        holder.binding.tvTitle.setText(model.getTitle());
        holder.binding.tvDescription.setText(model.getDescription());
        if (model.getImage() != null && !model.getImage().isEmpty()) {
            Picasso.with(context)
                    .load(model.getImage())
                    .error(R.drawable.articlecategory1)
                    .placeholder(R.drawable.articlecategory1)
                    .into(holder.binding.ivArticle);
        }
        holder.binding.llArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ArticleDescription.class);
                intent.putExtra("BLOG_ID", dataList.get(position).getBlogId());
                context.startActivity(intent);
            }
        });
        holder.binding.ivShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, model.getUrl());
                context.startActivity(Intent.createChooser(shareIntent, "Share link using"));

            }
        });

        holder.binding.ivFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!like) {
                    like = true;
                    holder.binding.ivFavourite.setImageResource(R.drawable.ic_fav);
                    addBlogToFavourite(position);
                } else {
                    holder.binding.ivFavourite.setImageResource(R.drawable.favourite);
                    like = false;
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private RowArticleViewBinding binding;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }

    private void addBlogToFavourite(int position) {
        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                Api api = RequestController.createService(context, Api.class);
                api.addFavouriteBlog(SharedPref.getStringPreferences(context, AppConstant.USER_ID), dataList.get(position).getBlogId())
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
