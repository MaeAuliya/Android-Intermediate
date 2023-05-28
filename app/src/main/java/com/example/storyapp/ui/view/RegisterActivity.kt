package com.example.storyapp.ui.view

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
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
import androidx.appcompat.app.AlertDialog
import com.example.storyapp.databinding.ActivityRegisterBinding
import com.example.storyapp.ui.viewmodel.RegisterViewModel
import com.example.storyapp.utils.Constant
import com.example.storyapp.utils.ViewModelFactory


class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var factory: ViewModelFactory
    private val registerViewModel: RegisterViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupViewModel()
        setupRegister()
        playAnimation()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else{
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupViewModel(){
        factory = ViewModelFactory.getInstance(this)

        registerViewModel.registerData.observe(this){ user ->
            if (!user.error){
                AlertDialog.Builder(this).apply {
                    setTitle("Akun berhasil dibuat!")
                    setMessage("Silahkan login terlebih dahulu")
                    setPositiveButton("Lanjut") {_, _ ->
                        finish()
                    }
                    create()
                    show()
                }
            }
        }

        registerViewModel.isLoading.observe(this){
            showLoading(it)
        }

        registerViewModel.isError.observe(this){
            showError(it)
        }

    }

    private fun setupRegister() {
        val registerButton = binding.signupButton
        val name = binding.nameEditText
        val email = binding.emailEditText
        val password = binding.passwordEditText

        enableButton()

        name.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                enableButton()
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

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

        registerButton.setOnClickListener {
            registerViewModel.postRegister(name.text.toString(), email.text.toString(), password.text.toString())
        }

    }

    private fun enableButton() {
        val registerButton = binding.signupButton
        val name = binding.nameEditText.text
        val email = binding.emailEditText.text
        val password = binding.passwordEditText.text

        when {
            name.toString().isNotEmpty() && name != null && email.toString().isNotEmpty() && email != null && password.toString().length >= 8 -> {
                registerButton.isEnabled = true
            }
            else -> {
                registerButton.isEnabled = false
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
        val nameTextView = ObjectAnimator.ofFloat(binding.nameEditText, View.ALPHA, 1f).setDuration(Constant.DURATION)
        val nameEditTextLayout = ObjectAnimator.ofFloat(binding.nameEditTextLayout, View.ALPHA, 1f).setDuration(Constant.DURATION)
        val emailTextView = ObjectAnimator.ofFloat(binding.emailEditText, View.ALPHA, 1f).setDuration(Constant.DURATION)
        val emailEditTextLayout = ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(Constant.DURATION)
        val passwordTextView = ObjectAnimator.ofFloat(binding.passwordEditText, View.ALPHA, 1f).setDuration(Constant.DURATION)
        val passwordEditTextLayout = ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(Constant.DURATION)
        val signup = ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA, 1f).setDuration(500)


        AnimatorSet().apply {
            playSequentially(
                title,
                nameTextView,
                nameEditTextLayout,
                emailTextView,
                emailEditTextLayout,
                passwordTextView,
                passwordEditTextLayout,
                signup
            )
            startDelay = Constant.DURATION
        }.start()
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