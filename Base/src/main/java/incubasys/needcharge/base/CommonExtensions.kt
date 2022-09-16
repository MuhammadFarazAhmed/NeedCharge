package incubasys.needcharge.base

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.androidadvance.topsnackbar.TSnackbar
import incubasys.needcharge.base.utils.Utils


fun Fragment.getColor(@ColorRes color: Int): Int {
    return ContextCompat.getColor(requireContext(), color)
}

fun View.closeKeyboard() {
    val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(this.windowToken, 0)
}

fun View.openKeyboard() {
    val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.toggleSoftInputFromWindow(
            applicationWindowToken,
            InputMethodManager.SHOW_FORCED, 0
    )
}

fun Fragment.getDrawable(@DrawableRes res: Int) = ContextCompat.getDrawable(requireContext(), res)


fun Fragment.showErrorMessage(messsage: String) {
    view?.let {
        val snackBar = TSnackbar
                .make(it, messsage, TSnackbar.LENGTH_LONG)
                .setActionTextColor(Color.WHITE)
                .setAction(" ") {}
                .setIconLeft(R.drawable.ic_error_outline_black_24dp, 24f)
                .setIconPadding(16)
        val snackBarView = snackBar.view
        snackBarView.setBackgroundColor(getColor(R.color.close_red))
        val textView = snackBarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text) as TextView
        val button = snackBarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_action) as Button
        button.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, getDrawable(R.drawable.ic_close)?.let { it1 -> Utils.fitDrawable(resources, it1, 42) }, null)
        val dp = Utils.convertPixelsToDp(16f, requireContext()).toInt()
        button.compoundDrawablePadding = dp
        textView.setPaddingRelative(dp, dp, dp, dp)
        textView.setTextColor(Color.WHITE)
        snackBar.show()
    }
}

fun Fragment.showProgress(title: String = "", message: String = "") {
    ProgressDialogFragment.newInstance(title, message).show(childFragmentManager, "showProgress")
}

fun Fragment.hideProgress() {
    childFragmentManager.findFragmentByTag("showProgress")?.let {
        (it as DialogFragment).dismiss()
    }
}

fun Fragment.onLoading(): (Boolean) -> Unit {
    return {
        if (it) {
            showProgress()
        } else {
            hideProgress()
        }
    }
}

fun Activity.showErrorMessage(messsage: String, view: View) {
    view.let {
        val snackBar = TSnackbar
                .make(it, messsage, TSnackbar.LENGTH_LONG)
                .setActionTextColor(Color.WHITE)
                .setAction(" ") {}
                .setIconLeft(R.drawable.ic_error_outline_black_24dp, 24f)
                .setIconPadding(16)
        val snackBarView = snackBar.view
        snackBarView.setBackgroundColor(ContextCompat.getColor(this, R.color.close_red))
        val textView = snackBarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text) as TextView
        val button = snackBarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_action) as Button
        button.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, getDrawable(R.drawable.ic_close_bold)?.let { it1 -> Utils.fitDrawable(resources, it1, 48) }, null)
        val dp = Utils.convertPixelsToDp(16f, this).toInt()
        button.compoundDrawablePadding = dp
        textView.setPaddingRelative(dp, dp, dp, dp)
        textView.setTextColor(Color.WHITE)
        snackBar.show()
    }
}

