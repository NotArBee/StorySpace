package com.ardev.myapplication.ui.fragment.enterForm.signUp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.ardev.myapplication.R
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
                Toast.makeText(requireContext(), "Sign Up Failed", Toast.LENGTH_SHORT).show()
            }
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

        binding.btnSignIn.setOnClickListener { view ->
            view.findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
