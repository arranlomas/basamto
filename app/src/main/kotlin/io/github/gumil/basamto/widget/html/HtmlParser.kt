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

import org.apache.commons.text.StringEscapeUtils
import java.util.*
import java.util.regex.Pattern

/**
 * Utility methods to transform html received from Reddit into a more parsable
 * format.
 *
 * The output will unescape all html, except for table tags and some special delimiter
 * token such as for code blocks.
 */
private val SPOILER_PATTERN = Pattern.compile("<a[^>]*title=\"([^\"]*)\"[^>]*>([^<]*)</a>")
private val TABLE_START_TAG = "<table>"
private val HR_TAG = "<hr/>"
private val TABLE_END_TAG = "</table>"

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
fun String.getBlocks(): List<String> {
    var html = this
    html = StringEscapeUtils.unescapeHtml4(html)
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

    if (html.contains("\n")) {
        html = html.substring(0, html.lastIndexOf("\n"))
    }

    if (html.contains("<!-- SC_ON -->")) {
        html = html.substring(15, html.lastIndexOf("<!-- SC_ON -->"))
    }

    html = parseSpoilerTags(html)
    if (html.contains("<ol") || html.contains("<ul")) {
        html = parseLists(html)
    }


    var codeBlockSeperated = parseCodeTags(html)

    if (html.contains(HR_TAG)) {
        codeBlockSeperated = parseHR(codeBlockSeperated)
    }

    return if (html.contains("<table")) {
        parseTableTags(codeBlockSeperated)
    } else {
        codeBlockSeperated
    }.toMutableList().apply {
        remove("<div class=\"md\">")
    }
}

private fun parseLists(html: String): String {
    var html = html
    val firstIndex: Int
    var isNumbered: Boolean
    val firstOl = html.indexOf("<ol")
    val firstUl = html.indexOf("<ul")

    if (firstUl != -1 && firstOl > firstUl || firstOl == -1) {
        firstIndex = firstUl
        isNumbered = false
    } else {
        firstIndex = firstOl
        isNumbered = true
    }
    val listNumbers = ArrayList<Int>()
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
            if ((ulClose == -1 && itemClose != -1 || itemClose != -1 && ulClose != -1 && itemClose < ulClose) && (olClose == -1 && itemClose != -1 || itemClose != -1 && olClose != -1 && itemClose < olClose)) {
                closeTag = itemClose
            } else if ((ulClose == -1 && olClose != -1 || olClose != -1 && ulClose != -1 && olClose < ulClose) && (olClose == -1 && itemClose != -1 || olClose != -1 && itemClose != -1 && olClose < itemClose)) {
                closeTag = olClose
            } else {
                closeTag = ulClose
            }

            val text = html.substring(tagEnd + 1, closeTag)
            var indentSpacing = ""
            for (j in 0 until indent) {
                indentSpacing += "&nbsp;&nbsp;&nbsp;&nbsp;"
            }
            if (isNumbered) {
                html = (html.substring(0, tagEnd + 1)
                        + indentSpacing +
                        listNumbers[indent] + ". " +
                        text + "<br/>" +
                        html.substring(closeTag))
                listNumbers[indent] = listNumbers[indent] + 1
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

private fun parseHR(blocks: List<String>): List<String> {
    val newBlocks = ArrayList<String>()
    for (block in blocks) {
        if (block.contains(HR_TAG)) {
            for (s in block.split(HR_TAG.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()) {
                newBlocks.add(s)
                newBlocks.add(HR_TAG)
            }
            newBlocks.removeAt(newBlocks.size - 1)
        } else {
            newBlocks.add(block)
        }
    }

    return newBlocks
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
private fun parseCodeTags(html: String): List<String> {
    val startTag = "<pre><code>"
    val endTag = "</code></pre>"
    val startSeperated = html.split(startTag.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
    val preSeperated = ArrayList<String>()

    var text: String
    var code: String
    var split: Array<String>

    preSeperated.add(startSeperated[0].replace("<code>", "<code>[[&lt;[").replace("</code>", "]&gt;]]</code>"))
    for (i in 1 until startSeperated.size) {
        text = startSeperated[i]
        split = text.split(endTag.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        code = split[0]
        code = code.replace("\n", "<br/>")
        code = code.replace(" ", "&nbsp;")

        preSeperated.add("$startTag[[&lt;[$code]&gt;]]$endTag")
        if (split.size > 1) {
            preSeperated.add(split[1].replace("<code>", "<code>[[&lt;[").replace("</code>", "]&gt;]]</code>"))
        }
    }

    return preSeperated
}


/**
 * Move the spoil text inside of the "title" attribute to inside the link
 * tag. Then surround the spoil text with `[[s[` and `]s]]`.
 *
 *
 * If there is no text inside of the link tag, insert "spoil".
 *
 * @param html
 * @return
 */
private fun parseSpoilerTags(html: String): String {
    var html = html
    var spoilerText: String
    var tag: String
    var spoilerTeaser: String
    val matcher = SPOILER_PATTERN.matcher(html)

    while (matcher.find()) {
        tag = matcher.group(0)
        spoilerText = matcher.group(1)
        spoilerTeaser = matcher.group(2)
        // Remove the last </a> tag, but keep the < for parsing.
        if (!tag.contains("<a href=\"http")) {
            html = html.replace(tag, tag.substring(0, tag.length - 4) + (if (spoilerTeaser.isEmpty()) "spoiler" else "") + "&lt; [[s[ " + spoilerText + "]s]]</a>")
        }
    }

    return html
}

/**
 * Parse a given list of html strings, splitting by table blocks.
 *
 * All table tags are html escaped.
 *
 * @param blocks list of html with or individual table blocks
 * @return list of html with tables split into it's entry
 */
private fun parseTableTags(blocks: List<String>): List<String> {
    val newBlocks = ArrayList<String>()
    for (block in blocks) {
        if (block.contains(TABLE_START_TAG)) {
            val startSeperated = block.split(TABLE_START_TAG.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            newBlocks.add(startSeperated[0].trim { it <= ' ' })
            for (i in 1 until startSeperated.size) {
                val split = startSeperated[i].split(TABLE_END_TAG.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                newBlocks.add("<table>" + split[0] + "</table>")
                if (split.size > 1) {
                    newBlocks.add(split[1])
                }
            }
        } else {
            newBlocks.add(block)
        }
    }

    return newBlocks
}
