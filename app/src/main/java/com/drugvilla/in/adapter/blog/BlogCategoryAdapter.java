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
import com.drugvilla.in.databinding.RowArticleCategoryBinding;
import com.drugvilla.in.model.Document;
import com.drugvilla.in.model.dashboard.CategoryData;
import com.drugvilla.in.ui.blog.CategoryWiseArticle;
import com.drugvilla.in.ui.doctors.DoctorListing;
import com.drugvilla.in.utils.ActivityController;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BlogCategoryAdapter extends RecyclerView.Adapter<BlogCategoryAdapter.MyViewHolder> {
    private Context context;
    private List<CategoryData> dataList;

    public BlogCategoryAdapter(Context context, List<CategoryData> list) {
        this.context = context;
        this.dataList = list;
    }

    @NonNull
    @Override
    public BlogCategoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_article_category, parent, false);
        return new BlogCategoryAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BlogCategoryAdapter.MyViewHolder holder, final int position) {

        final CategoryData model = dataList.get(position);
        if (model.getImage() != null && !model.getImage().isEmpty()) {
            Picasso.with(context)
                    .load(model.getImage())
                    .error(R.drawable.articlecategory1)
                    .placeholder(R.drawable.articlecategory1)
                    .into(holder.binding.ivCategory);
        }
        holder.binding.tvCategory.setText(model.getTitle());
        holder.binding.llCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CategoryWiseArticle.class);
                intent.putExtra("BLOG_CATEGORY_ID", dataList.get(position).getId());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private RowArticleCategoryBinding binding;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
