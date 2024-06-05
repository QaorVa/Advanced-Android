package com.example.storiesw.ui.register

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import com.example.storiesw.R
import com.example.storiesw.databinding.ActivityRegisterBinding
import com.example.storiesw.ui.base.BaseActivity
import com.example.storiesw.ui.login.LoginActivity
import com.example.storiesw.utils.showError
import com.example.storiesw.utils.showLoading

class RegisterActivity : BaseActivity<ActivityRegisterBinding>() {
    private lateinit var viewModel: RegisterViewModel

    override fun getViewBinding(): ActivityRegisterBinding {
        return ActivityRegisterBinding.inflate(layoutInflater)
    }

    override fun setUI() {
        setActivityLabel(getString(R.string.register))
    }

    override fun setActions() {
        binding.apply {
            registerButton.setOnClickListener {
                registerUser()
            }
        }
    }

    override fun setProcess() {
        viewModel = ViewModelProvider(this)[RegisterViewModel::class.java]
    }

    override fun setObservers() {
        viewModel.registerComplete.observe(this) { registerComplete ->
            if (registerComplete) {
                showToast(getString(R.string.register_success))

                navigateToLoginActivity()
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

    private fun registerUser() {
        with(binding) {
            val name = edRegisterName.text.toString()
            val email = edRegisterEmail.text.toString()
            val password = edRegisterPassword.text.toString()

            when {
                password.isEmpty() || password.length < 8 -> {
                    edRegisterPassword.showError(getString(R.string.validation_password))
                }
                email.isEmpty() -> {
                    edRegisterEmail.showError(getString(R.string.fill_required))
                }
                name.isEmpty() -> {
                    edRegisterName.showError(getString(R.string.fill_required))
                }
                else -> {
                    viewModel.register(name, email, password)
                }
            }
        }

    }

    private fun navigateToLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

}