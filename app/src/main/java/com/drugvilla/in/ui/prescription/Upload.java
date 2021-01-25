package com.drugvilla.in.ui.prescription;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.drugvilla.in.R;
import com.drugvilla.in.adapter.prescription.PrescriptionAdapter;
import com.drugvilla.in.databinding.ActivityUploadBinding;
import com.drugvilla.in.databinding.BottomCheckPincodeBinding;
import com.drugvilla.in.databinding.BottomUploadSheetDialogBinding;
import com.drugvilla.in.imageutil.CropImage;
import com.drugvilla.in.imageutil.TakePictureUtils;
import com.drugvilla.in.listener.OnCheckSelectedListener;
import com.drugvilla.in.model.Document;
import com.drugvilla.in.model.PrescriptionData;
import com.drugvilla.in.ui.address.SelectAddress;
import com.drugvilla.in.ui.labs.SelectLab;
import com.drugvilla.in.utils.ActivityController;
import com.drugvilla.in.utils.AppConstant;
import com.drugvilla.in.utils.CheckPermission;
import com.drugvilla.in.utils.CommonUtils;
import com.drugvilla.in.utils.DialogUtils;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static android.os.StrictMode.setVmPolicy;
import static com.drugvilla.in.imageutil.TakePictureUtils.PICK_GALLERY;
import static com.drugvilla.in.imageutil.TakePictureUtils.TAKE_PICTURE;

public class Upload extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    private ActivityUploadBinding binding;
    private static final int CROP_FROM_CAMERA = 99;
    private String image = "";
    private String from = " ";
    private boolean expand;
    ArrayList<Integer> images = new ArrayList<>();
    ArrayList<String> imagesId = new ArrayList<>();
    private PrescriptionAdapter prescriptionAdapter;
    private List<Document> listData = new ArrayList<>();

    private List<PrescriptionData> prescriptionDataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_upload);
        context = Upload.this;
        marshmallow();
        getData();
        setData();
        setToolbar();
    }

    private void setData() {
        binding.btnOk.setOnClickListener(this);
        binding.ivExpand.setOnClickListener(this);
        binding.ivPrescriptionDemo.setOnClickListener(this);
        binding.llCamera.setOnClickListener(this);
        binding.llGalery.setOnClickListener(this);
        binding.llMyPrescription.setOnClickListener(this);
        binding.cross.setOnClickListener(this);
        binding.btnSelectLab.setOnClickListener(this);
        if (from.equalsIgnoreCase(AppConstant.ORDER_MEDICINES_FROM_PRESCRIPTION)) {
            // binding.btnOk.setText(getResources().getString(R.string.Continue));
            //  binding.llcallingDetails.setVisibility(View.VISIBLE);
            //   binding.llBookLabTestSection.setVisibility(View.GONE);
        } else if (from.equalsIgnoreCase(AppConstant.BOOK_LAB_TEST_FROM_PRESCRIPTION)) {
            binding.btnOk.setVisibility(View.GONE);
            binding.llBookLabTestSection.setVisibility(View.VISIBLE);
        }
       /* else if(from.equalsIgnoreCase(AppConstant.FROM_CART))
        {

        }*/
        // setting the selected images from my prescription screen
        else if (from.equalsIgnoreCase("MY_PRESCRIPTIONNNN")) {
            // binding.btnOk.setText(getResources().getString(R.string.Continue));
            // binding.llcallingDetails.setVisibility(View.VISIBLE);
            // binding.llBookLabTestSection.setVisibility(View.GONE);

            for (int i = 0; i < PrescriptionAdapter.dataList.size(); i++) {
                if (PrescriptionAdapter.dataList.get(i).isSelected()) {
                  //  images.add(PrescriptionAdapter.dataList.get(i).getImage());
                    imagesId.add(PrescriptionAdapter.dataList.get(i).getId());
                    System.out.println("selected image's : " + images);
                }
            }
            if (images != null && !images.isEmpty()) {
                binding.ivupload.setVisibility(View.GONE);
                binding.rvSelectedPrescription.setVisibility(View.VISIBLE);
                listData = PrepareDataMessage(images);
                setSelectedPrescriptionAdapter(AppConstant.TYPE_SELECTED_PRESCRIPTION);
                //  prescriptionAdapter.notifyDataSetChanged();
            }
        } else // from.equalsIgnoreCase(AppConstant.FROM_CART)
        {
            binding.btnOk.setVisibility(View.VISIBLE);
        }
    }

    private List<Document> PrepareDataMessage(ArrayList<Integer> Images) {
        List<Document> data = new ArrayList<>();
        for (int i = 0; i < Images.size(); i++) {
            Document document = new Document();
            document.setImage(Images.get(i));
            data.add(document);
        }
        listData.addAll(data);
        return data;
    }

    private void setSelectedPrescriptionAdapter(String type) {
        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
        binding.rvSelectedPrescription.setLayoutManager(gridLayoutManager);
        binding.rvSelectedPrescription.hasFixedSize();
        prescriptionAdapter = new PrescriptionAdapter(context, prescriptionDataList, new OnCheckSelectedListener() {
            @Override
            public void onClick(View view, int position, boolean selected) {
                // noting to do as checkbox functionality is not needed
            }
        }, type);
        prescriptionAdapter.show(true);
        if (prescriptionDataList.size() == 0) {
            binding.ivupload.setVisibility(View.VISIBLE);
            binding.rvSelectedPrescription.setVisibility(View.GONE);
        } else {
            binding.rvSelectedPrescription.setAdapter(prescriptionAdapter);
            prescriptionAdapter.notifyDataSetChanged();
        }
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

    private void marshmallow() {
        if (CheckPermission.checkIsMarshMallowVersion()) {
            if (!CheckPermission.checkCameraPermission(context)) {
                CheckPermission.requestCameraPermission((Activity) context, CheckPermission.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            }
        }
    }

    private void setToolbar() {
        binding.menubar.tvTitle.setVisibility(View.VISIBLE);
        binding.menubar.ivRight.setVisibility(View.VISIBLE);
        binding.menubar.tvTitle.setText(R.string.upload_prescription);
        binding.menubar.ivBack.setImageResource(R.drawable.ic_keyboard_backspace_black_24dp);
        binding.menubar.ivRight.setImageResource(R.mipmap.info);
        binding.menubar.ivRight.setOnClickListener(this);
        binding.menubar.ivBack.setOnClickListener(this);
    }

    public void openGallery() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, PICK_GALLERY);
    }

    private void takePictureFromCamera(String fileName) {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        setVmPolicy(builder.build());
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            Uri mImageCaptureUri = Uri.fromFile(new File(context.getExternalFilesDir("temp"), fileName));
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
            intent.putExtra("return-data", true);
            startActivityForResult(intent, TAKE_PICTURE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == PICK_GALLERY) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    InputStream inputStream = context.getContentResolver().openInputStream(Objects.requireNonNull(intent.getData()));
                    FileOutputStream fileOutputStream = new FileOutputStream(new File(context.getExternalFilesDir("temp"), image));
                    assert inputStream != null;
                    TakePictureUtils.copyStream(inputStream, fileOutputStream);
                    fileOutputStream.close();
                    inputStream.close();
                    startImageCroper(context, image);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (requestCode == TAKE_PICTURE) {
            if (resultCode == Activity.RESULT_OK) {
                startImageCroper(context, image);
            }
        } else if (requestCode == CROP_FROM_CAMERA) {
            if (resultCode == Activity.RESULT_OK) {
                Uri imageUri = Uri.parse(intent.getStringExtra(CropImage.IMAGE_PATH));
                String path = intent.getStringExtra(CropImage.IMAGE_PATH);
                if (path == null) {
                    return;
                }

                // TODO : set upload prescription api, one image at a time and remove unnecessary static data

                Bitmap bm = BitmapFactory.decodeFile(path);
                Document documentList = new Document(); // the model between activity and adapter
                String imageBase64 = CommonUtils.getEncoded64ImageStringFromBitmap(bm);
                documentList.setBmp(bm);  // here i pass the photo
                binding.ivupload.setVisibility(View.GONE);
                binding.rvSelectedPrescription.setVisibility(View.VISIBLE);
                listData.add(documentList);
                setSelectedPrescriptionAdapter(AppConstant.TYPE_CAPTURED_IMAGE);
                prescriptionAdapter.updateList(prescriptionDataList);
                //prescriptionAdapter.notifyDataSetChanged();
            }
        }
    }


    private void startImageCroper(Context context, String image) {
        Intent intent = new Intent(context, CropImage.class);
        intent.putExtra(CropImage.IMAGE_PATH, new File(context.getExternalFilesDir("temp"), image).getPath());
        intent.putExtra(CropImage.SCALE, true);
        intent.putExtra(CropImage.ASPECT_X, 1);
        intent.putExtra(CropImage.ASPECT_Y, 1);
        intent.putExtra(CropImage.OUTPUT_X, 800);
        intent.putExtra(CropImage.OUTPUT_Y, 800);
        startActivityForResult(intent, CROP_FROM_CAMERA);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                finish();
                break;
            case R.id.cross:
                binding.ivupload.setVisibility(View.VISIBLE);
                binding.rlImage.setVisibility(View.GONE);
                break;
            case R.id.llCamera:
                image = System.currentTimeMillis() + "_photo.png";
                takePictureFromCamera(image);
                break;
            case R.id.llGalery:
                image = System.currentTimeMillis() + "_photo.png";
                openGallery();
                break;
            case R.id.ivPrescriptionDemo:
                CommonUtils.setZoomView(context, R.drawable.valid_prescription);
                break;
            case R.id.btnOk:
                // TODO : take the selected images id to next screen
                if (from.equalsIgnoreCase(AppConstant.ORDER_MEDICINES_FROM_PRESCRIPTION)) {
                    ActivityController.startActivity(context, SelectAddress.class, AppConstant.ORDER_MEDICINES_FROM_PRESCRIPTION);
                } else {
                    ActivityController.startActivity(context, SelectAddress.class, AppConstant.FROM_PRODUCT);
                }
                break;
            case R.id.ivRight:
                showBottomSheetDialog();
                break;
            case R.id.llMyPrescription:
                Intent intent = new Intent(context, MyPrescription.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                //ActivityController.startActivity(context, MyPrescription.class, AppConstant.TYPE_ORDER);
                break;
            case R.id.btnSelectLab:
                // case for book lab test from prescription
                ActivityController.startActivity(context, SelectLab.class, AppConstant.BOOK_LAB_TEST_FROM_PRESCRIPTION);
                break;
            case R.id.ivExpand:
                if (!expand) {
                    expand = true;
                    binding.ivPrescriptionDemo.setVisibility(View.VISIBLE);
                    binding.ivExpand.setImageDrawable(getResources().getDrawable(R.drawable.up_arrow));
                } else {
                    expand = false;
                    binding.ivPrescriptionDemo.setVisibility(View.GONE);
                    binding.ivExpand.setImageDrawable(getResources().getDrawable(R.drawable.down_arrow));
                }
                break;
            default:
                break;
        }
    }

    public void showBottomSheetDialog() {
        final BottomUploadSheetDialogBinding uploadBinding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.bottom_upload_sheet_dialog, null, false);
        final BottomSheetDialog dialog = DialogUtils.createBottomDialog(context, uploadBinding.getRoot());
        dialog.setCancelable(false);
        uploadBinding.ivCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        uploadBinding.tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
