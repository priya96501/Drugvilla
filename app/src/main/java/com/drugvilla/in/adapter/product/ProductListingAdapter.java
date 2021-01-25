package com.drugvilla.in.adapter.product;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.drugvilla.in.R;
import com.drugvilla.in.databinding.RowProductListingBinding;
import com.drugvilla.in.listener.OnSelectedListener;
import com.drugvilla.in.model.Document;
import com.drugvilla.in.model.product.productdetail.ProductData;
import com.drugvilla.in.service.Api;
import com.drugvilla.in.service.BaseCallback;
import com.drugvilla.in.service.BaseResponse;
import com.drugvilla.in.service.RequestController;
import com.drugvilla.in.ui.labs.LabDetail;
import com.drugvilla.in.ui.product.ProductDetail;
import com.drugvilla.in.utils.ActivityController;
import com.drugvilla.in.utils.AppConstant;
import com.drugvilla.in.utils.CommonUtils;
import com.drugvilla.in.utils.ProgressDialogUtils;
import com.drugvilla.in.utils.SharedPref;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;

public class ProductListingAdapter extends RecyclerView.Adapter<ProductListingAdapter.MyViewHolder> {
    private Context context;
    private Activity activity;
  //  private List<Document> dataList;
   private List<ProductData> dataList;
    private String type;
    private LayoutInflater inflater;
    private boolean wishlisted = false;
    private OnSelectedListener listener;
    private int size;

    public ProductListingAdapter(Activity activity /*List<Document> list*/,List<ProductData> list, OnSelectedListener listener, String type) {
        this.activity = activity;
        this.dataList = list;
        this.listener = listener;
        this.inflater = LayoutInflater.from(activity);
        this.type = type;
        size = CommonUtils.getWidth(activity) / 2;
    }


    @NonNull
    @Override
    public ProductListingAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_product_listing, parent, false);
        return new ProductListingAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ProductListingAdapter.MyViewHolder holder, final int position) {
       // final Document model = dataList.get(position);
        final ProductData model = dataList.get(position);
        if (type != null && !type.isEmpty()) {
            if (type.equalsIgnoreCase(AppConstant.FROM_WISHLIST)) {
                holder.binding.btnAdd.setVisibility(View.GONE);
                holder.binding.ivCross.setVisibility(View.VISIBLE);
                holder.binding.ivWishlist.setVisibility(View.GONE);
                holder.binding.llMoveToCart.setVisibility(View.VISIBLE);
            }
        }
      /*  holder.binding.ivProduct.setImageResource(model.getImage());
        holder.binding.tvProductName.setText(model.getText());
        holder.binding.tvAmount.setText(model.getText2());
        holder.binding.tvOff.setText(model.getText3());
        holder.binding.tvMRP.setText(model.getText4());*/

        holder.binding.tvProductName.setText(model.getName());
        holder.binding.tvAmount.setText(model.getAmount());
        holder.binding.tvOff.setText(model.getDiscount());
        holder.binding.tvMRP.setText(model.getMrp());
        if (model.getImage() != null && !model.getImage().isEmpty()) {
            Picasso.with(context).load(model.getImage())
                    .error(R.drawable.type_otc)
                    .placeholder(R.drawable.type_otc)
                    .into(holder.binding.ivProduct);
        }
        holder.binding.tvMRP.setPaintFlags(holder.binding.tvMRP.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


        holder.binding.llRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                intent = new Intent(context, ProductDetail.class);
                intent.putExtra("PRODUCT_ID", dataList.get(position).getProductId());
                context.startActivity(intent);
               // ActivityController.startActivity(activity, ProductDetail.class);
            }
        });
        holder.binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(view, position);
            }
        });
        holder.binding.btnMoveToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(view, position);
            }
        });
        holder.binding.ivCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(view, position);
            }
        });
        holder.binding.ivWishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!wishlisted) {
                    addToWishlistApi(position);
                    holder.binding.ivWishlist.setImageResource(R.drawable.ic_red_bookmark);
                } else {
                    removeFromWishlist(position);
                    holder.binding.ivWishlist.setImageResource(R.drawable.ic_gray_bookmark);
                }
            }
        });
    }

    private void removeFromWishlist(int position) {
        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                Api api = RequestController.createService(context, Api.class);
                api.removeFromWishlist(SharedPref.getStringPreferences(context, AppConstant.USER_ID),dataList.get(position).getId()/*,
                        dataList.get(position).getText()*/)
                        .enqueue(new BaseCallback<BaseResponse>(context) {
                            @Override
                            public void onSuccess(BaseResponse response) {
                                ProgressDialogUtils.dismiss();
                                if (response != null) {
                                    if (response.getStatus() == 1) {
                                        CommonUtils.showToastShort(context, response.getMessage());
                                        wishlisted = false;
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

    private void addToWishlistApi(int position) {
        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                Api api = RequestController.createService(context, Api.class);
                api.addToWishlist(SharedPref.getStringPreferences(context, AppConstant.USER_ID),dataList.get(position).getProductId()
                        /*, dataList.get(position).getText()*/)
                        .enqueue(new BaseCallback<BaseResponse>(context) {
                            @Override
                            public void onSuccess(BaseResponse response) {
                                ProgressDialogUtils.dismiss();
                                if (response != null) {
                                    if (response.getStatus() == 1) {
                                        CommonUtils.showToastShort(context, response.getMessage());
                                        wishlisted = true;
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

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private RowProductListingBinding binding;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
