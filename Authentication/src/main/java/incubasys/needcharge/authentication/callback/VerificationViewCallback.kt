package incubasys.needcharge.authentication.callback

interface VerificationViewCallback {

    fun onVerifyButtonClicked()

    fun onResendButtonClicked()

    fun onBackPressed()
}