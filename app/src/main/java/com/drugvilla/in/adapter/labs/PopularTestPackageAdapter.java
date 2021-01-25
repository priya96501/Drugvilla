package com.drugvilla.in.adapter.labs;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.drugvilla.in.R;
import com.drugvilla.in.databinding.RowPopularTestPackagesBinding;
import com.drugvilla.in.model.Document;
import com.drugvilla.in.model.dashboard.HomeSubData;
import com.drugvilla.in.ui.labs.PackageDetailActivity;
import com.drugvilla.in.utils.ActivityController;
import com.drugvilla.in.utils.CommonUtils;

import java.util.List;

public class PopularTestPackageAdapter extends RecyclerView.Adapter<PopularTestPackageAdapter.MyViewHolder> {
    private Context context;
    private List<HomeSubData> dataList;

    public PopularTestPackageAdapter(Context context, List<HomeSubData> list) {
        this.context = context;
        this.dataList = list;

    }

    @NonNull
    @Override
    public PopularTestPackageAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_popular_test_packages, parent, false);
        return new PopularTestPackageAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final PopularTestPackageAdapter.MyViewHolder holder, final int position) {
        final HomeSubData model = dataList.get(position);
        int[] backgrounds = {R.drawable.round_bottom_pink, R.drawable.round_bottom_skyblue, R.drawable.round_bottom_yellow};
        holder.binding.tvTitle.setText(model.getTitle());
        holder.binding.llBottomView.setBackground(context.getDrawable(backgrounds[position % 3]));
        CommonUtils.setRoundImage(context, holder.binding.ivTestPackage, model.getImage());
        holder.binding.llRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityController.startActivity(context, PackageDetailActivity.class);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private RowPopularTestPackagesBinding binding;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);

        }
    }
}
