package com.royalit.mvr

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.royalit.mvr.Config.Preferences
import com.royalit.mvr.Config.ViewController
import com.royalit.mvr.Model.HelpSupportResponse
import com.royalit.mvr.Model.ProfileModel
import com.royalit.mvr.Model.WalletResponse
import com.royalit.mvr.Retrofit.RetrofitClient
import com.royalit.mvr.databinding.FragmentHomeBinding
import com.royalit.mvr.databinding.FragmentWalletBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class WalletFragment : Fragment() {

    private var _binding: FragmentWalletBinding? = null
    private val binding get() = _binding!!

    private var amount = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentWalletBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getWalletApi()

        binding.btnSignin.setOnClickListener {
            val userId = Preferences.loadStringValue(requireActivity(), Preferences.userId, "")

            val amount_ = binding.editAmount.text?.trim().toString()
            val note_ = binding.editNote.text?.trim().toString()

            if(amount_.isEmpty()){
                ViewController.showToast(requireActivity(), "Enter Amount")
            }
            else if(note_.isEmpty()){
                ViewController.showToast(requireActivity(), "Enter Note")
            }else{

                ViewController.showToast(requireActivity(), amount.toString())
                if (amount == 0 ) {
                    ViewController.showToast(requireActivity(), "no amount in your wallet")
                }else{
                    val apiInterface = RetrofitClient.apiInterface
                    apiInterface.AddWalletApi(userId.toString(), amount_, note_).enqueue(object :
                        Callback<WalletResponse> {
                        override fun onResponse(call: Call<WalletResponse>, response: Response<WalletResponse>) {
                            ViewController.hideLoading()
                            if (response.isSuccessful) {
                                val loginResponse = response.body()
                                if (loginResponse != null && loginResponse.status == "true") {
                                    ViewController.showToast(requireActivity(), "Withdrawal Request Submitted Successfully")
                                    startActivity(Intent(requireActivity(), NavigationActivity::class.java))
                                } else {
                                    loginResponse?.message?.let {
                                        ViewController.showToast(requireActivity(), it)
                                    }
                                }
                            } else {
                                ViewController.showToast(requireActivity(), "Error: ${response.code()}")
                            }
                        }
                        override fun onFailure(call: Call<WalletResponse>, t: Throwable) {
                            ViewController.hideLoading()
                            ViewController.showToast(requireActivity(), "Try again: ${t.message}")
                        }
                    })
                }

            }

        }

    }

    private fun getWalletApi() {
        val userId = Preferences.loadStringValue(requireActivity(), Preferences.userId, "")

        ViewController.showLoading(requireActivity())
        val apiInterface = RetrofitClient.apiInterface
        apiInterface.getWalletApi(userId).enqueue(object :
            Callback<WalletResponse> {
            override fun onResponse(call: Call<WalletResponse>, response: Response<WalletResponse>) {
                ViewController.hideLoading()
                if (response.isSuccessful) {
                    val rsp = response.body()
                    if (rsp != null) {

                        if (rsp.status.equals("true")) {
                            amount = rsp.balance?.toIntOrNull() ?: 0
                            binding.txtBalance.text = "₹ "+rsp.balance.toString()
                        }
                    }
                } else {
                    ViewController.showToast(requireActivity(), "Error: ${response.code()}")
                }
            }
            override fun onFailure(call: Call<WalletResponse>, t: Throwable) {
                ViewController.hideLoading()
                ViewController.showToast(requireActivity(), "Try again: ${t.message}")
            }
        })
    }

}