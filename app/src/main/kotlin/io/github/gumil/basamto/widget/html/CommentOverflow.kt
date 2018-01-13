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

package io.github.gumil.basamto.widget.html

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TableRow
import io.github.gumil.basamto.R
import io.github.gumil.basamto.extensions.dip
import io.github.gumil.basamto.extensions.getColorRes

/**
 * Class that provides methods to help bind submissions with
 * multiple blocks of text.
 */
class CommentOverflow : LinearLayout {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val columnParams: ViewGroup.MarginLayoutParams by lazy {
        TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT).apply {
            setMargins(0, 0, 32, 0)
        }
    }

    private val marginParams: ViewGroup.MarginLayoutParams by lazy {
        LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT).apply {
            setMargins(0, 16, 0, 16)
        }
    }

    private val hrParams: ViewGroup.MarginLayoutParams by lazy {
        LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                context.dip(2)).apply {
            setMargins(0, 16, 0, 16)
        }
    }

    /**
     * Set the text for the corresponding views.
     *
     * @param blocks    list of all blocks to be set
     * @param subreddit
     */
    @JvmOverloads
    fun setViews(blocks: List<String>, click: View.OnClickListener? = null,
                 longClick: View.OnLongClickListener? = null) {
        removeAllViews()

        if (!blocks.isEmpty()) {
            visibility = View.VISIBLE
        }


        for (block in blocks) {
            when {
                block.startsWith("<table>") -> {
                    val scrollView = HorizontalScrollView(context)
                    scrollView.isScrollbarFadingEnabled = false
                    val table = formatTable(block, click, longClick)
                    scrollView.layoutParams = marginParams
                    table.setPaddingRelative(0, 0, 0, context.dip(10))
                    scrollView.addView(table)
                    addView(scrollView)
                }
                block == "<hr/>" -> {
                    val line = View(context)
                    line.layoutParams = hrParams
                    line.setBackgroundColor(context.getColorRes(R.color.line))
                    line.alpha = 0.6f
                    addView(line)
                }
                block.startsWith("<pre>") -> {
                    val scrollView = HorizontalScrollView(context)
                    scrollView.isScrollbarFadingEnabled = false
                    val newTextView = SpoilerTextView(context)
                    newTextView.setTextHtml(block)
                    scrollView.layoutParams = marginParams
                    newTextView.setPaddingRelative(0, 0, 0, context.dip(10))
                    scrollView.addView(newTextView)
                    if (click != null) newTextView.setOnClickListener(click)
                    if (longClick != null) newTextView.setOnLongClickListener(longClick)
                    addView(scrollView)

                }
                else -> {
                    val newTextView = SpoilerTextView(context)
                    newTextView.setTextHtml(block)
                    newTextView.layoutParams = marginParams
                    if (click != null) newTextView.setOnClickListener(click)
                    if (longClick != null) newTextView.setOnLongClickListener(longClick)
                    addView(newTextView)
                }
            }
        }
    }

    private fun formatTable(text: String, click: View.OnClickListener? = null,
                            longClick: View.OnLongClickListener? = null): TableLayout {
        val rowParams = TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT)

        val context = context
        val table = TableLayout(context)
        val params = TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT)
        table.layoutParams = params

        val tableStart = "<table>"
        val tableEnd = "</table>"
        val tableHeadStart = "<thead>"
        val tableHeadEnd = "</thead>"
        val tableRowStart = "<tr>"
        val tableRowEnd = "</tr>"
        val tableColumnStart = "<td>"
        val tableColumnEnd = "</td>"
        val tableColumnStartLeft = "<td align=\"left\">"
        val tableColumnStartRight = "<td align=\"right\">"
        val tableColumnStartCenter = "<td align=\"center\">"
        val tableHeaderStart = "<th>"
        val tableHeaderStartLeft = "<th align=\"left\">"
        val tableHeaderStartRight = "<th align=\"right\">"
        val tableHeaderStartCenter = "<th align=\"center\">"
        val tableHeaderEnd = "</th>"

        var i = 0
        var columnStart = 0
        var columnEnd: Int
        var gravity = Gravity.START
        var columnStarted = false

        var row: TableRow? = null

        while (i < text.length) {
            if (text[i] != '<') { // quick check otherwise it falls through to else
                i += 1
            } else if (text.subSequence(i, i + tableStart.length).toString() == tableStart) {
                i += tableStart.length
            } else if (text.subSequence(i, i + tableHeadStart.length)
                    .toString() == tableHeadStart) {
                i += tableHeadStart.length
            } else if (text.subSequence(i, i + tableRowStart.length)
                    .toString() == tableRowStart) {
                row = TableRow(context)
                row.layoutParams = rowParams
                i += tableRowStart.length
            } else if (text.subSequence(i, i + tableRowEnd.length)
                    .toString() == tableRowEnd) {
                table.addView(row)
                i += tableRowEnd.length
            } else if (text.subSequence(i, i + tableEnd.length).toString() == tableEnd) {
                i += tableEnd.length
            } else if (text.subSequence(i, i + tableHeadEnd.length)
                    .toString() == tableHeadEnd) {
                i += tableHeadEnd.length
            } else if (!columnStarted
                    && i + tableColumnStart.length < text.length
                    && (text.subSequence(i, i + tableColumnStart.length)
                    .toString() == tableColumnStart || text.subSequence(i, i + tableHeaderStart.length)
                    .toString() == tableHeaderStart)) {
                columnStarted = true
                gravity = Gravity.START
                i += tableColumnStart.length
                columnStart = i
            } else if (!columnStarted && i + tableColumnStartRight.length < text.length && (text
                    .subSequence(i, i + tableColumnStartRight.length)
                    .toString() == tableColumnStartRight || text.subSequence(i,
                    i + tableHeaderStartRight.length).toString() == tableHeaderStartRight)) {
                columnStarted = true
                gravity = Gravity.END
                i += tableColumnStartRight.length
                columnStart = i
            } else if (!columnStarted && i + tableColumnStartCenter.length < text.length && (text.subSequence(i, i + tableColumnStartCenter.length)
                    .toString() == tableColumnStartCenter || text.subSequence(i, i + tableHeaderStartCenter.length)
                    .toString() == tableHeaderStartCenter)) {
                columnStarted = true
                gravity = Gravity.CENTER
                i += tableColumnStartCenter.length
                columnStart = i
            } else if (!columnStarted
                    && i + tableColumnStartLeft.length < text.length
                    && (text.subSequence(i, i + tableColumnStartLeft.length)
                    .toString() == tableColumnStartLeft || text.subSequence(i,
                    i + tableHeaderStartLeft.length).toString() == tableHeaderStartLeft)) {
                columnStarted = true
                gravity = Gravity.START
                i += tableColumnStartLeft.length
                columnStart = i
            } else if (text.substring(i).startsWith("<td")) {
                // case for <td colspan="2"  align="left">
                // See last table in https://www.reddit.com/r/GlobalOffensive/comments/51s3r8/virtuspro_vs_vgcyberzen_sl_ileague_s2_finals/
                columnStarted = true
                i += text.substring(i).indexOf(">") + 1
                columnStart = i
            } else if (text.subSequence(i, i + tableColumnEnd.length)
                    .toString() == tableColumnEnd || text.subSequence(i, i + tableHeaderEnd.length)
                    .toString() == tableHeaderEnd) {
                columnEnd = i

                val textView = SpoilerTextView(context)
                textView.setTextHtml(text.subSequence(columnStart, columnEnd))
                textView.layoutParams = columnParams
                textView.gravity = gravity
                if (click != null) textView.setOnClickListener(click)
                if (longClick != null) textView.setOnLongClickListener(longClick)
                if (text.subSequence(i, i + tableHeaderEnd.length)
                        .toString() == tableHeaderEnd) {
                    textView.setTypeface(null, Typeface.BOLD)
                }
                if (row != null) {
                    row.addView(textView)
                }

                columnStart = 0
                columnStarted = false
                i += tableColumnEnd.length
            } else {
                i += 1
            }
        }

        return table
    }

}