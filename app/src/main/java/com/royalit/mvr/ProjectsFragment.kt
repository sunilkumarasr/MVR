package com.royalit.mvr

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.royalit.mvr.Account.ProjectDetailsActivity
import com.royalit.mvr.Account.ProjectDetailsWebActivity
import com.royalit.mvr.Adapters.ProjectBannersAdapter
import com.royalit.mvr.Adapters.TransactionsAdapter
import com.royalit.mvr.Config.ViewController
import com.royalit.mvr.Model.HelpSupportResponse
import com.royalit.mvr.Model.MyTeamsData
import com.royalit.mvr.Model.MyTeamsModel
import com.royalit.mvr.Model.ProjectData
import com.royalit.mvr.Model.ProjectModel
import com.royalit.mvr.Model.TransactionsData
import com.royalit.mvr.Model.TransactionsModel
import com.royalit.mvr.Retrofit.RetrofitClient
import com.royalit.mvr.databinding.ActivityProjectDetailsBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.Serializable

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class ProjectsFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

//    private var _binding: ActivityProjectDetailsBinding? = null
//    private val binding get() = _binding!!
    private lateinit var binding: ActivityProjectDetailsBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ActivityProjectDetailsBinding.inflate(inflater, container, false)
        val view = binding.root

        if(!ViewController.noInterNetConnectivity(requireActivity())){
            ViewController.showToast(requireActivity(), "Please check your connection ")
        }else{
            projectListApi()
        }


        return view
    }

    private fun projectListApi() {
        ViewController.showLoading(requireActivity())

        val apiInterface = RetrofitClient.apiInterface
        apiInterface.projectListApi().enqueue(object : Callback<ProjectModel> {
            override fun onResponse(call: Call<ProjectModel>, response: Response<ProjectModel>) {
                ViewController.hideLoading()
                if (response.isSuccessful) {
                    val rsp = response.body()
                    if (rsp != null) {
                        val translist = response.body()
                        if (translist != null) {
                            if (!translist.data.isNullOrEmpty()) {
                                binding.recyclerView.visibility = View.VISIBLE
                                binding.noData.visibility = View.GONE
                                projectDataSet(translist.data!!)
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
            override fun onFailure(call: Call<ProjectModel>, t: Throwable) {
                binding.recyclerView.visibility = View.GONE
                binding.noData.visibility = View.VISIBLE
                ViewController.hideLoading()
                ViewController.showToast(requireActivity(), "Try again: ${t.message}")
            }

        })
    }
    private fun projectDataSet(list: List<ProjectData>) {
        binding.recyclerView.layoutManager = GridLayoutManager(requireActivity(), 2) // 2 items per row
        binding.recyclerView.adapter = ProjectBannersAdapter(list) { item ->
            val intent = Intent(requireActivity(), ProjectDetailsActivity::class.java)
            intent.putExtra("pid", item.pid)
            startActivity(intent)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProjectsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
