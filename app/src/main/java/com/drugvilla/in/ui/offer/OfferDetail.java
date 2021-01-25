package com.drugvilla.in.ui.offer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.databinding.DataBindingUtil;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;

import com.drugvilla.in.R;
import com.drugvilla.in.databinding.ActivityOfferDetailBinding;
import com.drugvilla.in.model.offer.OfferBaseResponse;
import com.drugvilla.in.model.offer.OfferData;
import com.drugvilla.in.model.offer.OfferListResponse;
import com.drugvilla.in.service.Api;
import com.drugvilla.in.service.RequestController;
import com.drugvilla.in.utils.AppConstant;
import com.drugvilla.in.utils.CommonUtils;
import com.drugvilla.in.utils.ProgressDialogUtils;
import com.drugvilla.in.utils.SharedPref;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OfferDetail extends AppCompatActivity implements View.OnClickListener {
    private ActivityOfferDetailBinding binding;
    private String Offer_Id = "";
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_offer_detail);
        context = OfferDetail.this;
        getData();
        setToolbar();
        getOfferDetail();
        //CommonUtils.setImageRound(context, binding.ivOffer, R.drawable.banner2);
    }

    private void getOfferDetail() {
        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                Api api = RequestController.createService(context, Api.class);
                api.getOfferDetail(SharedPref.getStringPreferences(context, AppConstant.USER_ID),Offer_Id).
                        enqueue(new Callback<OfferBaseResponse>() {
                            @Override
                            public void onResponse(@NonNull Call<OfferBaseResponse> call, @NonNull Response<OfferBaseResponse> response) {
                                ProgressDialogUtils.dismiss();
                                if (response.body() != null) {
                                    if (response.body().getStatus() == 1) {
                                        CommonUtils.showToastShort(context, response.body().getMessage());
                                        if (response.body().getOfferData() != null) {
                                            CommonUtils.showToastShort(context, response.body().getMessage());
                                            OfferData offerData = response.body().getOfferData();
                                            binding.tvTitle.setText(offerData.getOfferName());
                                            binding.tvExpiryDate.setText(offerData.getOfferExpiryDate());
                                            binding.tvDescription.setText(offerData.getOfferDescription());
                                            binding.tvCode.setText(offerData.getOfferCode());
                                            binding.tvEligibility.setText(offerData.getOfferEligibility());
                                            binding.tvTermsConditions.setText(offerData.getOfferTermsConditions());
                                            if(offerData.getOfferBanner()!=null)
                                            {
                                                Picasso.with(context).load(offerData.getOfferBanner()).placeholder(R.drawable.gallery).
                                                        error(R.drawable.gallery).into(binding.ivOffer);
                                            }

                                        } else {
                                            CommonUtils.showToastShort(context, response.body().getMessage());

                                        }

                                    } else {

                                        CommonUtils.showToastShort(context, response.body().getMessage());
                                    }
                                } else {
                                    CommonUtils.showToastShort(context, getResources().getString(R.string.no_response));
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<OfferBaseResponse> call, @NonNull Throwable t) {
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

    private void getData() {

        Intent intent = getIntent();
        if(intent!=null)
        {
            if(intent.getStringExtra("Offer_ID")!=null && !intent.getStringExtra("Offer_ID").isEmpty())
            {
                Offer_Id = intent.getStringExtra("Offer_ID");
            }
        }
    }

    private void setToolbar() {
        binding.menubar.tvTitle.setVisibility(View.VISIBLE);
        binding.menubar.tvTitle.setText(R.string.offer_details);
        binding.menubar.ivBack.setImageResource(R.drawable.ic_keyboard_backspace_black_24dp);
        binding.menubar.ivBack.setOnClickListener(this);
        binding.CopyCode.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                finish();
                break;
            case R.id.CopyCode:
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("Source Text", binding.tvCode.getText().toString());
                Objects.requireNonNull(clipboardManager).setPrimaryClip(clipData);
                ClipData clipData2 = clipboardManager.getPrimaryClip();
                break;
            default:
                break;
        }
    }
}
