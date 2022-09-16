package incubasys.needcharge.authentication.callback

import incubasys.needcharge.base.FragmentCallback

interface ForgotCallback : FragmentCallback {
    fun onForgotEmailSentSuccessfuly()
}