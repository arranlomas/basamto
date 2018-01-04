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
import io.github.gumil.basamto.widget.html.Tags
import org.apache.commons.text.StringEscapeUtils
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

private const val REGEX_URL = "^[A-Za-z][A-Za-z0-9+.-]{1,120}:[A-Za-z0-9/](([A-Za-z0-9\$_.+!*,;/?:@&~=-])|%[A-Fa-f0-9]{2}){1,333}(#([a-zA-Z0-9][a-zA-Z0-9\$_.+!*,;/?:@&~=%-]{0,1000}))?$"

@Suppress("deprecation")
internal fun String.fromHtml() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
    Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
} else {
    Html.fromHtml(this)
}

internal fun String.formatHtml(): Document {
    val html = StringEscapeUtils.unescapeHtml4(this)
            .replace("\n", "<br/>")

    return Jsoup.parse(html)
            .remapNestedLists()
            .addIdsToOrderedList()
            .remapSpoilers()
}

private fun Document.addIdsToOrderedList(): Document {
    getElementsByTag("ol").forEach {
        var i = 1
        it.children().forEach {
            if (it.nodeName() == Tags.ITEM) {
                it.attr(Tags.ID, "${i++}")
            }
        }
    }
    return this
}

private fun Document.remapNestedLists(): Document {
    getElementsByTag("li").forEach { li ->
        li.children().forEach {
            if (it.nodeName() == Tags.UNORDERED || it.nodeName() == Tags.ORDERED) {
                it.remove()
                li.parent().insertChildren(li.elementSiblingIndex() + 1, it)
            }
        }
    }
    return this
}

private fun Document.remapSpoilers(): Document {
    getElementsByTag("a").forEach {
        if (it.hasAttr("title")) {
            val title = it.attr("title")
            it.attr("href", title)
            it.text(if (it.text().isNotEmpty()) {
                "${it.text()} $title"
            } else {
                title
            })
        }
    }
    return this
}

internal fun String.isValidUrl() = matches(Regex(REGEX_URL))