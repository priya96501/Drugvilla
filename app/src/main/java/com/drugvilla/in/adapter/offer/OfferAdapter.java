package com.drugvilla.in.adapter.offer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.drugvilla.in.R;
import com.drugvilla.in.databinding.RowOfferBinding;
import com.drugvilla.in.model.Document;
import com.drugvilla.in.model.offer.OfferListData;
import com.drugvilla.in.ui.labs.PackageDetailActivity;
import com.drugvilla.in.ui.offer.OfferDetail;
import com.drugvilla.in.utils.ActivityController;
import com.drugvilla.in.utils.CommonUtils;

import java.util.List;

public class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.MyViewHolder> {
    private Context context;
    private List<OfferListData> dataList;

    public OfferAdapter(Context context, List<OfferListData> list) {
        this.context = context;
        this.dataList = list;
    }

    @NonNull
    @Override
    public OfferAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_offer, parent, false);
        return new OfferAdapter.MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull OfferAdapter.MyViewHolder holder, int position) {
        final OfferListData model = dataList.get(position);
        String offerType = model.getOfferType();
        if (offerType.equalsIgnoreCase("TYPE_OTC")) {
            holder.binding.ivOffer.setImageResource(R.drawable.type_otc);
        } else if (offerType.equalsIgnoreCase("TYPE_NONOTC")) {
            holder.binding.ivOffer.setImageResource(R.drawable.type_nonotc);
        } else if (offerType.equalsIgnoreCase("TYPE_USER")) {
            holder.binding.ivOffer.setImageResource(R.drawable.default_user);
        } else if (offerType.equalsIgnoreCase("TYPE_OFFER")) {
            holder.binding.ivOffer.setImageResource(R.drawable.offer);
        }else
        {
            
        }
        holder.binding.tvTitle.setText(model.getOfferName());
        holder.binding.tvDescription.setText(model.getOfferDescription());
        holder.binding.tvExpiryDate.setText("Expires on " + model.getOfferExpiryDate());
        // CommonUtils.setImageRound(context, holder.binding.ivOffer, model.getOfferBanner());
        holder.binding.llRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, OfferDetail.class);
                intent.putExtra("Offer_ID", model.getOfferId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private RowOfferBinding binding;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
