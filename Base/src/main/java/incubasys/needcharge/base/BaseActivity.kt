package incubasys.needcharge.base

import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import dagger.android.support.DaggerAppCompatActivity
import incubasys.needcharge.base.utils.ProgressDialogCallback

abstract class BaseActivity : DaggerAppCompatActivity(), ProgressDialogCallback {
    
    lateinit var navigator: Navigator
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navigator = application as Navigator
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        }
    }
    
    protected fun showProgress(title: String = "", message: String = "") {
        ProgressDialogFragment.newInstance(title, message)
                .show(supportFragmentManager, "showProgress")
    }
    
    protected fun hideProgress() {
        supportFragmentManager.findFragmentByTag("showProgress")?.let {
            (it as DialogFragment).dismiss()
        }
    }
    
    override fun onProgressDialogCancelled() {
    
    }
    
    override fun onDestroy() {
        hideProgress()
        super.onDestroy()
    }
}