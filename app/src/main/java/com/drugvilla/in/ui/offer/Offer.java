package com.drugvilla.in.ui.offer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.drugvilla.in.R;
import com.drugvilla.in.adapter.offer.OfferAdapter;
import com.drugvilla.in.databinding.ActivityOfferBinding;
import com.drugvilla.in.model.Document;
import com.drugvilla.in.model.offer.OfferListData;
import com.drugvilla.in.model.offer.OfferListResponse;
import com.drugvilla.in.model.reminder.ReminderBaseResponse;
import com.drugvilla.in.service.Api;
import com.drugvilla.in.service.RequestController;
import com.drugvilla.in.utils.AppConstant;
import com.drugvilla.in.utils.CommonUtils;
import com.drugvilla.in.utils.ProgressDialogUtils;
import com.drugvilla.in.utils.SharedPref;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Offer extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    private ActivityOfferBinding binding;
    private List<Document> listData = new ArrayList<>();
    private List<OfferListData> offerListData = new ArrayList<>();
    private OfferAdapter offerAdapter;
    private final String[] name = {"New User Offer", "OTC/health Product Offer", "Labs Offer", "Medicine Offer", "Cashback Offer"};
    private final String[] desc = {"Flat 25% off on orders above Rs.800. Extra 50 Drugvilla coins on Sign Up.",
            "Get Cashback between Rs.75 to Rs.200 on minimum purchase of Rs.1000.",
            "Upto 30% off on all Aaryogyam Profines/Packages.",
            "Get 5% extra discount on every Medicine purchase after 5 successfull orders.",
            "10% cashback on miminum purchase of Rs.500 on any OTC product."};
    private final int[] image = {R.drawable.default_user, R.drawable.type_otc, R.drawable.type_injection, R.drawable.type_nonotc, R.drawable.offer};
    private final int[] offer = {R.drawable.b4, R.drawable.b5, R.drawable.b3, R.drawable.b2, R.drawable.banner2};
    private final String[] date = {"22 Dec 2019", "25 Dec 2019", "29 Dec 2019", "22 Dec 2019", "25 Dec 2019"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_offer);
        context = Offer.this;
        setToolbar();
        binding.swipeRefreshingLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAllOfferApi();
            }
        });
    }

    @Override
    protected void onResume() {
        getAllOfferApi();
        setAdapter();
        super.onResume();
    }

    private void getAllOfferApi() {
        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                Api api = RequestController.createService(context, Api.class);
                api.getOffersList(SharedPref.getStringPreferences(context, AppConstant.USER_ID)).
                        enqueue(new Callback<OfferListResponse>() {
                            @Override
                            public void onResponse(@NonNull Call<OfferListResponse> call, @NonNull Response<OfferListResponse> response) {
                                binding.swipeRefreshingLayout.setRefreshing(false);
                                ProgressDialogUtils.dismiss();
                                if (response.body() != null) {
                                    if (response.body().getStatus() == 1) {
                                        CommonUtils.showToastShort(context, response.body().getMessage());
                                        if (response.body().getOfferListData() != null&& !response.body().getOfferListData().isEmpty()) {
                                            setEmptyLayout(false);
                                            offerListData.clear();
                                            offerListData.addAll(response.body().getOfferListData());
                                            offerAdapter.notifyDataSetChanged();
                                        } else {
                                            setEmptyLayout(true);
                                            offerListData.clear();
                                            offerAdapter.notifyDataSetChanged();
                                        }

                                    } else {
                                        setEmptyLayout(true);
                                        offerListData.clear();
                                        offerAdapter.notifyDataSetChanged();
                                        CommonUtils.showToastShort(context, response.body().getMessage());
                                    }
                                } else {
                                    CommonUtils.showToastShort(context, getResources().getString(R.string.no_response));
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<OfferListResponse> call, @NonNull Throwable t) {
                                binding.swipeRefreshingLayout.setRefreshing(false);
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

    // visible when user have no reminders set
    private void setEmptyLayout(boolean value) {
        if (value) {
            binding.emptyLayout.llRoot.setVisibility(View.VISIBLE);
            binding.emptyLayout.ivImage.setImageResource(R.drawable.nonotificatin);
            binding.emptyLayout.btnSubmit.setVisibility(View.GONE);
            binding.emptyLayout.tvHeading.setText(getResources().getString(R.string.no_offers_available));
            binding.emptyLayout.tvSubHeading.setVisibility(View.GONE);
        } else {
            binding.emptyLayout.llRoot.setVisibility(View.GONE);
            binding.llData.setVisibility(View.VISIBLE);
        }
    }

    private void setToolbar() {
        binding.menubar.tvTitle.setVisibility(View.VISIBLE);
        binding.menubar.tvTitle.setText(R.string.all_offers);
        binding.menubar.ivBack.setImageResource(R.drawable.ic_keyboard_backspace_black_24dp);
        binding.menubar.ivBack.setOnClickListener(this);
    }

    private void setAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
        binding.rvOffer.setLayoutManager(layoutManager);
        binding.rvOffer.hasFixedSize();
        binding.rvOffer.setItemAnimator(new DefaultItemAnimator());
        offerAdapter = new OfferAdapter(context, offerListData);
        binding.rvOffer.setAdapter(offerAdapter);
    }



    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.ivBack) {
            finish();
        }
    }
}
