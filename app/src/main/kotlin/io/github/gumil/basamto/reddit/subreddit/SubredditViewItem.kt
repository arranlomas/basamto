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

package io.github.gumil.basamto.reddit.subreddit

import android.annotation.SuppressLint
import android.text.format.DateUtils
import android.view.View
import io.github.gumil.basamto.R
import io.github.gumil.basamto.common.adapter.ViewItem
import io.github.gumil.basamto.extensions.load
import io.github.gumil.basamto.extensions.setVisible
import kotlinx.android.synthetic.main.item_subreddit.view.subredditComments
import kotlinx.android.synthetic.main.item_subreddit.view.subredditPreview
import kotlinx.android.synthetic.main.item_subreddit.view.subredditSubtitle
import kotlinx.android.synthetic.main.item_subreddit.view.subredditTitle
import kotlinx.android.synthetic.main.item_subreddit.view.subredditUpvote

internal class SubredditViewItem : ViewItem<SubmissionItem> {

    override var onItemClick: ((SubmissionItem) -> Unit)? = null

    override val layout: Int = R.layout.item_subreddit

    @SuppressLint("SetTextI18n")
    override fun bind(view: View, item: SubmissionItem) {
        val display = DateUtils.getRelativeTimeSpanString(item.timestamp * 1000)

        view.subredditTitle.text = item.title
        view.subredditUpvote.text = item.numUpvotes.toString()
        view.subredditComments.text = item.numComments.toString()
        view.subredditSubtitle.text = "${item.subreddit} • $display • ${item.user}"

        view.setOnClickListener {
            onItemClick?.invoke(item)
        }

        view.subredditPreview.setVisible(item.preview != null)

        item.preview?.let {
            view.subredditPreview.load(it)
        }
    }
}
