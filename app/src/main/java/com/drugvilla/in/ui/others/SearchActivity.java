package com.drugvilla.in.ui.others;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.drugvilla.in.R;
import com.drugvilla.in.adapter.SearchAdapter;
import com.drugvilla.in.databinding.ActivitySearchBinding;
import com.drugvilla.in.model.Document;
import com.drugvilla.in.model.searching.SearchingResponse;
import com.drugvilla.in.model.searching.SearchingResultData;
import com.drugvilla.in.service.Api;
import com.drugvilla.in.service.BaseCallback;
import com.drugvilla.in.service.BaseResponse;
import com.drugvilla.in.service.RequestController;
import com.drugvilla.in.utils.AppConstant;
import com.drugvilla.in.utils.CommonUtils;
import com.drugvilla.in.utils.ProgressDialogUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {
    private Context context;
    private String from = " ";
    private ActivitySearchBinding binding;
    private List<SearchingResultData> searchingResultDataList = new ArrayList<>();
    private SearchAdapter searchAdapter;


    private List<Document> data = new ArrayList<>();

    // set search data
    private final String[] titleMedicines = {"Neurobione Forte", "Losar 50", "Gabaneurone", "Neurobione Forte", "Amtas - 5", "Neurobione Plus Injection",
            "Pantop 40", "Ebexid", "Femarelle", "Neurobione Plus Tablets"};


    private final String[] titleDoctor = {"Dr. Priyanka Rana", "Dr. Pankaj Kumar", "Dr. Tarun Chaturvedi", "Dr. Aprajita", "Dr. Shubha Rani"};
    private final String[] categoryDoctor = {"Neurology", "Physician", "Urology", "Cardiology", "Dental Surgery"};
    private final String[] locationDoctor = {"Delhi", "Delhi", "Delhi", "Delhi", "Delhi"};

    private final String[] titleLabs = {"TSH", "KFT", "Lipid Profile", "Urine R/M", "Dengue Antigen NS1, IgG & IgM", "MRI Brain",
            "PET CT Scan (Whole Body)", "USG Whole Abdomen", "X - Ray Chest PA View", "MRI Lumbosacral Spine"};

    private final String[] titleProduct = {"Fairness Creams", "Himalaya Shampoo", "Health Drinks", "Dabur Chyanwanprash 1kg (Get Dabur Honey 50g Free)", "Fairness Creams", "Himalaya Shampoo",
            "Dabur", "Himalaya", "Ayur", "Godrej"};
    private final String[] composition = {"Strip of 30 tablets", "Strip of 10 capsules", "Strip of 10 capsules", "Strip of 30 tablets",
            "Strip of 30 tablets", "Vial of 6 injections",
            "Dabur", "Himalaya", "Ayur", "Godrej"};
    private final String[] amount = {"100", "200", "650", "1000", "300", "1340", "340", "600", "890", "360"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search);
        context = SearchActivity.this;
        getData();
        setToolbar();
        binding.etSearchBar.addTextChangedListener(this);
        //setEmptyLayout(true);
    }

    private void getData() {
        Bundle bundle = new Bundle();
        bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.getString(AppConstant.FROM) != null && !Objects.requireNonNull(bundle.getString(AppConstant.FROM)).isEmpty()) {
                from = bundle.getString(AppConstant.FROM);
            }
        }
    }

    private void setEmptyLayout(boolean value) {
        if (value) {
            binding.emptyLayout.llRoot.setVisibility(View.VISIBLE);
            if (from.equalsIgnoreCase(AppConstant.TYPE_LABS)) {
                binding.emptyLayout.ivImage.setImageResource(R.drawable.search_labs);
            } else if (from.equalsIgnoreCase(AppConstant.DOCTORS)) {
                binding.emptyLayout.ivImage.setImageResource(R.drawable.search_doctors);
            } else {
                binding.emptyLayout.ivImage.setImageResource(R.drawable.search);
            }
            binding.emptyLayout.tvHeading.setVisibility(View.GONE);
            binding.emptyLayout.btnSubmit.setVisibility(View.GONE);
        } else {
            binding.emptyLayout.llRoot.setVisibility(View.GONE);
            binding.rvSearchItems.setVisibility(View.VISIBLE);

            if (from.equalsIgnoreCase(AppConstant.MEDICINES)) {
                getProductSearchApi();
                setSearchAdapter(AppConstant.MEDICINES);
            } else if (from.equalsIgnoreCase(AppConstant.DOCTORS)) {
                getDoctorSearchApi();
                setSearchAdapter(AppConstant.DOCTORS);
            } else if (from.equalsIgnoreCase(AppConstant.TYPE_PRODUCT)) {
                getProductSearchApi();
                setSearchAdapter(AppConstant.TYPE_PRODUCT);
            } else {
                getLabSearchApi();
                setSearchAdapter(AppConstant.TYPE_LABS);
            }


        }
    }

    private void getLabSearchApi() {
        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                Api api = RequestController.createService(context, Api.class);
                api.getLabSearch(binding.etSearchBar.getText().toString())
                        .enqueue(new BaseCallback<SearchingResponse>(context) {
                            @Override
                            public void onSuccess(SearchingResponse response) {
                                ProgressDialogUtils.dismiss();
                                if (response != null) {
                                    if (response.getStatus() == 1) {
                                        CommonUtils.showToastShort(context, response.getMessage());

                                        if (response.getSearchingResultDataList() != null && !response.getSearchingResultDataList().isEmpty()) {
                                            searchingResultDataList.clear();
                                            searchingResultDataList.addAll(response.getSearchingResultDataList());
                                            searchAdapter.notifyDataSetChanged();
                                        } else {
                                            searchingResultDataList.clear();
                                            searchAdapter.notifyDataSetChanged();
                                        }
                                    } else {
                                        CommonUtils.showToastShort(context, response.getMessage());
                                    }
                                } else {
                                    CommonUtils.showToastShort(context, getResources().getString(R.string.no_response));
                                }
                            }

                            @Override
                            public void onFail(Call<SearchingResponse> call, BaseResponse baseResponse) {
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

    private void getDoctorSearchApi() {
        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                Api api = RequestController.createService(context, Api.class);
                api.getDoctorSearch(binding.etSearchBar.getText().toString())
                        .enqueue(new BaseCallback<SearchingResponse>(context) {
                            @Override
                            public void onSuccess(SearchingResponse response) {
                                ProgressDialogUtils.dismiss();
                                if (response != null) {
                                    if (response.getStatus() == 1) {
                                        CommonUtils.showToastShort(context, response.getMessage());

                                        if (response.getSearchingResultDataList() != null && !response.getSearchingResultDataList().isEmpty()) {
                                            searchingResultDataList.clear();
                                            searchingResultDataList.addAll(response.getSearchingResultDataList());
                                            searchAdapter.notifyDataSetChanged();
                                        } else {
                                            searchingResultDataList.clear();
                                            searchAdapter.notifyDataSetChanged();
                                        }
                                    } else {
                                        CommonUtils.showToastShort(context, response.getMessage());
                                    }
                                } else {
                                    CommonUtils.showToastShort(context, getResources().getString(R.string.no_response));
                                }
                            }

                            @Override
                            public void onFail(Call<SearchingResponse> call, BaseResponse baseResponse) {
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

    private void getProductSearchApi() {
        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                Api api = RequestController.createService(context, Api.class);
                api.getProductSearch(binding.etSearchBar.getText().toString())
                        .enqueue(new BaseCallback<SearchingResponse>(context) {
                            @Override
                            public void onSuccess(SearchingResponse response) {
                                ProgressDialogUtils.dismiss();
                                if (response != null) {
                                    if (response.getStatus() == 1) {
                                        CommonUtils.showToastShort(context, response.getMessage());

                                        if (response.getSearchingResultDataList() != null && !response.getSearchingResultDataList().isEmpty()) {
                                            searchingResultDataList.clear();
                                            searchingResultDataList.addAll(response.getSearchingResultDataList());

                                            searchAdapter.notifyDataSetChanged();
                                        } else {
                                            searchingResultDataList.clear();

                                            searchAdapter.notifyDataSetChanged();
                                        }
                                    } else {
                                        CommonUtils.showToastShort(context, response.getMessage());
                                    }
                                } else {
                                    CommonUtils.showToastShort(context, getResources().getString(R.string.no_response));
                                }
                            }

                            @Override
                            public void onFail(Call<SearchingResponse> call, BaseResponse baseResponse) {
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

    private void setSearchAdapter(String type) {
        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
        binding.rvSearchItems.setLayoutManager(gridLayoutManager);
        binding.rvSearchItems.hasFixedSize();
        binding.rvSearchItems.setItemAnimator(new DefaultItemAnimator());
        searchAdapter = new SearchAdapter(context, searchingResultDataList, type);
        if (type.equalsIgnoreCase(AppConstant.MEDICINES)) {
            searchAdapter.showComposition(true);
        } else if (type.equalsIgnoreCase(AppConstant.DOCTORS)) {
            searchAdapter.showComposition(true);
        }
        binding.rvSearchItems.setAdapter(searchAdapter);
    }

    private List<Document> PrepareDataMessage5(String[] title, String[] composition, String[] amount) {
        List<Document> dataa = new ArrayList<>();
        for (int i = 0; i < title.length; i++) {
            Document document = new Document();
            document.setText(title[i]);
            document.setText2(amount[i]);
            document.setText3(composition[i]);
            dataa.add(document);
        }
        data.addAll(dataa);
        return dataa;
    }

    @SuppressLint("SetTextI18n")
    private void setToolbar() {
        binding.menubar.tvTitle.setVisibility(View.VISIBLE);
        if (from.equalsIgnoreCase(AppConstant.MEDICINES)) {
            binding.menubar.tvTitle.setText("Search Medicines and Products");
            binding.etSearchBar.setHint(getResources().getString(R.string.search_for_medicines_and_healthcare_products));
        } else if (from.equalsIgnoreCase(AppConstant.DOCTORS)) {
            binding.menubar.tvTitle.setText("Search Doctors");
            binding.etSearchBar.setHint(getResources().getString(R.string.search_doctors));
        } else if (from.equalsIgnoreCase(AppConstant.TYPE_PRODUCT)) {
            binding.menubar.tvTitle.setText("Search Health Products");
            binding.etSearchBar.setHint(getResources().getString(R.string.search_for_healthcare));
        } else {
            binding.menubar.tvTitle.setText("Search Labs");
            binding.etSearchBar.setHint(getResources().getString(R.string.search_for_labs));
        }
        binding.menubar.ivBack.setImageResource(R.drawable.ic_keyboard_backspace_black_24dp);
        binding.menubar.ivBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.ivBack) {
            finish();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (charSequence.length() >= 3)
            setEmptyLayout(false);

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}


