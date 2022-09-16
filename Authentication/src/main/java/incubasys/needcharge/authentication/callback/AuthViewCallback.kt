package incubasys.needcharge.authentication.callback

interface AuthViewCallback {
    fun onLoginWithEmailClicked()

    fun onLoginWithPhoneClicked()

    fun onLoginWithFacebookClicked()

    fun onSignupWithEmailClicked()

    fun onSignupWithPhoneClicked()

    fun onCloseButtonClicked()

}
