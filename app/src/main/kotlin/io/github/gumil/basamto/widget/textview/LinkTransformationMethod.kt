/*
 * The MIT License (MIT)
 *
 * Copyright 2017 Miguel Panelo
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.github.gumil.basamto.widget.textview

import android.graphics.Rect
import android.text.Spannable
import android.text.Spanned
import android.text.method.TransformationMethod
import android.text.style.URLSpan
import android.view.View
import android.widget.TextView
import io.github.gumil.basamto.extensions.isValidUrl
import timber.log.Timber

internal class LinkTransformationMethod : TransformationMethod {
    override fun onFocusChanged(p0: View?, p1: CharSequence?, p2: Boolean, p3: Int, p4: Rect?) {
    }

    override fun getTransformation(source: CharSequence, view: View?): CharSequence {
        return (view as? TextView)?.let { textView ->
            (textView.text as? Spannable)?.let { text ->
                val spans = text.getSpans(0, textView.length(), URLSpan::class.java)
                spans.reversed().forEach {
                    val start = text.getSpanStart(it)
                    val end = text.getSpanEnd(it)
                    val url = it.url
                    text.removeSpan(it)

                    val span = if (url.isValidUrl()) {
                        CustomTabsUrlSpan(url)
                    } else {
                        Timber.tag("tantrums").d("source = $source")
                        Timber.tag("tantrums").d("url = $url")
                        SpoilerSpan(source.toString(), url)
                    }
                    text.setSpan(span, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
                text
            } ?: source
        } ?: source
    }

}