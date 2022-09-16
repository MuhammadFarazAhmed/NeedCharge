package incubasys.needcharge.home.utils

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import incubasys.needcharge.base.closeKeyboard
import incubasys.needcharge.home.R
import android.view.View.OnFocusChangeListener
import incubasys.needcharge.base.utils.DrawableClickListener


class EditText(context: Context, attrs: AttributeSet) : EditText(context, attrs) {

    private var keyImeChangeListener: KeyImeChange? = null

    init {
        isCursorVisible = false
        setOnTouchListener(object :
            DrawableClickListener.RightDrawableClickListener(this@EditText) {
            override fun onDrawableClick(): Boolean {
                setText("")
                return false
            }
        })
        setOnClickListener {
            isCursorVisible = true
        }
        setOnEditorActionListener {_, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE) {
                isCursorVisible = false
                closeKeyboard()
            }
            true
        }
        onFocusChangeListener = OnFocusChangeListener {_, hasFocus ->
            if (!hasFocus) {
                closeKeyboard()
            }
        }
        addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrBlank()) {
                    setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_search, 0, 0, 0)
                } else if (s.isNotBlank()) {
                    setCompoundDrawablesRelativeWithIntrinsicBounds(
                        R.drawable.ic_search,
                        0,
                        R.drawable.ic_close_with_circle,
                        0
                    )
                }
            }

        })
    }

    fun setKeyImeChangeListener(listener: KeyImeChange) {
        keyImeChangeListener = listener
    }

    interface KeyImeChange {
        fun onKeyIme(keyCode: Int, event: KeyEvent)
    }

    override fun onKeyPreIme(keyCode: Int, event: KeyEvent): Boolean {
        if (keyImeChangeListener != null) {
            keyImeChangeListener!!.onKeyIme(keyCode, event)
        }
        if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
            isCursorVisible = false
        }
        return false
    }
}