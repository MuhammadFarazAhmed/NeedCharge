package incubasys.needcharge.authentication.callback

interface LoginViewCallack {

    fun onLoginButtonClicked()

    fun onLoginWithFacebookClicked()

    fun onForgotPasswordClicked()

    fun onBackPressed()
}