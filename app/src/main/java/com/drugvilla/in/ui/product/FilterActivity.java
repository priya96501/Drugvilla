package com.drugvilla.in.ui.product;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.drugvilla.in.R;
import com.drugvilla.in.adapter.product.sortFilter.FilterListingAdapter;
import com.drugvilla.in.adapter.product.sortFilter.FilterTypeAdapter;
import com.drugvilla.in.databinding.ActivityFilterBinding;
import com.drugvilla.in.listener.OnCheckSelectedListener;
import com.drugvilla.in.model.Document;
import com.drugvilla.in.model.product.filters.FilterCategoryData;
import com.drugvilla.in.model.product.filters.FilterData;
import com.drugvilla.in.model.product.filters.FilterResponse;
import com.drugvilla.in.model.product.sort.SortingResponse;
import com.drugvilla.in.service.Api;
import com.drugvilla.in.service.BaseCallback;
import com.drugvilla.in.service.BaseResponse;
import com.drugvilla.in.service.RequestController;
import com.drugvilla.in.utils.CommonUtils;
import com.drugvilla.in.utils.ProgressDialogUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class FilterActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityFilterBinding binding;
    Context context;
    private List<Document> listData = new ArrayList<>();
    private List<Document> listData2 = new ArrayList<>();
    private final String[] discountTitle = {"30% and above", "22% and above", "10% and above", "20% and above", "45% and above", "22% and above"};
    private final String[] brandName = {"Dabur", "Himalaya", "Ayur", "Godrej", "Nivea", "Bournvita", "Himalaya", "Ayur", "Godrej"};
    private final String[] categoryName = {"Women Care", "Men Care", "Baby Care", "Sr. citizen", "Ayurveda", "Healthcare", "Women Care", "Men Care", "Baby Care", "Sr. citizen", "Ayurveda", "Healthcare"};
    private final String[] productType = {"Oil", "Tablet", "Syrup", "Lotion", "Powder", "Capsule", "Oil", "Tablet"};

    private final String[] itemsBrand = {"1123", "34", "78", "898", "54", "10", "34", "78", "898", "54"};
    private final String[] itemsCategory = {"1123", "34", "78", "898", "54", "10", "1123", "34", "78", "898", "54", "10"};
    private final String[] itemsProductType = {"1123", "34", "78", "898", "54", "10", "8", "11"};
    private final String[] itemsDiscount = {"1123", "34", "78", "898", "54", "10"};
    private final String[] type = {"Categories", "Brand", "Discount", "Price", "Product Type"};
    private final String[] typeCount = {"", "", "2", "", "1"};


    private FilterTypeAdapter filterTypeAdapter;
    private FilterListingAdapter filterListingAdapter;
    private List<FilterCategoryData> filterCategoryDataList = new ArrayList<>();
    private List<FilterData> filterDataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_filter);
        context = FilterActivity.this;
        setToolbar();
        getFilterData();
        setAdapter();
    }


    private void setToolbar() {
        binding.menuBar.ivBack.setImageResource(R.drawable.ic_keyboard_backspace_black_24dp);
        binding.menuBar.tvTitle.setVisibility(View.VISIBLE);
        binding.menuBar.tvClearAll.setVisibility(View.VISIBLE);
        binding.menuBar.ivBack.setOnClickListener(this);
        binding.menuBar.tvClearAll.setOnClickListener(this);
        binding.menuBar.tvTitle.setText("Filters");
        binding.tvClose.setOnClickListener(this);
        binding.tvApply.setOnClickListener(this);

    }

   /* private List<Document> PrepareDataMessage() {

        List<Document> data = new ArrayList<>();
        for (int i = 0; i < type.length; i++) {

            Document document = new Document();

            document.setText(type[i]);
            document.setText2(typeCount[i]);

            data.add(document);

        }
        listData.addAll(data);

        return data;

    }
    private List<Document> PrepareDataMessage2(String[] title, String[] count) {

        List<Document> data = new ArrayList<>();
        for (int i = 0; i < title.length; i++) {

            Document document = new Document();
            document.setText2(count[i]);
            document.setText(title[i]);

            data.add(document);

        }
        listData2.addAll(data);

        return data;

    }
*/

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                finish();
                break;
            case R.id.tvClearAll:
                // TODO : remove all applied filters statically
                finish();
                break;
            case R.id.tvClose:
                finish();
                break;
            case R.id.tvApply:
                // TODO : apply all applied filters api
                break;
            default:
                break;
        }
    }

    private void getFilterData() {
        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                Api api = RequestController.createService(context, Api.class);
                api.getFiltersListing().enqueue(new BaseCallback<FilterResponse>(context) {
                    @Override
                    public void onSuccess(FilterResponse response) {
                        ProgressDialogUtils.dismiss();
                        if (response != null) {
                            if (response.getStatus() == 1) {
                                CommonUtils.showToastShort(context, response.getMessage());

                                if (response.getData() != null
                                        && !response.getData().isEmpty()) {
                                    filterCategoryDataList.clear();
                                    filterCategoryDataList.addAll(response.getData());
                                    setAdapter();
                                } else {
                                    filterCategoryDataList.clear();
                                }

                            } else {
                                CommonUtils.showToastShort(context, response.getMessage());
                            }
                        }
                    }

                    @Override
                    public void onFail(Call<FilterResponse> call, BaseResponse baseResponse) {
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

    private void setAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
        binding.rvFilter.setLayoutManager(layoutManager);
        binding.rvFilter.hasFixedSize();
        //  binding.rvFilter.setItemAnimator(new DefaultItemAnimator());
        filterTypeAdapter = new FilterTypeAdapter(context, filterCategoryDataList, new OnCheckSelectedListener() {
            @Override
            public void onClick(View view, int position, boolean selected) {

                if (filterCategoryDataList.get(position).isSelected()) {
                    filterCategoryDataList.get(position).setSelected(false);
                } else {
                    for (int i = 0; i < filterCategoryDataList.size(); i++) {
                        filterCategoryDataList.get(i).setSelected(false);

                    }
                    filterCategoryDataList.get(position).setSelected(true);

                }
                filterTypeAdapter.notifyDataSetChanged();
                filterDataList.clear();
                filterDataList.addAll(filterCategoryDataList.get(position).getFilterDataList());
                setAdapter2(position, filterDataList);
            }
        });
        binding.rvFilter.setAdapter(filterTypeAdapter);
    }

    private void setAdapter2(int position, List<FilterData> filterDataList) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
        binding.rvFilterType.setLayoutManager(layoutManager);
        binding.rvFilterType.hasFixedSize();
        binding.rvFilterType.setItemAnimator(new DefaultItemAnimator());
        /*listData2.clear();
        if (position == 0) {
            binding.rvFilterType.setVisibility(View.VISIBLE);
            binding.llPriceRange.setVisibility(View.GONE);
            listData2 = PrepareDataMessage2(categoryName, itemsCategory);
        } else if (position == 1) {
            binding.llPriceRange.setVisibility(View.GONE);
            binding.rvFilterType.setVisibility(View.VISIBLE);
            listData2 = PrepareDataMessage2(brandName, itemsBrand);
        } else if (position == 2) {
            binding.rvFilterType.setVisibility(View.VISIBLE);
            binding.llPriceRange.setVisibility(View.GONE);
            listData2 = PrepareDataMessage2(discountTitle, itemsDiscount);
        } else if (position == 3) {
            binding.llPriceRange.setVisibility(View.VISIBLE);
            binding.rvFilterType.setVisibility(View.GONE);
        } else {
            binding.llPriceRange.setVisibility(View.GONE);
            binding.rvFilterType.setVisibility(View.VISIBLE);
            listData2 = PrepareDataMessage2(productType, itemsProductType);
        }*/
        String filterType = filterCategoryDataList.get(position).getCategoryName();

        if (filterType.equalsIgnoreCase("Price")) {
            binding.llPriceRange.setVisibility(View.VISIBLE);
            binding.rvFilterType.setVisibility(View.GONE);
        } else {
            binding.llPriceRange.setVisibility(View.GONE);
            binding.rvFilterType.setVisibility(View.VISIBLE);
        }

        filterListingAdapter = new FilterListingAdapter(context, filterDataList);
        binding.rvFilterType.setAdapter(filterListingAdapter);
    }

}
