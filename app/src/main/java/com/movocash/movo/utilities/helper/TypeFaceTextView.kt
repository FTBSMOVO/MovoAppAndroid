package com.movocash.movo.utilities.helper

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.TintableBackgroundView
import com.movocash.movo.R

class TypeFaceTextView : AppCompatTextView, TintableBackgroundView {

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
        setCustomFont(ctx, customFont)
        a.recycle()
    }

    fun setCustomFont(ctx: Context, asset: String?): Boolean {
        var tf: Typeface? = null
        if (asset != null) {
            try {
                tf = Typeface.createFromAsset(ctx.assets, asset)
            } catch (e: Exception) {

                return false
            }
        } else {
            try {
                tf = Typeface.createFromAsset(ctx.assets, "regular.ttf")
            } catch (e: Exception) {

                return false
            }
        }
        typeface = tf
        return true
    }

    fun setOnClickListener() {
        TODO("Not yet implemented")
    }

    companion object {
        private val TAG = "TextView"
    }

}