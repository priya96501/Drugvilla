package com.drugvilla.in.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import com.drugvilla.in.R;
import com.drugvilla.in.adapter.DialogMenuUpload;
import com.drugvilla.in.ui.authentication.Login;
import com.drugvilla.in.ui.authentication.Signup;
import com.drugvilla.in.ui.prescription.MyPrescription;
import com.drugvilla.in.ui.prescription.Upload;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class CommonUtils {

    public static boolean isOnline(Context mContext) {
        ConnectivityManager connectivity = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            for (NetworkInfo networkInfo : info)
                if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
        }
        return false;
    }

    public static RequestBody parseString(String str) {
        return RequestBody.create(MediaType.parse("text/plain"), str != null ? str : "");

    }

    public static void setSnackbar(CoordinatorLayout coordinatorLayout, String message) {
        Snackbar snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    // upload option dialog
    public static void chooseUploadOption(final Context context/*, final String type*/) {
        DialogMenuUpload dialogMenu = new DialogMenuUpload(context);
        dialogMenu.setListener(new DialogMenuUpload.OnDialogMenuListener() {
            @Override
            public void onLoginClick() {
                goToNextActivity(context, Login.class, AppConstant.WITHOUT_LOGIN);
            }

            @Override
            public void onSignupClick() {
                goToNextActivity(context, Signup.class, AppConstant.WITHOUT_LOGIN);
            }
        });
    }

    // open dialer
    public static void openDialer(Context context, String number) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + number));
        context.startActivity(intent);
    }

    // share data
    public static void shareData(Context context, String value) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, value);
        context.startActivity(Intent.createChooser(shareIntent, "Share link using"));
    }

    public static void showToastShort(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    // displaying error messages for edittexts
    public static void setErrorMessage(EditText editText, TextView textView, String errorMessage) {
        editText.requestFocus();
        textView.setVisibility(View.VISIBLE);
        textView.setText(errorMessage);
    }

    // setting round cornors to image
    public static void setImageRound(Context context, ImageView imageView, int drawable) {
        Bitmap imageBit = BitmapFactory.decodeResource(context.getResources(), drawable);
        imageView.setImageBitmap(imageBit);
        RoundedBitmapDrawable RBD = RoundedBitmapDrawableFactory.create(context.getResources(), imageBit);
        float imageRadius = 10.0f;
        RBD.setCornerRadius(imageRadius);
        RBD.setAntiAlias(true);
        imageView.setImageDrawable(RBD);
    }

    public static void setListCount(List list, TextView textView, String string) {
        String listCount = String.format("%02d", list.size());
        textView.setText(string + "(" + listCount + ")");
    }

    // setting round cornors to image
    public static void setImageRound2(Context context, ImageView imageView, int drawable) {
        Bitmap imageBit = BitmapFactory.decodeResource(context.getResources(), drawable);
        imageView.setImageBitmap(imageBit);
        RoundedBitmapDrawable RBD = RoundedBitmapDrawableFactory.create(context.getResources(), imageBit);
        float imageRadius = 32.0f;
        RBD.setCornerRadius(imageRadius);
        RBD.setAntiAlias(true);
        imageView.setImageDrawable(RBD);
    }

    // setting round cornors to image using url
    public static void setRoundImage(Context context, ImageView imageView, String url) {
        if (url != null && !url.isEmpty()) {
            /*Glide.with(context)
                    .asBitmap()
                    .load(url)
                    .into(CommonUtils.getRoundedImageTarget(context, imageView, 12));*/
        }

    }

    // setting photo zoom on photo click
    public static void setZoomView(Context context, int drawable) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
        @SuppressLint("InflateParams") View mView = LayoutInflater.from(context).inflate(R.layout.dialog_custom_photoview, null);
        PhotoView photoView = mView.findViewById(R.id.imageView);
        Picasso.with(context).load(drawable).placeholder(R.drawable.gallery)
                .error(R.drawable.gallery).into(photoView);
                /*if (document.getImg() != null && !document.getImg().isEmpty()) {
                    Picasso.with(context).load(AppConstant.IMAGE_URL + document.getImg())
                            .placeholder(R.drawable.gallery)
                            .error(R.drawable.gallery)
                            .into(photoView);
                }*/
        mBuilder.setView(mView);
        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }

    // setting photo zoom on photo click
    public static void setZoomView(Context context, String url) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
        @SuppressLint("InflateParams") View mView = LayoutInflater.from(context).inflate(R.layout.dialog_custom_photoview, null);
        PhotoView photoView = mView.findViewById(R.id.imageView);
        if (url != null && !url.isEmpty()) {
            Picasso.with(context).load(/*AppConstant.IMAGE_URL +*/ url)
                    .placeholder(R.drawable.gallery)
                    .error(R.drawable.gallery)
                    .into(photoView);
        }
        mBuilder.setView(mView);
        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }

    // setting photo zoom on photo click
    public static void setZoomView(Context context, Bitmap bitmap) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
        @SuppressLint("InflateParams") View mView = LayoutInflater.from(context).inflate(R.layout.dialog_custom_photoview, null);
        PhotoView photoView = mView.findViewById(R.id.imageView);
        Picasso.with(context).load(String.valueOf(bitmap)).placeholder(R.drawable.gallery)
                .error(R.drawable.gallery).into(photoView);
                /*if (document.getImg() != null && !document.getImg().isEmpty()) {
                    Picasso.with(context).load(AppConstant.IMAGE_URL + document.getImg())
                            .placeholder(R.drawable.gallery)
                            .error(R.drawable.gallery)
                            .into(photoView);
                }*/
        mBuilder.setView(mView);
        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }

    // setting random colors for names
    public static void getRandomColors(TextView textView) {
        Random mRandom = new Random();
        int color = Color.argb(255, mRandom.nextInt(256), mRandom.nextInt(256), mRandom.nextInt(256));
        ((GradientDrawable) textView.getBackground()).setColor(color);

    }

    // calling next activity
    public static void goToNextActivity(Context context, Class activityName, String type) {
        Bundle bundle = new Bundle();
        Intent intent = new Intent(context, activityName);
        bundle.putString(AppConstant.FROM, type);
        intent.putExtras(bundle);
        context.startActivity(intent);

    }

    //method to convert the selected image to base64 encoded string
    public static String getEncoded64ImageStringFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
        byte[] byteFormat = baos.toByteArray();
        // get the base 64 string
        return Base64.encodeToString(byteFormat, Base64.DEFAULT);
    }


    public static void changeStatusBarColor(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    public static int getWidth(Activity context) {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels - 20;

        return width;
    }


    // for saving or downloading selected image
   /* public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(getContext(),
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::---&gt;" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }*/

    // open time picker
     /*private void setTimePicker() {

        timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                String amPm;
                if (hourOfDay >= 12) {
                    amPm = "PM";
                } else {
                    amPm = "AM";
                }
                binding.tvTime.setText(String.format("%02d:%02d", hourOfDay, minutes) + amPm);
            }
        },currentHour,currentMinute,false);
        timePickerDialog.show();
    }

*/


}
