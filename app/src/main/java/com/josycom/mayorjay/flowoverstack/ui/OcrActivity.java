package com.josycom.mayorjay.flowoverstack.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.josycom.mayorjay.flowoverstack.R;
import com.josycom.mayorjay.flowoverstack.adapters.SearchAdapter;
import com.josycom.mayorjay.flowoverstack.databinding.ActivityOcrBinding;
import com.josycom.mayorjay.flowoverstack.model.Owner;
import com.josycom.mayorjay.flowoverstack.model.Question;
import com.josycom.mayorjay.flowoverstack.util.AppConstants;
import com.josycom.mayorjay.flowoverstack.util.DateUtil;
import com.josycom.mayorjay.flowoverstack.viewmodel.SearchViewModel;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static com.josycom.mayorjay.flowoverstack.util.AppConstants.EXTRA_AVATAR_ADDRESS;
import static com.josycom.mayorjay.flowoverstack.util.AppConstants.EXTRA_QUESTION_ANSWERS_COUNT;
import static com.josycom.mayorjay.flowoverstack.util.AppConstants.EXTRA_QUESTION_DATE;
import static com.josycom.mayorjay.flowoverstack.util.AppConstants.EXTRA_QUESTION_FULL_TEXT;
import static com.josycom.mayorjay.flowoverstack.util.AppConstants.EXTRA_QUESTION_ID;
import static com.josycom.mayorjay.flowoverstack.util.AppConstants.EXTRA_QUESTION_NAME;
import static com.josycom.mayorjay.flowoverstack.util.AppConstants.EXTRA_QUESTION_OWNER_LINK;
import static com.josycom.mayorjay.flowoverstack.util.AppConstants.EXTRA_QUESTION_TITLE;
import static com.josycom.mayorjay.flowoverstack.util.AppConstants.EXTRA_QUESTION_VOTES_COUNT;

public class OcrActivity extends AppCompatActivity {

    private ActivityOcrBinding mActivityOcrBinding;
    private String mPhotoPath;
    private static int PERMISSION_REQUEST_CODE = 100;
    private static int CAMERA_REQUEST_CODE = 101;
    private String[] REQUIRED_PERMISSIONS = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private List<Question> mQuestions;
    private String mSearchInput;
    private SearchViewModel mSearchViewModel;
    private View.OnClickListener mOnClickListener;

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

        activateViewHolder();
        setupRecyclerView();
        hideAndShowScrollFab();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (allPermissionsGranted()) {
                startCamera();
            } else {
                returnToMainActivity();
                finish();
            }
        }
    }

    private void startCamera() {
        mActivityOcrBinding.ocrTextInputLayout.setVisibility(View.GONE);
        mActivityOcrBinding.ocrTextInputEditText.setVisibility(View.GONE);
        mActivityOcrBinding.btSearch.setVisibility(View.GONE);
        /*Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        captureIntent.resolveActivity(this.getPackageManager());
        Uri photoUri = Uri.fromFile(createImageFile());
        captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        startActivityForResult(captureIntent, CAMERA_REQUEST_CODE);*/
        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (captureIntent.resolveActivity(getPackageManager()) != null) {
            File photo = null;
            try {
                photo = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (photo != null) {
                Uri photoUri = FileProvider.getUriForFile(this, "com.josycom.mayorjay.flowoverstack.fileprovider", photo);
                captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(captureIntent, CAMERA_REQUEST_CODE);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        mPhotoPath = image.getAbsolutePath();
        /*File image = null;
        try {
            image = File.createTempFile(imageFileName, ".jpg", storageDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mPhotoPath = Objects.requireNonNull(image).getAbsolutePath();*/
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
                Toast.makeText(this, "Oops! an error occurred", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void analyseImage(Uri resultUri) {
        mActivityOcrBinding.btRecognise.setVisibility(View.VISIBLE);
        mActivityOcrBinding.btRecognise.setOnClickListener(view -> {
            mActivityOcrBinding.ocrProgressBar.setVisibility(View.VISIBLE);
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), resultUri);
                InputImage image = InputImage.fromBitmap(bitmap, 0);
                TextRecognizer recognizer = TextRecognition.getClient();
                recognizer.process(image)
                        .addOnSuccessListener(text -> {
                            mActivityOcrBinding.ocrProgressBar.setVisibility(View.GONE);
                            mActivityOcrBinding.btRecognise.setVisibility(View.GONE);
                            processTextRecognitionResult(text);
                        })
                        .addOnFailureListener(e -> {
                            mActivityOcrBinding.ocrProgressBar.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), "Sorry, something went wrong!", Toast.LENGTH_LONG).show();
                        });
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void processTextRecognitionResult(Text text) {
        List<Text.TextBlock> blocks = text.getTextBlocks();
        if (blocks.size() == 0) {
            Toast.makeText(getApplicationContext(), "No text found, scan again", Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, MainActivity.class));
            return;
        }
        mActivityOcrBinding.ocrTextInputLayout.setVisibility(View.VISIBLE);
        mActivityOcrBinding.ocrTextInputEditText.setVisibility(View.VISIBLE);
        mActivityOcrBinding.btSearch.setVisibility(View.VISIBLE);
        mActivityOcrBinding.ocrTextInputEditText.setText(text.getText());
        activateSearchButton();
    }

    private void setupRecyclerView() {
        mActivityOcrBinding.ocrRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        mActivityOcrBinding.ocrRecyclerview.setHasFixedSize(true);
        mActivityOcrBinding.ocrRecyclerview.setItemAnimator(new DefaultItemAnimator());
        final SearchAdapter searchAdapter = new SearchAdapter();

        mSearchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        mSearchViewModel.getResponseLiveData().observe(this, searchResponse -> {
            switch (searchResponse.networkState) {
                case AppConstants.LOADING:
                    onLoading();
                    break;
                case AppConstants.LOADED:
                    onLoaded();
                    mQuestions = searchResponse.questions;
                    searchAdapter.setQuestions(searchResponse.questions);
                    break;
                case AppConstants.NO_MATCHING_RESULT:
                    onNoMatchingResult();
                    break;
                case AppConstants.FAILED:
                    onError();
                    break;
            }
        });
        mActivityOcrBinding.ocrRecyclerview.setAdapter(searchAdapter);
        searchAdapter.setOnClickListener(mOnClickListener);
    }

    private void hideAndShowScrollFab() {
        mActivityOcrBinding.ocrScrollUpFab.setVisibility(View.INVISIBLE);
        mActivityOcrBinding.ocrNestedScrollview.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY > 0) {
                mActivityOcrBinding.ocrScrollUpFab.setVisibility(View.VISIBLE);
            } else {
                mActivityOcrBinding.ocrScrollUpFab.setVisibility(View.INVISIBLE);
            }
        });
        mActivityOcrBinding.ocrScrollUpFab.setOnClickListener(view -> mActivityOcrBinding.ocrNestedScrollview.scrollTo(0, 0));
    }

    private void activateViewHolder() {
        mOnClickListener = view -> {
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
            int position = viewHolder.getAdapterPosition();
            Intent answerActivityIntent = new Intent(getApplicationContext(), AnswerActivity.class);
            Question currentQuestion = mQuestions.get(position);
            Owner questionOwner = currentQuestion.getOwner();

            answerActivityIntent.putExtra(EXTRA_QUESTION_TITLE, currentQuestion.getTitle());
            answerActivityIntent.putExtra(EXTRA_QUESTION_NAME, questionOwner.getDisplayName());
            answerActivityIntent.putExtra(EXTRA_QUESTION_DATE,
                    DateUtil.toNormalDate(currentQuestion.getCreationDate()));
            answerActivityIntent.putExtra(EXTRA_QUESTION_FULL_TEXT, currentQuestion.getBody());
            answerActivityIntent.putExtra(EXTRA_AVATAR_ADDRESS, questionOwner.getProfileImage());
            answerActivityIntent.putExtra(EXTRA_QUESTION_ANSWERS_COUNT, currentQuestion.getAnswerCount());
            answerActivityIntent.putExtra(EXTRA_QUESTION_ID, currentQuestion.getQuestionId());
            answerActivityIntent.putExtra(EXTRA_QUESTION_VOTES_COUNT, currentQuestion.getScore());
            answerActivityIntent.putExtra(EXTRA_QUESTION_OWNER_LINK, questionOwner.getLink());

            startActivity(answerActivityIntent);
        };
    }

    private void activateSearchButton() {
        mActivityOcrBinding.btSearch.setOnClickListener(view -> {
            if (TextUtils.isEmpty(Objects.requireNonNull(mActivityOcrBinding.ocrTextInputEditText.getText()).toString())) {
                mActivityOcrBinding.ocrTextInputEditText.setError(getString(R.string.ocr_et_error_message));
            } else {
                mSearchInput = Objects.requireNonNull(mActivityOcrBinding.ocrTextInputEditText.getText()).toString();
                setQuery();
            }
        });
    }

    private void activateScanFab() {
        mActivityOcrBinding.ocrScanFab.setOnClickListener(view -> startCamera());
    }

    private void setQuery() {
        mSearchViewModel.setQuery(mSearchInput);
    }

    private void onLoading() {
        mActivityOcrBinding.ocrProgressBar.setVisibility(View.VISIBLE);
        mActivityOcrBinding.ocrRecyclerview.setVisibility(View.INVISIBLE);
        mActivityOcrBinding.ocrTvError.setVisibility(View.INVISIBLE);
    }

    private void onLoaded() {
        mActivityOcrBinding.ocrProgressBar.setVisibility(View.INVISIBLE);
        mActivityOcrBinding.ocrRecyclerview.setVisibility(View.VISIBLE);
        mActivityOcrBinding.ocrTvError.setVisibility(View.INVISIBLE);
        mActivityOcrBinding.ocrScanFab.setVisibility(View.VISIBLE);
        activateScanFab();
    }

    private void onNoMatchingResult() {
        mActivityOcrBinding.ocrProgressBar.setVisibility(View.INVISIBLE);
        mActivityOcrBinding.ocrRecyclerview.setVisibility(View.INVISIBLE);
        mActivityOcrBinding.ocrTvError.setVisibility(View.VISIBLE);
        mActivityOcrBinding.ocrTvError.setText(R.string.no_matching_result);
    }

    private void onError() {
        mActivityOcrBinding.ocrProgressBar.setVisibility(View.INVISIBLE);
        mActivityOcrBinding.ocrRecyclerview.setVisibility(View.INVISIBLE);
        mActivityOcrBinding.ocrTvError.setVisibility(View.VISIBLE);
        mActivityOcrBinding.ocrTvError.setText(R.string.search_error_message);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}