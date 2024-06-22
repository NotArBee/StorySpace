package com.ardev.myapplication.ui

import UserPreferences
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.ardev.myapplication.R
import com.ardev.myapplication.ui.fragment.enterForm.signIn.SignInFragment
import com.ardev.myapplication.ui.fragment.home.HomeFragment
import dataStore

class MainActivity : AppCompatActivity() {
    private lateinit var userPreferences: UserPreferences
    private var isLoggedIn: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        enableEdgeToEdge()
        setupWindowInsets()
        setupStatusBarColor()
        setupSystemUiVisibility()

        userPreferences = UserPreferences.getInstance(dataStore)

        // Lakukan pengecekan status login saat Activity dibuat
        checkLoggedIn()
    }

    private fun setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupStatusBarColor() {
        window.statusBarColor = ContextCompat.getColor(this, R.color.md_theme_onPrimaryContainer)
    }

    private fun setupSystemUiVisibility() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = 0
        }
    }

    private fun checkLoggedIn() {
        val userDataFlow = userPreferences.getUserData()

        lifecycleScope.launchWhenStarted {
            userDataFlow.collect { loginResult ->
                isLoggedIn = loginResult != null
                if (isLoggedIn) {
                    // Jika user sudah login, navigate ke HomeFragment
                    navigateToFragment(HomeFragment())
                } else {
                    // Jika belum login, navigate ke SignInFragment
                    navigateToFragment(SignInFragment())
                }
            }
        }
    }

    private fun navigateToFragment(fragment: Fragment) {
        // Menunda transaksi fragment jika belum yakin status login
        if (!isLoggedIn) return

        supportFragmentManager.beginTransaction()
            .replace(R.id.main, fragment)
            .commit()
    }
}
