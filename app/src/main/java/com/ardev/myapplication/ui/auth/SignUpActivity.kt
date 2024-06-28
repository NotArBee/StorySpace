package com.ardev.myapplication.ui.auth

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.ardev.myapplication.data.response.Result
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.ardev.myapplication.R
import com.ardev.myapplication.databinding.ActivitySignUpBinding
import com.ardev.myapplication.utils.ViewModelFactory

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding

    private val viewModel by viewModels<SignUpViewModel> {
        ViewModelFactory.getInstance(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        setOnClick()
        playAnimation()

        binding.apply {
            btnSignUp.setOnClickListener {
                val name = etUsernameSignUp.text.toString()
                val email = etEmailSignUp.text.toString()
                val password = etPasswordSignUp.text.toString()

                viewModel.register(name, email, password).observe(this@SignUpActivity) { result ->
                    when (result) {
                        is Result.Loading -> {
                            binding.btnSignUp.isEnabled = false
                            showLoading(true)
                        }
                        is Result.Success -> {
                            showLoading(false)
                            binding.btnSignUp.isEnabled = true

                            AlertDialog.Builder(this@SignUpActivity).apply {
                                setTitle("Yeay!")
                                setMessage("Regist Success, Go to Login Page")
                                setPositiveButton("Continue") { _, _ ->
                                    finish()
                                }
                                create()
                                show()
                            }

                        }
                        is Result.Error -> {
                            binding.btnSignUp.isEnabled = true
                            showLoading(false)
                            message(result.error)
                        }
                    }
                }



            }
            tvMasuk.setOnClickListener {
                val intent = Intent(this@SignUpActivity, SignInActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

    }

    private fun message(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun setOnClick() {
    }

    private fun playAnimation(){
        val title = ObjectAnimator.ofFloat(binding.titleSignUp, View.ALPHA, 1f).setDuration(500)
        val nameTextView = ObjectAnimator.ofFloat(binding.tiUsername, View.ALPHA, 1f).setDuration(500)
        val nameEditTextLayout = ObjectAnimator.ofFloat(binding.etUsernameSignUp, View.ALPHA, 1f).setDuration(500)
        val emailTextView = ObjectAnimator.ofFloat(binding.tiEmaill, View.ALPHA, 1f).setDuration(500)
        val emailEditTextLayout = ObjectAnimator.ofFloat(binding.etEmailSignUp, View.ALPHA, 1f).setDuration(500)
        val passwordTextView = ObjectAnimator.ofFloat(binding.tiPass, View.ALPHA, 1f).setDuration(500)
        val passwordEditTextLayout = ObjectAnimator.ofFloat(binding.etPasswordSignUp, View.ALPHA, 1f).setDuration(500)
        val signup = ObjectAnimator.ofFloat(binding.btnSignUp, View.ALPHA, 1f).setDuration(500)
        val already = ObjectAnimator.ofFloat(binding.tvMasuk, View.ALPHA, 1f).setDuration(500)


        AnimatorSet().apply {
            playSequentially(
                title,
                nameTextView,
                nameEditTextLayout,
                emailTextView,
                emailEditTextLayout,
                passwordTextView,
                passwordEditTextLayout,
                signup,
                already

            )
            startDelay = 500
        }.start()
    }
    private fun showLoading(isLoading: Boolean){
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}