package incubasys.needcharge.authentication.callback

import incubasys.needcharge.base.FragmentCallback

interface AuthCallback : FragmentCallback {

    fun showPhoneLoginScreen()

    fun showEmailLoginScreen()

    fun onSuccessLoginWithFacebook()

    fun showSignupWithEmailScreen()

    fun showSignupWithPhoneScreen()

    fun showPrivacyPolicyScreen(content: String)

    fun showTermsScreen(content: String)
    fun getFacebookAccessToken(onGotToken : (String) -> Unit, onError : (String) -> Unit)
}
