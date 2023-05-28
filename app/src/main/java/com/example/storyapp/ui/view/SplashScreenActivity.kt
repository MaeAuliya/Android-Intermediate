package com.example.storyapp.ui.view


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.viewModels
import com.example.storyapp.R
import com.example.storyapp.ui.viewmodel.MainViewModel
import com.example.storyapp.utils.Constant
import com.example.storyapp.utils.ViewModelFactory


class SplashScreenActivity : AppCompatActivity() {

    private lateinit var splashImageView: ImageView
    private lateinit var factory: ViewModelFactory
    private val mainViewModel: MainViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        splashImageView = findViewById(R.id.camera)

        splashImageView.alpha = 0f
        splashImageView.animate().setDuration(Constant.SPLASH_DURATION).alpha(1f).withEndAction{
            setupViewModel()
        }
    }

    private fun setupViewModel() {
        factory = ViewModelFactory.getInstance(this)

        mainViewModel.getUser().observe(this){user ->
            if (user.isLogin){
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }
        }
    }

}