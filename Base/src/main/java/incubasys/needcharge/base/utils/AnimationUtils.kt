package incubasys.needcharge.base.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.view.View
import android.view.ViewAnimationUtils
import androidx.interpolator.view.animation.FastOutSlowInInterpolator

object AnimationUtils {

    fun registerCircularRevealAnimation(
        context: Context,
        view: View,
        revealSettings: RevealAnimationSetting,
        startColor: Int,
        endColor: Int
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            view.addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                override fun onLayoutChange(
                    v: View,
                    left: Int,
                    top: Int,
                    right: Int,
                    bottom: Int,
                    oldLeft: Int,
                    oldTop: Int,
                    oldRight: Int,
                    oldBottom: Int
                ) {
                    v.removeOnLayoutChangeListener(this)
                    val cx = revealSettings.centerX
                    val cy = revealSettings.centerY
                    val width = revealSettings.width
                    val height = revealSettings.height
                    val duration = context.resources.getInteger(android.R.integer.config_mediumAnimTime)

                    //Simply use the diagonal of the view
                    val finalRadius = Math.sqrt((width * width + height * height).toDouble()).toFloat()
                    val anim = ViewAnimationUtils.createCircularReveal(v, cx, cy, 0f, finalRadius)
                        .setDuration(duration.toLong())
                    anim.interpolator = FastOutSlowInInterpolator()
                    anim.start()
                    startColorAnimation(view, startColor, endColor, duration)
                }
            })
        }
    }

    private fun startColorAnimation(view: View, startColor: Int, endColor: Int, duration: Int) {
        val anim = ValueAnimator()
        anim.setIntValues(startColor, endColor)
        anim.setEvaluator(ArgbEvaluator())
        anim.addUpdateListener { valueAnimator -> view.setBackgroundColor(valueAnimator.animatedValue as Int) }
        anim.duration = duration.toLong()
        anim.start()
    }

    fun startCircularExitAnimation(
        context: Context,
        view: View,
        revealSettings: RevealAnimationSetting,
        startColor: Int,
        endColor: Int,
        listener: Dismissible.OnDismissedListener
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val cx = revealSettings.centerX
            val cy = revealSettings.centerY
            val width = revealSettings.width
            val height = revealSettings.height
            val duration = context.resources.getInteger(android.R.integer.config_mediumAnimTime)

            val initRadius = Math.sqrt((width * width + height * height).toDouble()).toFloat()
            val anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, initRadius, 0f)
            anim.duration = duration.toLong()
            anim.interpolator = FastOutSlowInInterpolator()
            anim.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    listener.onDismissed()
                }
            })
            anim.start()
            startColorAnimation(view, startColor, endColor, duration)
        } else {
            listener.onDismissed()
        }
    }
}



