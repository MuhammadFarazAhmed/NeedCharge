package incubasys.needcharge.profileandsettings.callback

interface SettingsCallback {
    fun showOnBoardingScreen()
    fun showTermsScreen()
    fun showPrivacyPolicyScreen()
    fun showChangePasswordScreen()
    fun onBackPressed()
    fun onSignOutClicked()
    fun onSignOutSuccessful()

}
