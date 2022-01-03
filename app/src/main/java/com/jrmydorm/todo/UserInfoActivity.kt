package com.jrmydorm.todo

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import coil.load
import coil.transform.CircleCropTransformation
import com.google.android.material.snackbar.Snackbar
import com.google.modernstorage.mediastore.FileType
import com.google.modernstorage.mediastore.MediaStoreRepository
import com.google.modernstorage.mediastore.SharedPrimary
import com.jrmydorm.todo.databinding.ActivityUserInfoBinding
import com.jrmydorm.todo.network.Api
import com.jrmydorm.todo.user.UserInfoViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.*

class UserInfoActivity : AppCompatActivity() {

    private lateinit var photoUri: Uri
    private lateinit var binding: ActivityUserInfoBinding
    private val viewModel: UserInfoViewModel by viewModels()

    val mediaStore by lazy { MediaStoreRepository(this) }


    private fun showExplanation() {
        // ici on construit une pop-up système (Dialog) pour expliquer la nécessité de la demande de permission
        AlertDialog.Builder(this)
            .setMessage("🥺 On a besoin de la caméra, vraiment! 👉👈")
            .setPositiveButton("Bon, ok") { _, _ -> launchAppSettings() }
            .setNegativeButton("Nope") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun launchCameraWithPermission() {
        val camPermission = Manifest.permission.CAMERA
        val permissionStatus = checkSelfPermission(camPermission)
        val isAlreadyAccepted = permissionStatus == PackageManager.PERMISSION_GRANTED
        val isExplanationNeeded = shouldShowRequestPermissionRationale(camPermission)
        when {
            isAlreadyAccepted -> launchCamera()
            isExplanationNeeded -> showExplanation()
            else -> cameraPermissionLauncher.launch(camPermission)
        }
    }


    private val cameraPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { accepted ->
            if (accepted) {
                launchCamera()
            } else {
                showExplanation()
            }
        }


    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { accepted ->
            if (accepted) handleImage(photoUri)
            else Snackbar.make(binding.root, "Échec!", Snackbar.LENGTH_LONG)
        }


    private fun handleImage(imageUri: Uri) {
        lifecycleScope.launch {
            viewModel.uploadAvatar(contentResolver.openInputStream(imageUri)!!.readBytes())
        }
    }

    private fun launchCamera() {
        cameraLauncher.launch(photoUri)
    }

    private fun launchAppSettings() {
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", this.packageName, null)
        )
        startActivity(intent)
    }


    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                handleImage(uri)
                finish();
            }

        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        lifecycleScope.launch {
            viewModel.loadUserInfo()
            viewModel.userInfo.collectLatest { newUserInfo ->
                binding.userNameTextView.text = "${newUserInfo?.firstName} ${newUserInfo?.lastName}"
                binding.userEmailTextView.text = "${newUserInfo?.email}"
                binding.imageView.load(newUserInfo?.avatar) {
                    transformations(CircleCropTransformation())
                    error(R.drawable.ic_launcher_background)
                }
            }
        }

        val take_picture_button = binding.takePictureButton
        take_picture_button.setOnClickListener {
            launchCameraWithPermission()
        }

        val upload_image_button = binding.uploadImageButton
        upload_image_button.setOnClickListener {
            galleryLauncher.launch("image/*")
        }

        lifecycleScope.launchWhenStarted {
            photoUri = mediaStore.createMediaUri(
                filename = "picture-${UUID.randomUUID()}.jpg",
                type = FileType.IMAGE,
                location = SharedPrimary
            ).getOrThrow()
        }

        binding.editUserInfo.setOnClickListener {
            //val intent = Intent(activity, FormActivity::class.java)
        }

    }
    }