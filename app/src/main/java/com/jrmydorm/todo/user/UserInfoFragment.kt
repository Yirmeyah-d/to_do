package com.jrmydorm.todo.user

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.modernstorage.mediastore.MediaStoreRepository
import com.jrmydorm.todo.R
import com.jrmydorm.todo.databinding.FragmentUserInfoBinding
import android.content.Intent
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import coil.load
import com.google.android.material.snackbar.Snackbar
import com.google.modernstorage.mediastore.FileType
import com.google.modernstorage.mediastore.SharedPrimary
import com.jrmydorm.todo.models.UserInfo
import com.jrmydorm.todo.network.Api
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.*

class UserInfoFragment : Fragment() {
    private lateinit var photoUri: Uri
    private val viewModel: UserInfoViewModel by viewModels()
    private val mediaStore by lazy { MediaStoreRepository(activity!!) }
    private var _binding: FragmentUserInfoBinding? = null
    private val binding get() = _binding!!

    private fun showExplanation() {
        // ici on construit une pop-up système (Dialog) pour expliquer la nécessité de la demande de permission
        AlertDialog.Builder(activity)
            .setMessage("🥺 On a besoin de la caméra, vraiment! 👉👈")
            .setPositiveButton("Bon, ok") { _, _ -> launchAppSettings() }
            .setNegativeButton("Nope") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun launchCameraWithPermission() {
        val camPermission = Manifest.permission.CAMERA
        val permissionStatus = ContextCompat.checkSelfPermission(activity!!, camPermission)
        val isAlreadyAccepted = permissionStatus == PackageManager.PERMISSION_GRANTED
        val isExplanationNeeded = shouldShowRequestPermissionRationale(camPermission)
        when {
            isAlreadyAccepted -> launchCamera()
            isExplanationNeeded -> showExplanation()
            else -> cameraPermissionLauncher.launch(camPermission)
        }
    }

    private fun launchAppSettings() {
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", this.activity?.packageName, null)
        )
        startActivity(intent)
    }

    private fun handleImage(imageUri: Uri) {
        lifecycleScope.launch {
            viewModel.uploadAvatar(convert(imageUri))
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

    private fun launchCamera() {
        cameraLauncher.launch(photoUri)
    }

    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                handleImage(uri)
            }
            else Snackbar.make(binding.root, "Échec!", Snackbar.LENGTH_LONG)
        }

    private fun launchGallery(){
        galleryLauncher.launch("image/*")
    }


    private fun convert(uri: Uri): MultipartBody.Part {
        return MultipartBody.Part.createFormData(
            name = "avatar",
            filename = "temp.jpeg",
            body = this.activity?.contentResolver?.openInputStream(uri)!!.readBytes()
                .toRequestBody()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserInfoBinding.inflate(inflater, container, false)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            val userInfo = Api.userWebService.getInfo().body()!!
            binding.editEmailTxtview.setText(userInfo.email)
            binding.editLastnameTxtview.setText(userInfo.lastName)
            binding.editFirstnameTxtview.setText(userInfo.firstName)

            val take_picture_button = binding.takePictureButton
            take_picture_button.setOnClickListener {
                launchCameraWithPermission()
            }

            binding.uploadImageButton.setOnClickListener {
                launchGallery()
            }

            val upload_image_button = binding.uploadImageButton
            upload_image_button.setOnClickListener {
                launchGallery()
            }

            binding.editUserInfoButton.setOnClickListener {
                val email = binding.editEmailTxtview.text.toString()
                val lastname = binding.editLastnameTxtview.text.toString()
                val firstname = binding.editFirstnameTxtview.text.toString()

                if (email.isEmpty() || lastname.isEmpty() || firstname.isEmpty()) {
                    Toast.makeText(context, "Veuillez tout remplir", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(context, "Informations modifiées avec succès", Toast.LENGTH_LONG).show()
                    viewModel.updateUserInfo(UserInfo(email, firstname, lastname, userInfo.avatar))
                    binding.editEmailTxtview.clearFocus()
                    binding.editLastnameTxtview.clearFocus()
                    binding.editFirstnameTxtview.clearFocus()
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            photoUri = mediaStore.createMediaUri(
                filename = "picture-${UUID.randomUUID()}.jpg",
                type = FileType.IMAGE,
                location = SharedPrimary
            ).getOrThrow()
        }

        lifecycleScope.launch {
            viewModel.userInfo.collectLatest{   userInfo ->
                binding.userAvatar.load(userInfo?.avatar) {
                    error(R.drawable.ic_launcher_background)
                }
            }
        }
    }

}