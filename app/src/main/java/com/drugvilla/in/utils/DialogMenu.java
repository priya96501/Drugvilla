package com.drugvilla.in.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.drugvilla.in.R;

import java.util.Objects;

public class DialogMenu implements View.OnClickListener {
    private Dialog dialog;
    private OnDialogMenuListener listener;

    public DialogMenu(Context context) {
        dialog = new Dialog(context, R.style.Theme_AppCompat_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_upload_options);

        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();


        LinearLayout newPrescription =  dialog.findViewById(R.id.llUpload);
        LinearLayout myPrescription = dialog.findViewById(R.id.llMyPrescription);
        newPrescription.setOnClickListener(this);
        myPrescription.setOnClickListener(this);

    }

    public void setListener(OnDialogMenuListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.llUpload:
                listener.onUploadNewPressed();
                dialog.dismiss();
                break;
            case R.id.llMyPrescription:
                listener.onMyPrescriptionPressed();
                dialog.dismiss();
                break;
        }
    }

    public interface OnDialogMenuListener{
        void onUploadNewPressed();
        void onMyPrescriptionPressed();
    }
}
