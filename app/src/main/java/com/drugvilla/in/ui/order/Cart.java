package com.drugvilla.in.ui.order;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.drugvilla.in.R;
import com.drugvilla.in.adapter.labs.SelectedLabTestAdapter;
import com.drugvilla.in.adapter.order.SelectedProductAdapter;
import com.drugvilla.in.databinding.ActivityCartBinding;
import com.drugvilla.in.databinding.DialogLabbookingSelectionBinding;
import com.drugvilla.in.listener.OnSelectedListener;
import com.drugvilla.in.listener.OnSelectedTypeListener;
import com.drugvilla.in.model.Document;
import com.drugvilla.in.model.cart.CartData;
import com.drugvilla.in.model.cart.CartResponse;
import com.drugvilla.in.model.cart.SelectedItem;
import com.drugvilla.in.service.Api;
import com.drugvilla.in.service.BaseCallback;
import com.drugvilla.in.service.BaseResponse;
import com.drugvilla.in.service.RequestController;
import com.drugvilla.in.ui.patient.SelectPatient;
import com.drugvilla.in.ui.prescription.Upload;
import com.drugvilla.in.utils.ActivityController;
import com.drugvilla.in.utils.AppConstant;
import com.drugvilla.in.utils.CommonUtils;
import com.drugvilla.in.utils.DialogUtils;
import com.drugvilla.in.utils.ProgressDialogUtils;
import com.drugvilla.in.utils.SharedPref;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class Cart extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    private Activity activity;
    private ActivityCartBinding binding;
    private String from = " ";
    private List<Document> listData = new ArrayList<>();
    private List<Document> listDataProduct = new ArrayList<>();
    private SelectedProductAdapter selectedProductAdapter;
    private SelectedLabTestAdapter testAdapter;
    private String type = "1";

    private List<SelectedItem> selectedTestList = new ArrayList<>();


    // selected tests

    private final String[] testName = {"ACE (Absolute Eosinophil Count)", "Advance Health Care", "Beta Hcg/Hcg Beta Subunit"};
    private final String[] labName = {"Sure path labs pvt ltd.", "SRL Limited", "Delhi Path Lab"};
    private final String[] price = {"500", "1250", "750"};
    private final String[] option = {"test", "package", "test"};


    // selected products

    private final int[] productImage = {R.drawable.sp3, R.drawable.sp1, R.drawable.sp2};
    private final String[] productName = {"Cadbury Bournvita Five Star Magic Health Drink - Pouch 500 gm", "Swadeshi Vatantak Vati Powder 50 gm", "Bioderma Cicabio Arnica+ Creme 40 ml"};
    private final String[] by = {"By Cadbury India Ltd", "By Swadeshi", "By Naos Skin Care India Pvt Ltd"};
    private final String[] mrp = {"500", "1250", "750"};
    private final String[] amount = {"300", "1150", "600"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cart);
        context = Cart.this;
        activity = Cart.this;
        getData();
        setToolbar();
        binding.swipeRefreshingLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (from.equalsIgnoreCase(AppConstant.TYPE_LAB_CART)) {
                    getLabTestCart();
                } else {
                    getProductCart();
                }

            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        // cart section for any lab
        if (from.equalsIgnoreCase(AppConstant.TYPE_LAB_CART)) {
            getLabTestCart();
            setSelectedTestAdapter();
        }
        // cart section for any product or medicine
        else {
            getProductCart();
            setSelectedProductAdapter();

        }
    }

    private void getData() {
        Bundle bundle = new Bundle();
        bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.getString(AppConstant.FROM) != null && !(bundle.getString(AppConstant.FROM)).isEmpty()) {
                from = bundle.getString(AppConstant.FROM);
            }
        }
    }

    private void setToolbar() {
        binding.menubar.tvTitle.setVisibility(View.VISIBLE);
        binding.menubar.tvTitle.setText(R.string.cart);
        binding.menubar.ivBack.setImageResource(R.drawable.ic_keyboard_backspace_black_24dp);
        binding.menubar.ivBack.setOnClickListener(this);
        binding.btnSelectPatient.setOnClickListener(this);
        binding.llCoupon.setOnClickListener(this);
        binding.btnContinue.setOnClickListener(this);
    }

    private void setEmptyLayout(boolean value) {
        if (value) {
            binding.emptyLayout.llRoot.setVisibility(View.VISIBLE);
            if (from.equalsIgnoreCase(AppConstant.TYPE_LAB_CART)) {
                binding.emptyLayout.ivImage.setImageResource(R.drawable.no_lab_record);
                binding.emptyLayout.tvHeading.setText(getResources().getString(R.string.empty_lab_cart));
                binding.emptyLayout.btnSubmit.setText(getResources().getString(R.string.add_lab_tests));

            } else {
                binding.emptyLayout.ivImage.setImageResource(R.drawable.cancelorder);
                binding.emptyLayout.tvHeading.setText(getResources().getString(R.string.empty_cart));
                binding.emptyLayout.btnSubmit.setVisibility(View.GONE);
            }

            binding.llBottom.setVisibility(View.GONE);
            binding.llLabData.setVisibility(View.GONE);
            binding.llProductData.setVisibility(View.GONE);
            binding.llpaymentDetails.setVisibility(View.GONE);

        } else {
            binding.llpaymentDetails.setVisibility(View.VISIBLE);
            if (from.equalsIgnoreCase(AppConstant.TYPE_LAB_CART)) {
                binding.btnContinue.setVisibility(View.GONE);
                binding.btnSelectPatient.setVisibility(View.VISIBLE);
                binding.llLabData.setVisibility(View.VISIBLE);
            } else {
                binding.btnContinue.setVisibility(View.VISIBLE);
                binding.btnSelectPatient.setVisibility(View.GONE);
                binding.llProductData.setVisibility(View.VISIBLE);

            }
        }
    }

    private void setSelectedProductAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
        binding.rvSelectedProduct.setLayoutManager(layoutManager);
        binding.rvSelectedProduct.hasFixedSize();
        selectedProductAdapter = new SelectedProductAdapter(context, selectedTestList, AppConstant.TYPE_CART, new OnSelectedListener() {
            @Override
            public void onClick(View view, int position) {
                removeItemApi(selectedTestList.get(position).getItemId());
            }
        }, new OnSelectedTypeListener() {
            @Override
            public void onClick(View view, int position, String quantity) {
                switch (view.getId()) {
                    case R.id.btnPlus:
                        updateCartApi(selectedTestList.get(position).getItemId(), quantity);
                        break;
                    case R.id.btnMinus:
                        updateCartApi(selectedTestList.get(position).getItemId(), quantity);
                        break;
                    default:
                        break;
                }
            }
        });
        binding.rvSelectedProduct.setAdapter(selectedProductAdapter);
    }

    private List<Document> PrepareDataMessage2() {
        List<Document> data = new ArrayList<>();
        for (int i = 0; i < productName.length; i++) {
            Document document = new Document();
            document.setText(productName[i]);
            document.setImage(productImage[i]);
            document.setText2(by[i]);
            document.setText3(mrp[i]);
            document.setText4(amount[i]);
            data.add(document);
        }
        listDataProduct.addAll(data);
        return data;

    }

    private void setSelectedTestAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
        binding.rvSelectedTests.setLayoutManager(layoutManager);
        binding.rvSelectedTests.hasFixedSize();
        testAdapter = new SelectedLabTestAdapter(context, selectedTestList, AppConstant.TYPE_CART, new OnSelectedListener() {
            @Override
            public void onClick(View view, int position) {
                removeLabItemApi(selectedTestList.get(position).getItemId());
            }
        });
        binding.rvSelectedTests.setAdapter(testAdapter);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                finish();
                break;
          /*  case R.id.llCoupon:
                showBottomSheetDialog();
                break;*/
            case R.id.btnSelectPatient:
                openSelectionDialog();
                break;
            case R.id.btnContinue:
                // if prescription is required
                ActivityController.startActivity(context, Upload.class, AppConstant.FROM_CART);
                //TODO : if prescription is not required
                //  ActivityController.startActivity(context, SelectAddress.class, AppConstant.FROM_PRODUCT);
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
                    // visit lab option selected (no address required)
                    if (selectionBinding.RgOptions.getCheckedRadioButtonId() == R.id.rb_visit_lab) {
                        SharedPref.saveStringPreference(context, AppConstant.Selected_Collection_Type, AppConstant.TYPE_2);
                       /* Bundle bundle = new Bundle();
                        bundle.putString(AppConstant.Selected_Collection_Type, AppConstant.TYPE_2);
                        bundle.putString(AppConstant.FROM, AppConstant.FROM_LABS_CART);
                        ActivityController.startActivity(activity, SelectPatient.class, bundle, false, false);*/
                        ActivityController.startActivity(context, SelectPatient.class, AppConstant.FROM_LABS_CART);
                    }
                    // collect sample option selected (address required)
                    else {
                        SharedPref.saveStringPreference(context, AppConstant.Selected_Collection_Type, AppConstant.TYPE_1);
                      /*  Bundle bundle = new Bundle();
                        bundle.putString(AppConstant.Selected_Collection_Type, AppConstant.TYPE_1);
                        bundle.putString(AppConstant.FROM, AppConstant.FROM_LABTEST_CART);
                        ActivityController.startActivity(activity, SelectPatient.class, bundle, false, false);*/
                        ActivityController.startActivity(context, SelectPatient.class, AppConstant.FROM_LABTEST_CART);
                    }
                }
            }
        });
    }

    private void getProductCart() {
        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                Api api = RequestController.createService(context, Api.class);
                api.getCart(SharedPref.getStringPreferences(context, AppConstant.USER_ID))
                        .enqueue(new BaseCallback<CartResponse>(context) {
                            @Override
                            public void onSuccess(CartResponse response) {
                                binding.swipeRefreshingLayout.setRefreshing(false);
                                ProgressDialogUtils.dismiss();
                                if (response != null) {
                                    if (response.getStatus() == 1) {
                                        CommonUtils.showToastShort(context, response.getMessage());
                                        //   CartData data = response.getCartData();

                                        if (response.getCartData().getSelectedItems() != null && !response.getCartData().getSelectedItems().isEmpty()) {
                                            setEmptyLayout(false);
                                            binding.tvTotalAmount.setText(response.getCartData().getOrderTotal());
                                            binding.tvBagTotal.setText(response.getCartData().getTotalMrp());
                                            binding.tvTotalCharges.setText(response.getCartData().getOrderTotal());
                                            binding.tvOrderTotal.setText(response.getCartData().getOrderTotal());
                                            binding.tvDiscount.setText(response.getCartData().getTotalDiscount());

                                            selectedTestList.clear();
                                            selectedTestList.addAll(response.getCartData().getSelectedItems());
                                           // selectedProductAdapter.notifyDataSetChanged();
                                        } else {
                                            setEmptyLayout(true);
                                            selectedTestList.clear();
                                          //  selectedProductAdapter.notifyDataSetChanged();
                                        }

                                    } else {
                                        setEmptyLayout(true);
                                        selectedTestList.clear();
                                      //  selectedProductAdapter.notifyDataSetChanged();
                                        CommonUtils.showToastShort(context, response.getMessage());
                                    }
                                } else {
                                    CommonUtils.showToastShort(context, getResources().getString(R.string.no_response));
                                }
                            }

                            @Override
                            public void onFail(Call<CartResponse> call, BaseResponse baseResponse) {
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

    private void removeItemApi(String itemId) {
        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                Api api = RequestController.createService(context, Api.class);
                api.removeCartItem(SharedPref.getStringPreferences(context, AppConstant.USER_ID), itemId)
                        .enqueue(new BaseCallback<BaseResponse>(context) {
                            @Override
                            public void onSuccess(BaseResponse response) {
                                ProgressDialogUtils.dismiss();
                                if (response != null) {
                                    if (response.getStatus() == 1) {
                                        CommonUtils.showToastShort(context, response.getMessage());
                                        getProductCart();
                                    } else {

                                        CommonUtils.showToastShort(context, response.getMessage());
                                    }
                                } else {
                                    CommonUtils.showToastShort(context, getResources().getString(R.string.no_response));
                                }
                            }

                            @Override
                            public void onFail(Call<BaseResponse> call, BaseResponse baseResponse) {
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

    private void updateCartApi(String itemId, String quantity) {
        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                Api api = RequestController.createService(context, Api.class);
                api.updateCart(SharedPref.getStringPreferences(context, AppConstant.USER_ID), itemId, quantity)
                        .enqueue(new BaseCallback<BaseResponse>(context) {
                            @Override
                            public void onSuccess(BaseResponse response) {
                                ProgressDialogUtils.dismiss();
                                if (response != null) {
                                    if (response.getStatus() == 1) {
                                        CommonUtils.showToastShort(context, response.getMessage());
                                        getProductCart();
                                    } else {

                                        CommonUtils.showToastShort(context, response.getMessage());
                                    }
                                } else {
                                    CommonUtils.showToastShort(context, getResources().getString(R.string.no_response));
                                }
                            }

                            @Override
                            public void onFail(Call<BaseResponse> call, BaseResponse baseResponse) {
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

    private void removeLabItemApi(String itemId) {
        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                Api api = RequestController.createService(context, Api.class);
                api.removeLabCartItem(SharedPref.getStringPreferences(context, AppConstant.USER_ID), itemId)
                        .enqueue(new BaseCallback<BaseResponse>(context) {
                            @Override
                            public void onSuccess(BaseResponse response) {
                                ProgressDialogUtils.dismiss();
                                if (response != null) {
                                    if (response.getStatus() == 1) {
                                        CommonUtils.showToastShort(context, response.getMessage());
                                        getLabTestCart();
                                    } else {

                                        CommonUtils.showToastShort(context, response.getMessage());
                                    }
                                } else {
                                    CommonUtils.showToastShort(context, getResources().getString(R.string.no_response));
                                }
                            }

                            @Override
                            public void onFail(Call<BaseResponse> call, BaseResponse baseResponse) {
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

    private void getLabTestCart() {

        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                Api api = RequestController.createService(context, Api.class);
                api.getLabTestCart(SharedPref.getStringPreferences(context, AppConstant.USER_ID))
                        .enqueue(new BaseCallback<CartResponse>(context) {
                            @Override
                            public void onSuccess(CartResponse response) {
                                binding.swipeRefreshingLayout.setRefreshing(false);
                                ProgressDialogUtils.dismiss();
                                if (response != null) {
                                    if (response.getStatus() == 1) {
                                        CommonUtils.showToastShort(context, response.getMessage());
                                        CartData data = response.getCartData();
                                        if (data.getSelectedItems() != null && !data.getSelectedItems().isEmpty()) {
                                            setEmptyLayout(false);
                                            selectedTestList.clear();
                                            selectedTestList.addAll(data.getSelectedItems());
                                            testAdapter.notifyDataSetChanged();
                                        } else {
                                            setEmptyLayout(true);
                                            selectedTestList.clear();
                                            testAdapter.notifyDataSetChanged();
                                        }

                                    } else {
                                        setEmptyLayout(true);
                                        selectedTestList.clear();
                                        testAdapter.notifyDataSetChanged();
                                        CommonUtils.showToastShort(context, response.getMessage());
                                    }
                                } else {
                                    CommonUtils.showToastShort(context, getResources().getString(R.string.no_response));
                                }
                            }

                            @Override
                            public void onFail(Call<CartResponse> call, BaseResponse baseResponse) {
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



  /*  public static void setBadgeCount(Context context, LayerDrawable icon, int count) {

        BadgeDrawable badge;

        // Reuse drawable if possible
        Drawable reuse = icon.findDrawableByLayerId(R.id.ic_badge);
        if (reuse != null && reuse instanceof BadgeDrawable) {
            badge = (BadgeDrawable) reuse;
        } else {
            badge = new BadgeDrawable(context);
        }

        badge.setCount(""+count);
        icon.mutate();
        icon.setDrawableByLayerId(R.id.ic_badge, badge);
    }*/
   /* public void showBottomSheetDialog() {
        View view = getLayoutInflater().inflate(R.layout.bottom_apply_coupon, null);
        final BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(view);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        ImageView ivCross = dialog.findViewById(R.id.ivCross);
        final LinearLayout llCoupon = dialog.findViewById(R.id.llCoupon);
        final EditText etCoupon = dialog.findViewById(R.id.etCoupon);
        TextView textView = dialog.findViewById(R.id.tvApply);
        etCoupon.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                llCoupon.setBackground(getResources().getDrawable(R.drawable.back_gray));

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etCoupon.getText().toString().isEmpty()) {

                    llCoupon.setBackground(getResources().getDrawable(R.drawable.border_red));
                } else if (etCoupon.getText().toString().length() < 6) {

                    llCoupon.setBackground(getResources().getDrawable(R.drawable.border_red));

                } else {
                    //binding.tvCouponCode.setText(etCoupon.getText().toString());
                    dialog.dismiss();
                }

            }
        });


        ivCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
            }
        });
        dialog.show();
    }

   */
}
