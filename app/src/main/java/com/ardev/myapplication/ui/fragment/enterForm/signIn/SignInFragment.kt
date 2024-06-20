package com.ardev.myapplication.ui.fragment.enterForm.signIn

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.ardev.myapplication.R
import com.ardev.myapplication.databinding.FragmentSignInBinding

class SignInFragment : Fragment() {

    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SignInViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.isSignInSuccessful.observe(viewLifecycleOwner, Observer { isSignInSuccessful ->
            if (isSignInSuccessful) {
                Toast.makeText(
                    requireContext(),
                    "Sign in successful. Now you can sign in",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Sign in failed. Please try again",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

        viewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            if (isLoading) {
                binding.progressOverlay.visibility = View.VISIBLE
            } else {
                binding.progressOverlay.visibility = View.GONE
            }
        })

        binding.btnLogin.setOnClickListener {
            viewModel.signIn(
                binding.etEmail.text.toString(),
                binding.etPassword.text.toString()
            )
        }

        binding.etEmail.addTextChangedListener(textWatcher)
        binding.etPassword.addTextChangedListener(textWatcher)
        setMyButtonEnable()

        binding.btnSignup.setOnClickListener { view ->
            view.findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
        }
    }

    private fun setMyButtonEnable() {
        binding.apply {
            val editTextEmail = etEmail.text
            val editTextPassword = etPassword.text

            btnLogin.isEnabled = editTextEmail != null && editTextEmail.toString()
                .isNotEmpty() && editTextPassword != null && editTextPassword.toString()
                .isNotEmpty()
        }
    }

    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            setMyButtonEnable()
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}