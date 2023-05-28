package com.example.storyapp.ui.view

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import com.example.storyapp.R
import com.example.storyapp.databinding.ActivityProfileBinding
import com.example.storyapp.ui.viewmodel.ProfileViewModel
import com.example.storyapp.utils.ViewModelFactory


class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var factory: ViewModelFactory
    private val profileViewModel: ProfileViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupViewModel()
        setupAction()

    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupViewModel() {
        factory = ViewModelFactory.getInstance(this)

        profileViewModel.getUser().observe(this){user ->
            binding.nameTextView.text = getString(R.string.greeting, user.name)
        }
    }

    private fun setupAction() {
        binding.logoutButton.setOnClickListener {
            profileViewModel.logout()
            startActivity(Intent(this, WelcomeActivity::class.java))
        }
    }

}