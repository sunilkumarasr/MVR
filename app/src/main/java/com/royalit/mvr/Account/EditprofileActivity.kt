package com.royalit.mvr.Account

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.Manifest.permission.READ_MEDIA_VIDEO
import android.Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.royalit.mvr.Config.Preferences
import com.royalit.mvr.Config.ViewController
import com.royalit.mvr.Model.ProfileModel
import com.royalit.mvr.Model.TransactionsModel
import com.royalit.mvr.Model.UpdateProfileResponse
import com.royalit.mvr.NavigationActivity
import com.royalit.mvr.R
import com.royalit.mvr.Retrofit.RetrofitClient
import com.royalit.mvr.databinding.ActivityEditprofileBinding
import com.royalit.mvr.databinding.ActivityProfileBinding
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class EditprofileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditprofileBinding


    //image selection
    private val IMAGE_PICK_CODE = 1000
    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityEditprofileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        toolbarSetup()


        getProfileApi()


        binding.chooseFile.setOnClickListener {
            val intent = Intent()
            intent.setType("image/*")
            intent.setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1)
        }

        binding.btnSignin.setOnClickListener {
            if(!ViewController.noInterNetConnectivity(applicationContext)){
                ViewController.showToast(applicationContext, "Please check your connection ")
            }else{
                editProfileApi()
            }
        }

    }



    fun toolbarSetup(){
        val toolbar = binding.materialtoolbar
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = "Edit Profile"
        }
        // Set Navigation Icon Color
        toolbar.navigationIcon?.let { icon ->
            icon.setColorFilter(
                resources.getColor(android.R.color.white, theme),
                PorterDuff.Mode.SRC_ATOP
            )
        }
        // Set Navigation Click Listener
        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun getProfileApi() {
        val userId = Preferences.loadStringValue(this@EditprofileActivity, Preferences.userId, "")

        Log.e("userId_", userId.toString())

        ViewController.showLoading(this@EditprofileActivity)
        val apiInterface = RetrofitClient.apiInterface
        apiInterface.getProfileApi(userId).enqueue(object :
            Callback<ProfileModel> {
            override fun onResponse(call: Call<ProfileModel>, response: Response<ProfileModel>) {
                ViewController.hideLoading()
                if (response.isSuccessful) {
                    val rsp = response.body()
                    if (rsp != null) {
                        val translist = response.body()
                        if (translist != null) {
                           binding.txtName.text = translist.user?.name ?:""
                           binding.txtEmailid.text = translist.user?.email ?:""
                            binding.editName.setText(translist.user?.name ?: "")
                            binding.editEmail.setText(translist.user?.email ?: "")
                            binding.editPhone.setText(translist.user?.phone ?: "")
                            binding.editAddress.setText(translist.user?.address ?: "")
                            binding.editCity.setText(translist.user?.city ?: "")
                            binding.editCity.setText(translist.user?.city ?: "")

                            Glide.with(this@EditprofileActivity)
                                .load(translist.user?.image ?: "")
                                .placeholder(R.drawable.profileimg)
                                .error(R.drawable.profileimg)
                                .into(binding.profilePic)

                        }
                    }
                } else {
                    ViewController.showToast(this@EditprofileActivity, "Error: ${response.code()}")
                }
            }
            override fun onFailure(call: Call<ProfileModel>, t: Throwable) {
                ViewController.hideLoading()
                ViewController.showToast(this@EditprofileActivity, "Try again: ${t.message}")
            }
        })
    }


    private fun editProfileApi() {
        val userId = Preferences.loadStringValue(this@EditprofileActivity, Preferences.userId, "")

        val name = binding.editName.text?.trim().toString()
        val email = binding.editEmail.text?.trim().toString()
        val mobile = binding.editPhone.text?.trim().toString()
        val address = binding.editAddress.text?.trim().toString()
        val city = binding.editCity.text?.trim().toString()

        if(name.isEmpty()){
            ViewController.showToast(applicationContext, "Enter Name")
            return
        }
        if(email.isEmpty()){
            ViewController.showToast(applicationContext, "Enter Email")
            return
        }
        if(mobile.isEmpty()){
            ViewController.showToast(applicationContext, "Enter Mobile Number")
            return
        }
        if(address.isEmpty()){
            ViewController.showToast(applicationContext, "Enter Address")
            return
        }
        if(city.isEmpty()){
            ViewController.showToast(applicationContext, "Enter City")
            return
        }

        // Prepare form data
        val userId_ = RequestBody.create(MultipartBody.FORM, userId.toString())
        val name_ = RequestBody.create(MultipartBody.FORM, name)
        val email_ = RequestBody.create(MultipartBody.FORM, email)
        val mobile_ = RequestBody.create(MultipartBody.FORM, mobile)
        val address_ = RequestBody.create(MultipartBody.FORM, address)
        val city_ = RequestBody.create(MultipartBody.FORM, city)

        val body: MultipartBody.Part
        if (selectedImageUri != null) {
            val file = File(getRealPathFromURI(selectedImageUri!!))
            val requestFile = RequestBody.create(MultipartBody.FORM, file)
            body = MultipartBody.Part.createFormData("image", file.name, requestFile)
        } else {
            //send empty image
            body = createEmptyImagePart()
        }

        ViewController.showLoading(this@EditprofileActivity)
        val apiInterface = RetrofitClient.apiInterface
        apiInterface.updateProfileApi(userId_, name_, email_, mobile_ , address_, city_,  body)
            .enqueue(object : Callback<UpdateProfileResponse> {
                override fun onResponse(
                    call: Call<UpdateProfileResponse>,
                    response: Response<UpdateProfileResponse>
                ) {
                    ViewController.hideLoading()
                    if (response.isSuccessful) {
                        val addResponse = response.body()
                        if (addResponse != null) {
                            startActivity(
                                Intent(this@EditprofileActivity, NavigationActivity::class.java)
                            )
                        }
                    } else {
                        ViewController.showToast(applicationContext, "Error: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<UpdateProfileResponse>, t: Throwable) {
                    ViewController.hideLoading()
                    ViewController.showToast(applicationContext, "Try again: ${t.message}")
                    Log.e("Tryagain:_ ", t.message.toString())
                }
            })


    }


    private fun getRealPathFromURI(uri: Uri): String {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            return cursor.getString(columnIndex)
        }
        return ""
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //single image selection
        if (data != null) {
            selectedImageUri = data.data!!
            val file = File(getRealPathFromURI(selectedImageUri!!))
            binding.chooseFile.text = file.name
            binding.profilePic.setImageURI(selectedImageUri)
        }

    }

    //update profile
    private fun createEmptyImagePart(): MultipartBody.Part {
        // Create an empty RequestBody
        val requestFile = RequestBody.create(MultipartBody.FORM, ByteArray(0))
        return MultipartBody.Part.createFormData("image", "", requestFile)
    }


}