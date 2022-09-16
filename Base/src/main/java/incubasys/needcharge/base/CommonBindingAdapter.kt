package incubasys.needcharge.base

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.text.TextUtils
import android.widget.EditText
import android.widget.ImageView
import android.widget.ImageView.ScaleType.*
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import java.io.File


@BindingAdapter(
        value = ["android:src", "placeholderRes", "errorRes", "android:scaleType", "isCircle", "isRoundCorners", "cornerType"],
        requireAll = false
)
fun loadImageDrawable(
        imageView: ImageView,
        image: Any?,
        placeholderResId: Drawable? = null,
        errorResId: Drawable? = null,
        scaleType: ImageView.ScaleType? = null,
        isCircle: Boolean = false,
        isRoundCorners: Boolean = false,
        cornerType: RoundedCornersTransformation.CornerType? = RoundedCornersTransformation.CornerType.ALL
) {
    val requestManager = Glide.with(imageView)
    var requestBuilder: RequestBuilder<*>? = null
    if (image != null) {
        requestBuilder = when (image) {
            is String -> if (image.startsWith("http://") || image.startsWith("https://")) {
                requestManager.load(image)
            } else {
                requestManager.load(File(image))
            }
            is Uri -> requestManager.load(image as Uri?)
            is Drawable -> requestManager.load(image as Drawable?)
            is Bitmap -> requestManager.load(image as Bitmap?)
            is Int -> requestManager.load(image as Int?)
            is File -> requestManager.load(image as File?)
            is Array<*> -> requestManager.load(image as Array<*>?)
            else -> requestManager.load(image)
        }
    }
    var options = RequestOptions()
    options = options.override(imageView.width, imageView.height)
    if (placeholderResId != null) {
        if (requestBuilder == null) {
            requestBuilder = requestManager.load(placeholderResId)
        }
    }
    if (scaleType != null) {
        when (scaleType) {
            CENTER_CROP -> options = options.centerCrop()
            FIT_CENTER -> options = options.fitCenter()
            CENTER_INSIDE -> options = options.centerInside()
            MATRIX -> TODO()
            FIT_XY -> TODO()
            FIT_START -> TODO()
            FIT_END -> TODO()
            CENTER -> TODO()
        }
    }
    if (isCircle) {
        options = options.circleCrop()
    }
    if (isRoundCorners) {
        options =
                RequestOptions.bitmapTransform(MultiTransformation(CenterCrop(), RoundedCornersTransformation(34, 0, cornerType
                        ?: RoundedCornersTransformation.CornerType.ALL)))
    }
    requestBuilder?.apply(options.placeholder(placeholderResId).error(errorResId))
            // ?.transition(withCrossFade())
            ?.into(imageView)
}


@BindingAdapter("htmlText")
fun setHtmlText(textView: TextView, text: String) {
    if (!TextUtils.isEmpty(text)) {
        textView.text = HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_LEGACY)
    }
}

@BindingAdapter("errorMessage")
fun setError(textView: EditText, errorMessage: String?) {
    textView.error = errorMessage
}
