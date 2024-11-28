package com.example.storie.ui.activity.splashscreen

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.storie.MainActivity
import com.example.storie.R
import com.example.storie.data.preferences.SettingPreference
import com.example.storie.data.preferences.UserPreference
import com.example.storie.data.preferences.dataStore
import com.example.storie.ui.activity.setting.SettingViewModel
import com.example.storie.ui.activity.setting.SettingViewModelFactory
import com.example.storie.ui.activity.welcome.WelcomeActivity

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    private lateinit var splashViewModel: SplashViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash_screen)
        val userPreferences = UserPreference.getInstance(application.dataStore)
        splashViewModel = ViewModelProvider(
            this,
            SplashViewModelFactory(userPreferences)
        )[SplashViewModel::class.java]

        val settingPreference = SettingPreference.getInstance(application.dataStore)
        val settingViewModel = ViewModelProvider(
            this,
            SettingViewModelFactory(settingPreference)
        )[SettingViewModel::class.java]

        settingViewModel.getThemeSettings().observe(this) { darkModeActive ->
            if (darkModeActive) {
                delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_YES
            } else {
                delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_NO
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        Handler(Looper.getMainLooper()).postDelayed({
            splashViewModel.isUserTokenAvailable.observe(this) { userTokenAvailable ->
                if (userTokenAvailable) {
                    intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    intent = Intent(this, WelcomeActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }, 2000)
    }
}