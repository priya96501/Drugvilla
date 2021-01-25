package com.drugvilla.in.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.drugvilla.in.R;
import com.drugvilla.in.databinding.RowSearchViewBinding;
import com.drugvilla.in.model.Document;
import com.drugvilla.in.model.searching.SearchingResultData;
import com.drugvilla.in.ui.doctors.DoctorListing;
import com.drugvilla.in.ui.doctors.DoctorProfile;
import com.drugvilla.in.ui.labs.TestDetailActivity;
import com.drugvilla.in.ui.product.MedicineDetail;
import com.drugvilla.in.ui.product.ProductDetail;
import com.drugvilla.in.ui.product.ProductsListing;
import com.drugvilla.in.utils.ActivityController;
import com.drugvilla.in.utils.AppConstant;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder> {
    private Context context;
    private List<SearchingResultData> dataList;
    private String type;
    private boolean value = false;

    public SearchAdapter(Context context, List<SearchingResultData> list, String type) {
        this.context = context;
        this.dataList = list;
        this.type = type;
    }

    public void showComposition(boolean value) {
        this.value = value;
    }

    @NonNull
    @Override
    public SearchAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_search_view, parent, false);
        return new SearchAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SearchAdapter.MyViewHolder holder, final int position) {
        final SearchingResultData model = dataList.get(position);
        if (value) {
            holder.binding.tvComposition.setVisibility(View.VISIBLE);
            holder.binding.tvComposition.setText(model.getSubtitle());
        }
        holder.binding.tvTitle.setText(model.getTitle());

        final String item_type = model.getItem_type();
        if (item_type.equalsIgnoreCase("by_name")) {
            holder.binding.rs.setVisibility(View.GONE);
            holder.binding.tvAmountLocation.setText(model.getLocation());
            holder.binding.ivType.setImageResource(R.drawable.finddoctor);
        } else if (item_type.equalsIgnoreCase("by_category")) {
            holder.binding.rs.setVisibility(View.GONE);
            holder.binding.tvAmountLocation.setText(model.getLocation());
            holder.binding.ivType.setImageResource(R.drawable.finddoctor);
        } else if (item_type.equalsIgnoreCase("product_by_name")) {
            holder.binding.ivType.setImageResource(R.drawable.ic_otc);
            holder.binding.tvAmountLocation.setText(model.getAmount());
        } else if (item_type.equalsIgnoreCase("product_by_category")) {
            holder.binding.ivType.setImageResource(R.drawable.ic_otc);
            holder.binding.tvAmountLocation.setText(model.getAmount());
        } else {
            // TODO : lab section
            holder.binding.ivType.setImageResource(R.drawable.labs);
            holder.binding.tvAmountLocation.setText(model.getAmount());
        }


       /* if (type.equalsIgnoreCase(AppConstant.TYPE_LABS)) {
            holder.binding.ivType.setImageResource(R.drawable.labs);
            holder.binding.tvAmountLocation.setText(model.getAmount());
        } else if (type.equalsIgnoreCase(AppConstant.TYPE_PRODUCT)) {
            holder.binding.ivType.setImageResource(R.drawable.ic_otc);
            holder.binding.tvAmountLocation.setText(model.getAmount());
        } else if (type.equalsIgnoreCase(AppConstant.DOCTORS)) {
            holder.binding.rs.setVisibility(View.GONE);
            holder.binding.tvAmountLocation.setText(model.getLocation());
            holder.binding.ivType.setImageResource(R.drawable.finddoctor);
        } else {
            holder.binding.tvAmountLocation.setText(model.getAmount());
            holder.binding.ivType.setImageResource(R.drawable.pills);
        }
*/

        holder.binding.llRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (item_type.equalsIgnoreCase("by_name")) {
                    Intent intent = new Intent(context, DoctorProfile.class);
                    intent.putExtra("DOCTOR_ID", dataList.get(position).getItem_id());
                    context.startActivity(intent);

                }
                else if (item_type.equalsIgnoreCase("by_category")) {
                    // TODO : pass id
                    ActivityController.startActivity(context, DoctorListing.class);
                }
                else if (item_type.equalsIgnoreCase("product_by_name")) {
                    Intent intent;
                    if (type.equalsIgnoreCase(AppConstant.MEDICINES)) {
                        intent = new Intent(context, MedicineDetail.class);
                    } else {
                        intent = new Intent(context, ProductDetail.class);
                    }
                    intent.putExtra("PRODUCT_ID", dataList.get(position).getItem_id());
                    context.startActivity(intent);

                }
                else if (item_type.equalsIgnoreCase("product_by_category")) {
                    // TODO : pass id
                    ActivityController.startActivity(context, ProductsListing.class);
                }
                else {
                    // TODO : lab section
                    ActivityController.startActivity(context, TestDetailActivity.class);
                }


                /*if (type.equalsIgnoreCase(AppConstant.TYPE_LABS)) {
                    ActivityController.startActivity(context, TestDetailActivity.class);
                } else if (type.equalsIgnoreCase(AppConstant.MEDICINES)) {
                    ActivityController.startActivity(context, MedicineDetail.class);
                } else if (type.equalsIgnoreCase(AppConstant.DOCTORS)) {
                    ActivityController.startActivity(context, DoctorProfile.class);
                }
                // case for products
                else {
                    ActivityController.startActivity(context, ProductDetail.class);
                }*/
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private RowSearchViewBinding binding;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
