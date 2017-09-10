/*
 * Copyright 2017 Miguel Panelo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.gumil.basamto.common

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.annotation.ColorRes
import android.support.annotation.StyleRes
import android.support.v4.content.ContextCompat
import android.widget.TextView
import io.github.gumil.basamto.R

internal fun Context.findActivity(): Activity {
    return if (this is Activity) {
        this
    } else {
        val contextWrapper = this as ContextWrapper
        val baseContext = contextWrapper.baseContext ?: throw IllegalStateException("Activity was not found as base context of view!")
        baseContext.findActivity()
    }
}

internal fun Context.getSelectableItemBackground(): Drawable {
    val typedArray = this.obtainStyledAttributes(intArrayOf(R.attr.selectableItemBackground))
    return typedArray.getDrawable(0).apply {
        typedArray.recycle()
    }
}

internal fun Context.getColorRes(@ColorRes color: Int): Int = ContextCompat.getColor(this, color)

@Suppress("deprecation")
internal fun TextView.textAppearance(@StyleRes style: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        setTextAppearance(style)
    } else {
        setTextAppearance(context, style)
    }
}