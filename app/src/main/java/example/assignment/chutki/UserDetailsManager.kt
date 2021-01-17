package example.assignment.chutki

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.AndroidEntryPoint
import example.assignment.chutki.view.MainApplication
import javax.inject.Inject

object UserDetailsManager {

    const val SHARED_PREFERENCES_NAME = "UserDetailsSharedPreference"
    private const val USER_EMAIL_KEY = "UserEmail"
    private var sharedPreferences = MainApplication.appContext.
    getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)


    fun setLoginInfo(email: String) {

        sharedPreferences
            .edit()
            .putString(
                USER_EMAIL_KEY,
                email
            )
            .apply()
    }

    fun getLoginInfo(): String? {
        return sharedPreferences.getString(USER_EMAIL_KEY, "")
    }

    fun logout() {
        sharedPreferences.edit()
            .remove(USER_EMAIL_KEY)
            .apply()
    }
}