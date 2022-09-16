package incubasys.needcharge.authentication.utils

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import dagger.android.support.DaggerAppCompatDialogFragment
import incubasys.needcharge.base.ProgressDialogFragment
import incubasys.needcharge.base.showErrorMessage
import incubasys.needcharge.base.utils.ProgressDialogCallback
import incubasys.needcharge.datainterfaces.models.Message

abstract class AuthBaseFragment : DaggerAppCompatDialogFragment(), ProgressDialogCallback {
    override fun onProgressDialogCancelled() {

    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {

            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window?.setLayout(width, height)
            val back = ColorDrawable(Color.TRANSPARENT)
            val inset = InsetDrawable(back, 20, 80, 20, 20)
            dialog.window?.setBackgroundDrawable(inset)
        }
    }

    fun Fragment.onErrorSimple(): (Message) -> Unit {
        return {
            showErrorMessage(it.message)
        }
    }
}