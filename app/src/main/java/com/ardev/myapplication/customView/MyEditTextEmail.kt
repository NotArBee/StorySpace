package com.ardev.myapplication.customView

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.ardev.myapplication.R
import java.util.regex.Pattern

class MyEditTextEmail @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs) {

    // Regex pattern for validating email
    private val emailPattern: Pattern = Pattern.compile(
        "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
    )

    init {
        setHintTextColor(ContextCompat.getColor(context, R.color.md_theme_onPrimary))

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No action needed before text is changed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val inputText = s.toString()
                if (!emailPattern.matcher(inputText).matches()) {
                    setError("The text you entered is not an email", null)
                } else {
                    error = null
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // No action needed after text is changed
            }
        })
    }

}
