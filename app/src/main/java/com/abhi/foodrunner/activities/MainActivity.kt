package com.abhi.foodrunner.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.abhi.foodrunner.R
import com.abhi.foodrunner.fragments.*
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    lateinit var drawerLayout: DrawerLayout
    lateinit var toolbar: Toolbar
    lateinit var navigationDrawer: NavigationView
    lateinit var sharedPreferences: SharedPreferences
    lateinit var frame: FrameLayout
    var previousMenuItem: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawerLayout = findViewById(R.id.drawerLayout)
        toolbar = findViewById(R.id.toolbar)
        navigationDrawer = findViewById(R.id.navigationDrawer)
        frame = findViewById(R.id.frame)

        setUpToolbar()

        val actionBarDrawerToggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            R.string.open_drawer,
            R.string.close_drawer
        )
        actionBarDrawerToggle.syncState()
        drawerLayout.addDrawerListener(actionBarDrawerToggle)

        openHome()

        navigationDrawer.setNavigationItemSelectedListener {

            if (it.itemId != R.id.logout) {
                if (previousMenuItem != null)
                    previousMenuItem?.isChecked = false

                it.isCheckable = true
                it.isChecked = true
                previousMenuItem = it
            }

            when (it.itemId) {
                R.id.home -> {
                    openHome()
                    drawerLayout.closeDrawers()
                }
                R.id.MyProfile -> {
                    supportActionBar?.title = "My Profile"
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame, MyProfileFragment())
                        .commit()
                    drawerLayout.closeDrawers()
                }
                R.id.favourite -> {
                    supportActionBar?.title = "Favourite"
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame, FavouritesFragment())
                        .commit()
                    drawerLayout.closeDrawers()
                }
                R.id.orderHistory -> {
                    supportActionBar?.title = "Order History"
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame, HistoryFragment())
                        .commit()
                    drawerLayout.closeDrawers()
                }
                R.id.faqs -> {
                    supportActionBar?.title = "FAQs"
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame, FaqsFragment())
                        .commit()
                    drawerLayout.closeDrawers()
                }
                R.id.logout -> {
                    val dialog = AlertDialog.Builder(this)
                    dialog.setTitle("Confirmation")
                    dialog.setMessage("Are you sure you want to log out?")
                    dialog.setPositiveButton("YES") { text, listner ->
                        drawerLayout.closeDrawers()
                        sharedPreferences.edit().putBoolean("isLoggedIn", false).apply()
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    dialog.setNegativeButton("NO") { text, listner ->
                        drawerLayout.closeDrawers()
                    }
                    dialog.create()
                    dialog.show()
                }
            }
            return@setNavigationItemSelectedListener true
        }

    }

    fun setUpToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = "All Restaurant"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            val namePerson : TextView = findViewById(R.id.txtNamePerson)
            val mobilePerson : TextView = findViewById(R.id.txtMobilePerson)
            sharedPreferences = getSharedPreferences(getString(R.string.preferences_file), Context.MODE_PRIVATE)
            namePerson.text=sharedPreferences.getString("name","Name")
            mobilePerson.text=sharedPreferences.getString("mobile_number","9999999999")
            drawerLayout.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }

    fun openHome() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame, HomeFragment())
            .commit()
        supportActionBar?.title = "All Restaurant"
        navigationDrawer.setCheckedItem(R.id.home)
    }

    override fun onBackPressed() {
        val frag = supportFragmentManager.findFragmentById(R.id.frame)
        when (frag) {
            !is HomeFragment -> openHome()
            else -> super.onBackPressed()

        }
    }

}