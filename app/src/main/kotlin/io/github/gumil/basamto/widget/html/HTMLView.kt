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
import android.text.method.LinkMovementMethod
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import io.github.gumil.basamto.extensions.fromHtml
import io.github.gumil.basamto.widget.textview.LinkTransformationMethod
import org.jsoup.nodes.Document
import org.jsoup.nodes.Node
import org.jsoup.select.NodeVisitor
import timber.log.Timber
import java.util.*

internal class HTMLView : LinearLayout, NodeVisitor {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        orientation = VERTICAL
    }

    private val viewStack = Stack<ViewElement>()

    fun populate(document: Document) {
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
        val textView = TextView(context).apply {
            fromHtml(node?.outerHtml().toString())
            text = text.trim()
            transformationMethod = LinkTransformationMethod()
            movementMethod = LinkMovementMethod.getInstance()
        }
        val viewElement = when (node?.nodeName()) {
            BODY -> ViewGroupElement(this, node.nodeName())
            TABLE -> ViewGroupElement(TableLayout(context), node.nodeName())
            ROW -> ViewGroupElement(TableRow(context), node.nodeName())
            HEADING -> ViewGroupElement(TableRow(context), node.nodeName())
        //BLOCK -> {}
        //UNORDERED -> {}
        //ORDERED -> {}
            PRE -> ViewGroupElement(HorizontalScrollView(context), node.nodeName())
            CELL -> TextViewElement(textView, node.nodeName())
            ITEM -> TextViewElement(textView, node.nodeName())
            CODE -> TextViewElement(textView, node.nodeName())
            PARAGRAPH -> TextViewElement(textView, node.nodeName())
            else -> null
        }

        viewElement?.let {
            viewStack.push(it)
        }
    }

    interface ViewElement {
        val tag: String

        fun add(element: ViewElement)
    }

    /**
     * TAGS: [HEADING], [CELL], [PARAGRAPH], [ITEM], [CODE]
     */
    class TextViewElement(
            val textView: TextView, override val tag: String
    ) : ViewElement {
        override fun add(element: ViewElement) {
        }

        override fun toString(): String = "$tag = ${textView.text}"
    }

    /**
     * TAGS: [BODY], [TABLE], [THEAD], [TBODY], [BLOCK], [UNORDERED], [ORDERED], [ROW], [PRE]
     */
    class ViewGroupElement(
            val viewGroup: ViewGroup, override val tag: String
    ) : ViewElement {

        override fun add(element: ViewElement) {
            Timber.tag("tantrums").d("add $element to ${viewGroup.javaClass.simpleName}")
            when (element) {
                is TextViewElement -> viewGroup.addView(element.textView)
                is ViewGroupElement -> viewGroup.addView(element.viewGroup)
            }
        }

        override fun toString(): String {
            return tag
        }
    }

    companion object {
        private const val BODY = "body"
        private const val TABLE = "table"
        private const val THEAD = "thead"
        private const val TBODY = "tbody"
        private const val ROW = "tr"
        private const val HEADING = "th"
        private const val CELL = "td"
        private const val BLOCK = "blockquote"
        private const val UNORDERED = "ul"
        private const val ORDERED = "ol"
        private const val ITEM = "li"
        private const val PRE = "pre"
        private const val CODE = "code"

        private const val DIV = "div"
        private const val TEXT = "#text"
        private const val PARAGRAPH = "p"
        private const val ITALIC = "em"
        private const val BOLD = "strong"
        private const val LINK = "a"
        private const val STRIKETHROUGH = "del"
        private const val SUPER = "sup"
    }

}