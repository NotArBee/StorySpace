package com.ardev.myapplication.ui.fragment.enterForm.signUp

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.ardev.myapplication.R
import com.ardev.myapplication.customView.MyButtonRegister
import com.ardev.myapplication.customView.MyEditTextEmail
import com.ardev.myapplication.customView.MyEditTextPassword
import com.ardev.myapplication.databinding.FragmentSignUpBinding

class SignUpFragment : Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SignUpViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.isSignUpSuccessful.observe(viewLifecycleOwner, Observer { isSuccess ->
            if (isSuccess) {
                Toast.makeText(requireContext(), "Sign Up Successful", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.errorMessage.observe(viewLifecycleOwner, Observer { errorMessage ->
                    errorMessage?.let {
                        Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                    }
                })            }
            binding.progressOverlay.visibility = View.GONE
        })

        viewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            if (isLoading) {
                binding.progressOverlay.visibility = View.VISIBLE
            } else {
                binding.progressOverlay.visibility = View.GONE
            }
        })

        binding.btnRegister.setOnClickListener {
            viewModel.signUp(
                binding.etUsername.text.toString(),
                binding.etEmail.text.toString(),
                binding.etPassword.text.toString()
            )
        }

        binding.etUsername.addTextChangedListener(textWatcher)
        binding.etEmail.addTextChangedListener(textWatcher)
        binding.etPassword.addTextChangedListener(textWatcher)
        setMyButtonEnable()

        binding.btnSignIn.setOnClickListener { view ->
            view.findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
        }
    }

    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            setMyButtonEnable()
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }

    private fun setMyButtonEnable() {
        binding.apply {
            val editTextUsername = etUsername.text
            val editTextEmail = etEmail.text
            val editTextPassword = etPassword.text

            btnRegister.isEnabled = editTextUsername != null && editTextUsername.toString()
                .isNotEmpty() && editTextEmail != null && editTextEmail.toString()
                .isNotEmpty() && editTextPassword != null && editTextPassword.toString().isNotEmpty()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}