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
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Typeface
import android.os.Environment
import android.support.v7.widget.AppCompatTextView
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.style.BackgroundColorSpan
import android.text.style.CharacterStyle
import android.text.style.ForegroundColorSpan
import android.text.style.ImageSpan
import android.text.style.QuoteSpan
import android.text.style.StrikethroughSpan
import android.text.style.StyleSpan
import android.text.style.TypefaceSpan
import android.text.style.URLSpan
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.WindowManager
import android.widget.TextView
import io.github.gumil.basamto.R
import io.github.gumil.basamto.extensions.fromHtml
import io.github.gumil.basamto.extensions.getColorRes
import io.github.gumil.basamto.widget.html.span.CustomQuoteSpan
import org.apache.commons.lang3.StringUtils
import java.io.File
import java.util.*
import java.util.regex.Pattern

class SpoilerTextView : AppCompatTextView {
    private val storedSpoilerSpans = ArrayList<CharacterStyle>()
    private val storedSpoilerStarts = ArrayList<Int>()
    private val storedSpoilerEnds = ArrayList<Int>()

    private var isSpoilerClicked = false

    constructor(context: Context) : super(context) {
        setLineSpacing(0f, 1.1f)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        setLineSpacing(0f, 1.1f)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        setLineSpacing(0f, 1.1f)
    }

    private fun resetSpoilerClicked() {
        isSpoilerClicked = false
    }

    /**
     * Set the text from html. Handles formatting spoilers, links etc.
     *
     * The text must be valid
     * html.
     *
     * @param baseText  html text
     * @param subreddit the subreddit to theme
     */
    fun setTextHtml(baseText: CharSequence) {
        val text = wrapAlternateSpoilers(saveEmotesFromDestruction(baseText.toString().trim { it <= ' ' }))
        var builder = text.fromHtml() as SpannableStringBuilder

        replaceQuoteSpans(builder) //replace the <blockquote> blue line with something more colorful

        if (text.contains("<a")) {
            setEmoteSpans(builder) //for emote enabled subreddits
        }
        if (text.contains("[")) {
            setCodeFont(builder)
            setSpoilerStyle(builder)
        }
        if (text.contains("[[d[")) {
            setStrikethrough(builder)
        }
        if (text.contains("[[h[")) {
            setHighlight(builder)
        }

        builder = removeNewlines(builder)

        builder.append(" ")

        super.setText(builder, TextView.BufferType.SPANNABLE)
    }


    /**
     * Replaces the blue line produced by <blockquote>s with something more visible
     *
     * @param spannable parsed comment text #fromHtml
    </blockquote> */
    private fun replaceQuoteSpans(spannable: Spannable) {
        val quoteSpans = spannable.getSpans(0, spannable.length, QuoteSpan::class.java)

        for (quoteSpan in quoteSpans) {
            val start = spannable.getSpanStart(quoteSpan)
            val end = spannable.getSpanEnd(quoteSpan)
            val flags = spannable.getSpanFlags(quoteSpan)

            spannable.removeSpan(quoteSpan)

            val barWidth = 4f
            val gap = 5f

            spannable.setSpan(CustomQuoteSpan(Color.TRANSPARENT, //background color
                    context.getColorRes(R.color.line), //bar color
                    barWidth, //bar width
                    gap), //bar + text gap
                    start, end, flags)
        }
    }

    private fun wrapAlternateSpoilers(html: String): String {
        var html = html
        val htmlSpoilerMatcher = htmlSpoilerPattern.matcher(html)
        while (htmlSpoilerMatcher.find()) {
            val newPiece = htmlSpoilerMatcher.group()
            val inner = ("<a href=\"/spoiler\">spoiler&lt; [[s[ "
                    + newPiece.substring(newPiece.indexOf(">") + 1,
                    newPiece.indexOf("<", newPiece.indexOf(">")))
                    + "]s]]</a>")
            html = html.replace(htmlSpoilerMatcher.group(), inner)
        }
        return html
    }

    private fun saveEmotesFromDestruction(html: String): String {
        var html = html
        //Emotes often have no spoiler caption, and therefore are converted to empty anchors. Html.fromHtml removes anchors with zero length node text. Find zero length anchors that start with "/" and add "." to them.
        val htmlEmotePattern = Pattern.compile("<a href=\"/.*\"></a>")
        val htmlEmoteMatcher = htmlEmotePattern.matcher(html)
        while (htmlEmoteMatcher.find()) {
            var newPiece = htmlEmoteMatcher.group()
            //Ignore empty tags marked with sp.
            if (!htmlEmoteMatcher.group().contains("href=\"/sp\"")) {
                newPiece = newPiece.replace("></a", ">.</a")
                html = html.replace(htmlEmoteMatcher.group(), newPiece)
            }
        }
        return html
    }

    private fun setEmoteSpans(builder: SpannableStringBuilder) {
        for (span in builder.getSpans(0, builder.length, URLSpan::class.java)) {
            /*TODO Link Types
            if (SettingValues.typeInText) {
                setLinkTypes(builder, span)
            }*/
            val emoteDir = File(Environment.getExternalStorageDirectory(), "RedditEmotes")
            val emoteFile = File(emoteDir, span.url.replace("/", "").replace("-.*".toRegex(), "") + ".png") //BPM uses "-" to add dynamics for emotes in browser. Fall back to original here if exists.
            val startsWithSlash = span.url.startsWith("/")
            val hasOnlyOneSlash = StringUtils.countMatches(span.url, "/") == 1

            if (emoteDir.exists() && startsWithSlash && hasOnlyOneSlash && emoteFile.exists()) {
                //We've got an emote match
                val start = builder.getSpanStart(span)
                val end = builder.getSpanEnd(span)
                val textCovers = builder.subSequence(start, end)

                //Make sure bitmap loaded works well with screen density.
                val options = BitmapFactory.Options()
                val metrics = DisplayMetrics()
                (context.getSystemService(
                        Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.getMetrics(metrics)
                options.inDensity = 240
                options.inScreenDensity = metrics.densityDpi
                options.inScaled = true

                //Since emotes are not directly attached to included text, add extra character to attach image to.
                builder.removeSpan(span)
                if (builder.subSequence(start, end)[0] != '.') {
                    builder.insert(start, "")
                }
                val emoteBitmap = BitmapFactory.decodeFile(emoteFile.absolutePath, options)
                builder.setSpan(ImageSpan(context, emoteBitmap), start, start + 1,
                        Spanned.SPAN_INCLUSIVE_INCLUSIVE)
                //Check if url span has length. If it does, it's a spoiler/caption
                if (textCovers.length > 1) {
                    builder.setSpan(URLSpan("/sp"), start + 1, end + 1,
                            Spanned.SPAN_INCLUSIVE_INCLUSIVE)
                    builder.setSpan(StyleSpan(Typeface.ITALIC), start + 1, end + 1,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
                builder.append("\n") //Newline to fix text wrapping issues
            }
        }
    }

    /* TODO Link Types
    private fun setLinkTypes(builder: SpannableStringBuilder, span: URLSpan) {
        var url = span.url
        if (url.endsWith("/")) {
            url = url.substring(0, url.length - 1)
        }
        val text = builder.subSequence(builder.getSpanStart(span), builder.getSpanEnd(span))
                .toString()
        if (!text.equals(url, ignoreCase = true)) {
            val contentType = ContentType.getContentType(url)
            var bod: String
            try {
                bod = " (" + (if (url.contains("/") && url.startsWith("/") && url.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().size <= 2)
                    url
                else
                    context.getString(ContentType.getContentID(contentType, false)) + if (contentType === ContentType.Type.LINK)
                        " " + Uri.parse(url)
                                .host
                    else
                        "") + ")"
            } catch (e: Exception) {
                bod = (" ("
                        + context.getString(ContentType.getContentID(contentType, false))
                        + ")")
            }

            val b = SpannableStringBuilder(bod)
            b.setSpan(StyleSpan(Typeface.BOLD), 0, b.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            b.setSpan(RelativeSizeSpan(0.8f), 0, b.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            builder.insert(builder.getSpanEnd(span), b)
        }
    }*/

    private fun setStrikethrough(builder: SpannableStringBuilder) {
        val offset = "[[d[".length // == "]d]]".length()

        var start = -1
        var end: Int

        var i = 0
        while (i < builder.length - 3) {
            if (builder[i] == '['
                    && builder[i + 1] == '['
                    && builder[i + 2] == 'd'
                    && builder[i + 3] == '[') {
                start = i + offset
            } else if (builder[i] == ']'
                    && builder[i + 1] == 'd'
                    && builder[i + 2] == ']'
                    && builder[i + 3] == ']') {
                end = i
                builder.setSpan(StrikethroughSpan(), start, end,
                        Spanned.SPAN_INCLUSIVE_INCLUSIVE)
                builder.delete(end, end + offset)
                builder.delete(start - offset, start)
                i -= offset + (end - start) // length of text
            }
            i++
        }
    }

    private fun setHighlight(builder: SpannableStringBuilder) {
        val offset = "[[h[".length // == "]h]]".length()

        var start = -1
        var end: Int
        var i = 0
        while (i < builder.length - 4) {
            if (builder[i] == '['
                    && builder[i + 1] == '['
                    && builder[i + 2] == 'h'
                    && builder[i + 3] == '[') {
                start = i + offset
            } else if (builder[i] == ']'
                    && builder[i + 1] == 'h'
                    && builder[i + 2] == ']'
                    && builder[i + 3] == ']') {
                end = i
                builder.setSpan(BackgroundColorSpan(context.getColorRes(R.color.colorAccent)), start, end,
                        Spanned.SPAN_INCLUSIVE_INCLUSIVE)
                builder.delete(end, end + offset)
                builder.delete(start - offset, start)
                i -= offset + (end - start) // length of text
            }
            i++
        }
    }

    /* TODO default to custom tabs
    fun onLinkClick(url: String?, xOffset: Int, subreddit: String, span: URLSpan?) {
        if (url == null) {
            (parent as View).callOnClick()
            return
        }

        val type = ContentType.getContentType(url)
        val context = context
        var activity: Activity? = null
        if (context is Activity) {
            activity = context
        } else if (context is android.support.v7.view.ContextThemeWrapper) {
            activity = context.baseContext as Activity
        } else if (context is ContextWrapper) {
            val context1 = context.baseContext
            if (context1 is Activity) {
                activity = context1
            } else if (context1 is ContextWrapper) {
                val context2 = context1.baseContext
                if (context2 is Activity) {
                    activity = context2
                } else if (context2 is ContextWrapper) {
                    activity = (context2 as android.support.v7.view.ContextThemeWrapper).baseContext as Activity
                }
            }
        } else {
            throw RuntimeException("Could not find activity from context:" + context)
        }

        if (!PostMatch.openExternal(url) || type === ContentType.Type.VIDEO) {
            when (type) {
                DEVIANTART, IMGUR, XKCD -> if (SettingValues.image) {
                    val intent2 = Intent(activity, MediaView::class.java)
                    intent2.putExtra(MediaView.EXTRA_URL, url)
                    intent2.putExtra(MediaView.SUBREDDIT, subreddit)
                    activity!!.startActivity(intent2)
                } else {
                    Reddit.defaultShare(url, activity)
                }
                REDDIT -> OpenRedditLink(activity, url)
                LINK -> {
                    LogUtil.v("Opening link")
                    LinkUtil.openUrl(url, Palette.getColor(subreddit), activity)
                }
                SELF -> {
                }
                STREAMABLE, VID_ME -> openStreamable(url, subreddit)
                ALBUM -> if (SettingValues.album) {
                    if (SettingValues.albumSwipe) {
                        val i = Intent(activity, AlbumPager::class.java)
                        i.putExtra(Album.EXTRA_URL, url)
                        i.putExtra(AlbumPager.SUBREDDIT, subreddit)
                        activity!!.startActivity(i)
                    } else {
                        val i = Intent(activity, Album::class.java)
                        i.putExtra(Album.SUBREDDIT, subreddit)
                        i.putExtra(Album.EXTRA_URL, url)
                        activity!!.startActivity(i)
                    }
                } else {
                    Reddit.defaultShare(url, activity)
                }
                TUMBLR -> if (SettingValues.image) {
                    if (SettingValues.albumSwipe) {
                        val i = Intent(activity, TumblrPager::class.java)
                        i.putExtra(Album.EXTRA_URL, url)
                        activity!!.startActivity(i)
                    } else {
                        val i = Intent(activity, TumblrPager::class.java)
                        i.putExtra(Album.EXTRA_URL, url)
                        activity!!.startActivity(i)
                    }
                } else {
                    Reddit.defaultShare(url, activity)
                }
                IMAGE -> openImage(url, subreddit)
                GIF -> openGif(url, subreddit, activity)
                NONE -> {
                }
                VIDEO -> {
                    if (Reddit.videoPlugin) {
                        try {
                            val sharingIntent = Intent(Intent.ACTION_SEND)
                            sharingIntent.setClassName("ccrama.me.slideyoutubeplugin",
                                    "ccrama.me.slideyoutubeplugin.YouTubeView")
                            sharingIntent.putExtra("url", url)
                            activity!!.startActivity(sharingIntent)

                        } catch (e: Exception) {
                            Reddit.defaultShare(url, activity)
                        }

                    } else {
                        Reddit.defaultShare(url, activity)
                    }
                    isSpoilerClicked = true
                    setOrRemoveSpoilerSpans(xOffset, span)
                }
                SPOILER -> {
                    isSpoilerClicked = true
                    setOrRemoveSpoilerSpans(xOffset, span)
                }
                EXTERNAL -> Reddit.defaultShare(url, activity)
            }
        } else {
            Reddit.defaultShare(url, context)
        }
    }

    fun onLinkLongClick(baseUrl: String?, event: MotionEvent) {
        if (baseUrl == null) {
            return
        }
        val url = StringEscapeUtils.unescapeHtml4(baseUrl)

        performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
        var activity: Activity? = null
        val context = context
        if (context is Activity) {
            activity = context
        } else if (context is android.support.v7.view.ContextThemeWrapper) {
            activity = context.baseContext as Activity
        } else if (context is ContextWrapper) {
            val context1 = context.baseContext
            if (context1 is Activity) {
                activity = context1
            } else if (context1 is ContextWrapper) {
                val context2 = context1.baseContext
                if (context2 is Activity) {
                    activity = context2
                } else if (context2 is ContextWrapper) {
                    activity = (context2 as android.support.v7.view.ContextThemeWrapper).baseContext as Activity
                }
            }
        } else {
            throw RuntimeException("Could not find activity from context:" + context)
        }

        if (activity != null && !activity.isFinishing) {
            if (SettingValues.peek) {
                Peek.into(R.layout.peek_view, object : SimpleOnPeek() {
                    fun onInflated(peekView: PeekView, rootView: View) {
                        //do stuff
                        val text = rootView.findViewById(R.id.title) as TextView
                        text.text = url
                        text.setTextColor(Color.WHITE)
                        (rootView.findViewById(R.id.peek) as PeekMediaView).setUrl(url)

                        peekView.addButton(R.id.copy, object : OnButtonUp() {
                            fun onButtonUp() {
                                val clipboard = rootView.context
                                        .getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText("Link", url)
                                clipboard.primaryClip = clip
                                Toast.makeText(rootView.context,
                                        R.string.submission_link_copied, Toast.LENGTH_SHORT).show()
                            }
                        })

                        peekView.setOnRemoveListener(object : OnRemove() {
                            fun onRemove() {
                                (rootView.findViewById(R.id.peek) as PeekMediaView).doClose()
                            }
                        })

                        peekView.addButton(R.id.share, object : OnButtonUp() {
                            fun onButtonUp() {
                                Reddit.defaultShareText("", url, rootView.context)
                            }
                        })

                        peekView.addButton(R.id.pop, object : OnButtonUp() {
                            fun onButtonUp() {
                                Reddit.defaultShareText("", url, rootView.context)
                            }
                        })

                        peekView.addButton(R.id.external, object : OnButtonUp() {
                            fun onButtonUp() {
                                LinkUtil.openExternally(url, context, false)
                            }
                        })
                        peekView.setOnPop(object : OnPop() {
                            fun onPop() {
                                onLinkClick(url, 0, "", null)
                            }
                        })
                    }
                })
                        .with(PeekViewOptions().setFullScreenPeek(true))
                        .show(activity as PeekViewActivity?, event)
            } else {
                val b = BottomSheet.Builder(activity).title(url).grid()
                val attrs = intArrayOf(R.attr.tintColor)
                val ta = getContext().obtainStyledAttributes(attrs)

                val color = ta.getColor(0, Color.WHITE)
                val open = resources.getDrawable(R.drawable.ic_open_in_browser)
                open.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
                val share = resources.getDrawable(R.drawable.ic_share)
                share.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
                val copy = resources.getDrawable(R.drawable.ic_content_copy)
                copy.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)

                ta.recycle()

                b.sheet(R.id.open_link, open,
                        resources.getString(R.string.submission_link_extern))
                b.sheet(R.id.share_link, share, resources.getString(R.string.share_link))
                b.sheet(R.id.copy_link, copy,
                        resources.getString(R.string.submission_link_copy))
                val finalActivity = activity
                b.listener(DialogInterface.OnClickListener { dialog, which ->
                    when (which) {
                        R.id.open_link -> LinkUtil.openExternally(url, context, false)
                        R.id.share_link -> Reddit.defaultShareText("", url, finalActivity)
                        R.id.copy_link -> {
                            val clipboard = finalActivity.getSystemService(
                                    Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("Link", url)
                            clipboard.primaryClip = clip
                            Toast.makeText(finalActivity, R.string.submission_link_copied,
                                    Toast.LENGTH_SHORT).show()
                        }
                    }
                }).show()
            }
        }
    }

    private fun openGif(url: String, subreddit: String, activity: Activity?) {
        if (SettingValues.gif) {
            if (GifUtils.AsyncLoadGif.getVideoType(url).shouldLoadPreview()) {
                LinkUtil.openUrl(url, Palette.getColor(subreddit), activity)
            } else {
                val myIntent = Intent(context, MediaView::class.java)
                myIntent.putExtra(MediaView.EXTRA_URL, url)
                myIntent.putExtra(MediaView.SUBREDDIT, subreddit)
                context.startActivity(myIntent)
            }
        } else {
            Reddit.defaultShare(url, context)
        }
    }

    private fun openStreamable(url: String, subreddit: String) {
        if (SettingValues.video) { //todo maybe streamable here?
            val myIntent = Intent(context, MediaView::class.java)

            myIntent.putExtra(MediaView.EXTRA_URL, url)
            myIntent.putExtra(MediaView.SUBREDDIT, subreddit)
            context.startActivity(myIntent)

        } else {
            Reddit.defaultShare(url, context)
        }
    }

    private fun openImage(submission: String, subreddit: String) {
        if (SettingValues.image) {
            val myIntent = Intent(context, MediaView::class.java)
            myIntent.putExtra(MediaView.EXTRA_URL, submission)
            myIntent.putExtra(MediaView.SUBREDDIT, subreddit)
            context.startActivity(myIntent)
        } else {
            Reddit.defaultShare(submission, context)
        }

    }*/

    private fun setOrRemoveSpoilerSpans(endOfLink: Int, span: URLSpan?) {
        if (span != null) {
            val offset = if (span.url.contains("hidden")) -1 else 2
            val text = text as Spannable
            // add 2 to end of link since there is a white space between the link text and the spoiler
            val foregroundColors = text.getSpans(endOfLink + offset, endOfLink + offset,
                    ForegroundColorSpan::class.java)

            if (foregroundColors.size > 1) {
                text.removeSpan(foregroundColors[1])
                setText(text)
            } else {
                for (i in 1 until storedSpoilerStarts.size) {
                    if (storedSpoilerStarts[i] < endOfLink + offset && storedSpoilerEnds[i] > endOfLink + offset) {
                        try {
                            text.setSpan(storedSpoilerSpans[i], storedSpoilerStarts[i],
                                    if (storedSpoilerEnds[i] > text.toString().length)
                                        storedSpoilerEnds[i] + offset
                                    else
                                        storedSpoilerEnds[i],
                                    Spanned.SPAN_INCLUSIVE_INCLUSIVE)
                        } catch (ignored: Exception) {
                            //catch out of bounds
                            ignored.printStackTrace()
                        }

                    }
                }
                setText(text)
            }
        }
    }

    /**
     * Set the necessary spans for each spoiler.
     *
     * The algorithm works in the same way as
     * `setCodeFont`.
     *
     * @param sequence
     * @return
     */
    private fun setSpoilerStyle(sequence: SpannableStringBuilder): CharSequence {
        var start = 0
        var end = 0
        var i = 0
        while (i < sequence.length) {
            if (sequence[i] == '[' && i < sequence.length - 3) {
                if (sequence[i + 1] == '['
                        && sequence[i + 2] == 's'
                        && sequence[i + 3] == '[') {
                    start = i
                }
            } else if (sequence[i] == ']' && i < sequence.length - 3) {
                if (sequence[i + 1] == 's'
                        && sequence[i + 2] == ']'
                        && sequence[i + 3] == ']') {
                    end = i
                }
            }

            if (end > start) {
                sequence.delete(end, end + 4)
                sequence.delete(start, start + 4)

                val color = context.getColorRes(R.color.colorAccent)
                val backgroundColorSpan = BackgroundColorSpan(color)
                val foregroundColorSpan = ForegroundColorSpan(color)
                val underneathColorSpan = ForegroundColorSpan(Color.WHITE)

                val urlSpan = sequence.getSpans(start, start, URLSpan::class.java)[0]
                sequence.setSpan(urlSpan, sequence.getSpanStart(urlSpan), start - 1,
                        Spanned.SPAN_INCLUSIVE_INCLUSIVE)


                sequence.setSpan(URLSpanNoUnderline("#spoilerhidden"), start, end - 4,
                        Spannable.SPAN_INCLUSIVE_INCLUSIVE)
                // spoiler text has a space at the front
                sequence.setSpan(backgroundColorSpan, start + 1, end - 4,
                        Spannable.SPAN_INCLUSIVE_INCLUSIVE)
                sequence.setSpan(underneathColorSpan, start, end - 4,
                        Spannable.SPAN_INCLUSIVE_INCLUSIVE)
                sequence.setSpan(foregroundColorSpan, start, end - 4,
                        Spannable.SPAN_INCLUSIVE_INCLUSIVE)

                storedSpoilerSpans.add(underneathColorSpan)
                storedSpoilerSpans.add(foregroundColorSpan)
                storedSpoilerSpans.add(backgroundColorSpan)
                // Shift 1 to account for remove of beginning "<"

                storedSpoilerStarts.add(start - 1)
                storedSpoilerStarts.add(start - 1)
                storedSpoilerStarts.add(start - 1)
                storedSpoilerEnds.add(end - 5)
                storedSpoilerEnds.add(end - 5)
                storedSpoilerEnds.add(end - 5)

                sequence.delete(start - 2, start - 1) // remove the trailing <
                start = 0
                end = 0
                i -= 5 // move back to compensate for removal of [[s[
            }
            i++
        }

        return sequence
    }

    private inner class URLSpanNoUnderline(url: String) : URLSpan(url) {

        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.isUnderlineText = false
        }
    }

    /**
     * Sets the styling for string with code segments.
     *
     * The general process is to search for
     * `[[<[` and `]>]]` tokens to find the code fragments within the
     * escaped text. A `Spannable` is created which which breaks up the origin sequence
     * into non-code and code fragments, and applies a monospace font to the code fragments.
     *
     * @param sequence the Spannable generated from Html.fromHtml
     * @return the message with monospace font applied to code fragments
     */
    private fun setCodeFont(sequence: SpannableStringBuilder): SpannableStringBuilder {
        var start = 0
        var end = 0
        var i = 0
        while (i < sequence.length) {
            if (sequence[i] == '[' && i < sequence.length - 3) {
                if (sequence[i + 1] == '['
                        && sequence[i + 2] == '<'
                        && sequence[i + 3] == '[') {
                    start = i
                }
            } else if (sequence[i] == ']' && i < sequence.length - 3) {
                if (sequence[i + 1] == '>'
                        && sequence[i + 2] == ']'
                        && sequence[i + 3] == ']') {
                    end = i
                }
            }

            if (end > start) {
                sequence.delete(end, end + 4)
                sequence.delete(start, start + 4)
                sequence.setSpan(TypefaceSpan("monospace"), start, end - 4,
                        Spannable.SPAN_INCLUSIVE_INCLUSIVE)
                start = 0
                end = 0
                i = i - 4 // move back to compensate for removal of [[<[
            }
            i++
        }

        return sequence
    }

    companion object {
        private val htmlSpoilerPattern = Pattern.compile("<a href=\"([#/](?:spoiler|sp|s))\">([^<]*)</a>")

        private fun removeNewlines(s: SpannableStringBuilder): SpannableStringBuilder {
            var start = 0
            var end = s.length
            while (start < end && Character.isWhitespace(s[start])) {
                start++
            }

            while (end > start && Character.isWhitespace(s[end - 1])) {
                end--
            }

            return s.subSequence(start, end) as SpannableStringBuilder
        }
    }


}
