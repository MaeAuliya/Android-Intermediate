package com.example.storyapp.ui.view

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.storyapp.data.model.StoryModel
import com.example.storyapp.data.response.DetailStoryResponse
import com.example.storyapp.databinding.ActivityDetailBinding
import com.example.storyapp.ui.viewmodel.DetailViewModel
import com.example.storyapp.utils.ViewModelFactory
import androidx.activity.viewModels
import com.example.storyapp.utils.Constant


class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var factory: ViewModelFactory
    private val detailViewModel: DetailViewModel by viewModels {
        factory
    }

    private var id: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupViewModel()

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
        supportActionBar?.hide()
    }

    private fun setupViewModel() {
        factory = ViewModelFactory.getInstance(this)

        id = intent.getStringExtra(EXTRA_DATA) ?: ""

        detailViewModel.getUser().observe(this){user ->
            detailViewModel.getDetail(user.token, id ?: "")
        }

        detailViewModel.detailData.observe(this){story ->
            setupData(story)
        }

        detailViewModel.isLoading.observe(this){
            showLoading(it)
        }

        detailViewModel.isError.observe(this){
            showError(it)
        }

    }

    private fun setupData(data: DetailStoryResponse){
        data.story?.let { setupStoryData(it) }
    }

    private fun setupStoryData(story: StoryModel){
        binding.apply {
            tvTitle.text = story.name
            tvDescItem.text = story.description
            Glide.with(ivStory.context)
                .load(story.photoUrl)
                .into(ivStory)
        }
    }

    private fun showLoading(isLoading : Boolean){
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun showError(isError : Boolean){
        if (isError){
            Toast.makeText(this, Constant.ERROR_MESSAGE, Toast.LENGTH_SHORT).show()
        }
    }

    companion object{
        const val EXTRA_DATA = "extra_data"
    }

}