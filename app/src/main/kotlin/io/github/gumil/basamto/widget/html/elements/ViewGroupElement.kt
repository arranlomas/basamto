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

package io.github.gumil.basamto.widget.html.elements

import android.view.View
import android.view.ViewGroup
import io.github.gumil.basamto.widget.html.Tags

internal class ViewGroupElement(
        override val tag: String,
        private val viewGroup: ViewGroup
) : ViewElement {

    override val view: View
        get() = viewGroup

    init {
        when(tag) {
            Tags.CELL -> {}
            Tags.HEADING -> {}
            Tags.PARAGRAPH -> {}
            Tags.ITEM -> {}
            Tags.CODE -> {}
        }
    }

    override fun add(element: ViewElement) {
        viewGroup.addView(element.view)
    }

    override fun toString(): String = tag
}