package com.ardev.myapplication.ui.fragment.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.ardev.myapplication.R
import com.ardev.myapplication.data.paging.StoryLoadStateAdapter
import com.ardev.myapplication.databinding.FragmentHomeBinding
import com.ardev.myapplication.ui.MainActivity
import com.ardev.myapplication.ui.activity.maps.MapsActivity
import com.ardev.myapplication.ui.activity.postActivity.PostStoryActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dataStore
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeFragmentViewModel by viewModels()

    private lateinit var storyAdapter: StoryPagingAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        storyAdapter = StoryPagingAdapter()
        binding.rvStories.layoutManager = LinearLayoutManager(requireActivity())
        binding.rvStories.adapter = storyAdapter.withLoadStateFooter(
            footer = StoryLoadStateAdapter { storyAdapter.retry() }
        )

        homeViewModel.userData.observe(viewLifecycleOwner) { loginResult ->
            loginResult?.let {
                val token = loginResult.token
                lifecycleScope.launch {
                    homeViewModel.getStories(token).collectLatest {
                        storyAdapter.submitData(it)
                    }
                }
            }
        }

        storyAdapter.addLoadStateListener { loadState ->
            if (loadState.refresh is LoadState.Loading) {
                showLoading(true)
            } else {
                showLoading(false)
                val errorState = loadState.source.append as? LoadState.Error
                    ?: loadState.source.prepend as? LoadState.Error
                    ?: loadState.append as? LoadState.Error
                    ?: loadState.prepend as? LoadState.Error
                errorState?.let {
                    Log.e(TAG, "LoadState Error: ${it.error}")
                }
            }
        }

        binding.fabAddStory.setOnClickListener {
            val intent = Intent(requireActivity(), PostStoryActivity::class.java)
            startActivity(intent)
        }

        binding.fabLogout.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(resources.getString(R.string.logout))
                .setMessage(resources.getString(R.string.logout_message))
                .setNegativeButton(resources.getString(R.string.cancel)) { dialog, _ ->
                    dialog.dismiss()
                }
                .setPositiveButton(resources.getString(R.string.sure)) { dialog, _ ->
                    runBlocking {
                        val mUserPreferences =
                            UserPreferences.getInstance(requireActivity().dataStore)
                        mUserPreferences.clearUserData()

                        val intent = Intent(requireActivity(), MainActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        requireActivity().finish()
                    }
                }
                .show()
        }

        binding.fabMaps.setOnClickListener {
            val intent = Intent(requireActivity(), MapsActivity::class.java)
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