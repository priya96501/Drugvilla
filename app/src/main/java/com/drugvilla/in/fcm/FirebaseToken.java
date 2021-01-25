package com.drugvilla.in.fcm;

import android.content.Intent;
import android.util.Log;

import com.drugvilla.in.utils.AppConstant;
import com.drugvilla.in.utils.PrefManager;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;


public class FirebaseToken extends FirebaseInstanceIdService {

    private String tokenRefreshed ="";

    @Override
    public void onTokenRefresh() {
        PrefManager prefManager = PrefManager.getInstance(getApplicationContext());

        super.onTokenRefresh();
        tokenRefreshed= FirebaseInstanceId.getInstance().getToken();
        if (tokenRefreshed!=null && tokenRefreshed.equalsIgnoreCase(""))
        {
            prefManager.savePreference(AppConstant.DEVICE_TOKEN_,tokenRefreshed);
            Log.e("Device Token====>",tokenRefreshed);
        }
        sendRegisterToken(tokenRefreshed);
    }

    private void sendRegisterToken(String tokenRefreshed) {
        sendBroadcast(new Intent("Token").putExtra("refreshedToken",tokenRefreshed));
        Log.e("Device Token====>",tokenRefreshed);
    }
}
