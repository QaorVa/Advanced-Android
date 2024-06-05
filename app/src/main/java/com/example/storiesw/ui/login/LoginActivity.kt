package com.example.storiesw.ui.login

import android.content.Intent
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import com.example.storiesw.R
import com.example.storiesw.databinding.ActivityLoginBinding
import com.example.storiesw.ui.base.BaseActivity
import com.example.storiesw.ui.home.HomeActivity
import com.example.storiesw.ui.register.RegisterActivity
import com.example.storiesw.utils.showError
import com.example.storiesw.utils.showLoading

class LoginActivity : BaseActivity<ActivityLoginBinding>(){
    private lateinit var viewModel: LoginViewModel

    override fun getViewBinding(): ActivityLoginBinding {
        return ActivityLoginBinding.inflate(layoutInflater)
    }

    override fun setUI() {
        binding.apply {
            loginButton.setOnClickListener {
                loginUser()

            }

            registerButton.setOnClickListener {
                navigateToRegisterActivity()
            }
        }

        setActivityLabel(getString(R.string.login))
    }

    override fun setProcess() {
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
    }

    override fun setActions() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finishAffinity()
            }
        })
    }

    override fun setObservers() {
        viewModel.loginComplete.observe(this) { loginComplete ->
            if (loginComplete) {
                navigateToHomeActivity()
            }
        }

        viewModel.isLoading.observe(this) {
            showLoading(binding.progressCard, it)
        }

        viewModel.isError.observe(this) { isError ->
            if(isError) {
                showErrorDialog()
            }
        }

        viewModel.errorMessage.observe(this) { message ->
            showToast(message)
        }
    }

    private fun loginUser() {
        val email = binding.edLoginEmail.text.toString()
        val password = binding.edLoginPassword.text.toString()
        when {
            password.isEmpty() || password.length < 8  -> {
                binding.edLoginPassword.showError(getString(R.string.validation_password))
            }
            email.isEmpty() -> {
                binding.edLoginEmail.showError(getString(R.string.fill_required))
            }
            else -> {
                viewModel.login(email, password)

            }
        }

    }

    private fun navigateToHomeActivity() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToRegisterActivity() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }


}