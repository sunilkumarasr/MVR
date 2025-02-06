package com.royalit.mvr

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.royalit.mvr.Account.AccountFragment
import com.royalit.mvr.Account.HelpActivity
import com.royalit.mvr.Account.MyTeamActivity
import com.royalit.mvr.Account.MyProfileActivity
import com.royalit.mvr.Authentication.LoginActivity
import com.royalit.mvr.Config.Preferences
import com.royalit.mvr.Config.ViewController
import com.royalit.mvr.Model.ProfileModel
import com.royalit.mvr.Retrofit.RetrofitClient
import com.royalit.mvr.databinding.ActivityNavigationBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NavigationActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var bottomNavigationView: BottomNavigationView

    private lateinit var binding: ActivityNavigationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNavigationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar: Toolbar =  binding.appBarMain.materialtoolbar
        setSupportActionBar(toolbar)

        //login
        Preferences.saveStringValue(applicationContext, Preferences.LOGINCHECK, "Login")

        getProfileApi()

        // Initialize DrawerLayout and NavigationView
         drawerLayout = binding.drawerLayout
        navigationView = binding.navView
        binding.appBarMain.imgWhatsaapp.setOnClickListener{
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=+6281909995&text=Hi%20there"))
            startActivity(browserIntent)
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }
        binding.myteam.setOnClickListener{
            val intent = Intent(this, MyTeamActivity::class.java)
            startActivity(intent)
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }
        binding.profile.setOnClickListener{
            val intent = Intent(this, MyProfileActivity::class.java)
            startActivity(intent)
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }
        binding.projects.setOnClickListener{
            binding.appBarMain.txtTitle.setText("Projects")
            loadFragment(ProjectsFragment())
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }
        binding.transactions.setOnClickListener{
            val intent = Intent(this, TransactionsActivity::class.java)
            startActivity(intent)
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }
        binding.sales.setOnClickListener{
            val intent = Intent(this, SalesActivity::class.java)
            startActivity(intent)
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }
        binding.wallet.setOnClickListener{
            binding.appBarMain.txtTitle.setText("Wallet")
            loadFragment(WalletFragment())
            binding.drawerLayout.closeDrawer(GravityCompat.START)

        }
        binding.contactUs.setOnClickListener{
            val intent = Intent(this, ContactusActivity::class.java)
            startActivity(intent)
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }
        binding.helpSupport.setOnClickListener{
            val intent = Intent(this, HelpActivity::class.java)
            startActivity(intent)
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }
         binding.logout.setOnClickListener{
             logOut()
        }
        toggle = ActionBarDrawerToggle(
            this, binding.drawerLayout, toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        toggle.setDrawerIndicatorEnabled(false) // Disable default hamburger icon
        toolbar.setNavigationIcon(R.drawable.icon_menu) // Use your custom white icon
        toolbar.setNavigationOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START) // Handle icon click event
        }


        // Load the initial fragment
        binding.appBarMain.txtTitle.setText("Home")
        loadFragment(HomeFragment())

        // Set up BottomNavigationView
        bottomNavigationView = binding.appBarMain.navigationView

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id. navigation_dashboard-> {
                    binding.appBarMain.txtTitle.setText("Home")
                    loadFragment(HomeFragment())
                    true
                }
                R.id. navigation_projects-> {
                    binding.appBarMain.txtTitle.setText("Projects")
                    loadFragment(ProjectsFragment())
                    true
                }
                R.id. navigation_wallet-> {
                    binding.appBarMain.txtTitle.setText("Wallet")
                    loadFragment(WalletFragment())
                    true
                }
                R.id.navigation_profile -> {
                    binding.appBarMain.txtTitle.setText("Profile")
                    loadFragment(AccountFragment())
                    true
                }
                else -> false
            }
        }
    }
    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.content_frame, fragment)
        transaction.commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        // Handle action bar item clicks here
        return super.onOptionsItemSelected(item)
    }


    private fun getProfileApi() {
        val userId = Preferences.loadStringValue(this@NavigationActivity, Preferences.userId, "")

        Log.e("userId_", userId.toString())

        ViewController.showLoading(this@NavigationActivity)
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

                            Glide.with(this@NavigationActivity)
                                .load(translist.user?.image ?: "")
                                .placeholder(R.drawable.profileimg)
                                .error(R.drawable.profileimg)
                                .into(binding.profilePic)
                        }
                    }
                } else {
                    ViewController.showToast(this@NavigationActivity, "Error: ${response.code()}")
                }
            }
            override fun onFailure(call: Call<ProfileModel>, t: Throwable) {
                ViewController.hideLoading()
                ViewController.showToast(this@NavigationActivity, "Try again: ${t.message}")
            }
        })
    }


    private fun logOut() {
        val builder = AlertDialog.Builder(this@NavigationActivity)
        builder.setMessage("Are you sure you want to logout?")
        builder.setTitle("Alert!")
        builder.setCancelable(false)
        builder.setPositiveButton(
            "Yes"
        ) { dialog: DialogInterface?, which: Int ->
            Preferences.deleteSharedPreferences(this@NavigationActivity)
            startActivity(Intent(this@NavigationActivity, LoginActivity::class.java))
            finishAffinity()
        }
        builder.setNegativeButton(
            "No"
        ) { dialog: DialogInterface, which: Int ->
            dialog.cancel()
        }
        val alertDialog = builder.create()
        alertDialog.show()
    }



}