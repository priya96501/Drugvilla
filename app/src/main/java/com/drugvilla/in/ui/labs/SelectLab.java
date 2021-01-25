package com.drugvilla.in.ui.labs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.drugvilla.in.R;
import com.drugvilla.in.adapter.labs.SelectedLabAdapter;
import com.drugvilla.in.databinding.ActivitySelectLabBinding;
import com.drugvilla.in.databinding.DialogLabbookingSelectionBinding;
import com.drugvilla.in.listener.OnCheckSelectedListener;
import com.drugvilla.in.model.Document;
import com.drugvilla.in.model.cart.CartData;
import com.drugvilla.in.model.cart.CartResponse;
import com.drugvilla.in.model.lab.SelectLabData;
import com.drugvilla.in.model.lab.SelectLabResponse;
import com.drugvilla.in.model.lab.labs.LabData;
import com.drugvilla.in.service.Api;
import com.drugvilla.in.service.BaseCallback;
import com.drugvilla.in.service.BaseResponse;
import com.drugvilla.in.service.RequestController;
import com.drugvilla.in.ui.order.Cart;
import com.drugvilla.in.ui.patient.SelectPatient;
import com.drugvilla.in.utils.ActivityController;
import com.drugvilla.in.utils.AppConstant;
import com.drugvilla.in.utils.CommonUtils;
import com.drugvilla.in.utils.DialogUtils;
import com.drugvilla.in.utils.ProgressDialogUtils;
import com.drugvilla.in.utils.SharedPref;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;

public class SelectLab extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    private String from = " ", itemId = " ", selectedLabId;
    private List<LabData> providedByLabList = new ArrayList<>();
    private SelectedLabAdapter selectedLabAdapter;
    private ActivitySelectLabBinding binding;
    private List<Document> listData = new ArrayList<>();
    private SelectedLabAdapter labAdapter;
    private final String[] tvCertification = {"ISO,NABL", "NABL,NABH", "ISO,CAP,NABH", "ISO,NABL", "NABL,NABH", "ISO,CAP,NABH"};
    private final String[] tvAddress = {"Chirag Delhi", "Ashok Nagar", "Sangam Vihar", "Chirag Delhi", "Ashok Nagar", "Sangam Vihar"};
    private final String[] tvLabName = {"Delhi Path Lab", "Dr. A. Lalchandani's Pathology Laboratories", "Sure path labs pvt ltd",
            "Delhi Path Lab", "Dr. A. Lalchandani's Pathology Laboratories", "Sure path labs pvt ltd"};
    private final String[] tvPrice = {"400", "900", "450", "400", "900", "450"};
    private final int[] labImages = {R.drawable.lab1, R.drawable.lab2, R.drawable.lab3, R.drawable.lab1, R.drawable.lab2, R.drawable.lab3};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_select_lab);
        context = SelectLab.this;
        getData();
        setToolbar();
        setListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (from.equalsIgnoreCase(AppConstant.TYPE_LAB_CART)) {
            getSelectLabListing();
            setLabAdapter();
        }
    }


    private void getData() {
        Bundle bundle = new Bundle();
        bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.getString(AppConstant.FROM) != null && !Objects.requireNonNull(bundle.getString(AppConstant.FROM)).isEmpty()) {
                from = bundle.getString(AppConstant.FROM);
            }

            // TODO : get the selected item id from previous screens (test or package id)
            if (bundle.getString(AppConstant.ITEM_ID) != null && !Objects.requireNonNull(bundle.getString(AppConstant.ITEM_ID)).isEmpty()) {
                itemId = bundle.getString(AppConstant.ITEM_ID);
            }
        }


    }

    private void setLabAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
        binding.rvLabListing.setLayoutManager(layoutManager);
        binding.rvLabListing.hasFixedSize();
        binding.rvLabListing.setItemAnimator(new DefaultItemAnimator());
        labAdapter = new SelectedLabAdapter(context, providedByLabList, new OnCheckSelectedListener() {
            @Override
            public void onClick(View view, int position, boolean selected) {

                if (listData.get(position).isSelected()) {
                    listData.get(position).setSelected(false);
                } else {
                    for (int i = 0; i < listData.size(); i++) {
                        listData.get(i).setSelected(false);
                    }
                    listData.get(position).setSelected(true);
                }
                labAdapter.notifyDataSetChanged();

                if (!from.equalsIgnoreCase(AppConstant.BOOK_LAB_TEST_FROM_PRESCRIPTION)) {

                    binding.llBottomView.setVisibility(View.VISIBLE);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    layoutParams.setMargins(0, 0, 0, 120);
                    binding.llLabsListing.setLayoutParams(layoutParams);

                }

            }
        });
        binding.rvLabListing.setAdapter(labAdapter);
    }


    private void setToolbar() {
        binding.menuBar.tvTitle.setVisibility(View.VISIBLE);
        binding.menuBar.ivRight.setVisibility(View.VISIBLE);
        binding.menuBar.tvTitle.setText(getResources().getString(R.string.select_lab));
        binding.menuBar.ivBack.setImageResource(R.drawable.ic_keyboard_backspace_black_24dp);
        binding.menuBar.ivBack.setOnClickListener(this);
        binding.menuBar.ivRight.setOnClickListener(this);
        binding.menuBar.ivRight.setBadgeValue(3);
    }

    private void setListener() {
        binding.llSearch.setOnClickListener(this);
        binding.btnContinue.setOnClickListener(this);
        binding.btnGoToCart.setOnClickListener(this);

        if (from.equalsIgnoreCase(AppConstant.BOOK_LAB_TEST_FROM_PRESCRIPTION)) {
            binding.llBtnContinue.setVisibility(View.VISIBLE);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 0, 0, 110);
            binding.llLabsListing.setLayoutParams(layoutParams);
        }
    }

    private boolean isLabChoosen() {
        boolean checked = false;
        for (int i = 0; i < providedByLabList.size(); i++) {
            if (providedByLabList.get(i).isSelected()) {
                selectedLabId = providedByLabList.get(i).getLab_id();
                checked = true;
                break;
            } else {
                checked = false;
            }
        }
        return checked;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.ivRight:
                ActivityController.startActivity(context, Cart.class, AppConstant.TYPE_LAB_CART);
                break;
            case R.id.ivBack:
                finish();
                break;
            case R.id.btnGoToCart:
                addToCartApi();
                break;
            case R.id.btnContinue:
                if (isLabChoosen()) {
                    openSelectionDialog();
                } else {
                    Toast.makeText(context, "Please select lab.", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }


    private void openSelectionDialog() {
        final DialogLabbookingSelectionBinding selectionBinding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.dialog_labbooking_selection, null, false);
        final BottomSheetDialog dialog = DialogUtils.createBottomDialog(context, selectionBinding.getRoot());
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        selectionBinding.btnSelectOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectionBinding.RgOptions.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(context, "Please select one option!", Toast.LENGTH_SHORT).show();
                } else {
                    // visit lab option selected (no address required) from book from prescription flow
                    if (selectionBinding.RgOptions.getCheckedRadioButtonId() == R.id.rb_visit_lab) {
                        ActivityController.startActivity(context, SelectPatient.class, AppConstant.FROM_LABS_PRESCRIPTION_CART);
                    }
                    // collect sample option selected (address required) from book from prescription flow
                    else {
                        ActivityController.startActivity(context, SelectPatient.class, AppConstant.FROM_LABTEST_PRESCRIPTION_CART);
                    }
                }
            }
        });
    }

    private void setEmptyLayout(boolean value) {
        if (value) {
            binding.emptyLayout.llRoot.setVisibility(View.VISIBLE);
            binding.emptyLayout.ivImage.setImageResource(R.drawable.no_lab_record);
            binding.emptyLayout.tvHeading.setText(getResources().getString(R.string.no_lab_found));
            binding.emptyLayout.btnSubmit.setVisibility(View.GONE);
            binding.emptyLayout.tvSubHeading.setVisibility(View.GONE);

            binding.llData.setVisibility(View.GONE);
            binding.llBottomView.setVisibility(View.GONE);
            binding.llBtnContinue.setVisibility(View.GONE);

        } else {
            binding.llData.setVisibility(View.VISIBLE);
            if (from.equalsIgnoreCase(AppConstant.BOOK_LAB_TEST_FROM_PRESCRIPTION)) {
                binding.llBtnContinue.setVisibility(View.VISIBLE);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(0, 0, 0, 110);
                binding.llLabsListing.setLayoutParams(layoutParams);
            } else {
                binding.llData.setVisibility(View.VISIBLE);
            }
        }
    }


    private void addToCartApi() {
        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                Api api = RequestController.createService(context, Api.class);
                HashMap<String, String> map = new HashMap<>();
                map.put("user_id", SharedPref.getStringPreferences(context, AppConstant.USER_ID));
                map.put("item_id", itemId);
                if (from.equalsIgnoreCase(AppConstant.TYPE_LAB_CART)) {
                    map.put("selected_lab_id", selectedLabId);
                }
                api.addToLabTestCart(map)
                        .enqueue(new BaseCallback<com.drugvilla.in.service.BaseResponse>(context) {
                            @Override
                            public void onSuccess(com.drugvilla.in.service.BaseResponse response) {
                                ProgressDialogUtils.dismiss();
                                if (response != null) {
                                    if (response.getStatus() == 1) {
                                        CommonUtils.showToastShort(context, response.getMessage());
                                        ActivityController.startActivity(context, Cart.class, AppConstant.TYPE_LAB_CART);
                                    } else {

                                        CommonUtils.showToastShort(context, response.getMessage());
                                    }
                                } else {
                                    CommonUtils.showToastShort(context, getResources().getString(R.string.no_response));
                                }
                            }

                            @Override
                            public void onFail(Call<com.drugvilla.in.service.BaseResponse> call, BaseResponse baseResponse) {
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

    private void getSelectLabListing() {
        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                Api api = RequestController.createService(context, Api.class);
                api.getSelectLabListing(itemId)
                        .enqueue(new BaseCallback<SelectLabResponse>(context) {
                            @Override
                            public void onSuccess(SelectLabResponse response) {
                                ProgressDialogUtils.dismiss();
                                if (response != null) {
                                    if (response.getStatus() == 1) {
                                        CommonUtils.showToastShort(context, response.getMessage());
                                        SelectLabData data = response.getData();
                                        binding.tvSelectedTestName.setText(data.getTestName());
                                        itemId= data.getTestId();
                                        if (data.getLabDataList() != null && !data.getLabDataList().isEmpty()) {
                                            setEmptyLayout(false);
                                            providedByLabList.clear();
                                            providedByLabList.addAll(data.getLabDataList());
                                            selectedLabAdapter.notifyDataSetChanged();
                                        } else {
                                            setEmptyLayout(true);
                                            providedByLabList.clear();
                                            selectedLabAdapter.notifyDataSetChanged();
                                        }

                                    } else {
                                        setEmptyLayout(true);
                                        providedByLabList.clear();
                                        selectedLabAdapter.notifyDataSetChanged();
                                        CommonUtils.showToastShort(context, response.getMessage());
                                    }
                                } else {
                                    CommonUtils.showToastShort(context, getResources().getString(R.string.no_response));
                                }
                            }

                            @Override
                            public void onFail(Call<SelectLabResponse> call, BaseResponse baseResponse) {
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
}
