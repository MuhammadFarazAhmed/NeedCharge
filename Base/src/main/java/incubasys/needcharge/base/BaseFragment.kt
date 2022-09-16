package incubasys.needcharge.base

import dagger.android.support.DaggerFragment
import incubasys.needcharge.base.utils.ProgressDialogCallback

abstract class BaseFragment : DaggerFragment(), ProgressDialogCallback {

    override fun onProgressDialogCancelled() {

    }
}