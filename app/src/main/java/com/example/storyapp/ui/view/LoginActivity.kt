package com.example.storyapp.ui.view

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import com.example.storyapp.data.model.UserModel
import com.example.storyapp.databinding.ActivityLoginBinding
import com.example.storyapp.ui.viewmodel.LoginViewModel
import com.example.storyapp.utils.Constant
import com.example.storyapp.utils.ViewModelFactory



class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var factory: ViewModelFactory
    private val loginViewModel: LoginViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupViewModel()
        setupLogin()
        playAnimation()
    }

    private fun setupView(){
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

        loginViewModel.loginData.observe(this){ user ->

            if (!user.error){
                saveSession(
                    UserModel(
                        user.loginResult?.name.toString(),
                        "Bearer " + user.loginResult?.token.toString(),
                        true
                    )
                )
                Toast.makeText(this, "Login Success", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
            }
        }

        loginViewModel.isLoading.observe(this){
            showLoading(it)
        }

        loginViewModel.isError.observe(this){
            showError(it)
        }

    }

    private fun setupLogin() {
        val loginButton = binding.loginButton
        val email = binding.emailEditText
        val password = binding.passwordEditText

        enableButton()

        email.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                enableButton()
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

        password.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                enableButton()
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

        loginButton.setOnClickListener {
            loginViewModel.postLogin(email.text.toString(), password.text.toString())
        }

    }

    private fun enableButton() {
        val loginButton = binding.loginButton
        val email = binding.emailEditText.text
        val password = binding.passwordEditText.text

        when {
            email.toString().isNotEmpty() && email != null && password.toString().length >= 8 -> {
                loginButton.isEnabled = true
            }
            else -> {
                loginButton.isEnabled = false
            }
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = Constant.IMAGE_DURATION
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(Constant.DURATION)
        val emailTextView = ObjectAnimator.ofFloat(binding.emailEditText, View.ALPHA, 1f).setDuration(Constant.DURATION)
        val emailEditTextLayout = ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(Constant.DURATION)
        val passwordTextView = ObjectAnimator.ofFloat(binding.passwordEditText, View.ALPHA, 1f).setDuration(Constant.DURATION)
        val passwordEditTextLayout = ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(Constant.DURATION)
        val login = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(Constant.DURATION)

        AnimatorSet().apply {
            playSequentially(title, emailTextView, emailEditTextLayout, passwordTextView, passwordEditTextLayout, login)
            startDelay = Constant.DURATION
        }.start()
    }

    private fun saveSession(user: UserModel){
        loginViewModel.saveUser(user)
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

}