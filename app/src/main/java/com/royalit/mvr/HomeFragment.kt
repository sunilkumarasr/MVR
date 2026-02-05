package com.royalit.mvr

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Spannable
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.royalit.mvr.Account.ProjectDetailsActivity
import com.royalit.mvr.Account.ProjectDetailsWebActivity
import com.royalit.mvr.Adapters.BannerAdapter
import com.royalit.mvr.Adapters.SaleAdapter
import com.royalit.mvr.Config.Preferences
import com.royalit.mvr.Config.ViewController
import com.royalit.mvr.Model.ProfileModel
import com.royalit.mvr.Model.ProjectData
import com.royalit.mvr.Model.ProjectModel
import com.royalit.mvr.Model.SaleData
import com.royalit.mvr.Model.SaleModel
import com.royalit.mvr.Model.WalletResponse
import com.royalit.mvr.Retrofit.RetrofitClient
import com.royalit.mvr.databinding.FragmentHomeBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.Serializable

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    //banners
    private val handler = Handler(Looper.getMainLooper())
    private var currentPage = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(!ViewController.noInterNetConnectivity(requireActivity())){
            ViewController.showToast(requireActivity(), "Please check your connection ")
        }else{
            BannersListApi()
            saleListApi()
            getProfileApi()
            getWalletApi()
        }

        binding.txtHeading.text = SpannableString("Recent Transactions").apply {
            setSpan(UnderlineSpan(), 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

    }

    private fun BannersListApi() {
        val apiInterface = RetrofitClient.apiInterface
        apiInterface.projectListApi().enqueue(object : Callback<ProjectModel> {
            override fun onResponse(call: Call<ProjectModel>, response: Response<ProjectModel>) {
                if (response.isSuccessful) {
                    val rsp = response.body()
                    if (rsp != null) {
                        val translist = response.body()
                        if (translist != null) {
                            if (!translist.data.isNullOrEmpty()) {
                                projectDataSet(translist.data!!)
                            }
                        }
                    }
                }
            }
            override fun onFailure(call: Call<ProjectModel>, t: Throwable) {
            }
        })
    }
    private fun projectDataSet(list: List<ProjectData>) {
        val adapter = BannerAdapter(list) { projectData ->
            // Navigate to ProjectDetailsActivity
            val intent = Intent(requireActivity(), ProjectDetailsActivity::class.java)
            intent.putExtra("pid", projectData.pid)
            startActivity(intent)
        }
        binding.viewPager.adapter = adapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { _, _ -> }.attach()

        // Auto-slide
        setupAutoSlide(list.size)
    }


    private fun setupAutoSlide(pageCount: Int) {
        val runnable = object : Runnable {
            override fun run() {
                if (currentPage == pageCount) currentPage = 0
                binding.viewPager.setCurrentItem(currentPage++, true)
                handler.postDelayed(this, 3000) // Slide interval: 3 seconds
            }
        }
        handler.post(runnable)
    }




    private fun saleListApi() {
        val userId = Preferences.loadStringValue(requireActivity(), Preferences.userId, "")

        val apiInterface = RetrofitClient.apiInterface
        apiInterface.saleListApi(userId).enqueue(object : Callback<SaleModel> {
            override fun onResponse(call: Call<SaleModel>, response: Response<SaleModel>) {
                if (response.isSuccessful) {
                    val rsp = response.body()
                    if (rsp != null) {
                        val translist = response.body()
                        if (translist != null) {
                            if (!translist.data.isNullOrEmpty()) {
                                binding.recyclerView.visibility = View.VISIBLE
                                binding.noData.visibility = View.GONE
                                saleListDataSet(translist.data!!)
                            }else{
                                binding.recyclerView.visibility = View.GONE
                                binding.noData.visibility = View.VISIBLE
                            }
                        }
                    }
                } else {
                    binding.recyclerView.visibility = View.GONE
                    binding.noData.visibility = View.VISIBLE
                }
            }
            override fun onFailure(call: Call<SaleModel>, t: Throwable) {
                binding.recyclerView.visibility = View.GONE
                binding.noData.visibility = View.VISIBLE
                ViewController.showToast(requireActivity(), "Try again: ${t.message}")
            }

        })
    }
    private fun saleListDataSet(list: List<SaleData>) {
        binding.recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        binding.recyclerView.adapter = SaleAdapter(list) { item ->
            //Toast.makeText(activity, "Clicked: ${item.text}", Toast.LENGTH_SHORT).show()
            val intent = Intent(requireActivity(), SaleHistoryDetailsActivity::class.java)
            intent.putExtra("item", item as Serializable)
            startActivity(intent)
        }
    }

    private fun getProfileApi() {
        val userId = Preferences.loadStringValue(requireActivity(), Preferences.userId, "")

        Log.e("userId_", userId.toString())

        val apiInterface = RetrofitClient.apiInterface
        apiInterface.getProfileApi(userId).enqueue(object :
            Callback<ProfileModel> {
            override fun onResponse(call: Call<ProfileModel>, response: Response<ProfileModel>) {
                if (response.isSuccessful) {
                    val rsp = response.body()
                    if (rsp != null) {
                        val translist = response.body()
                        if (translist != null) {
                            binding.txtName.text = translist.user?.name ?:""
                            binding.txtReferID.text = "ID:"+translist.user?.empId ?:""
                            translist.user?.referralId?.let {
                                Preferences.saveStringValue(requireActivity(), Preferences.referralId,
                                    it
                                )
                            }
                            translist.user?.empId?.let {
                                Preferences.saveStringValue(requireActivity(), Preferences.empId,
                                    it
                                )
                            }

                            val imageUrl = translist.user?.image ?: ""
                            if (imageUrl.isNotEmpty()) {
                                Glide.with(requireContext())
                                    .load(imageUrl)
                                    .placeholder(R.drawable.profileimg)
                                    .error(R.drawable.profileimg)
                                    .into(binding.profilePic)
                            } else {
                                binding.profilePic.setImageResource(R.drawable.profileimg)
                            }

                        }
                    }
                } else {
                    ViewController.showToast(requireActivity(), "Error: ${response.code()}")
                }
            }
            override fun onFailure(call: Call<ProfileModel>, t: Throwable) {
                ViewController.showToast(requireActivity(), "Try again: ${t.message}")
            }
        })
    }
    private fun getWalletApi() {
        val userId = Preferences.loadStringValue(requireActivity(), Preferences.userId, "")

        val apiInterface = RetrofitClient.apiInterface
        apiInterface.getWalletApi(userId).enqueue(object :
            Callback<WalletResponse> {
            override fun onResponse(call: Call<WalletResponse>, response: Response<WalletResponse>) {
                if (response.isSuccessful) {
                    val rsp = response.body()
                    if (rsp != null) {

                        if (rsp.status.equals("true")) {
                            binding.txtWalletAmount.text = "₹"+rsp.balance.toString()
                        }
                    }
                } else {
                    ViewController.showToast(requireActivity(), "Error: ${response.code()}")
                }
            }
            override fun onFailure(call: Call<WalletResponse>, t: Throwable) {
                ViewController.showToast(requireActivity(), "Try again: ${t.message}")
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null) // Remove callbacks when activity is destroyed
    }


}