package com.josycom.mayorjay.flowoverstack.ui.activity

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.josycom.mayorjay.flowoverstack.R
import com.josycom.mayorjay.flowoverstack.adapters.SearchAdapter
import com.josycom.mayorjay.flowoverstack.databinding.ActivityOcrBinding
import com.josycom.mayorjay.flowoverstack.model.Question
import com.josycom.mayorjay.flowoverstack.util.AppConstants
import com.josycom.mayorjay.flowoverstack.util.AppUtils
import com.josycom.mayorjay.flowoverstack.viewmodel.CustomSearchViewModelFactory
import com.josycom.mayorjay.flowoverstack.viewmodel.SearchViewModel
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import dagger.android.AndroidInjection
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Date
import javax.inject.Inject

class OcrActivity : AppCompatActivity() {

    private lateinit var mActivityOcrBinding: ActivityOcrBinding
    private var mPhotoPath: String? = null
    private val requiredPermissions = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    private var mQuestions: List<Question>? = null
    private var mSearchInput: String? = null
    private lateinit var mSearchViewModel: SearchViewModel
    private lateinit var mOnClickListener: View.OnClickListener
    @Inject
    lateinit var viewModelFactory: CustomSearchViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        mActivityOcrBinding = ActivityOcrBinding.inflate(layoutInflater)
        setContentView(mActivityOcrBinding.root)
        checkPermissionAndStartCamera()
        activateViewHolder()
        setupRecyclerView()
        hideAndShowScrollFab()
        activateSearchButton()
        activateScanFab()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                returnToMainActivity()
                finish()
            }
        }
    }

    private fun checkPermissionAndStartCamera() {
        if (allPermissionsGranted()) {
            startCamera()
            mActivityOcrBinding.ivCroppedImage.setOnClickListener { startCamera() }
        } else if (shouldShowRequestPermissionRationale()) {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("App Permissions")
                    .setMessage("This application requires access to your camera and storage to function!")
                    .setNegativeButton("No") { _: DialogInterface?, _: Int -> startActivity(Intent(applicationContext, MainActivity::class.java)) }
                    .setPositiveButton("Ask Me") { _: DialogInterface?, _: Int ->
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            requestPermissions(requiredPermissions, PERMISSION_REQUEST_CODE)
                        }
                    }.show()
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(requiredPermissions, PERMISSION_REQUEST_CODE)
            }
        }
    }

    private fun startCamera() {
        mActivityOcrBinding.apply {
            ocrTextInputLayout.visibility = View.GONE
            ocrTextInputEditText.visibility = View.GONE
            btSearch.visibility = View.GONE
            ocrRecyclerview.visibility = View.GONE
            ivCroppedImage.visibility = View.GONE
        }
        val captureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (captureIntent.resolveActivity(packageManager) != null) {
            var photo: File? = null
            try {
                photo = createImageFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            if (photo != null) {
                val photoUri = FileProvider.getUriForFile(this, "com.josycom.mayorjay.flowoverstack.fileprovider", photo)
                captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                startActivityForResult(captureIntent, CAMERA_REQUEST_CODE)
            }
        }
    }

    private fun createImageFile(): File? {
        return try {
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val imageFileName = "JPEG_" + timeStamp + "_"
            val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            val image = File.createTempFile(imageFileName, ".jpg", storageDir)
            mPhotoPath = image.absolutePath
            image
        } catch (ex: IOException) {
            ex.printStackTrace()
            null
        }
    }

    private fun returnToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        Snackbar.make(findViewById(R.id.main_root_layout), "Permission Denied", Snackbar.LENGTH_SHORT).show()
    }

    private fun allPermissionsGranted(): Boolean {
        var granted = false
        for (item in requiredPermissions) {
            granted = ContextCompat.checkSelfPermission(this, item) == PackageManager.PERMISSION_GRANTED
        }
        return granted
    }

    private fun shouldShowRequestPermissionRationale(): Boolean {
        var shouldRequest = false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (item in requiredPermissions) {
                shouldRequest = shouldShowRequestPermissionRationale(item)
            }
        }
        return shouldRequest
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mActivityOcrBinding.ivCroppedImage.visibility = View.VISIBLE
        val bitmap = BitmapFactory.decodeFile(mPhotoPath)
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode == RESULT_OK && bitmap != null && mPhotoPath != null) {
                val file = File(mPhotoPath!!)
                val uri = Uri.fromFile(file)
                CropImage.activity(uri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(this)
            } else if (bitmap == null) {
                startActivity(Intent(this, MainActivity::class.java))
            }
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == RESULT_OK) {
                val resultUri = result.uri
                Glide.with(this)
                        .load(resultUri)
                        .into(mActivityOcrBinding.ivCroppedImage)
                analyseImage(resultUri)
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
                error.printStackTrace()
                Toast.makeText(this, "Oops! an error occurred, try again", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun analyseImage(resultUri: Uri) {
        mActivityOcrBinding.btRecognise.visibility = View.VISIBLE
        mActivityOcrBinding.btRecognise.setOnClickListener {
            mActivityOcrBinding.ocrProgressBar.visibility = View.VISIBLE
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, resultUri)
                val image = InputImage.fromBitmap(bitmap, 0)
                val recognizer = TextRecognition.getClient()
                recognizer.process(image)
                        .addOnSuccessListener { text: Text ->
                            mActivityOcrBinding.ocrProgressBar.visibility = View.GONE
                            mActivityOcrBinding.btRecognise.visibility = View.GONE
                            processTextRecognitionResult(text)
                        }
                        .addOnFailureListener {
                            mActivityOcrBinding.ocrProgressBar.visibility = View.GONE
                            Toast.makeText(applicationContext, "Oops!, that text could not be recognized. Scan again", Toast.LENGTH_LONG).show()
                        }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun processTextRecognitionResult(text: Text) {
        val blocks = text.textBlocks
        if (blocks.size == 0) {
            Toast.makeText(applicationContext, "No text found, scan again", Toast.LENGTH_LONG).show()
            startActivity(Intent(this, MainActivity::class.java))
            return
        }
        mActivityOcrBinding.apply {
            ocrTextInputLayout.visibility = View.VISIBLE
            ocrTextInputEditText.visibility = View.VISIBLE
            btSearch.visibility = View.VISIBLE
            ocrTextInputEditText.setText(text.text)
        }
    }

    private fun setupRecyclerView() {
        mActivityOcrBinding.apply {
            ocrRecyclerview.layoutManager = LinearLayoutManager(this@OcrActivity)
            ocrRecyclerview.setHasFixedSize(true)
            ocrRecyclerview.itemAnimator = DefaultItemAnimator()
        }
        val searchAdapter = SearchAdapter()
        mSearchViewModel = ViewModelProvider(this, viewModelFactory).get(SearchViewModel::class.java)
        mSearchViewModel.responseLiveData.observe(this, {
            when (it.networkState) {
                AppConstants.LOADING -> onLoading()
                AppConstants.LOADED -> {
                    onLoaded()
                    mQuestions = it.questions
                    searchAdapter.setQuestions(it.questions)
                }
                AppConstants.NO_MATCHING_RESULT -> onNoMatchingResult()
                AppConstants.FAILED -> onError()
            }
        })
        mActivityOcrBinding.ocrRecyclerview.adapter = searchAdapter
        searchAdapter.setOnClickListener(mOnClickListener)
    }

    private fun hideAndShowScrollFab() {
        mActivityOcrBinding.apply {
            ocrScrollUpFab.visibility = View.INVISIBLE
            ocrNestedScrollview.setOnScrollChangeListener { _: NestedScrollView?, _: Int, scrollY: Int, _: Int, _: Int ->
                if (scrollY > 0) {
                    ocrScrollUpFab.visibility = View.VISIBLE
                } else {
                    ocrScrollUpFab.visibility = View.INVISIBLE
                }
            }
            ocrScrollUpFab.setOnClickListener { ocrNestedScrollview.scrollTo(0, 0) }
        }
    }

    private fun activateViewHolder() {
        mOnClickListener = View.OnClickListener {
            val viewHolder = it.tag as RecyclerView.ViewHolder
            val position = viewHolder.adapterPosition
            Intent(applicationContext, AnswerActivity::class.java).apply {
                val currentQuestion = mQuestions!![position]
                putExtra(AppConstants.EXTRA_QUESTION_TITLE, currentQuestion.title)
                putExtra(AppConstants.EXTRA_QUESTION_DATE, AppUtils.toNormalDate(currentQuestion.creationDate!!.toLong()))
                putExtra(AppConstants.EXTRA_QUESTION_FULL_TEXT, currentQuestion.body)
                putExtra(AppConstants.EXTRA_QUESTION_ANSWERS_COUNT, currentQuestion.answerCount)
                putExtra(AppConstants.EXTRA_QUESTION_ID, currentQuestion.questionId)
                putExtra(AppConstants.EXTRA_QUESTION_VOTES_COUNT, currentQuestion.score)
                val questionOwner = currentQuestion.owner
                if (questionOwner != null) {
                    putExtra(AppConstants.EXTRA_QUESTION_NAME, questionOwner.displayName)
                    putExtra(AppConstants.EXTRA_AVATAR_ADDRESS, questionOwner.profileImage)
                    putExtra(AppConstants.EXTRA_QUESTION_OWNER_LINK, questionOwner.link)
                }
                startActivity(this)
            }
        }
    }

    private fun activateSearchButton() {
        mActivityOcrBinding.btSearch.setOnClickListener {
            if (TextUtils.isEmpty(mActivityOcrBinding.ocrTextInputEditText.text.toString())) {
                mActivityOcrBinding.ocrTextInputEditText.error = getString(R.string.ocr_et_error_message)
            } else {
                mSearchInput = mActivityOcrBinding.ocrTextInputEditText.text.toString()
                setQuery()
            }
        }
    }

    private fun activateScanFab() {
        mActivityOcrBinding.ocrScanFab.setOnClickListener { startCamera() }
    }

    private fun setQuery() {
        mSearchViewModel.setQuery(mSearchInput!!)
    }

    private fun onLoading() = mActivityOcrBinding.apply {
        ocrProgressBar.visibility = View.VISIBLE
        ocrRecyclerview.visibility = View.INVISIBLE
        ocrTvError.visibility = View.INVISIBLE
    }

    private fun onLoaded() = mActivityOcrBinding.apply {
        ocrProgressBar.visibility = View.INVISIBLE
        ocrRecyclerview.visibility = View.VISIBLE
        ocrTvError.visibility = View.INVISIBLE
        ocrScanFab.visibility = View.VISIBLE
        btSearch.visibility = View.INVISIBLE
        ocrTextInputEditText.visibility = View.INVISIBLE
    }

    private fun onNoMatchingResult() = mActivityOcrBinding.apply {
        ocrProgressBar.visibility = View.INVISIBLE
        ocrRecyclerview.visibility = View.INVISIBLE
        ocrTvError.visibility = View.VISIBLE
        ocrTvError.setText(R.string.no_matching_result)
    }

    private fun onError() = mActivityOcrBinding.apply {
        ocrProgressBar.visibility = View.INVISIBLE
        ocrRecyclerview.visibility = View.INVISIBLE
        ocrTvError.visibility = View.VISIBLE
        ocrTvError.setText(R.string.search_error_message)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 100
        private const val CAMERA_REQUEST_CODE = 101
    }
}