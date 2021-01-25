package com.drugvilla.in.ui.startAndDashboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
import com.drugvilla.in.R;
import com.drugvilla.in.databinding.ActivitySplashBinding;
import com.drugvilla.in.utils.ActivityController;
import com.drugvilla.in.utils.AppConstant;
import com.drugvilla.in.utils.PrefManager;
import com.drugvilla.in.utils.SharedPref;
import com.google.firebase.iid.FirebaseInstanceId;

public class Splash extends AppCompatActivity {
    private Context context;
    private String refreshedToken;
    private PrefManager prefManager;
    private Activity activity;
    private int SPLASH_TIME_OUT = 500;
    private ActivitySplashBinding binding;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        context = Splash.this;
        activity = Splash.this;
        // fixing portrait mode problem for SDK 26 if using windowIsTranslucent = true
        if (Build.VERSION.SDK_INT == 26) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        prefManager = PrefManager.getInstance(activity);
        getDeviceToken();

    }
    private void getDeviceToken() {
        if (prefManager.getPreference(AppConstant.DEVICE_TOKEN_) != null && prefManager.getPreference(AppConstant.DEVICE_TOKEN_).toString().trim().length() > 0) {
            decideNextActivity();
        } else {
            try {
                refreshedToken = FirebaseInstanceId.getInstance().getToken();
            } catch (Exception e) {
                e.printStackTrace();
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (prefManager.getPreference(AppConstant.DEVICE_TOKEN_) != null && !prefManager.getPreference(AppConstant.DEVICE_TOKEN_).toString().trim().isEmpty()) {
                        decideNextActivity();

                    } else {
                        if (refreshedToken != null) {
                            prefManager.savePreference(AppConstant.DEVICE_TOKEN_, refreshedToken);
                            if (refreshedToken.trim().length() > 0) {
                                decideNextActivity();

                            } else {
                                getDeviceToken();
                                Toast.makeText(context, "fcm token : " + prefManager.getPreference(AppConstant.DEVICE_TOKEN_), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            getDeviceToken();
                            Toast.makeText(context, "fcm token : " + prefManager.getPreference(AppConstant.DEVICE_TOKEN_), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }, SPLASH_TIME_OUT);
        }
        Log.e("token", "" + prefManager.getPreference(AppConstant.DEVICE_TOKEN_));
    }





    // code for user session
    private void decideNextActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (SharedPref.getBooleanPreferences(context, AppConstant.IS_LOGIN)) {
                    // go to home activity
                    ActivityController.startActivity(activity,Home.class,true);
                } else {
                    // go to login or welcome screen
                    ActivityController.startActivity(activity,Welcome.class,true);
                }
            }
        },SPLASH_TIME_OUT);

    }














    @Override
    public void onStop() {
        finish();
        super.onStop();
    }

}
