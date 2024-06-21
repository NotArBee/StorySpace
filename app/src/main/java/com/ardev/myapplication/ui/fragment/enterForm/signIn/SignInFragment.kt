package com.ardev.myapplication.ui.fragment.enterForm.signIn

import UserPreferences
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
import androidx.navigation.fragment.findNavController
import com.ardev.myapplication.R
import com.ardev.myapplication.databinding.FragmentSignInBinding
import com.ardev.myapplication.utils.ViewModelFactory

class SignInFragment : Fragment() {

    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SignInViewModel by viewModels {
        ViewModelFactory(UserPreferences.getInstance(requireContext().applicationContext.dataStore))
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.isSignInSuccessful.observe(viewLifecycleOwner, Observer { isSignInSuccessful ->
            if (isSignInSuccessful) {
                Toast.makeText(
                    requireContext(),
                    "Sign in successful. Now you can proceed",
                    Toast.LENGTH_SHORT
                ).show()

                // Navigate to HomeFragment or any other destination upon successful sign in
                findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
            } else {
                Toast.makeText(
                    requireContext(),
                    "Sign in failed. Please try again",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

        viewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            binding.progressOverlay.visibility = if (isLoading) View.VISIBLE else View.GONE
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

        binding.btnSignup.setOnClickListener {
            it.findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
        }
    }

    private fun setMyButtonEnable() {
        binding.apply {
            val editTextEmail = etEmail.text
            val editTextPassword = etPassword.text

            btnLogin.isEnabled = !editTextEmail.isNullOrEmpty() && !editTextPassword.isNullOrEmpty()
        }
    }

    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            setMyButtonEnable()
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}