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

package io.github.gumil.basamto.extensions

import android.os.Build
import android.text.Html
import java.util.regex.Matcher
import java.util.regex.Pattern

private val SPOILER_PATTERN = Pattern.compile("<a[^>]*title=\"([^\"]*)\"[^>]*>([^<]*)</a>")
private const val TABLE_START_TAG = "<table>"
private const val HR_TAG = "<hr/>"
private const val TABLE_END_TAG = "</table>"

@Suppress("deprecation")
internal fun String.fromHtml() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
    Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
} else {
    Html.fromHtml(this)
}

internal fun String.decodeHtml() = this.replace("&amp;", "&")
        .replace("&lt;", "<")
        .replace("&gt;", ">")
        .replace("\n","<br/>")

/**
 * Parses html and returns a list corresponding to blocks of text to be
 * formatted.
 *
 * Each block is one of:
 * - Vanilla text
 * - Code block
 * - Table
 *
 * Note that this method will unescape html entities, so this is best called
 * with the raw html received from reddit.
 *
 * @param html html to be formatted. Can be raw from the api
 * @return list of text blocks
 */
internal fun String.getBlocks(): List<String> {
    fromHtml().toString()
            .replace("<p>", "<div>")
            .replace("</p>", "</div>")
            .replace("<li>\\s*<div>", "<li>")
            .replace("</div>\\s*</li>", "</li>")
            .replace("<li><div>", "<li>")
            .replace("</div></li>", "</li>")
            .replace("<del>", "[[d[")
            .replace("<sup>", "<sup><small>")
            .replace("</sup>", "</small></sup>")
            .replace("</del>", "]d]]")
            .let {
                if (it.contains("\n")) {
                    it.substring(0, it.lastIndexOf("\n"))
                } else {
                    it
                }
            }
            .let {
                if (it.contains("<!-- SC_ON -->")) {
                    it.substring(15, it.lastIndexOf("<!-- SC_ON -->"))
                } else {
                    it
                }
            }
            .parseSpoilerTags()
            .parseLists()
            .let { html ->
                html.parseCodeTags().let {
                    if (html.contains(HR_TAG)) {
                        it.parseHR()
                    } else {
                        it
                    }
                }.let {
                    return if (html.contains("<table")) {
                        it.parseTableTags()
                    } else {
                        it
                    }
                }
            }
}


/**
 * Move the spoil text inside of the "title" attribute to inside the link
 * tag. Then surround the spoil text with `[[s[` and `]s]]`.
 *
 * If there is no text inside of the link tag, insert "spoil".
 *
 * @param html
 * @return
 */
private fun String.parseSpoilerTags(): String {
    var html = this

    fun Matcher.parseSpoilers(body: (tag: String, text: String, teaser: String) -> Unit) {
        while (find()) {
            body(group(0), group(1), group(2))
        }
    }

    SPOILER_PATTERN.matcher(this).parseSpoilers { tag, text, teaser ->
        if (!tag.contains("<a href=\"http")) {
            html = replace(tag, tag.substring(0, tag.length - 4) +
                    (if (teaser.isEmpty()) "spoiler" else "") +
                    "&lt; [[s[ " + text + "]s]]</a>")
        }
    }

    return html
}

private fun String.parseLists(): String {
    if (!contains("<ol") && !contains("<ul")) return this

    var html = this
    val firstOl = html.indexOf("<ol")
    val firstUl = html.indexOf("<ul")
    var isNumbered = firstOl != -1 && firstUl > firstOl || firstUl == -1
    val firstIndex = if (isNumbered) {
        firstOl
    } else {
        firstUl
    }
    val listNumbers = mutableListOf<Int>()
    var indent = -1

    var i = firstIndex
    while (i < html.length - 4 && i != -1) {
        if (html.substring(i, i + 3) == "<ol" || html.substring(i, i + 3) == "<ul") {
            if (html.substring(i, i + 3) == "<ol") {
                isNumbered = true
                indent++
                listNumbers.add(indent, 1)
            } else {
                isNumbered = false
            }
            i = html.indexOf("<li", i)
        } else if (html.substring(i, i + 3) == "<li") {
            val tagEnd = html.indexOf(">", i)
            val itemClose = html.indexOf("</li", tagEnd)
            val ulClose = html.indexOf("<ul", tagEnd)
            val olClose = html.indexOf("<ol", tagEnd)
            val closeTag: Int

            // Find what is closest: </li>, <ul>, or <ol>
            closeTag = if ((ulClose == -1 && itemClose != -1 || itemClose != -1 && ulClose != -1 && itemClose < ulClose) && (olClose == -1 && itemClose != -1 || itemClose != -1 && olClose != -1 && itemClose < olClose)) {
                itemClose
            } else if ((ulClose == -1 && olClose != -1 || olClose != -1 && ulClose != -1 && olClose < ulClose) && (olClose == -1 && itemClose != -1 || olClose != -1 && itemClose != -1 && olClose < itemClose)) {
                olClose
            } else {
                ulClose
            }

            val text = html.substring(tagEnd + 1, closeTag)
            var indentSpacing = ""
            for (j in 0 until indent) {
                indentSpacing += "&nbsp;&nbsp;&nbsp;&nbsp;"
            }
            if (isNumbered) {
                html = (html.substring(0, tagEnd + 1)
                        + indentSpacing +
                        listNumbers.get(indent) + ". " +
                        text + "<br/>" +
                        html.substring(closeTag))
                listNumbers.set(indent, listNumbers.get(indent) + 1)
                i = closeTag + 3
            } else {
                html = html.substring(0, tagEnd + 1) + indentSpacing + "• " + text + "<br/>" + html.substring(closeTag)
                i = closeTag + 2
            }
        } else {
            i = html.indexOf("<", i + 1)
            if (i != -1 && html.substring(i, i + 4) == "</ol") {
                indent--
                if (indent == -1) {
                    isNumbered = false
                }
            }
        }
    }

    html = html.replace("<ol>", "").replace("<ul>", "").replace("<li>", "").replace("</li>", "").replace("</ol>", "").replace("</ul>", "") //Remove the tags, which actually work in Android 7.0 on

    return html
}

/**
 * For code within `<pre>` tags, line breaks are converted to
 * `<br />` tags, and spaces to &amp;nbsp;. This allows for Html.fromHtml
 * to preserve indents of these blocks.
 *
 *
 * In addition, `[[<[` and `]>]]` are inserted to denote the
 * beginning and end of code segments, for styling later.
 *
 * @param html the unparsed HTML
 * @return the code parsed HTML with additional markers, split but code blocks
 */
private fun String.parseCodeTags(): List<String> {
    val startTag = "<pre><code>"
    val endTag = "</code></pre>"
    val startSeparated = split(startTag)
    val preSeparated = mutableListOf<String>()

    preSeparated.add(startSeparated[0].replace("<code>", "<code>[[&lt;[").replace("</code>", "]&gt;]]</code>"))
    for (i in 1 until startSeparated.size) {
        val split = startSeparated[i].split(endTag).toTypedArray()

        val code = split[0].replace("\n", "<br/>")
                .replace(" ", "&nbsp;")

        preSeparated.add("$startTag[[&lt;[$code]&gt;]]$endTag")
        if (split.size > 1) {
            preSeparated.add(split[1].replace("<code>", "<code>[[&lt;[").replace("</code>", "]&gt;]]</code>"))
        }
    }

    return preSeparated
}

private fun List<String>.parseHR(): List<String> {
    val newBlocks = mutableListOf<String>()

    forEach {
        if (contains(HR_TAG)) {
            it.split(HR_TAG).forEach {
                newBlocks.add(it)
                newBlocks.add(HR_TAG)
            }
            newBlocks.removeAt(newBlocks.size - 1)
        } else {
            newBlocks.add(it)
        }
    }

    return newBlocks
}

/**
 * Parse a given list of html strings, splitting by table blocks.
 *
 * All table tags are html escaped.
 *
 * @param blocks list of html with or individual table blocks
 * @return list of html with tables split into it's entry
 */
private fun List<String>.parseTableTags(): List<String> {
    val newBlocks = mutableListOf<String>()

    forEach {
        if (it.contains(TABLE_START_TAG)) {
            val startSeperated = it.split(TABLE_START_TAG)
            newBlocks.add(startSeperated[0].trim { it <= ' ' })
            for (i in 1 until startSeperated.size) {
                val split = startSeperated[i].split(TABLE_END_TAG)
                newBlocks.add("<table>" + split[0] + "</table>")
                if (split.size > 1) {
                    newBlocks.add(split[1])
                }
            }
        } else {
            newBlocks.add(it)
        }
    }

    return newBlocks
}