package incubasys.needcharge.home.utils


import android.content.Context
import android.content.res.Resources
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View

import androidx.viewpager.widget.ViewPager
import androidx.core.view.ViewCompat.setScaleY
import android.opengl.ETC1.getHeight
import android.transition.TransitionManager
import android.util.Log


class CustomViewPager @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : ViewPager(context, attrs), ViewPager.PageTransformer {
    private var MAX_SCALE = 0.0f
    private var mPageMargin: Int = 0


    init {
        // clipping should be off on the pager for its children so that they can scale out of bounds.
        clipChildren = false
        clipToPadding = false
        // to avoid fade effect at the end of the page
        overScrollMode = 2
        setPageTransformer(false, this)
        offscreenPageLimit = 20
        mPageMargin = dp2px(context.resources, 30)
         pageMargin = dp2px(context.resources, 15)
        setPadding(mPageMargin, 0, mPageMargin, 0)
    }

    fun dp2px(resource: Resources, dp: Int): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), resource.displayMetrics).toInt()
    }

    override fun transformPage(page: View, position: Float) {
        val pageWidth = measuredWidth -
                paddingLeft - paddingRight
        val pageHeight = height
        val paddingLeft = paddingLeft
        val transformPos = (page.left - (scrollX + paddingLeft)).toFloat() / pageWidth
        page.apply {
            when {
                transformPos < -1 -> { // [-Infinity,-1)
                    // This page is way off-screen to the left.
                }
                transformPos <= 1 -> { // [-1,1]
                    // Modify the default slide transition to shrink the page as well
                    val scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(transformPos))
                    scaleY = scaleFactor
                }
                else -> { // (1,+Infinity]
                    // This page is way off-screen to the right.
                }
            }
        }
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var heightMeasureSpec = heightMeasureSpec
        val mode = View.MeasureSpec.getMode(heightMeasureSpec)
        // Unspecified means that the ViewPager is in a ScrollView WRAP_CONTENT.
        // At Most means that the ViewPager is not in a ScrollView WRAP_CONTENT.
        if (mode == View.MeasureSpec.UNSPECIFIED || mode == View.MeasureSpec.AT_MOST) {
            // super has to be called in the beginning so the child views can be initialized.
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
            var height = 0
            for (i in 0 until childCount) {
                val child = getChildAt(i)
                child.measure(widthMeasureSpec, View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
                val h = child.measuredHeight
                if (h > height) height = h
            }
            heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY)
        }
        // super has to be called again so the new specs are treated as exact measurements
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    companion object {
        private const val MIN_SCALE = 0.85f
        val TAG = "KKViewPager"

    }
}