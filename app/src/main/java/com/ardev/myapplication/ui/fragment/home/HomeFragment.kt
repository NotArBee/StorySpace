package com.ardev.myapplication.ui.fragment.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.ardev.myapplication.databinding.FragmentHomeBinding
import com.ardev.myapplication.ui.activity.postActivity.PostStoryActivity

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeFragmentViewModel by viewModels()

    private lateinit var listStoryAdapter: ListStoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listStoryAdapter = ListStoryAdapter(emptyList())
        binding.rvStories.layoutManager = LinearLayoutManager(requireActivity())
        binding.rvStories.adapter = listStoryAdapter

        homeViewModel.userData.observe(viewLifecycleOwner) { loginResult ->
            loginResult?.let {
                val token = loginResult.token
                homeViewModel.getStories("Bearer $token")
            }
        }

        homeViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        homeViewModel.story.observe(viewLifecycleOwner, Observer { stories ->
            stories?.let {
                Log.d(TAG, "Stories Updated: $stories")
                listStoryAdapter = ListStoryAdapter(stories)
                binding.rvStories.adapter = listStoryAdapter
            }
        })

        binding.fabAddStory.setOnClickListener {
            val intent = Intent(requireActivity(), PostStoryActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "HomeFragment"
    }
}
