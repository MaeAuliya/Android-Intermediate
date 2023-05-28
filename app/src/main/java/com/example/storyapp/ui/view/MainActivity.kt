package com.example.storyapp.ui.view

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyapp.R
import com.example.storyapp.databinding.ActivityMainBinding
import com.example.storyapp.ui.viewmodel.MainViewModel
import com.example.storyapp.utils.LoadingStateAdapter
import com.example.storyapp.utils.StoryAdapter
import com.example.storyapp.utils.ViewModelFactory


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var factory: ViewModelFactory
    private val mainViewModel: MainViewModel by viewModels { factory }
    private lateinit var storyAdapter: StoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupViewModel()
        setupAdapter()
        setupAction()
        setupUser()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu1 -> {
                startActivity(Intent(this, ProfileActivity::class.java))
                true
            }
            R.id.menu2 -> {
                startActivity(Intent(this, LocationActivity::class.java))
                true
            }
            else -> {
                true
            }
        }
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }

    private fun setupViewModel() {
        factory = ViewModelFactory.getInstance(this)
    }

    private fun setupUser() {
        mainViewModel.getUser().observe(this){
            mainViewModel.listStories().observe(this){
                storyAdapter.submitData(lifecycle, it)
            }
        }
    }


    private fun setupAdapter(){
        storyAdapter = StoryAdapter()
        binding.rvStories.layoutManager = LinearLayoutManager(this)
        binding.rvStories.adapter = storyAdapter.withLoadStateFooter(
            footer = LoadingStateAdapter{
                storyAdapter.retry()
            }
        )

    }

    private fun setupAction(){
        binding.fabAdd.setOnClickListener {
            startActivity(Intent(this, UploadStoryActivity::class.java))
        }
    }


    override fun onBackPressed() {
        finishAffinity()
    }

}