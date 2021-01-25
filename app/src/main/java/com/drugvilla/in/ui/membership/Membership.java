package com.drugvilla.in.ui.membership;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import com.drugvilla.in.R;
import com.drugvilla.in.databinding.ActivityMembershipBinding;
import com.drugvilla.in.databinding.WalletHelpBinding;
import com.drugvilla.in.ui.others.Redirect;
import com.drugvilla.in.utils.ActivityController;
import com.drugvilla.in.utils.AppConstant;
import com.drugvilla.in.utils.CommonUtils;
import com.drugvilla.in.utils.DialogUtils;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class Membership extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    private ActivityMembershipBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_membership);
        context = Membership.this;
        setToolbar();
        setListener();
    }

    private void setListener() {
        binding.btnAdd.setOnClickListener(this);
        binding.tvTermsConditions.setOnClickListener(this);
    }

    private void setToolbar() {
        binding.menubar.tvTitle.setVisibility(View.VISIBLE);
        binding.menubar.ivRight.setVisibility(View.VISIBLE);
        binding.menubar.tvTitle.setText(R.string.drugvilla_membership);
        binding.menubar.ivBack.setImageResource(R.drawable.ic_keyboard_backspace_black_24dp);
        binding.menubar.ivBack.setOnClickListener(this);
        binding.menubar.ivRight.setImageResource(R.mipmap.info);
        binding.menubar.ivRight.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                finish();
                break;
            case R.id.btnAdd:
                break;
            case R.id.tvTermsConditions:
                ActivityController.startActivity(context, Redirect.class, AppConstant.TERMS_CONDITIONS);
                break;
            case R.id.ivRight:
                showBottomSheetDialog();
                break;
            default:
                break;
        }
    }

    public void showBottomSheetDialog() {
        final WalletHelpBinding walletBinding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.wallet_help, null, false);
        final BottomSheetDialog dialog = DialogUtils.createBottomDialog(context, walletBinding.getRoot());
        dialog.setCancelable(false);
        walletBinding.ivCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}
