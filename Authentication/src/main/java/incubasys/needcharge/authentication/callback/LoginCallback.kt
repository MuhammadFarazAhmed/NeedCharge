package incubasys.needcharge.authentication.callback

import incubasys.needcharge.authentication.data.AuthType
import incubasys.needcharge.base.FragmentCallback

interface LoginCallback : FragmentCallback{

    fun onLoginSuccess(authType: AuthType)

    fun showPhoneVerificationForLogin(phone: String)

    fun onSuccessLoginWithFacebook()

    fun showForgotPasswordScreen()

    fun getFacebookAccessToken(onGotToken : (String) -> Unit, onError : (String) -> Unit)
}