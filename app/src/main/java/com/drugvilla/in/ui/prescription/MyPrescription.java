package com.drugvilla.in.ui.prescription;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.drugvilla.in.R;
import com.drugvilla.in.adapter.prescription.PrescriptionAdapter;
import com.drugvilla.in.databinding.ActivityMyPrescriptionBinding;
import com.drugvilla.in.listener.OnCheckSelectedListener;
import com.drugvilla.in.model.Document;
import com.drugvilla.in.model.PrescriptionData;
import com.drugvilla.in.model.order.PrescriptionResponse;
import com.drugvilla.in.service.Api;
import com.drugvilla.in.service.BaseCallback;
import com.drugvilla.in.service.BaseResponse;
import com.drugvilla.in.service.RequestController;
import com.drugvilla.in.utils.AppConstant;
import com.drugvilla.in.utils.CommonUtils;
import com.drugvilla.in.utils.ProgressDialogUtils;
import com.drugvilla.in.utils.SharedPref;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;

public class MyPrescription extends AppCompatActivity implements View.OnClickListener {
    private ActivityMyPrescriptionBinding binding;
    private Context context;
    private String from = " ";
    private PrescriptionAdapter prescriptionAdapter;
    private List<Document> listData = new ArrayList<>();
    ArrayList<String> idd = new ArrayList<>();
    // ArrayList<Integer> idd = new ArrayList<>();
    private final int[] rx = {R.drawable.top_package2, R.drawable.rx, R.drawable.top_package3, R.drawable.top_package6, R.drawable.top_package5};

    private List<PrescriptionData> prescriptionDataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_prescription);
        context = MyPrescription.this;
        getData();
        setToolbar();
        getMyPrescriptions();
        setAdapter();


    }


    private void setEmptyLayout(boolean value) {
        if (value) {
            binding.emptyLayout.llRoot.setVisibility(View.VISIBLE);

            binding.emptyLayout.ivImage.setImageResource(R.drawable.prescription);
            binding.emptyLayout.tvHeading.setText(getResources().getString(R.string.no_prescription_found));
            binding.emptyLayout.btnSubmit.setVisibility(View.GONE);

            binding.rvMyPrescription.setVisibility(View.GONE);
            binding.btnSelectPrescription.setVisibility(View.GONE);

        } else {
            binding.emptyLayout.llRoot.setVisibility(View.GONE);
            binding.rvMyPrescription.setVisibility(View.VISIBLE);
            binding.btnSelectPrescription.setVisibility(View.VISIBLE);
        }
    }

    private void getMyPrescriptions() {
        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                Api api = RequestController.createService(context, Api.class);
                api.getMyPrescriptions(SharedPref.getStringPreferences(context, AppConstant.USER_ID))
                        .enqueue(new BaseCallback<PrescriptionResponse>(context) {
                            @Override
                            public void onSuccess(PrescriptionResponse response) {
                                ProgressDialogUtils.dismiss();
                                if (response != null) {
                                    if (response.getStatus() == 1) {
                                        CommonUtils.showToastShort(context, response.getMessage());

                                        if (response.getPrescriptionDataList() != null && !response.getPrescriptionDataList().isEmpty()) {
                                            setEmptyLayout(false);
                                            prescriptionDataList.clear();
                                            prescriptionDataList.addAll(response.getPrescriptionDataList());
                                        } else {
                                            prescriptionDataList.clear();
                                            setEmptyLayout(true);
                                        }

                                        setAdapter();

                                    } else {
                                        setEmptyLayout(true);
                                        CommonUtils.showToastShort(context, response.getMessage());
                                    }
                                } else {
                                    CommonUtils.showToastShort(context, getResources().getString(R.string.no_response));
                                }
                            }

                            @Override
                            public void onFail(Call<PrescriptionResponse> call, BaseResponse baseResponse) {
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

    private List<Document> PrepareDataMessage() {
        List<Document> data = new ArrayList<>();
        for (int i = 0; i < rx.length; i++) {
            Document document = new Document();
            document.setImage(rx[i]);
            data.add(document);
        }
        listData.addAll(data);
        return data;
    }

    private void setAdapter() {
       /* //listData.clear();
        listData = PrepareDataMessage();*/

        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 3);
        binding.rvMyPrescription.setLayoutManager(gridLayoutManager);
        binding.rvMyPrescription.hasFixedSize();
        prescriptionAdapter = new PrescriptionAdapter(context, prescriptionDataList, new OnCheckSelectedListener() {
            @Override
            public void onClick(View view, int position, boolean selected) {
                if (prescriptionDataList.get(position).isSelected()) {
                    prescriptionDataList.get(position).setSelected(false);
                } else {
                    prescriptionDataList.get(position).setSelected(true);
                }
                prescriptionAdapter.notifyDataSetChanged();
            }
        }, AppConstant.TYPE_MY_PRESCRIPTIONS);
        if (from.equalsIgnoreCase(AppConstant.PROFILE)) {
            prescriptionAdapter.show(true);
        }
        binding.rvMyPrescription.setAdapter(prescriptionAdapter);
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

    private boolean isPrescriptionChoosen() {
        boolean checked = false;


        for (int i = 0; i < PrescriptionAdapter.dataList.size(); i++) {
            if (PrescriptionAdapter.dataList.get(i).isSelected()) {
                idd.add(PrescriptionAdapter.dataList.get(i).getId());
                System.out.println("selected image's : " + idd);
            }

            if (idd.size() < 1) {
                checked = false;
            } else {
                checked = true;
            }
        }
        return checked;
    }

    private void setToolbar() {
        binding.menubar.tvTitle.setVisibility(View.VISIBLE);
        if (from.equalsIgnoreCase(AppConstant.PROFILE)) {
            binding.menubar.tvTitle.setText(R.string.my_prescription);
            binding.btnSelectPrescription.setVisibility(View.GONE);
            binding.menubar.tvClearAll.setVisibility(View.VISIBLE);
            binding.menubar.tvClearAll.setText("Select");
        } else {
            binding.btnSelectPrescription.setVisibility(View.VISIBLE);
            binding.btnDeletePrescription.setVisibility(View.GONE);
            binding.menubar.tvTitle.setText(R.string.select_prescription);
        }
        binding.menubar.ivBack.setImageResource(R.drawable.ic_keyboard_backspace_black_24dp);
        binding.menubar.ivBack.setOnClickListener(this);
        binding.menubar.tvClearAll.setOnClickListener(this);
        binding.btnSelectPrescription.setOnClickListener(this);
        binding.btnDeletePrescription.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                finish();
                break;
            case R.id.tvClearAll:
                prescriptionAdapter.show(false);
                prescriptionAdapter.notifyDataSetChanged();
                binding.btnDeletePrescription.setVisibility(View.VISIBLE);
                break;
            case R.id.btnSelectPrescription:

                if (isPrescriptionChoosen()) {
                    Bundle bundle = new Bundle();
                    bundle.putString(AppConstant.FROM, "MY_PRESCRIPTIONNNN");
                    Intent intent = new Intent(context, Upload.class);
                    // intent.putIntegerArrayListExtra("list", idd);
                    intent.putExtras(bundle);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    CommonUtils.setSnackbar(binding.coordinatorLayout, "Please select prescription to upload.");

                }
                break;

            case R.id.btnDeletePrescription:
                if (isPrescriptionChoosen()) {
                    // TODO: delete selected prescription api
                    CommonUtils.setSnackbar(binding.coordinatorLayout, "Prescription Deleted.");


                } else {
                    CommonUtils.setSnackbar(binding.coordinatorLayout, "Please select prescription to delete.");

                }
                break;

            default:
                break;
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(context, Upload.class);
        startActivity(intent);
        finish();
    }
}
