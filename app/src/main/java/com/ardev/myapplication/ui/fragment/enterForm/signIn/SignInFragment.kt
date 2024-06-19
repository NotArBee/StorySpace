package com.ardev.myapplication.ui.fragment.enterForm.signIn

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.ardev.myapplication.R
import com.ardev.myapplication.databinding.FragmentSignInBinding

class SignInFragment : Fragment() {

    private var _binding : FragmentSignInBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSignup.setOnClickListener { view ->
            view.findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}