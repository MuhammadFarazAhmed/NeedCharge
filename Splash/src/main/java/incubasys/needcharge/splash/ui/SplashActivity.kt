package incubasys.needcharge.splash.ui

import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import incubasys.needcharge.base.BaseActivity
import incubasys.needcharge.base.Navigator
import incubasys.needcharge.splash.R
import incubasys.needcharge.splash.callback.SplashCallback
import incubasys.needcharge.splash.databinding.ActivitySplashBinding

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class SplashActivity : BaseActivity(), SplashCallback {
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_splash
        )

        supportFragmentManager.beginTransaction().add(R.id.flSplash, SplashFragment.newInstance(), "SplashFragment")
            .commit()
    }

    override fun onStartActivity(module: Navigator.Modules) {
        navigator.startModule(this, module)
        finish()
    }

}
