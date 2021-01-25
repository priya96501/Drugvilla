package com.drugvilla.in.adapter.dummy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.drugvilla.in.R;
import com.drugvilla.in.adapter.blog.PopularArticleAdapter;
import com.drugvilla.in.adapter.labs.PopularTestPackageAdapter;
import com.drugvilla.in.adapter.product.BrandAdapter;
import com.drugvilla.in.adapter.product.HealthConcernAdapter;
import com.drugvilla.in.adapter.product.ProductAdapter;
import com.drugvilla.in.databinding.RowOuterBinding;
import com.drugvilla.in.listener.OnSelectedListener;
import com.drugvilla.in.listener.OnSelectedTypeListener;
import com.drugvilla.in.model.dashboard.HomeData;
import com.drugvilla.in.model.dashboard.HomeSubData;
import com.drugvilla.in.utils.AppConstant;

import java.util.ArrayList;
import java.util.List;

public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.MyViewHolder> {
    private Context context;
    int flag = 2;
    private List<HomeData> homeData;
    private LayoutInflater inflater;
    private LinearLayoutManager linearLayoutManager;
    private List<HomeSubData> homeSubData = new ArrayList<>();
    private OnSelectedTypeListener listener;
    private PopularArticleAdapter popularArticleAdapter;
    private HealthConcernAdapter healthConcernAdapter;
    private BrandAdapter brandAdapter;
    private PopularTestPackageAdapter popularTestPackageAdapter;


    public DashboardAdapter(Context context, List<HomeData> list, OnSelectedTypeListener listener) {
        this.context = context;
        this.homeData = list;
        this.listener = listener;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public DashboardAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_outer, parent, false);
        return new DashboardAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final DashboardAdapter.MyViewHolder holder, final int position) {
        final HomeData model = homeData.get(position);
        final String api_Type = model.getApiType();
        final String isDiscount = model.getIsDiscount();
        final String isRating = model.getIsRating();
        final String isViewAll = model.getIsViewAll();

        if (model.getSubData() != null && !model.getSubData().isEmpty()) {
            homeSubData.clear();
            homeSubData.addAll(model.getSubData());
        } else {
            homeSubData.clear();
        }

        holder.binding.tvHeading.setText(model.getHeading());
        holder.binding.tvDescription.setText(model.getDescription());
        if (isViewAll.equalsIgnoreCase("1")) {
            holder.binding.tvViewAll.setVisibility(View.VISIBLE);
        } else {
            holder.binding.tvViewAll.setVisibility(View.GONE);
        }

        holder.binding.tvViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v, position, api_Type);
            }
        });

        if (api_Type.equalsIgnoreCase("PRODUCT")) {
            if (isRating.equalsIgnoreCase("1") && isDiscount.equalsIgnoreCase("1")) {
                setProductAdapter(holder.binding.rvRoot, true, true, AppConstant.TYPE_PRODUCT);
            } else if (isRating.equalsIgnoreCase("1") && !isDiscount.equalsIgnoreCase("1")) {
                setProductAdapter(holder.binding.rvRoot, true, false, AppConstant.TYPE_PRODUCT);
            } else if (!isRating.equalsIgnoreCase("1") && isDiscount.equalsIgnoreCase("1")) {
                setProductAdapter(holder.binding.rvRoot, false, true, AppConstant.TYPE_PRODUCT);
            } else {
                setProductAdapter(holder.binding.rvRoot, false, false, AppConstant.TYPE_PRODUCT);
            }
        } else if (api_Type.equalsIgnoreCase("BRAND")) {
            setBrandAdapter(holder.binding.rvRoot);
        } else if (api_Type.equalsIgnoreCase("ARTICLE")) {
            setArticleAdapter(holder.binding.rvRoot);
        } else if (api_Type.equalsIgnoreCase("PACKAGE")) {
            setPackageAdapter(holder.binding.rvRoot);
        } else if (api_Type.equalsIgnoreCase("HEALTH_CONCERN")) {
            setHealthConcernAdapter(holder.binding.rvRoot);
        } else if (api_Type.equalsIgnoreCase("MEMBERSHIP")) {
            holder.binding.llRoot.setVisibility(View.GONE);
            holder.binding.llMembership.setVisibility(View.VISIBLE);
        } else if (api_Type.equalsIgnoreCase("DOCTOR")) {
            holder.binding.llRoot.setVisibility(View.GONE);
            holder.binding.llDoctorConsultation.setVisibility(View.VISIBLE);
        } else if (api_Type.equalsIgnoreCase("REWARDS")) {
            holder.binding.llRoot.setVisibility(View.GONE);
            holder.binding.llReferEarn.setVisibility(View.VISIBLE);
        } else {
            holder.binding.llRoot.setVisibility(View.GONE);
        }

    }

    private void setHealthConcernAdapter(RecyclerView rvHealthConcern) {
        linearLayoutManager = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
        rvHealthConcern.setLayoutManager(linearLayoutManager);
        rvHealthConcern.hasFixedSize();
        rvHealthConcern.setItemAnimator(new DefaultItemAnimator());
        healthConcernAdapter = new HealthConcernAdapter(context, homeSubData, AppConstant.HEALTH_CONCERN_SQUARE);
        rvHealthConcern.setAdapter(healthConcernAdapter);
    }

    private void setPackageAdapter(RecyclerView rvTopTest) {
        linearLayoutManager = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
        rvTopTest.setLayoutManager(linearLayoutManager);
        rvTopTest.hasFixedSize();
        rvTopTest.setItemAnimator(new DefaultItemAnimator());
        popularTestPackageAdapter = new PopularTestPackageAdapter(context, homeSubData);
        rvTopTest.setAdapter(popularTestPackageAdapter);

    }

    private void setArticleAdapter(RecyclerView rvArticles) {
        linearLayoutManager = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
        rvArticles.setLayoutManager(linearLayoutManager);
        rvArticles.hasFixedSize();
        rvArticles.setItemAnimator(new DefaultItemAnimator());
        popularArticleAdapter = new PopularArticleAdapter(context, homeSubData);
        rvArticles.setAdapter(popularArticleAdapter);
    }

    private void setBrandAdapter(RecyclerView rvBrands) {
        linearLayoutManager = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
        rvBrands.setLayoutManager(linearLayoutManager);
        rvBrands.hasFixedSize();
        rvBrands.setItemAnimator(new DefaultItemAnimator());
        brandAdapter = new BrandAdapter(context, homeSubData);
        rvBrands.setAdapter(brandAdapter);
    }

    private void setProductAdapter(RecyclerView rvProduct, boolean showRating, boolean showDiscount, String type) {
        linearLayoutManager = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
        rvProduct.setLayoutManager(linearLayoutManager);
        rvProduct.hasFixedSize();
        ProductAdapter productAdapter = new ProductAdapter(context, homeSubData, type);
        productAdapter.showOffer(showDiscount);
        productAdapter.showRating(showRating);
        rvProduct.setAdapter(productAdapter);
    }

    @Override
    public int getItemCount() {
        return homeData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private RowOuterBinding binding;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
