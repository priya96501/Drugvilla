package com.drugvilla.in.ui.order;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.drugvilla.in.R;
import com.drugvilla.in.databinding.ActivityPaymentSelectionBinding;
import com.drugvilla.in.databinding.PopupPaymentBinding;
import com.drugvilla.in.model.order.saveOrder.SaveOrderResponse;
import com.drugvilla.in.model.order.saveOrder.SaveOrderResponse2;
import com.drugvilla.in.service.Api;
import com.drugvilla.in.service.BaseCallback;
import com.drugvilla.in.service.BaseResponse;
import com.drugvilla.in.service.RequestController;
import com.drugvilla.in.utils.ActivityController;
import com.drugvilla.in.utils.AppConstant;
import com.drugvilla.in.utils.CommonUtils;
import com.drugvilla.in.utils.DialogUtils;
import com.drugvilla.in.utils.ProgressDialogUtils;
import com.drugvilla.in.utils.SharedPref;

import java.util.HashMap;
import java.util.Objects;

import retrofit2.Call;

public class PaymentSelection extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    private ActivityPaymentSelectionBinding binding;
    private String from = " ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_payment_selection);
        context = PaymentSelection.this;
        getData();
        setToolbar();
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

    private void setToolbar() {
        binding.menubar.tvTitle.setVisibility(View.VISIBLE);
        binding.menubar.tvTitle.setText(R.string.select_payment_mode);
        binding.menubar.ivBack.setImageResource(R.drawable.ic_keyboard_backspace_black_24dp);
        binding.menubar.ivBack.setOnClickListener(this);
        binding.rbOnline.setOnClickListener(this);
        binding.tvViewAll.setOnClickListener(this);
        binding.btnSelect.setOnClickListener(this);

        binding.rgPaymentSelection.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                if (checkedId == R.id.rb_cod) {
                    setSelectedRadioButton(binding.rbCod, binding.rbOnline);
                } else {
                    setSelectedRadioButton(binding.rbOnline, binding.rbCod);
                }
            }
        });
    }

    private void setSelectedRadioButton(RadioButton selected, RadioButton unSelected) {
        selected.setTextColor(getResources().getColor(R.color.colorAccent));
        unSelected.setTextColor(getResources().getColor(R.color.lightGray));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                finish();
                break;
            case R.id.btnSelect:

                if (binding.rgPaymentSelection.getCheckedRadioButtonId() == -1) {
                    CommonUtils.showToastShort(context, "Please select payment type first.");
                } else {

                    if (from.equalsIgnoreCase(AppConstant.TYPE_LABS)) {
                        ActivityController.startActivity(context, OrderConfirmation.class, AppConstant.TYPE_LABS);
                    } else if (from.equalsIgnoreCase(AppConstant.TYPE_PRODUCT)) {
                        confirmOrderApi();
                    }
                /*else if (from.equalsIgnoreCase(AppConstant.ORDER_MEDICINES_FROM_PRESCRIPTION)) {
                    ActivityController.startActivity(context, OrderConfirmation.class, AppConstant.ORDER_MEDICINES_FROM_PRESCRIPTION);
                }*/
                    else if (from.equalsIgnoreCase(AppConstant.BOOK_LAB_TEST_FROM_PRESCRIPTION)) {
                        ActivityController.startActivity(context, OrderConfirmation.class, AppConstant.BOOK_LAB_TEST_FROM_PRESCRIPTION);
                    } else {
                        //   ActivityController.startActivity(context,OrderConfirmation.class);
                    }
                }
                break;
            case R.id.tvViewAll:
                binding.tvViewAll.setVisibility(View.GONE);
                binding.llData.setVisibility(View.VISIBLE);
                break;
          /*  case R.id.llCOD:
                setData(binding.ivCOD, binding.ivPayOnline, binding.tvCOD, binding.tvPayOnline);
                break;*/
            case R.id.rb_online:
                if (from.equalsIgnoreCase(AppConstant.ORDER_MEDICINES_FROM_PRESCRIPTION)) {
                    openDialog();
                } else if (from.equalsIgnoreCase(AppConstant.BOOK_LAB_TEST_FROM_PRESCRIPTION)) {
                    openDialog();
                } else {
                    setSelectedRadioButton(binding.rbOnline, binding.rbCod);
                    //setData(binding.ivPayOnline, binding.ivCOD, binding.tvPayOnline, binding.tvCOD);
                }
                break;
            default:
                break;
        }
    }

    private void openDialog() {
        final PopupPaymentBinding paymentBinding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.popup_payment, null, false);
        final Dialog dialog = DialogUtils.createDialog(context, paymentBinding.getRoot(), 0);
        dialog.setCancelable(false);
        paymentBinding.closePopupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void setData(ImageView ivSelected, ImageView ivUnSelected, TextView tvSelected, TextView unSelected) {
        ivSelected.setImageResource(R.drawable.ic_checked);
        ivUnSelected.setImageResource(R.drawable.ic_bullet_point);
        tvSelected.setTextColor(getResources().getColor(R.color.colorAccent));
        unSelected.setTextColor(getResources().getColor(R.color.lightGray));
    }

    private void confirmOrderApi() {
        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                Api api = RequestController.createService(context, Api.class);
                HashMap<String, String> map = new HashMap<>();
                map.put("user_id", SharedPref.getStringPreferences(context, AppConstant.USER_ID));
                map.put("order_id", SharedPref.getStringPreferences(context, AppConstant.ORDERID));

                if (binding.rgPaymentSelection.getCheckedRadioButtonId() == R.id.rb_cod) {
                    map.put("payment_type", "COD");
                } else {
                    map.put("payment_type", "RazorPay");
                }
                api.confirmOrder(map).enqueue(new BaseCallback<SaveOrderResponse2>(context) {
                    @Override
                    public void onSuccess(SaveOrderResponse2 response) {
                        ProgressDialogUtils.dismiss();
                        if (response != null) {
                            if (response.getStatus() == 1) {
                                SharedPref.saveStringPreference(context, AppConstant.ORDERID, response.getSaveOrderData().getOrderId());
                                CommonUtils.showToastShort(context, response.getMessage());
                                ActivityController.startActivity(context, OrderConfirmation.class, AppConstant.TYPE_PRODUCT);
                            } else {
                                CommonUtils.showToastShort(context, response.getMessage());
                            }
                        } else {
                            CommonUtils.showToastShort(context, getResources().getString(R.string.no_response));
                        }
                    }

                    @Override
                    public void onFail(Call<SaveOrderResponse2> call, BaseResponse baseResponse) {
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
