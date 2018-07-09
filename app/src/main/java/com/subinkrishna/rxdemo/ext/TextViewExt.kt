@file:Suppress("NOTHING_TO_INLINE")

package com.subinkrishna.rxdemo.ext

import android.widget.TextView
import androidx.core.view.isVisible

/**
 * Extension functions for [TextView]
 *
 * @author Subinkrishna Gopi
 */

/**
 * Hides the TextView if the text is empty or blank
 */
inline fun TextView.setTextOrHide(text: CharSequence? = null) {
    this.text = text ?: ""
    isVisible = !text.isNullOrBlank()
}
