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
import android.util.AttributeSet
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TableRow
import org.jsoup.nodes.Document
import org.jsoup.nodes.Node
import org.jsoup.select.NodeVisitor
import java.util.*

internal class HTMLView : LinearLayout, NodeVisitor {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        orientation = VERTICAL
    }

    private val viewStack = Stack<ViewElement>()

    fun populate(document: Document) {
        removeAllViews()
        document.body().traverse(this)
    }

    override fun tail(node: Node?, depth: Int) {
        if (viewStack.isNotEmpty()) {
            if (viewStack.peek().tag == node?.nodeName()) {
                val viewElement = viewStack.pop()

                if (viewStack.isNotEmpty()) {
                    viewStack.peek().add(viewElement)
                }
            }
        }
    }

    override fun head(node: Node?, depth: Int) {
        val viewElement = when (node?.nodeName()) {
            Tags.BODY -> ViewGroupElement(node.nodeName(), this)
            Tags.TABLE -> ViewGroupElement(node.nodeName(), TableLayout(context))
            Tags.ROW -> ViewGroupElement(node.nodeName(), TableRow(context))
        //BLOCK -> {}
        //UNORDERED -> {}
        //ORDERED -> {}
            Tags.PRE -> ViewGroupElement(node.nodeName(), HorizontalScrollView(context))
            Tags.CELL, Tags.ITEM, Tags.CODE, Tags.PARAGRAPH,
            Tags.HEADING -> TextViewElement(node, context)
            else -> null
        }

        viewElement?.let {
            viewStack.push(it)
        }
    }
}