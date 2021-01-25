package com.drugvilla.in.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;

import com.drugvilla.in.R;


public class ProgressDialogUtils {

    private static ProgressDialog progressDialog;

    public ProgressDialogUtils() {
        throw new Error("U will not able to instantiate it");
    }

    public static void show(Context context) {
        try {
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            int style;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                style = android.R.style.Theme_Material_Light_Dialog;
            } else {

                style = ProgressDialog.THEME_HOLO_LIGHT;
            }
            progressDialog = new ProgressDialog(context, style);
            progressDialog.setMessage(context.getResources().getString(R.string.please_wait));
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void dismiss() {
        try {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
                progressDialog = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
