package incubasys.needcharge.base.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.VectorDrawable
import android.text.TextUtils
import android.util.Base64
import android.util.DisplayMetrics
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.TextView
import incubasys.needcharge.base.R
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.ArrayList

object Utils {

    fun getFacebookKeyHash(context: Context): String {
        var keyhash = ""

        try {
            @SuppressLint("PackageManagerGetSignatures") val info = context.packageManager.getPackageInfo(
                    context.packageName,
                    PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                keyhash = Base64.encodeToString(md.digest(), Base64.DEFAULT)
                Log.d("KeyHash:", keyhash)
            }
        } catch (ignored: PackageManager.NameNotFoundException) {

        } catch (ignored: NoSuchAlgorithmException) {
        }

        return keyhash
    }

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp      A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    fun convertDpToPixel(dp: Float, context: Context): Float {
        val resources = context.resources
        val metrics = resources.displayMetrics
        return dp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px      A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    fun convertPixelsToDp(px: Float, context: Context): Float {
        return px / (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

    fun isValidEmail(target: String): Boolean {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }

    fun isPasswordValid(password: String): Boolean {
        return !TextUtils.isEmpty(password) && password.length >= 8
    }

    fun isValidPhone(target: String): Boolean {
        return !TextUtils.isEmpty(target) && TextUtils.isDigitsOnly(target) && target.length == 12
    }

    fun mergeNames(firstName: String, lastName: String): String {
        return String.format("%s %s", firstName, lastName)
    }

    inline fun <T : Any> ifLet(vararg elements: T?, closure: (List<T>) -> Unit) {
        if (elements.all { it != null }) {
            closure(elements.filterNotNull())
        }
    }



    fun fitDrawable(resources: Resources, drawable: Drawable, sizePx: Int): Drawable {
        var drawable = drawable
        if (drawable.intrinsicWidth != sizePx || drawable.intrinsicHeight != sizePx) {

            if (drawable is BitmapDrawable) {

                drawable = BitmapDrawable(resources, Bitmap.createScaledBitmap(getBitmap(drawable), sizePx, sizePx, true))
            }
        }
        drawable.setBounds(0, 0, sizePx, sizePx)

        return drawable
    }

    fun getBitmap(drawable: Drawable): Bitmap {
        return if (drawable is BitmapDrawable) {
            drawable.bitmap
        } else (drawable as? VectorDrawable)?.let { getBitmap(it) }
                ?: throw IllegalArgumentException("unsupported drawable type")
    }

}
