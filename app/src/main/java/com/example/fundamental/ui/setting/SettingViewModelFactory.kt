package com.example.fundamental.ui.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class SettingViewModelFactory(private val pref: SettingPreferences) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingMainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SettingMainViewModel(pref) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}
