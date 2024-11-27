package com.example.storie.ui.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.example.storie.R

class EditTextPassword @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs), View.OnTouchListener {
    private var showPasswordIcon: Drawable = ContextCompat.getDrawable(
        context,
        R.drawable.ic_eye_close
    ) as Drawable
    private var hidePasswordIcon: Drawable =
        ContextCompat.getDrawable(context, R.drawable.ic_eye_open) as Drawable
    private var isPasswordVisible = false

    init {
        setOnTouchListener(this)

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (isPasswordVisible) {
                    if (s.toString().isNotEmpty()) {
                        showHideButton()
                    } else {
                        clearHideButton()
                    }
                } else {
                    hideHideButton()
                }

                if (s.toString().isEmpty()) {
                    clearHideButton()
                }

            }

            override fun afterTextChanged(s: Editable?) {
                if (s.toString().length < 8) {
                    setError(context.getString(R.string.password_error_length), null)
                } else {
                    error = null
                }
            }

        })
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        hint = context.getString(R.string.password)
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }

    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        if (compoundDrawables[2] != null) {
            val hideButtonStart: Float
            val hideButtonEnd: Float
            var isHideButtonClicked = false
            if (layoutDirection == View.LAYOUT_DIRECTION_RTL) {
                hideButtonEnd = (hidePasswordIcon.intrinsicWidth + paddingStart).toFloat()
                when {
                    event.x < hideButtonEnd -> isHideButtonClicked = true
                }
            } else {
                hideButtonStart = (width - paddingEnd - hidePasswordIcon.intrinsicWidth).toFloat()
                when {
                    event.x > hideButtonStart -> isHideButtonClicked = true
                }
            }
            if (isHideButtonClicked) {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        passwordVisibility()
                        return true
                    }

                    else -> return false
                }
            } else return false
        }
        return false
    }

    private fun passwordVisibility() {
        isPasswordVisible = !isPasswordVisible
        if (isPasswordVisible) {
            showHideButton()
            transformationMethod = null
        } else {
            hideHideButton()
            transformationMethod = PasswordTransformationMethod.getInstance()
        }
    }

    private fun showHideButton() {
        setButtonDrawables(endOfTheText = hidePasswordIcon)
    }

    private fun hideHideButton() {
        setButtonDrawables(endOfTheText = showPasswordIcon)
    }

    private fun clearHideButton() {
        setButtonDrawables()
    }

    private fun setButtonDrawables(
        endOfTheText: Drawable? = null,
        topOfTheText: Drawable? = null,
        startOfTheText: Drawable? = null,
        bottomOfTheText: Drawable? = null
    ) {
        setCompoundDrawablesWithIntrinsicBounds(
            startOfTheText,
            topOfTheText,
            endOfTheText,
            bottomOfTheText
        )
    }
}