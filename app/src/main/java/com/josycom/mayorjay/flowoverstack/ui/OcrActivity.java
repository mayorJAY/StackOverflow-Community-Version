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
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;
import com.josycom.mayorjay.flowoverstack.R;
import com.josycom.mayorjay.flowoverstack.databinding.ActivityOcrBinding;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
    }
}