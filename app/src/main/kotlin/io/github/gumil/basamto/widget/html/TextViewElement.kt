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

package io.github.gumil.basamto.widget.html

import android.content.Context
import android.graphics.Typeface
import android.text.method.LinkMovementMethod
import android.widget.TextView
import io.github.gumil.basamto.extensions.fromHtml
import io.github.gumil.basamto.extensions.setPadding
import io.github.gumil.basamto.widget.textview.LinkTransformationMethod
import org.jsoup.nodes.Node

internal class TextViewElement(
        node: Node,
        context: Context
) : ViewElement {

    override val tag: String = node.nodeName()

    private val html = node.outerHtml().toString()

    val textView = TextView(context).apply {
        transformationMethod = LinkTransformationMethod()
        movementMethod = LinkMovementMethod.getInstance()
        fromHtml(node.outerHtml().toString())
    }

    init {
        when(tag) {
            Tags.HEADING -> {
                textView.setTypeface(textView.typeface, Typeface.BOLD)
                textView.setPadding(4)
            }
            Tags.CELL -> {
                textView.setPadding(4)
            }
            Tags.ITEM -> {
                /**
                 * handle ordered and unordered list
                 */
            }
            Tags.CODE -> {
                textView.typeface = Typeface.MONOSPACE
                val spaces = html.replace(" ", "&nbsp;")
                textView.fromHtml(spaces)
            }
        }
    }

    override fun add(element: ViewElement) {
    }

    override fun toString(): String = "$tag = ${textView.text}"
}