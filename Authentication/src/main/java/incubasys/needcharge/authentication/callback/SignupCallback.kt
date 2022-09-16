package incubasys.needcharge.authentication.callback

import incubasys.needcharge.authentication.data.AuthType
import incubasys.needcharge.base.FragmentCallback

interface SignupCallback : FragmentCallback {
    fun showPhoneVerificationForSignup(fullName : String, phone : String)
    fun signUpSucessful(signupType: AuthType)
}
