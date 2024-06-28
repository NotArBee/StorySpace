package com.ardev.myapplication.ui.auth

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.ardev.myapplication.R
import com.ardev.myapplication.data.response.Result
import com.ardev.myapplication.data.pref.PreferenceDataSource
import com.ardev.myapplication.databinding.ActivitySignInBinding
import com.ardev.myapplication.ui.home.HomeActivity
import com.ardev.myapplication.utils.ViewModelFactory
import com.ardev.myapplication.utils.gone
import com.ardev.myapplication.utils.show

class SignInActivity : AppCompatActivity() {


    private lateinit var binding: ActivitySignInBinding

    private val viewModel by viewModels<SignInViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private val pref by lazy {
        PreferenceDataSource.invoke(this)
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        playAnimation()

        binding.apply {
            btnSignIn.setOnClickListener {
                val email = etEmailSignIn.text.toString()
                val password =etPasswordSignIn.text.toString()

                if (password.length >= 8) {
                    viewModel.login(email, password).observe(this@SignInActivity) { result ->
                        when (result) {
                            is Result.Loading -> {
                                showLoading(true)
                            }
                            is Result.Success -> {
                                showLoading(false)
                                binding.btnSignIn.isEnabled = true
                                result.data.let {
                                    if (!it.error) {
                                        pref.saveAuthToken(it.loginResult.token)
                                        message(it.message)
                                        intent =
                                            Intent(this@SignInActivity, HomeActivity::class.java)
                                        intent.flags =
                                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                        startActivity(intent)
                                        finish()
                                    } else {
                                        message(it.message)
                                    }
                                }
                            }
                            is Result.Error -> {
                                showLoading(false)
                                message(result.error)
                            }
                        }

                    }
                }
            }
            tvDaftar.setOnClickListener {
                val intent = Intent(this@SignInActivity, SignUpActivity::class.java)
                startActivity(intent)
            }
        }
    }





    private fun message(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }



    private fun playAnimation(){

        val title = ObjectAnimator.ofFloat(binding.tvSignIn, View.ALPHA, 1f).setDuration(500)
        val emailTextView = ObjectAnimator.ofFloat(binding.tiEmaill, View.ALPHA, 1f).setDuration(500)
        val emailEditTextLayout = ObjectAnimator.ofFloat(binding.etEmailSignIn, View.ALPHA, 1f).setDuration(500)
        val passwordTextView = ObjectAnimator.ofFloat(binding.tiPass, View.ALPHA, 1f).setDuration(500)
        val passwordEditTextLayout = ObjectAnimator.ofFloat(binding.etPasswordSignIn, View.ALPHA, 1f).setDuration(500)
        val login = ObjectAnimator.ofFloat(binding.btnSignIn, View.ALPHA, 1f).setDuration(500)
        val register= ObjectAnimator.ofFloat(binding.tvDaftar, View.ALPHA,1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(title,emailTextView,emailEditTextLayout,passwordTextView,passwordEditTextLayout,login,register)
            startDelay = 500
        }.start()
    }

    private fun showLoading(isLoading: Boolean){
        if (isLoading) binding.progressBar.show() else binding.progressBar.gone()
    }
}