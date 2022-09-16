package incubasys.needcharge.base.utils

import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.widget.TextView

/**
 * This class can be used to define a listener for a compound drawable.
 *
 * @author Matthew Weiler
 */
abstract class DrawableClickListener
/**
 * This will create a new instance of a [DrawableClickListener]
 * object.
 *
 * @param view
 * The [TextView] that this [DrawableClickListener]
 * is associated with.
 * @param drawableIndex
 * The index of the drawable that this
 * [DrawableClickListener] pertains to.
 * <br></br>
 * *use one of the values:
 * **DrawableOnTouchListener.DRAWABLE_INDEX_****
 * @param fuzzOverride
 * The number of pixels of &quot;fuzz&quot; that should be
 * included to account for the size of a finger.
 */
@JvmOverloads constructor(
    private val view: TextView, private val drawableIndex: Int,
    /* PRIVATE VARIABLES */
    /**
     * This stores the number of pixels of &quot;fuzz&quot; that should be
     * included to account for the size of a finger.
     */
    private val fuzz: Int = DEFAULT_FUZZ
) : OnTouchListener {
    /**
     * This will store a reference to the [Drawable].
     */
    private var drawable: Drawable? = null

    init {
        val drawables = view.compoundDrawables
        if (drawables != null && drawables.size == 4) {
            this.drawable = drawables[drawableIndex]
        }
    }

    /* OVERRIDDEN PUBLIC METHODS */
    override fun onTouch(v: View, event: MotionEvent): Boolean {
        val drawables = view.compoundDrawables
        if (drawables.size == 4) {
            this.drawable = drawables[drawableIndex]
        }
        if (event.action == MotionEvent.ACTION_DOWN && drawable != null) {
            val x = event.x.toInt()
            val y = event.y.toInt()
            val bounds = drawable!!.bounds
            if (this.isClickOnDrawable(x, y, v, bounds, this.fuzz)) {
                return this.onDrawableClick()
            }
        }
        return false
    }

    /* PUBLIC METHODS */
    /**
     *
     */
    abstract fun isClickOnDrawable(x: Int, y: Int, view: View, drawableBounds: Rect, fuzz: Int): Boolean

    /**
     * This method will be fired when the drawable is touched/clicked.
     *
     * @return
     * `true` if the listener has consumed the event;
     * `false` otherwise.
     */
    abstract fun onDrawableClick(): Boolean

    /* PUBLIC CLASSES */
    /**
     * This class can be used to define a listener for a **LEFT** compound
     * drawable.
     */
    abstract class LeftDrawableClickListener : DrawableClickListener {

        /* CONSTRUCTORS */
        /**
         * This will create a new instance of a
         * [LeftDrawableClickListener] object.
         *
         * @param view
         * The [TextView] that this
         * [LeftDrawableClickListener] is associated with.
         */
        constructor(view: TextView) : super(view, DRAWABLE_INDEX_LEFT) {}

        /**
         * This will create a new instance of a
         * [LeftDrawableClickListener] object.
         *
         * @param view
         * The [TextView] that this
         * [LeftDrawableClickListener] is associated with.
         * @param fuzzOverride
         * The number of pixels of &quot;fuzz&quot; that should be
         * included to account for the size of a finger.
         */
        constructor(view: TextView, fuzz: Int) : super(view, DRAWABLE_INDEX_LEFT, fuzz) {}

        /* PUBLIC METHODS */
        override fun isClickOnDrawable(x: Int, y: Int, view: View, drawableBounds: Rect, fuzz: Int): Boolean {
            if (x >= view.paddingLeft - fuzz) {
                if (x <= view.paddingLeft + drawableBounds.width() + fuzz) {
                    if (y >= view.paddingTop - fuzz) {
                        if (y <= view.height - view.paddingBottom + fuzz) {
                            return true
                        }
                    }
                }
            }
            return false
        }

    }

    /**
     * This class can be used to define a listener for a **TOP** compound
     * drawable.
     */
    abstract class TopDrawableClickListener : DrawableClickListener {

        /* CONSTRUCTORS */
        /**
         * This will create a new instance of a [TopDrawableClickListener]
         * object.
         *
         * @param view
         * The [TextView] that this
         * [TopDrawableClickListener] is associated with.
         */
        constructor(view: TextView) : super(view, DRAWABLE_INDEX_TOP) {}

        /**
         * This will create a new instance of a [TopDrawableClickListener]
         * object.
         *
         * @param view
         * The [TextView] that this
         * [TopDrawableClickListener] is associated with.
         * @param fuzzOverride
         * The number of pixels of &quot;fuzz&quot; that should be
         * included to account for the size of a finger.
         */
        constructor(view: TextView, fuzz: Int) : super(view, DRAWABLE_INDEX_TOP, fuzz) {}

        /* PUBLIC METHODS */
        override fun isClickOnDrawable(x: Int, y: Int, view: View, drawableBounds: Rect, fuzz: Int): Boolean {
            if (x >= view.paddingLeft - fuzz) {
                if (x <= view.width - view.paddingRight + fuzz) {
                    if (y >= view.paddingTop - fuzz) {
                        if (y <= view.paddingTop + drawableBounds.height() + fuzz) {
                            return true
                        }
                    }
                }
            }
            return false
        }

    }

    /**
     * This class can be used to define a listener for a **RIGHT** compound
     * drawable.
     */
    abstract class RightDrawableClickListener : DrawableClickListener {

        /* CONSTRUCTORS */
        /**
         * This will create a new instance of a
         * [RightDrawableClickListener] object.
         *
         * @param view
         * The [TextView] that this
         * [RightDrawableClickListener] is associated with.
         */
        constructor(view: TextView) : super(view, DRAWABLE_INDEX_RIGHT) {}

        /**
         * This will create a new instance of a
         * [RightDrawableClickListener] object.
         *
         * @param view
         * The [TextView] that this
         * [RightDrawableClickListener] is associated with.
         * @param fuzzOverride
         * The number of pixels of &quot;fuzz&quot; that should be
         * included to account for the size of a finger.
         */
        constructor(view: TextView, fuzz: Int) : super(view, DRAWABLE_INDEX_RIGHT, fuzz) {}

        /* PUBLIC METHODS */
        override fun isClickOnDrawable(x: Int, y: Int, view: View, drawableBounds: Rect, fuzz: Int): Boolean {
            if (x >= view.width - view.paddingRight - drawableBounds.width() - fuzz) {
                if (x <= view.width - view.paddingRight + fuzz) {
                    if (y >= view.paddingTop - fuzz) {
                        if (y <= view.height - view.paddingBottom + fuzz) {
                            return true
                        }
                    }
                }
            }
            return false
        }

    }

    /**
     * This class can be used to define a listener for a **BOTTOM** compound
     * drawable.
     */
    abstract class BottomDrawableClickListener : DrawableClickListener {

        /* CONSTRUCTORS */
        /**
         * This will create a new instance of a
         * [BottomDrawableClickListener] object.
         *
         * @param view
         * The [TextView] that this
         * [BottomDrawableClickListener] is associated with.
         */
        constructor(view: TextView) : super(view, DRAWABLE_INDEX_BOTTOM) {}

        /**
         * This will create a new instance of a
         * [BottomDrawableClickListener] object.
         *
         * @param view
         * The [TextView] that this
         * [BottomDrawableClickListener] is associated with.
         * @param fuzzOverride
         * The number of pixels of &quot;fuzz&quot; that should be
         * included to account for the size of a finger.
         */
        constructor(view: TextView, fuzz: Int) : super(view, DRAWABLE_INDEX_BOTTOM, fuzz) {}

        /* PUBLIC METHODS */
        override fun isClickOnDrawable(x: Int, y: Int, view: View, drawableBounds: Rect, fuzz: Int): Boolean {
            if (x >= view.paddingLeft - fuzz) {
                if (x <= view.width - view.paddingRight + fuzz) {
                    if (y >= view.height - view.paddingBottom - drawableBounds.height() - fuzz) {
                        if (y <= view.height - view.paddingBottom + fuzz) {
                            return true
                        }
                    }
                }
            }
            return false
        }

    }

    companion object {

        /* PUBLIC CONSTANTS */
        /**
         * This represents the left drawable.
         */
        val DRAWABLE_INDEX_LEFT = 0
        /**
         * This represents the top drawable.
         */
        val DRAWABLE_INDEX_TOP = 1
        /**
         * This represents the right drawable.
         */
        val DRAWABLE_INDEX_RIGHT = 2
        /**
         * This represents the bottom drawable.
         */
        val DRAWABLE_INDEX_BOTTOM = 3
        /**
         * This stores the default value to be used for the
         * [DrawableClickListener.fuzz].
         */
        val DEFAULT_FUZZ = 10
    }

}/* CONSTRUCTORS */
/**
 * This will create a new instance of a [DrawableClickListener]
 * object.
 *
 * @param view
 * The [TextView] that this [DrawableClickListener]
 * is associated with.
 * @param drawableIndex
 * The index of the drawable that this
 * [DrawableClickListener] pertains to.
 * <br></br>
 * *use one of the values:
 * **DrawableOnTouchListener.DRAWABLE_INDEX_****
 */