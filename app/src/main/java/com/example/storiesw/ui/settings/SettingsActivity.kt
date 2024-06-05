package com.example.storiesw.ui.settings

import android.content.Intent
import android.provider.Settings
import com.example.storiesw.R
import com.example.storiesw.databinding.ActivitySettingsBinding
import com.example.storiesw.ui.base.BaseActivity
import com.example.storiesw.ui.login.LoginActivity
import com.example.storiesw.utils.PreferenceManager

class SettingsActivity : BaseActivity<ActivitySettingsBinding>() {
    private lateinit var preferenceManager: PreferenceManager

    override fun getViewBinding(): ActivitySettingsBinding {
        return ActivitySettingsBinding.inflate(layoutInflater)
    }

    override fun setUI() {
        setActivityLabel(getString(R.string.settings))
    }

    override fun setProcess() {

    }

    override fun setActions() {
        preferenceManager = PreferenceManager(this)

        binding.apply {
            logoutButton.setOnClickListener {
                preferenceManager.clearPreferences()

                moveToLoginActivity()

                finish()
            }

            translateButton.setOnClickListener {
                val intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(intent)
            }
        }
    }

    override fun setObservers() {

    }

    private fun moveToLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

}