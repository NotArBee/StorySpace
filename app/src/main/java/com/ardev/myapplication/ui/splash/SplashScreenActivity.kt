package com.ardev.myapplication.ui.splash

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ardev.myapplication.R
import com.ardev.myapplication.data.pref.PreferenceDataSource
import com.ardev.myapplication.databinding.ActivitySplashScreenBinding
import com.ardev.myapplication.ui.auth.SignInActivity
import com.ardev.myapplication.ui.home.HomeActivity

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding

    private val pref by lazy {
        PreferenceDataSource.invoke(this)
    }
    companion object{
        val DELAY= 1000L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        hideSystemUI()
        playAnimation()
        val token = pref.fetchAuthToken()
        Handler(Looper.getMainLooper()).postDelayed({
            if (token.isNullOrEmpty()) {
                startActivity(Intent(this@SplashScreenActivity, SignInActivity::class.java))
                finish()
            } else {
                startActivity(Intent(this@SplashScreenActivity, HomeActivity::class.java))
                finish()
            }
            finish()
        }, DELAY)
    }

    private fun hideSystemUI() {

        supportActionBar?.hide()
    }

    private fun playAnimation(){
        val title = ObjectAnimator.ofFloat(binding.textView, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(
                title

            )
            startDelay = 200
        }.start()
    }
}