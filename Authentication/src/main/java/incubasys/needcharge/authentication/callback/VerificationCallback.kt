package incubasys.needcharge.authentication.callback

import incubasys.needcharge.authentication.data.VerificationType
import incubasys.needcharge.base.FragmentCallback

interface VerificationCallback : FragmentCallback {
    fun onVerificationSuccessful(verificationType: VerificationType)
}