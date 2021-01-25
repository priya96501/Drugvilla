package com.drugvilla.in.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;

import com.drugvilla.in.R;
import com.drugvilla.in.adapter.slidder.TextAdapter;
import com.drugvilla.in.databinding.DialogBinding;
import com.drugvilla.in.databinding.PopupLoginSignupBinding;
import com.drugvilla.in.model.Document;
import com.drugvilla.in.utils.AppConstant;
import com.drugvilla.in.utils.DialogUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class DialogMenuUpload implements View.OnClickListener {
    private Dialog dialog;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    final PopupLoginSignupBinding loginSignupPopupBinding;
    // bottom slidder
    List<Document> bottomTextData = new ArrayList<>();
    private final String[] myText = {"Book Appointment", "Book Your Lab Tests", "Order Top Products And Genuine Medicines"};
    private final int[] myImageList = new int[]{R.drawable.welcome1, R.drawable.welcome2, R.drawable.welcome3};
    private OnDialogMenuListener listener;

    public DialogMenuUpload(Context context) {
        loginSignupPopupBinding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.popup_login_signup, null, false);
        dialog = DialogUtils.createDialog(context, loginSignupPopupBinding.getRoot(), 0);
        dialog.setCancelable(true);

        bottomTextData = PrepareDataMessage();
        setSliderViews2(context, bottomTextData);
        loginSignupPopupBinding.btnLogin.setOnClickListener(this);
        loginSignupPopupBinding.btnSignup.setOnClickListener(this);
    }

    private List<Document> PrepareDataMessage() {
        List<Document> data3 = new ArrayList<>();
        for (int i = 0; i < myText.length; i++) {
            Document document = new Document();
            document.setImage(myImageList[i]);
            document.setText2(myText[i]);
            data3.add(document);
        }
        bottomTextData.addAll(data3);
        return data3;
    }

    private void setSliderViews2(Context context, List<Document> list) {
        loginSignupPopupBinding.pager.setAdapter(new TextAdapter(context, list, AppConstant.TYPE_SLIDDER));
        loginSignupPopupBinding.indicator.setViewPager(loginSignupPopupBinding.pager);
        NUM_PAGES = list.size();
        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                loginSignupPopupBinding.pager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 1000, 1000);
        // Pager listener over indicator
        loginSignupPopupBinding.indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                currentPage = position;
            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int pos) {
            }
        });
    }

    public void setListener(OnDialogMenuListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSignup:
                listener.onSignupClick();
                dialog.dismiss();
                break;
            case R.id.btnLogin:
                listener.onLoginClick();
                dialog.dismiss();
                break;
        }
    }

    public interface OnDialogMenuListener {
        void onLoginClick();
        void onSignupClick();
    }
}
