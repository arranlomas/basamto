/*
 * The MIT License (MIT)
 *
 * Copyright 2018 Miguel Panelo
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

package io.github.gumil.basamto.widget.html.span

import android.graphics.Canvas
import android.graphics.Paint
import android.text.Layout
import android.text.style.LeadingMarginSpan
import android.text.style.LineBackgroundSpan

/**
 * android.text.style.QuoteSpan hard-codes the strip color and gap; so this will change that
 */
class CustomQuoteSpan(
        private val backgroundColor: Int,
        private val stripeColor: Int,
        private val stripeWidth: Float,
        private val gap: Float
) : LeadingMarginSpan, LineBackgroundSpan {

    override fun getLeadingMargin(first: Boolean): Int = (stripeWidth + gap).toInt()

    override fun drawLeadingMargin(c: Canvas, p: Paint, x: Int, dir: Int, top: Int, baseline: Int, bottom: Int,
                                   text: CharSequence, start: Int, end: Int, first: Boolean, layout: Layout) {
        val style = p.style
        val paintColor = p.color

        p.style = Paint.Style.FILL
        p.color = stripeColor

        c.drawRect(x.toFloat(), top.toFloat(), x + dir * stripeWidth, bottom.toFloat(), p)

        p.style = style
        p.color = paintColor
    }

    override fun drawBackground(c: Canvas, p: Paint, left: Int, right: Int, top: Int, baseline: Int, bottom: Int, text: CharSequence, start: Int, end: Int, lnum: Int) {
        val paintColor = p.color
        p.color = backgroundColor
        c.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), p)
        p.color = paintColor
    }
}