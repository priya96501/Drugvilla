package com.drugvilla.in.ui.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import com.drugvilla.in.R;
import com.drugvilla.in.databinding.ActivityRefferEarnBinding;
import com.drugvilla.in.model.authentication.LoginData;
import com.drugvilla.in.utils.AppConstant;
import com.drugvilla.in.utils.CommonUtils;
import com.drugvilla.in.utils.SharedPref;

public class RefferEarn extends AppCompatActivity implements View.OnClickListener {
    private ActivityRefferEarnBinding binding;
    private Context context;
    private String ReferalCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_reffer_earn);
        context = RefferEarn.this;
        setToolbar();
        binding.btnShare.setOnClickListener(this);
        ReferalCode = SharedPref.getStringPreferences(context, AppConstant.USER_REFERAL_CODE);
        binding.tvReferalCode.setText(ReferalCode);
    }

    private void setToolbar() {
        binding.menubar.tvTitle.setVisibility(View.VISIBLE);
        binding.menubar.tvTitle.setText(R.string.refer_earn);
        binding.menubar.ivBack.setImageResource(R.drawable.ic_keyboard_backspace_black_24dp);
        binding.menubar.ivBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                finish();
                break;
            case R.id.btnShare:

                // Share to Social Media
                Drawable mDrawable = binding.ivReferEarn.getDrawable();
                Bitmap mBitmap = ((BitmapDrawable) mDrawable).getBitmap();
                String path = MediaStore.Images.Media.insertImage(context.getContentResolver(),
                        mBitmap, "Design", null);
                Uri uri = Uri.parse(path);
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("image/*");
                share.putExtra(Intent.EXTRA_STREAM, uri);
                share.putExtra(Intent.EXTRA_TEXT, "Refer and get extra 5% off on your first order.\n Referal Code : "+binding.tvReferalCode.getText().toString() +"\n http://drugvilla.com/demo/");
                //share.putExtra(Intent.EXTRA_TEXT, "Referal Code :  " + binding.tvReferalCode.getText().toString());
              //  share.putExtra(Intent.EXTRA_TEXT, "http://drugvilla.com/demo/");
                context.startActivity(Intent.createChooser(share, "Share Your Design!"));
                // CommonUtils.shareData(context, ReferalCode);
                break;
            default:
                break;
        }
    }
}


