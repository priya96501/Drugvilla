package com.drugvilla.in.adapter.dummy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.drugvilla.in.R;
import com.drugvilla.in.adapter.labs.PopularHealthPackageAdapter;
import com.drugvilla.in.adapter.product.AllCategoryAdapter;
import com.drugvilla.in.adapter.product.CategoryAdapter;
import com.drugvilla.in.adapter.product.ProductAdapter;
import com.drugvilla.in.databinding.RowOuterBinding;
import com.drugvilla.in.listener.OnSelectedListener;
import com.drugvilla.in.model.Document;
import com.drugvilla.in.utils.AppConstant;

import java.util.ArrayList;
import java.util.List;

public class OuterAdapter extends RecyclerView.Adapter<OuterAdapter.MyViewHolder> {
    private Context context;
    int flag = 2;
    private LayoutInflater inflater;
    private List<Document> dataList;
    private List<Document> list1 = new ArrayList<>();
    private List<Document> list2 = new ArrayList<>();
    private List<Document> list3 = new ArrayList<>();
    private List<Document> list4 = new ArrayList<>();

    private LinearLayoutManager layoutManager;
    GridLayoutManager gridLayoutManager;
    private MultiViewTypeAdapter adapter;
    // shop by categories
    private final int[] category = {R.drawable.cat1, R.drawable.cat2, R.drawable.cat3, R.drawable.cat4, R.drawable.lab_cat5, R.drawable.lab_cat6,
            R.drawable.lab_cat7, R.drawable.lab_cat8, R.drawable.lab_cat9, R.drawable.lab_cat10, R.drawable.cat8, R.drawable.lab_cat12};
    private final String[] categoryName = {"Women Health", "Men \nHealth", "Full Body Checkup", "Sr. citizen Checkup", "Diabeties", "Heart", "Pregnancy", "Kidney", "Skin",
            "Bone", "Sexual Wellness", "thyroid"};
    // featured labs
    private final int[] labImages = {R.drawable.lab1, R.drawable.lab2, R.drawable.lab3, R.drawable.lab4, R.drawable.lab1};
    private final String[] labName = {"Delhi Path Lab", "Dr. A. Lalchandani's Pathology Laboratories", "Sure path labs pvt ltd", "Thyrocare Laboratories Ltd.", "SRL Limited"};
    // health packages
    private final String[] packageName = {"Advance Health Care", "Healthy Heart", "Complete Care", "Basic Health Care", "Diabetes Care Package"};
    private final String[] packageBy = {"By Delhi Path Lab", "By Delhi Path Lab", "By Sure path labs pvt ltd", "By Thyrocare Laboratories Ltd.", "By Delhi Path Lab"};
    private final String[] packagePrice = {"1600", "400", "750", "1100", "500"};
    // health packages
    private final String[] testName = {"ACE (Absolute Eosinophil Count)", "Thyroid Profile", "Liver Function Test", "Beta Hcg/Hcg Beta Subunit", "Liver Function Test"};
    public OuterAdapter(Context context, List<Document> list) {
        this.context = context;
        this.dataList = list;
        this.inflater = LayoutInflater.from(context);
    }
    @NonNull
    @Override
    public OuterAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_outer, parent, false);
        return new OuterAdapter.MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull final OuterAdapter.MyViewHolder holder, int position) {
        final Document model = dataList.get(position);
        holder.binding.tvHeading.setText(model.getText2());
        holder.binding.tvDescription.setText(model.getText3());
        list1.clear();
        list2.clear();
        list3.clear();
        list4.clear();
        list1 = PrepareDataMessage1();
        list2 = PrepareDataMessage2();
        list3 = PrepareDataMessage3();
        list4 = PrepareDataMessage4();
        if (model.getType() == 0) {
            holder.binding.tvViewAll.setVisibility(View.GONE);
            if (list4.size() > 4) {
                holder.binding.llViewMore.setVisibility(View.VISIBLE);
            }
            gridLayoutManager = new GridLayoutManager(context, 4);
            holder.binding.rvRoot.setLayoutManager(gridLayoutManager);
            holder.binding.rvRoot.hasFixedSize();
            adapter = new MultiViewTypeAdapter(context, list4, 0);
            //   CategoryAdapter categoryAdapter = new CategoryAdapter(context, list4, AppConstant.TYPE_LABS_CATEGORY);
            holder.binding.rvRoot.setAdapter(adapter);
            holder.binding.rvRoot.setHasFixedSize(true);
        } else if (model.getType() == 1) {
/*

            PopularHealthPackageAdapter   packageAdapter = new PopularHealthPackageAdapter(context, list1, new OnSelectedListener() {
                @Override
                public void onClick(View view, int position) {


                   // binding.llBottomView.setVisibility(View.VISIBLE);

                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    layoutParams.setMargins(0, 0, 0, 150);
                  //  binding.llslidders.setLayoutParams(layoutParams);
                }
            }, AppConstant.TYPE_ALL_LAB_TESTS);


*/
            layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
            holder.binding.rvRoot.setLayoutManager(layoutManager);
            adapter = new MultiViewTypeAdapter(context, list1, 1);
            holder.binding.rvRoot.setAdapter(adapter);
            holder.binding.rvRoot.setHasFixedSize(true);
        } else if (model.getType() == 2) {
            layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
            holder.binding.rvRoot.setLayoutManager(layoutManager);
          /*  ProductAdapter productAdapter = new ProductAdapter(context, list2, AppConstant.TYPE_ALL_LABS);
            productAdapter.showRating(true);
*/
            adapter = new MultiViewTypeAdapter(context, list2, 2);
            holder.binding.rvRoot.setAdapter(adapter);
            holder.binding.rvRoot.setHasFixedSize(true);
        }
        else if(model.getType()==3)
        {
            holder.binding.llStaticView.setVisibility(View.VISIBLE);
            holder.binding.llRoot.setVisibility(View.GONE);
        }

        else if(model.getType()==4){
            layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
            holder.binding.rvRoot.setLayoutManager(layoutManager);
           /* PopularHealthPackageAdapter packageAdapter = new PopularHealthPackageAdapter(context, list3, new OnSelectedListener() {
                @Override
                public void onClick(View view, int position) {

                    //binding.llBottomView.setVisibility(View.VISIBLE);

                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    layoutParams.setMargins(0, 0, 0, 150);
                    //binding.llslidders.setLayoutParams(layoutParams);
                }
            }, AppConstant.TYPE_ALL_HEALTH_PACKAGE);
*/
            adapter = new MultiViewTypeAdapter(context, list3, 4);
            holder.binding.rvRoot.setAdapter(adapter);
            holder.binding.rvRoot.setHasFixedSize(true);
        }


        holder.binding.llViewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag == 1) {
                    list4.clear();
                    list4 = PrepareDataMessage4();
                    if (list4.size() > 4) {
                        holder.binding.llViewMore.setVisibility(View.VISIBLE);
                    }
                    gridLayoutManager = new GridLayoutManager(context, 4);
                    holder.binding.rvRoot.setLayoutManager(gridLayoutManager);
                   // CategoryAdapter categoryAdapter = new CategoryAdapter(context, list4, AppConstant.TYPE_LABS_CATEGORY);
                   adapter = new MultiViewTypeAdapter(context, list4, model.getType());
                    holder.binding.rvRoot.setAdapter(adapter);
                    holder.binding.rvRoot.setHasFixedSize(true);
                    holder.binding.tvViewmore.setText(context.getResources().getString(R.string.view_more));
                    holder.binding.ivView.setImageDrawable(context.getResources().getDrawable(R.drawable.down_arrow));
                    flag = 2;
                } else if (flag == 2) {
                    list4.clear();
                    list4 = PrepareDataMessage4();
                    gridLayoutManager = new GridLayoutManager(context, 4, RecyclerView.VERTICAL, false);
                    holder.binding.rvRoot.setLayoutManager(gridLayoutManager);
                    holder.binding.rvRoot.hasFixedSize();
                    adapter = new MultiViewTypeAdapter(context, list4, model.getType());
                   // AllCategoryAdapter allCategoryAdapter = new AllCategoryAdapter(context, list4, AppConstant.TYPE_LABS_CATEGORY);
                    holder.binding.rvRoot.setAdapter(adapter);
                    holder.binding.tvViewmore.setText(context.getResources().getString(R.string.viewless));
                    holder.binding.ivView.setImageDrawable(context.getResources().getDrawable(R.drawable.up_arrow));
                    flag = 1;
                }
            }
        });

    }

    private List<Document> PrepareDataMessage4() {
        List<Document> data = new ArrayList<>();
        for (int i = 0; i < category.length; i++) {
            Document document = new Document();
            document.setImage(category[i]);
            document.setText(categoryName[i]);
            data.add(document);
        }
        list4.addAll(data);
        return data;

    }

    private List<Document> PrepareDataMessage1() {
        List<Document> data3 = new ArrayList<>();
        for (int i = 0; i < testName.length; i++) {
            Document document = new Document();
            document.setText3(packagePrice[i]);
            document.setText(testName[i]);
            data3.add(document);
        }
        list1.addAll(data3);
        return data3;
    }

    private List<Document> PrepareDataMessage2() {
        List<Document> data4 = new ArrayList<>();
        for (int i = 0; i < labImages.length; i++) {
            Document document = new Document();
            document.setImage(labImages[i]);
            document.setText(labName[i]);
            data4.add(document);
        }
        list2.addAll(data4);
        return data4;
    }

    private List<Document> PrepareDataMessage3() {
        List<Document> data3 = new ArrayList<>();
        for (int i = 0; i < packageName.length; i++) {
            Document document = new Document();
            document.setText(packageName[i]);
            document.setText2(packageBy[i]);
            document.setText3(packagePrice[i]);
            data3.add(document);
        }
        list3.addAll(data3);
        return data3;
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return dataList.get(position).getType();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private RowOuterBinding binding;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
