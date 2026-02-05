package com.royalit.mvr

import android.graphics.PorterDuff
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.royalit.mvr.Model.SaleData
import com.royalit.mvr.databinding.ActivityAboutUsBinding
import com.royalit.mvr.databinding.ActivitySaleHistoryDetailsBinding

class SaleHistoryDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySaleHistoryDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySaleHistoryDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        toolbarSetup()

        val saleData: SaleData? = intent.getSerializableExtra("item") as? SaleData

        saleData?.let {
            binding.txtProjectName.text = it.project_name
            binding.txtDes.text = it.description
            binding.txtName.text = it.customer_name
            binding.txtMobile.text = it.customer_mobile
            binding.txtEmail.text = it.customer_email
            binding.txtSsy.text = it.sold_square_yard
            binding.txtCPrice.text = "+₹"+it.employee_commision
            binding.txtPrice.text = "₹"+it.sale_amount
            binding.txtPlotNo.text = "Plot Number: "+it.plot_no
            binding.txtfacing.text = it.facing

            if (it.status.equals("0")){
                binding.txtStatus.text = " Requested "
                binding.txtStatus.setTextColor(ContextCompat.getColor(this@SaleHistoryDetailsActivity, R.color.requested))
                binding.txtStatus.setBackgroundResource(R.drawable.round_requested)
            }
            if (it.status.equals("1")){
                binding.txtStatus.text = " Approved "
                binding.txtStatus.setTextColor(ContextCompat.getColor(this@SaleHistoryDetailsActivity, R.color.approve))
                binding.txtStatus.setBackgroundResource(R.drawable.round_approved)
            }
            if (it.status.equals("2")){
                binding.txtStatus.text = " Rejected "
                binding.txtStatus.setTextColor(ContextCompat.getColor(this@SaleHistoryDetailsActivity, R.color.reject))
                binding.txtStatus.setBackgroundResource(R.drawable.round_reject)
            }
            if (it.status.equals("3")){
                binding.txtStatus.text = " Credited "
                binding.txtStatus.setTextColor(ContextCompat.getColor(this@SaleHistoryDetailsActivity, R.color.credit))
                binding.txtStatus.setBackgroundResource(R.drawable.round_credited)
            }

        }


    }

    fun toolbarSetup(){
        val toolbar = binding.materialtoolbar
        setSupportActionBar(toolbar)
        toolbar.title = "Sale History Details"
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = "Sale History Details"
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

}