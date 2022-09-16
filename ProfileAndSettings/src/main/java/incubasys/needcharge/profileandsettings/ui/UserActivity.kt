package incubasys.needcharge.profileandsettings.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.facebook.login.LoginManager
import com.mlykotom.valifi.ValiFi
import incubasys.needcharge.base.BaseActivity
import incubasys.needcharge.base.Navigator
import incubasys.needcharge.profileandsettings.R
import incubasys.needcharge.profileandsettings.callback.ProfileCallback
import incubasys.needcharge.profileandsettings.callback.SettingsCallback
import incubasys.needcharge.profileandsettings.callback.UpdateProfileCallback
import incubasys.needcharge.profileandsettings.data.UpdateType

class UserActivity : BaseActivity(), SettingsCallback, UpdateProfileCallback, ProfileCallback {

    val UPDATE_PHONE_REQUEST_CODE = 1234
    val CHANGE_PASSWORD_REQUEST_CODE = 4321
    private val goto: Int by lazy {
        if (intent.getBundleExtra(Navigator.EXTRAS) != null)
            intent.getBundleExtra(Navigator.EXTRAS).getInt(Navigator.PROFILE_AND_SETTINGS_ARG_GOTO, -1)
        else
            -1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ValiFi.install(applicationContext)
        setContentView(R.layout.user_activity)
        when (goto) {
            0 -> supportFragmentManager.beginTransaction()
                    .replace(R.id.fContainer,
                            SettingsFragment.newInstance()).commit()

            1 -> supportFragmentManager.beginTransaction()
                    .replace(R.id.fContainer,
                            ProfileFragment.newInstance()).commit()
            -1 -> finish()
        }
    }


    override fun showOnBoardingScreen() {
        val bundle = Bundle()
        bundle.putBoolean(Navigator.ONBOARDING_ARG_SHOWNFROM, true)
        navigator.startModule(this, Navigator.Modules.ONBOARDING, bundle)
    }

    override fun showTermsScreen() {
        val bundle = Bundle()
        bundle.putInt(Navigator.AUTH_ARG_GOTO, 0)
        navigator.startModule(this, Navigator.Modules.AUTH, bundle)
    }

    override fun showPrivacyPolicyScreen() {
        val bundle = Bundle()
        bundle.putInt(Navigator.AUTH_ARG_GOTO, 1)
        navigator.startModule(this, Navigator.Modules.AUTH, bundle)
    }

    override fun showChangePasswordScreen() {
        val bundle = Bundle()
        bundle.putInt(Navigator.AUTH_ARG_GOTO, 3)
        navigator.startModule(this, Navigator.Modules.AUTH, bundle, CHANGE_PASSWORD_REQUEST_CODE)
    }

    override fun onSignOutClicked() {
        LoginManager.getInstance().logOut()
    }

    override fun onUpdatePhoneClicked(phone: String) {
        val bundle = Bundle()
        bundle.putInt(Navigator.AUTH_ARG_GOTO, 2)
        bundle.putString(Navigator.AUTH_ARG_PHONE, phone)
        navigator.startModule(this, Navigator.Modules.AUTH, bundle, UPDATE_PHONE_REQUEST_CODE)
    }

    override fun onSignOutSuccessful() {
        finish()
    }

    override fun onUpdateSuccessful(type: UpdateType) {
        finish()
    }

    override fun showUpdateNameScreen() {
        startUpdateFragment(UpdateType.NAME)
    }

    override fun showUpdateEmailScreen() {
        startUpdateFragment(UpdateType.EMAIL)
    }

    override fun showUpdatePhoneScreen() {
        startUpdateFragment(UpdateType.PHONE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == UPDATE_PHONE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            supportFragmentManager.popBackStack()
            onUpdateSuccessful(UpdateType.PHONE)
        } else if (requestCode == CHANGE_PASSWORD_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            finish()
        }
    }

    private fun startUpdateFragment(type: UpdateType) {
        supportFragmentManager.beginTransaction().replace(R.id.fContainer,
                UpdateProfileFragment.newInstance(type), "UpdateProfileFragment")
                .addToBackStack("UpdateProfileFragment").commit()
    }

}
