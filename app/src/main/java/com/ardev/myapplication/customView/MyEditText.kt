package com.ardev.myapplication.customView

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.ardev.myapplication.R

class MyEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs) {

    init {
        setHintTextColor(ContextCompat.getColor(context, R.color.md_theme_onPrimary))
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

    }
}
