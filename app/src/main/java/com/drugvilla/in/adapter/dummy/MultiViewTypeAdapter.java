package com.drugvilla.in.adapter.dummy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.drugvilla.in.R;
import com.drugvilla.in.databinding.RowAddressBinding;
import com.drugvilla.in.databinding.RowCategoriesBinding;
import com.drugvilla.in.databinding.RowHealthPackagesBinding;
import com.drugvilla.in.databinding.RowListingViewBinding;
import com.drugvilla.in.databinding.RowProductBinding;
import com.drugvilla.in.model.Document;
import com.drugvilla.in.utils.AppConstant;

import java.util.List;

public class MultiViewTypeAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<Document> dataList;
    private int type;
    private RowProductBinding rowProductBinding;
    private RowHealthPackagesBinding rowHealthPackagesBinding;
    private RowCategoriesBinding binding;

    public MultiViewTypeAdapter(Context context, List<Document> data, int type) {
        this.dataList = data;
        this.context = context;
        this.type = type;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (type == 0) {
            return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.row_categories, parent, false));
        } else if (type == 1) {
            return new PopularTestPackagesHolder(LayoutInflater.from(context).inflate(R.layout.row_health_packages, parent, false));
        } else if (type == 2) {
            return new FeaturedLabsHolder(LayoutInflater.from(context).inflate(R.layout.row_product, parent, false));
        } else if (type == 4) {
            return new PopularTestPackagesHolder(LayoutInflater.from(context).inflate(R.layout.row_health_packages, parent, false));
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Document model = dataList.get(position);
        if (model != null) {
            if (type == 0) {
                binding.ivSmall.setVisibility(View.VISIBLE);
                binding.ivBig.setVisibility(View.GONE);
                binding.ivSmall.setImageResource(model.getImage());
                binding.tvCategory.setText(model.getText());
            } else if (type == 1) {
                rowHealthPackagesBinding.llrating.setVisibility(View.GONE);
                rowHealthPackagesBinding.tvBy.setVisibility(View.GONE);
                rowHealthPackagesBinding.tvCertifications.setVisibility(View.GONE);
                rowHealthPackagesBinding.tvProvidedBy.setVisibility(View.VISIBLE);
                rowHealthPackagesBinding.ivHead.setImageResource(R.drawable.ic_all_labs);


                rowHealthPackagesBinding.tvTitle.setText(model.getText());
                rowHealthPackagesBinding.tvAmount.setText(model.getText3());

            } else if (type == 2) {
                rowProductBinding.llrating.setVisibility(View.VISIBLE);
                rowProductBinding.ivCategory.setImageResource(model.getImage());
                rowProductBinding.tvCategory.setText(model.getText());
            } else if (type == 4){
                rowHealthPackagesBinding.tvTitle.setText(model.getText());
                rowHealthPackagesBinding.tvBy.setText(model.getText2());
                rowHealthPackagesBinding.tvAmount.setText(model.getText3());
            }
        }
    }

    @Override
    public int getItemViewType(int position) {


        if (type == 0) {
            return 0;
        } else if (type == 1) {
            return 1;
        } else if (type == 2) {
            return 2;
        } else if (type == 4) {
            return 4;
        } else {
            return -1;
        }
    }

    @Override
    public int getItemCount() {
        if (type == 0) {
            if (dataList.size() >= 4)
                return 4;
            return dataList.size();
        } else {
            return dataList.size();
        }
    }

    public class PopularTestPackagesHolder extends RecyclerView.ViewHolder {


        public PopularTestPackagesHolder(@NonNull View itemView) {
            super(itemView);
            rowHealthPackagesBinding = DataBindingUtil.bind(itemView);
        }
    }

    public class FeaturedLabsHolder extends RecyclerView.ViewHolder {

        public FeaturedLabsHolder(@NonNull View itemView) {
            super(itemView);
            rowProductBinding = DataBindingUtil.bind(itemView);
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }

}
