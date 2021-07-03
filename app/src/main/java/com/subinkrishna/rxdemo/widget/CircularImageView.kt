package com.subinkrishna.rxdemo.widget

import android.content.Context
import android.graphics.Outline
import android.util.AttributeSet
import android.view.View
import android.view.ViewOutlineProvider
import androidx.appcompat.widget.AppCompatImageView

/**
 * Circular ImageView implementation
 *
 * @author Subinkrishna Gopi
 * @see ViewOutlineProvider
 */
class CircularImageView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0) : AppCompatImageView(context, attrs, defStyleAttr) {

    companion object {
        // Singleton circular outline provider
        @JvmStatic val CIRCULAR = Circular()
    }

    init {
        outlineProvider = CIRCULAR
        clipToOutline = true
    }
}

/**
 * Circular view outline provider implementation
 */
class Circular : ViewOutlineProvider() {
    override fun getOutline(view: View?, outline: Outline?) {
        view?.let {
            val left = it.paddingLeft
            val top = it.paddingTop
            val right = it.width - it.paddingRight
            val bottom = it.height - it.paddingBottom
            outline?.setOval(left, top, right, bottom)
        }
    }
}