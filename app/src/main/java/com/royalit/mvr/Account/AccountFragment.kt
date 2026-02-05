package com.royalit.mvr.Account

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.royalit.mvr.Authentication.LoginActivity
import com.royalit.mvr.Config.Preferences
import com.royalit.mvr.Config.ViewController
import com.royalit.mvr.ContactusActivity
import com.royalit.mvr.Model.ProfileModel
import com.royalit.mvr.R
import com.royalit.mvr.Retrofit.RetrofitClient
import com.royalit.mvr.databinding.FragmentAccountBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AccountFragment : Fragment() {

    private var _binding: FragmentAccountBinding? = null

    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAccountBinding.inflate(inflater, container, false)


        getProfileApi()

        binding.txtMyTeam.setOnClickListener{
            val intent = Intent(requireContext(), MyTeamActivity::class.java)
            startActivity(intent)
        }
        binding.txtFax.setOnClickListener{
            val intent = Intent(requireContext(), FaqActivity::class.java)
            startActivity(intent)
        }

        binding.txtContactUs.setOnClickListener{
            val intent = Intent(requireContext(), ContactusActivity::class.java)
            startActivity(intent)
        }
        binding.txtHelpSupport.setOnClickListener{
            val intent = Intent(requireContext(), HelpActivity::class.java)
            startActivity(intent)
        }
        binding.txtAboutUs.setOnClickListener{
            val intent = Intent(requireContext(), AboutUsActivity::class.java)
            startActivity(intent)
        }
        binding.txtTermsConditions.setOnClickListener{
            val intent = Intent(requireContext(), TermsAndConditionsActivity::class.java)
            startActivity(intent)
        }
        binding.txtPrivacy.setOnClickListener{
            val intent = Intent(requireContext(), PrivacyPolicyActivity::class.java)
            startActivity(intent)
        }
        binding.imgEditProfile.setOnClickListener{
            val intent = Intent(requireContext(), MyProfileActivity::class.java)
            startActivity(intent)
        }
        binding.txtLogout.setOnClickListener{
            logOut()
        }



        return binding.root
    }



    private fun getProfileApi() {
        val userId = Preferences.loadStringValue(requireActivity(), Preferences.userId, "")

        Log.e("userId_", userId.toString())

        ViewController.showLoading(requireActivity())
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
                            binding.txtPhoneNo.text = translist.user?.phone ?:""

                            Glide.with(requireActivity())
                                .load(translist.user?.image ?: "")
                                .placeholder(R.drawable.profileimg)
                                .error(R.drawable.profileimg)
                                .into(binding.profilePic)
                        }
                    }
                } else {
                    ViewController.showToast(requireActivity(), "Error: ${response.code()}")
                }
            }
            override fun onFailure(call: Call<ProfileModel>, t: Throwable) {
                ViewController.hideLoading()
                ViewController.showToast(requireActivity(), "Try again: ${t.message}")
            }
        })
    }


    private fun logOut() {
        val builder = AlertDialog.Builder(requireActivity())
        builder.setMessage("Are you sure you want to logout?")
        builder.setTitle("Alert!")
        builder.setCancelable(false)
        builder.setPositiveButton(
            "Yes"
        ) { dialog: DialogInterface?, which: Int ->
            Preferences.deleteSharedPreferences(requireActivity())
            startActivity(Intent(requireActivity(), LoginActivity::class.java))
            requireActivity().finishAffinity()
        }
        builder.setNegativeButton(
            "No"
        ) { dialog: DialogInterface, which: Int ->
            dialog.cancel()
        }
        val alertDialog = builder.create()
        alertDialog.show()
    }



    override fun onDestroyView() {
        super.onDestroyView()
        // Clear the binding when the view is destroyed
        _binding = null
    }


}