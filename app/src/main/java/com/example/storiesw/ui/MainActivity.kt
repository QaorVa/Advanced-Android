package com.example.storiesw.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.storiesw.R
import com.example.storiesw.ui.home.HomeActivity
import com.example.storiesw.ui.login.LoginActivity
import com.example.storiesw.utils.PreferenceManager

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val preferenceManager = PreferenceManager(this)
        val isLoggedIn = preferenceManager.isLoggedIn()

        if (isLoggedIn) {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        } else {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        finish()

    }


}