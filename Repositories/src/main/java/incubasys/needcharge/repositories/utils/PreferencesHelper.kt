package incubasys.needcharge.repositories.utils

import android.content.SharedPreferences
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.google.gson.Gson
import incubasys.needcharge.datainterfaces.models.AuthHeader
import incubasys.needcharge.datainterfaces.models.User

import javax.inject.Inject

class PreferencesHelper @Inject
constructor(private val prefs: SharedPreferences) {

    private val KEY_isonboardingshown = "isOnboardingShown"
    private val KEY_sid = "sid"
    private val KEY_stoken = "stoken"
    private val KEY_ISLOGGEDIN = "ISLOGGEDIN"
    private val KEY_USER = "USER"

    val authHeaders = ObservableField<AuthHeader>()
    val isLoggedIn = ObservableBoolean()

    init {
        authHeaders.set(getAuthHeaders())
        isLoggedIn.set(isLoggedIn())
    }

    var isOnboardingShown: Boolean
        get() = prefs.getBoolean(KEY_isonboardingshown, false)
        set(isFirstTime) = prefs.edit().putBoolean(KEY_isonboardingshown, isFirstTime).apply()

    fun saveAuthHeaders(stoken: String) {
        prefs.edit().putBoolean(KEY_ISLOGGEDIN, true).apply()
        prefs.edit().putString(KEY_stoken, stoken).apply()
        authHeaders.set(AuthHeader(null, stoken))
        isLoggedIn.set(true)
    }

    fun getAuthHeaders(): AuthHeader {
        val tempSid = prefs.getInt(KEY_sid, -1)
        val sid = if (tempSid == -1) null else tempSid
        val stoken = prefs.getString(KEY_stoken, null)
        return AuthHeader(sid, stoken)
    }

    fun isLoggedIn() = prefs.getBoolean(KEY_ISLOGGEDIN, false)

    fun getUser(): User = Gson().fromJson(prefs.getString(KEY_USER, "{}"), User::class.java)

    fun removeHeaders() {
        prefs.edit().remove(KEY_ISLOGGEDIN).apply()
        prefs.edit().remove(KEY_stoken).apply()
        authHeaders.set(null)
        isLoggedIn.set(false)
    }

    fun saveUserInfo(user: User) {
        prefs.edit().putString(KEY_USER, Gson().toJson(user)).apply()
    }

    fun removeUserInfo() {
        prefs.edit().remove(KEY_USER).apply()
    }
}
