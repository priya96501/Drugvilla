package com.drugvilla.in.ui.others;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.drugvilla.in.R;
import com.drugvilla.in.databinding.ActivityLegalInfoOptionBinding;
import com.drugvilla.in.ui.labs.SelectLab;
import com.drugvilla.in.utils.ActivityController;
import com.drugvilla.in.utils.AppConstant;
import com.drugvilla.in.utils.CommonUtils;

public class LegalInfoOption extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    private ActivityLegalInfoOptionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_legal_info_option);
        context = LegalInfoOption.this;
        setToolbar();
    }

    private void setToolbar() {
        binding.menubar.tvTitle.setVisibility(View.VISIBLE);
        binding.menubar.tvTitle.setText(R.string.legal_information);
        binding.menubar.ivBack.setImageResource(R.drawable.ic_keyboard_backspace_black_24dp);
        binding.menubar.ivBack.setOnClickListener(this);
        binding.llPrivacyPolicy.setOnClickListener(this);
        binding.llTermsConditions.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                finish();
                break;
            case R.id.llPrivacyPolicy:
                 ActivityController.startActivity(context,Redirect.class, AppConstant.PRIVACY_POLICY);
                break;
            case R.id.llTermsConditions:
                 ActivityController.startActivity(context, Redirect.class, AppConstant.TERMS_CONDITIONS);
                break;
            default:
                break;
        }
    }
}

