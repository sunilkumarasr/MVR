package com.royalit.mvr.Account

import android.app.AlertDialog
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.royalit.mvr.Adapters.ProjectAmenitiesAdapter
import com.royalit.mvr.Adapters.ProjectGalleryAdapter
import com.royalit.mvr.Adapters.ProjectHighlightsAdapter
import com.royalit.mvr.Config.Preferences
import com.royalit.mvr.Config.ViewController
import com.royalit.mvr.Model.Amenity
import com.royalit.mvr.Model.GalleryImage
import com.royalit.mvr.Model.HelpSupportResponse
import com.royalit.mvr.Model.Highlight
import com.royalit.mvr.Model.PlotsStatusResponse
import com.royalit.mvr.Model.ProjectData
import com.royalit.mvr.Model.ProjectModel
import com.royalit.mvr.Model.WalletResponse
import com.royalit.mvr.NavigationActivity
import com.royalit.mvr.R
import com.royalit.mvr.Retrofit.RetrofitClient
import com.royalit.mvr.databinding.ActivityProjectDetails2Binding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProjectDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProjectDetails2Binding

    private lateinit var downloadReceiver: BroadcastReceiver
    private var refid: Long = -1

    var brochureUrl: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProjectDetails2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        toolbarSetup()

        val pid = intent.getStringExtra("pid")  // Retrieve the pid string passed from the previous activity

        if(!ViewController.noInterNetConnectivity(this@ProjectDetailsActivity)){
            ViewController.showToast(this@ProjectDetailsActivity, "Please check your connection ")
        }else{
            getPlotsStatusApi(pid)
            projectListApi(pid)
        }

        binding.enquiry.setOnClickListener {
            enquiryPopup()
        }

        binding.txtViewMap.setOnClickListener {
            val intent = Intent(this@ProjectDetailsActivity, ProjectDetailsWebActivity::class.java)
            intent.putExtra("pid", pid)
            startActivity(intent)
        }

        binding.txtBroucher.setOnClickListener {
            val fileUrl = RetrofitClient.Image_Path + brochureUrl
            downloadFilePdf(fileUrl)
        }

    }


    fun toolbarSetup(){
        val toolbar = binding.materialtoolbar
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = "Project Details"
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

    private fun getPlotsStatusApi(pid: String?) {
        val apiInterface = RetrofitClient.apiInterface
        apiInterface.getPlotsStatusApi(pid).enqueue(object :
            Callback<PlotsStatusResponse> {
            override fun onResponse(call: Call<PlotsStatusResponse>, response: Response<PlotsStatusResponse>) {
                if (response.isSuccessful) {
                    val rsp = response.body()
                    if (rsp != null) {

                        if (rsp.status.equals("success")) {
                            binding.txtTotalPlots.text = "Total Plots: "+rsp.user?.total_plots.toString()
                            binding.txtAllotedPlots.text = "Plots Alloted: "+rsp.user?.booked_plots.toString()
                            binding.txtAvailablePlots.text = "Plots Available: "+rsp.user?.available_plots.toString()
                        }

                    }
                }
            }
            override fun onFailure(call: Call<PlotsStatusResponse>, t: Throwable) {
                ViewController.showToast(this@ProjectDetailsActivity, "Try again: ${t.message}")
            }
        })
    }

    private fun projectListApi(pid: String?) {
        ViewController.showLoading(this@ProjectDetailsActivity)

        val apiInterface = RetrofitClient.apiInterface
        apiInterface.projectListApi().enqueue(object : Callback<ProjectModel> {
            override fun onResponse(call: Call<ProjectModel>, response: Response<ProjectModel>) {
                ViewController.hideLoading()
                if (response.isSuccessful) {
                    val rsp = response.body()
                    if (rsp != null) {
                        val translist = response.body()
                        if (translist != null) {
                            // Iterate through the list of ProjectData
                            for (i in translist.data!!.indices) {
                                if (translist.data[i].pid == pid) {
                                    // When a matching pid is found, call projectDataSet with the matched ProjectData
                                    projectDataSet(translist.data[i])
                                    break  // Exit the loop after finding the first match
                                }
                            }
                        }
                    }
                }
            }
            override fun onFailure(call: Call<ProjectModel>, t: Throwable) {
                ViewController.hideLoading()
                ViewController.showToast(this@ProjectDetailsActivity, "Try again: ${t.message}")
            }
        })
    }
    private fun projectDataSet(list: ProjectData) {
        Glide.with(this)  // "this" refers to the context (Activity or Fragment)
            .load(RetrofitClient.Image_Path+list.banner_image)  // Image URL
            .into(binding.imageView)
        binding.txtName.text = list.projectName
        binding.txtAbout.text = HtmlCompat.fromHtml(list.aboutProject.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY)

        brochureUrl = list.broucher.toString()

        Glide.with(this)  // "this" refers to the context (Activity or Fragment)
            .load(RetrofitClient.Image_Path+list.plotImage)  // Image URL
            .into(binding.imgroot)


        //highlight
        if (!list.highlight.isNullOrEmpty()) {
            projectHighlights(list.highlight)
        }else{
            binding.recyclerViewHighlights.visibility = View.GONE
            binding.txtNoHighlights.visibility = View.VISIBLE
        }
        //amenities
        if (!list.amenities.isNullOrEmpty()) {
            projectAmenities(list.amenities)
        }else{
            binding.recyclerViewAmenities.visibility = View.GONE
            binding.txtNoAmenities.visibility = View.VISIBLE
        }
        //gallery
        if (!list.gallery.isNullOrEmpty()) {
            projectGallery(list.gallery)
        }else{
            binding.recyclerViewGallery.visibility = View.GONE
            binding.txtNoGallery.visibility = View.VISIBLE
        }


        binding.relativeVideoPlay.setOnClickListener {
            videoPopup(list.video)
        }

        mapView(list.map)
    }


    private fun projectHighlights(highlight: List<Highlight>) {
        binding.recyclerViewHighlights.layoutManager = GridLayoutManager(this@ProjectDetailsActivity, 3)
        binding.recyclerViewHighlights.adapter = ProjectHighlightsAdapter(highlight) { item ->
        }
    }

    private fun projectAmenities(amenities: List<Amenity>) {
        binding.recyclerViewAmenities.layoutManager = GridLayoutManager(this@ProjectDetailsActivity, 3)
        binding.recyclerViewAmenities.adapter = ProjectAmenitiesAdapter(amenities) { item ->
        }
    }

    private fun projectGallery(gallery: List<GalleryImage>) {
        binding.recyclerViewGallery.layoutManager = GridLayoutManager(this@ProjectDetailsActivity, 3)
        binding.recyclerViewGallery.adapter = ProjectGalleryAdapter(gallery) { item ->
        }
    }

    private fun videoPopup(video: String?) {
        val popupView = LayoutInflater.from(this@ProjectDetailsActivity).inflate(R.layout.popup_video, null)
        val closeButton: ImageView = popupView.findViewById(R.id.close_button)
        val webVideo: WebView = popupView.findViewById(R.id.webVideo)
        val loadingIndicator: ProgressBar = popupView.findViewById(R.id.loadingIndicator)
        val popupDialog = AlertDialog.Builder(this@ProjectDetailsActivity)
            .setView(popupView)
            .create()

        popupDialog.show()

        closeButton.setOnClickListener {
            popupDialog.dismiss()
        }

        webVideo.settings.javaScriptEnabled = true
        webVideo.settings.setSupportZoom(true)
        webVideo.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: android.graphics.Bitmap?) {
                super.onPageStarted(view, url, favicon)
                loadingIndicator.visibility = View.VISIBLE  // Show loading indicator
            }
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                loadingIndicator.visibility = View.GONE  // Hide loading indicator
            }
        }

        webVideo.webChromeClient = WebChromeClient()
        if (video != null) {
            webVideo.loadUrl(video)
        }
    }


    private fun mapView(map: String?) {
        binding.webMap.settings.javaScriptEnabled = true
        binding.webMap.settings.setSupportZoom(true)
        binding.webMap.webViewClient = WebViewClient()
        binding.webMap.webChromeClient = WebChromeClient()
        if (map != null) {
            binding.webMap.loadUrl(map)
        }
    }

    private fun enquiryPopup() {
        val popupView = LayoutInflater.from(this@ProjectDetailsActivity).inflate(R.layout.popup_layout, null)
        val closeButton: ImageView = popupView.findViewById(R.id.close_button)
        val edit_name: EditText = popupView.findViewById(R.id.edit_name)
        val edit_phone: EditText = popupView.findViewById(R.id.edit_phone)
        val edit_email: EditText = popupView.findViewById(R.id.edit_email)
        val edit_subject: EditText = popupView.findViewById(R.id.edit_subject)
        val edit_message: EditText = popupView.findViewById(R.id.edit_message)
        val btn_signin: TextView = popupView.findViewById(R.id.btn_signin)
        val popupDialog = AlertDialog.Builder(this@ProjectDetailsActivity)
            .setView(popupView)
            .create()

        popupDialog.show()

        btn_signin.setOnClickListener {
            if(!ViewController.noInterNetConnectivity(this@ProjectDetailsActivity)){
                ViewController.showToast(this@ProjectDetailsActivity, "Please check your connection ")
            }else{
                val userId = Preferences.loadStringValue(this@ProjectDetailsActivity, Preferences.userId, "")

                val name_ = edit_name.text?.trim().toString()
                val mobile_ = edit_phone.text?.trim().toString()
                val email_ = edit_email.text?.trim().toString()
                val subject_ = edit_subject.text?.trim().toString()
                val message_ = edit_message.text?.trim().toString()

                if(name_.isEmpty()){
                    ViewController.showToast(this@ProjectDetailsActivity, "Enter Name")
                }
                else if(mobile_.isEmpty()){
                    ViewController.showToast(this@ProjectDetailsActivity, "Enter Mobile Number")
                }
                else if (!validateMobileNumber(mobile_)) {
                    ViewController.showToast(this@ProjectDetailsActivity, "Enter Valid Mobile Number")
                }
                else if(email_.isEmpty()){
                    ViewController.showToast(this@ProjectDetailsActivity, "Enter Email")
                }
                else if (!validateEmail(email_)) {
                    ViewController.showToast(this@ProjectDetailsActivity, "Enter Valid Email")
                }
                else if(subject_.isEmpty()){
                    ViewController.showToast(this@ProjectDetailsActivity, "Enter Subject")
                }
                else if (message_.isEmpty()) {
                    ViewController.showToast(this@ProjectDetailsActivity, "Enter Message")
                }else{
                    val apiInterface = RetrofitClient.apiInterface
                    apiInterface.enqueryApi("1", name_, mobile_, email_, subject_, message_, userId.toString()).enqueue(object :
                        Callback<HelpSupportResponse> {
                        override fun onResponse(call: Call<HelpSupportResponse>, response: Response<HelpSupportResponse>) {
                            ViewController.hideLoading()
                            if (response.isSuccessful) {
                                val loginResponse = response.body()
                                if (loginResponse != null && loginResponse.status == "true") {
                                    ViewController.showToast(this@ProjectDetailsActivity, "Project Enquiry Submitted Successfully")
                                    startActivity(Intent(this@ProjectDetailsActivity, NavigationActivity::class.java))
                                } else {
                                    loginResponse?.message?.let {
                                        ViewController.showToast(this@ProjectDetailsActivity, it)
                                    }
                                }
                            } else {
                                ViewController.showToast(this@ProjectDetailsActivity, "Error: ${response.code()}")
                            }
                        }
                        override fun onFailure(call: Call<HelpSupportResponse>, t: Throwable) {
                            ViewController.hideLoading()
                            ViewController.showToast(this@ProjectDetailsActivity, "Try again: ${t.message}")
                        }
                    })
                }




            }
        }

        closeButton.setOnClickListener {
            popupDialog.dismiss()
        }

    }

    private fun validateMobileNumber(mobile: String): Boolean {
        val mobilePattern = "^[6-9][0-9]{9}\$"
        return Patterns.PHONE.matcher(mobile).matches() && mobile.matches(Regex(mobilePattern))
    }

    private fun validateEmail(email: String): Boolean {
        val emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
        return email.matches(Regex(emailPattern))
    }


    private fun downloadFilePdf(fileUrl: String) {
        val url = Uri.parse(fileUrl)
        val request = DownloadManager.Request(url)
            .setTitle("Broucher Download")
            .setDescription("Downloading pdf file using Download Manager.")
            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
            .setAllowedOverRoaming(false)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationInExternalPublicDir(android.os.Environment.DIRECTORY_DOWNLOADS, "pdf_name.pdf")

        val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        refid = downloadManager.enqueue(request)

        Toast.makeText(this, "Download..", Toast.LENGTH_SHORT).show()

        // Initialize the receiver and its functionality
        downloadReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val downloadId = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                if (downloadId == refid) {
                    Toast.makeText(context, "Download Completed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


}