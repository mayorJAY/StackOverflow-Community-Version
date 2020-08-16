package com.josycom.mayorjay.flowoverstack.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.josycom.mayorjay.flowoverstack.R;
import com.josycom.mayorjay.flowoverstack.databinding.ActivityOcrBinding;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class OcrActivity extends AppCompatActivity {

    private ActivityOcrBinding mActivityOcrBinding;
    private String mPhotoPath;
    private static int PERMISSION_REQUEST_CODE = 100;
    private static int CAMERA_REQUEST_CODE = 101;
    private String[] REQUIRED_PERMISSIONS = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityOcrBinding = ActivityOcrBinding.inflate(getLayoutInflater());
        setContentView(mActivityOcrBinding.getRoot());
        if (allPermissionsGranted()) {
            startCamera();
            mActivityOcrBinding.ivCroppedImage.setOnClickListener(view -> startCamera());
        } else if (shouldShowRequestPermissionRationale()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("App Permissions")
                    .setMessage("This application requires access to your camera and storage to function!")
                    .setNegativeButton("No", (dialogInterface, i) ->
                            startActivity(new Intent(getApplicationContext(), MainActivity.class)))
                    .setPositiveButton("Ask Me", (dialogInterface, i) -> {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            requestPermissions(REQUIRED_PERMISSIONS, PERMISSION_REQUEST_CODE);
                        }
                    }).show();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(REQUIRED_PERMISSIONS, PERMISSION_REQUEST_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (allPermissionsGranted()) {
                startCamera();
            } else {
                returnToMainActivity();
            }
        }
    }

    private void startCamera() {
        mActivityOcrBinding.ocrTextInputLayout.setVisibility(View.GONE);
        mActivityOcrBinding.ocrTextInputEditText.setVisibility(View.GONE);
        mActivityOcrBinding.btSearch.setVisibility(View.GONE);
        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        captureIntent.resolveActivity(this.getPackageManager());
        Uri photoUri = Uri.fromFile(createImageFile());
        captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        startActivityForResult(captureIntent, CAMERA_REQUEST_CODE);
    }

    private File createImageFile() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = null;
        try {
            image = File.createTempFile(imageFileName, ".jpg", storageDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mPhotoPath = Objects.requireNonNull(image).getAbsolutePath();
        return image;
    }

    private void returnToMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
        Snackbar.make(findViewById(R.id.main_root_layout), "Permission Denied", Snackbar.LENGTH_SHORT).show();
    }

    private boolean allPermissionsGranted() {
        boolean granted = false;
        for (String item: REQUIRED_PERMISSIONS) {
            granted = ContextCompat.checkSelfPermission(this, item) == PackageManager.PERMISSION_GRANTED;
        }
        return granted;
    }

    private boolean shouldShowRequestPermissionRationale() {
        boolean shouldRequest = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String item: REQUIRED_PERMISSIONS) {
                shouldRequest = shouldShowRequestPermissionRationale(item);
            }
        }
        return shouldRequest;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bitmap bitmap = BitmapFactory.decodeFile(mPhotoPath);
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode == RESULT_OK && bitmap != null) {
                File file = new File(mPhotoPath);
                Uri uri = Uri.fromFile(file);
                CropImage.activity(uri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(this);
            } else if (bitmap == null) {
                startActivity(new Intent(this, MainActivity.class));
            }
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = Objects.requireNonNull(result).getUri();
                Glide.with(this)
                        .load(resultUri)
                        .into(mActivityOcrBinding.ivCroppedImage);
                analyseImage(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = Objects.requireNonNull(result).getError();
                error.printStackTrace();
                Toast.makeText(this, "Oops! an error occurred", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void analyseImage(Uri resultUri) {
        mActivityOcrBinding.btScan.setVisibility(View.VISIBLE);
        mActivityOcrBinding.btScan.setOnClickListener(view -> {
            mActivityOcrBinding.ocrProgressBar.setVisibility(View.VISIBLE);
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), resultUri);
                InputImage image = InputImage.fromBitmap(bitmap, 0);
                TextRecognizer recognizer = TextRecognition.getClient();
                recognizer.process(image)
                        .addOnSuccessListener(text -> {
                            mActivityOcrBinding.ocrProgressBar.setVisibility(View.GONE);
                            mActivityOcrBinding.btScan.setVisibility(View.GONE);
                            processTextRecognitionResult(text);
                        })
                        .addOnFailureListener(e -> {
                            mActivityOcrBinding.ocrProgressBar.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), "Sorry, something went wrong!", Toast.LENGTH_SHORT).show();
                        });
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void processTextRecognitionResult(Text text) {
        List<Text.TextBlock> blocks = text.getTextBlocks();
        if (blocks.size() == 0) {
            Toast.makeText(getApplicationContext(), "No text found, scan again", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, MainActivity.class));
            return;
        }
        mActivityOcrBinding.ocrTextInputLayout.setVisibility(View.VISIBLE);
        mActivityOcrBinding.ocrTextInputEditText.setVisibility(View.VISIBLE);
        mActivityOcrBinding.btSearch.setVisibility(View.VISIBLE);
        mActivityOcrBinding.ocrTextInputEditText.setText(text.getText());
        mActivityOcrBinding.btSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                intent.putExtra("query", text.getText());
                startActivity(intent);
            }
        });
    }
}