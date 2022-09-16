package incubasys.needcharge.base

import android.app.Activity
import android.os.Bundle

interface Navigator {
    fun startModule(activity: Activity, modules: Modules, bundle: Bundle? = null, startForResult: Int? = null)
    enum class Modules {
        SPLASH, ONBOARDING, HOME, AUTH, PROFILE_AND_SETTINGS, ORDERS_AND_PAYMENT
    }

    companion object {
        const val EXTRAS = "extra_bundle"
        const val AUTH_ARG_GOTO = "GOTO"
        const val AUTH_ARG_PHONE = "phone"
        const val ONBOARDING_ARG_SHOWNFROM = "SHOWN_FROM"
        const val PROFILE_AND_SETTINGS_ARG_GOTO = "GOTO"
    }
}