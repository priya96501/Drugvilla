package com.drugvilla.in.adapter.blog;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.drugvilla.in.R;
import com.drugvilla.in.databinding.RowArticlesBinding;
import com.drugvilla.in.model.Document;
import com.drugvilla.in.model.blog.BlogData;
import com.drugvilla.in.model.dashboard.HomeSubData;
import com.drugvilla.in.ui.blog.ArticleDescription;
import com.drugvilla.in.ui.blog.CategoryWiseArticle;
import com.drugvilla.in.utils.ActivityController;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PopularArticleAdapter extends RecyclerView.Adapter<PopularArticleAdapter.MyViewHolder> {
    private Context context;
    private List<HomeSubData> dataList;

    public PopularArticleAdapter(Context context, List<HomeSubData> list) {
        this.context = context;
        this.dataList = list;
    }

    @NonNull
    @Override
    public PopularArticleAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_articles, parent, false);
        return new PopularArticleAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PopularArticleAdapter.MyViewHolder holder, final int position) {
        final HomeSubData model = dataList.get(position);
        if (model.getImage() != null && !model.getImage().isEmpty()) {
            Picasso.with(context)
                    .load(model.getImage())
                    .error(R.drawable.articlecategory1)
                    .placeholder(R.drawable.articlecategory1)
                    .into(holder.binding.ivArticle);
        }
        holder.binding.tvTitle.setText(model.getTitle());
        holder.binding.llArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ArticleDescription.class);
                intent.putExtra("BLOG_ID", dataList.get(position).getId());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private RowArticlesBinding binding;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
