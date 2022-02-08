package com.movocash.movo.utilities.helper

import android.content.Context
import android.graphics.Typeface
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.movocash.movo.R

class TypeFaceEditText : AppCompatEditText {

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        setCustomFont(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        setCustomFont(context, attrs)
    }

    private fun setCustomFont(ctx: Context, attrs: AttributeSet) {
        val a = ctx.obtainStyledAttributes(attrs, R.styleable.TextViewPlus)
        val customFont = a.getString(R.styleable.TextViewPlus_customFont)
        val isSpacesAllowed = a.getBoolean(R.styleable.TextViewPlus_spacesAllow , true)
        setCustomFont(ctx, customFont)
        setSpacesAllowed(isSpacesAllowed)

        a.recycle()
    }

    fun setCustomFont(ctx: Context, asset: String?): Boolean {
        var tf: Typeface? = null
        if (asset != null){
            try {
                tf = Typeface.createFromAsset(ctx.assets, asset)
            } catch (e: Exception) {

                return false
            }
        }else {
            try {
                tf = Typeface.createFromAsset(ctx.assets, "regular.ttf")
            } catch (e: Exception) {

                return false
            }
        }

        typeface = tf
        return true
    }

    fun setSpacesAllowed(isAllowed: Boolean){
        if (!isAllowed){
            this.filters = arrayOf(InputFilter { source, _, _, _, _, _ ->
                source.toString().filterNot { it.isWhitespace() }
            })
        }else {
            stopSpaceFromStart(isAllowed)
        }
    }

    fun stopSpaceFromStart(isAllowed: Boolean){
        this.addTextChangedListener(object  : TextWatcher{
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val text = this@TypeFaceEditText.text.toString()
                    if (text.startsWith(" ")) {
                        this@TypeFaceEditText.setText(text.trim { it <= ' ' })
                    }

            }
        })
    }

    companion object {
        private val TAG = "TextView"
    }

}
