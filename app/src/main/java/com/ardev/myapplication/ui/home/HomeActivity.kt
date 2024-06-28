package com.ardev.myapplication.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ardev.myapplication.R
import com.ardev.myapplication.data.pref.PreferenceDataSource
import com.ardev.myapplication.databinding.ActivityHomeBinding
import com.ardev.myapplication.ui.auth.SignInActivity
import com.ardev.myapplication.ui.location.MapsActivity
import com.ardev.myapplication.utils.ViewModelFactory
import com.ardev.myapplication.ui.upload_stories.UploadStoryActivity

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    private val viewModel by viewModels<HomeViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var storyAdapter: StoryAdapter

    private val pref by lazy {
        PreferenceDataSource.invoke(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setView()
        setObserver()

        supportActionBar?.let {

            it.title = "Story App"
        }


        binding.fbAddStory.setOnClickListener {
            val mIntent = Intent(this@HomeActivity, UploadStoryActivity::class.java)
            startActivity(mIntent)
        }
    }




    private fun setObserver() {
        viewModel.listStoryResponse.observe(this) {
            storyAdapter.submitData(lifecycle, it)
        }
    }

    private fun setView() = with(binding) {
        storyAdapter = StoryAdapter()
        rvHome.apply {
            layoutManager = LinearLayoutManager(this@HomeActivity)
            setHasFixedSize(true)
            adapter = storyAdapter.withLoadStateFooter(
                footer = LoadingStateAdapter {
                    storyAdapter.retry()
                }
            )
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.item_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_logout -> {
                val alertDialog = AlertDialog.Builder(this)
                alertDialog.setTitle("Are You Sure You Wanna Logout?")
                    ?.setPositiveButton("Yes") {_,_ ->
                        pref.deleteDataAuth()
                        val intent = Intent (this, SignInActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                        startActivity(intent)
                        finish()

                    }
                    ?.setNegativeButton("Cancel",null)
                val alert = alertDialog.create()
                alert.show()
            }
            R.id.maps -> {
                val mIntent = Intent(this@HomeActivity, MapsActivity::class.java)
                startActivity(mIntent)
            }

        }
        return true
    }
}