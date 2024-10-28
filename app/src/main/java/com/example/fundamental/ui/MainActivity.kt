    package com.example.fundamental.ui

    import android.os.Bundle
    import com.google.android.material.bottomnavigation.BottomNavigationView
    import androidx.appcompat.app.AppCompatActivity
    import androidx.appcompat.app.AppCompatDelegate
    import androidx.lifecycle.lifecycleScope
    import androidx.navigation.findNavController
    import androidx.navigation.ui.AppBarConfiguration
    import androidx.navigation.ui.setupActionBarWithNavController
    import androidx.navigation.ui.setupWithNavController
    import com.example.fundamental.R
    import com.example.fundamental.databinding.ActivityMainBinding
    import com.example.fundamental.ui.setting.SettingPreferences
    import com.example.fundamental.ui.setting.dataStore
    import kotlinx.coroutines.launch

    class MainActivity : AppCompatActivity() {

        private lateinit var binding: ActivityMainBinding
        private lateinit var settingPreferences: SettingPreferences

        override fun onCreate(savedInstanceState: Bundle?) {
            settingPreferences = SettingPreferences.getInstance(applicationContext.dataStore)

            checkThemeSettings()

            super.onCreate(savedInstanceState)
            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)

            val navView: BottomNavigationView = binding.navView
            val navController = findNavController(R.id.nav_host_fragment_activity_main)
            val appBarConfiguration = AppBarConfiguration(
                setOf(
                    R.id.navigation_home, R.id.navigation_upcoming, R.id.navigation_finished
                )
            )
            setupActionBarWithNavController(navController, appBarConfiguration)
            navView.setupWithNavController(navController)
        }

        private fun checkThemeSettings() {
            lifecycleScope.launch {
                settingPreferences.getThemeSetting().collect { isDarkModeActive ->
                    if (isDarkModeActive) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    } else {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    }
                }
            }
        }
    }
