package com.drugvilla.in.ui.others;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.drugvilla.in.R;
import com.drugvilla.in.databinding.ActivityRedirectBinding;
import com.drugvilla.in.utils.AppConstant;
import com.drugvilla.in.utils.SharedPref;

import java.util.Objects;

public class Redirect extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    private ActivityRedirectBinding binding;
    private String from = " ";

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_redirect);
        context = Redirect.this;
        binding.webview.getSettings().setLoadsImagesAutomatically(true);
        binding.webview.getSettings().setJavaScriptEnabled(true);
        binding.webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        getData();
        setData();
    }

    private void setData() {
        binding.menubar.tvTitle.setVisibility(View.VISIBLE);
        if (from.equalsIgnoreCase(AppConstant.PRIVACY_POLICY)) {
            binding.menubar.tvTitle.setText(R.string.privacy_policy);
            setWebview(SharedPref.getStringPreferences(context, AppConstant.PRIVACY_POLICY),AppConstant.PRIVACY_POLICY_URL);
        } else if (from.equalsIgnoreCase(AppConstant.ABOUT_US)) {
            binding.menubar.tvTitle.setText(R.string.about_us);
            setWebview(SharedPref.getStringPreferences(context, AppConstant.ABOUT_US),AppConstant.PRIVACY_POLICY_URL);
        } else if (from.equalsIgnoreCase(AppConstant.FAQ)) {
            binding.menubar.tvTitle.setText(R.string.faq);
            setWebview(SharedPref.getStringPreferences(context, AppConstant.FAQ),AppConstant.PRIVACY_POLICY_URL);
        } else if (from.equalsIgnoreCase(AppConstant.TERMS_CONDITIONS)) {
            binding.menubar.tvTitle.setText(getResources().getString(R.string.terms_conditions));
            setWebview(SharedPref.getStringPreferences(context, AppConstant.TERMS_CONDITIONS),AppConstant.BLOG_URL);

        } /*else {
            //binding.menubar.tvTitle.setText();
        }*/
        binding.menubar.ivBack.setImageResource(R.drawable.ic_keyboard_backspace_black_24dp);
        binding.menubar.ivBack.setOnClickListener(this);
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

    private void setWebview(String SavedUrl, String staticUrl) {
        if (SavedUrl != null) {
            binding.webview.loadUrl(SavedUrl);
        } else {
            binding.webview.loadUrl(staticUrl);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.ivBack) {
            finish();
        }
    }
}
