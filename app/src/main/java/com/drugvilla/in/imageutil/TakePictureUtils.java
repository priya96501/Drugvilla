package com.drugvilla.in.imageutil;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * this class is used for image operation
 */

@SuppressWarnings({"TryWithIdenticalCatches", "UnusedAssignment", "unused"})
public class TakePictureUtils {

    public static final int TAKE_PICTURE = 1;
    public static final int PICK_GALLERY = 2;
    public static final int CROP_FROM_CAMERA = 3;
    public static final int TAKE_PICTURE_PROFILE = 11;
    public static final int PICK_GALLERY_PROFILE = 22;
    public static final int CROP_FROM_CAMERA_PROFILE = 33;


    public static void takePicture(Activity context, String fileName) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            Uri mImageCaptureUri = null;
            mImageCaptureUri = Uri.fromFile(new File(context.getExternalFilesDir("temp"), fileName + ".jpg"));
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
            intent.putExtra("return-data", true);


            context.startActivityForResult(intent, TAKE_PICTURE);

        } catch (Exception ignored) {

        }
    }

    /**
     * this method is used for take picture from gallery
     */
    public static void openGallery(Activity context) {

        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        context.startActivityForResult(photoPickerIntent, PICK_GALLERY);
    }

    public static void openGalleryFragment(Fragment context) {

        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        context.startActivityForResult(photoPickerIntent, PICK_GALLERY);
    }

    public static void takePictureProfile(Activity context, String fileName) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            Uri mImageCaptureUri = null;
            mImageCaptureUri = Uri.fromFile(new File(context.getExternalFilesDir("temp"), fileName + ".jpg"));
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
            intent.putExtra("return-data", true);
            context.startActivityForResult(intent, TAKE_PICTURE_PROFILE);

        } catch (Exception ignored) {

        }
    }

    /**
     * this method is used for take picture from gallery
     */
    public static void openGalleryProfile(Activity context) {

        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        context.startActivityForResult(photoPickerIntent, PICK_GALLERY_PROFILE);
    }

    public static void openGalleryFragmentProfile(Fragment context) {

        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        context.startActivityForResult(photoPickerIntent, PICK_GALLERY_PROFILE);
    }


    /**
     * this method is used for open crop image
     */
    public static void startCropImage(Activity context, String fileName) {
        Intent intent = new Intent(context, CropImage.class);
        intent.putExtra(CropImage.IMAGE_PATH, new File(context.getExternalFilesDir("temp"), fileName).getPath());
        intent.putExtra(CropImage.SCALE, true);
        intent.putExtra(CropImage.ASPECT_X, 1);
        intent.putExtra(CropImage.ASPECT_Y, 1);
        intent.putExtra(CropImage.OUTPUT_X, 600);
        intent.putExtra(CropImage.OUTPUT_Y, 600);
        context.startActivityForResult(intent, CROP_FROM_CAMERA);
    }    /**
     * this method is used for open crop image
     */
    public static void startCropImageProfile(Activity context, String fileName) {
        Intent intent = new Intent(context, CropImage.class);
        intent.putExtra(CropImage.IMAGE_PATH, new File(context.getExternalFilesDir("temp"), fileName).getPath());
        intent.putExtra(CropImage.SCALE, true);
        intent.putExtra(CropImage.ASPECT_X, 1);
        intent.putExtra(CropImage.ASPECT_Y, 1);
        intent.putExtra(CropImage.OUTPUT_X, 600);
        intent.putExtra(CropImage.OUTPUT_Y, 600);
        context.startActivityForResult(intent, CROP_FROM_CAMERA_PROFILE);
    }



    /*
     *//*this method is used for open crop image *//*
	public static void startCropImage(Activity context, String fileName) {
		Uri mImageCaptureUri = null;
		mImageCaptureUri = Uri.fromFile(new File(context.getExternalFilesDir("temp"), fileName));
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(mImageCaptureUri, "image*//*");
		intent.putExtra("crop", "true");
		intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
		intent.putExtra("return-data", false);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true);
		if (intent.resolveActivity(context.getPackageManager()) != null) {
			context.startActivityForResult(intent, CROP_FROM_CAMERA);
		} else {
			Toast.makeText(context, "No Crop App Available", Toast.LENGTH_SHORT).show();
		}
	}*/


    /**
     * this method is used for copy stream
     */

    public static void copyStream(InputStream input, OutputStream output)
            throws IOException {

        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
    }


}
