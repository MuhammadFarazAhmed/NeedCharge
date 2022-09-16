package incubasys.needcharge.base

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.view.ViewGroup

import dagger.android.support.DaggerAppCompatDialogFragment

import incubasys.needcharge.base.utils.ProgressDialogCallback


abstract class DialogBaseFragment : DaggerAppCompatDialogFragment(), ProgressDialogCallback {
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
}