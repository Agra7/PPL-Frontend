// com/example/becycle/utils/UserPreference.kt
package com.example.becycle.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.becycle.model.AuthResponse

class UserPreference(context: Context) {

    private val preferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun saveAuthTokens(accessToken: String, refreshToken: String, userId: String) {
        val editor = preferences.edit()
        editor.putString(KEY_ACCESS_TOKEN, accessToken)
        editor.putString(KEY_REFRESH_TOKEN, refreshToken)
        editor.putString(KEY_USER_ID, userId)
        editor.apply()
    }

    fun getAccessToken(): String? {
        return preferences.getString(KEY_ACCESS_TOKEN, null)
    }

    fun getRefreshToken(): String? {
        return preferences.getString(KEY_REFRESH_TOKEN, null)
    }

    fun getUserId(): String? {
        return preferences.getString(KEY_USER_ID, null)
    }

    fun clearAuthTokens() {
        val editor = preferences.edit()
        editor.remove(KEY_ACCESS_TOKEN)
        editor.remove(KEY_REFRESH_TOKEN)
        editor.remove(KEY_USER_ID)
        editor.apply()
    }

    companion object {
        private const val PREFS_NAME = "user_prefs"
        private const val KEY_ACCESS_TOKEN = "access_token"
        private const val KEY_REFRESH_TOKEN = "refresh_token"
        private const val KEY_USER_ID = "user_id"
    }
}