package incubasys.needcharge.onboarding.ui

import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import incubasys.needcharge.base.BaseActivity
import incubasys.needcharge.base.Navigator
import incubasys.needcharge.base.Navigator.Companion.EXTRAS
import incubasys.needcharge.base.Navigator.Companion.ONBOARDING_ARG_SHOWNFROM
import incubasys.needcharge.onboarding.R
import incubasys.needcharge.onboarding.callback.OnboardingCallback


class OnboardingActivity : BaseActivity(), OnboardingCallback {

    private val shownFromSettings: Boolean by lazy {
        if (intent.getBundleExtra(EXTRAS) != null)
            intent.getBundleExtra(EXTRAS).getBoolean(ONBOARDING_ARG_SHOWNFROM, false)
        else
            false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.onboarding_activity)
    }


    override fun onFinishButtonClicked() {
        if (!shownFromSettings) {
            navigator.startModule(this, Navigator.Modules.HOME)
        }
        finish()
    }
}
