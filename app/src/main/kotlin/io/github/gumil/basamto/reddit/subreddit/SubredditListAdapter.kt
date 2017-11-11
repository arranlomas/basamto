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
import android.view.ViewGroup
import io.github.gumil.basamto.R
import io.github.gumil.core.ui.adapter.BaseListAdapter
import io.github.gumil.core.ui.adapter.BaseViewHolder
import io.github.gumil.core.ui.extensions.inflateLayout
import kotlinx.android.synthetic.main.item_subreddit.view.subredditComments
import kotlinx.android.synthetic.main.item_subreddit.view.subredditSubiitle
import kotlinx.android.synthetic.main.item_subreddit.view.subredditTitle
import kotlinx.android.synthetic.main.item_subreddit.view.subredditUpvote


internal class SubredditListAdapter: BaseListAdapter<ThreadItem, SubredditListAdapter.ViewHolder>() {

    val after get() = list.lastOrNull()?.after

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<ThreadItem> =
            ViewHolder(parent.inflateLayout(R.layout.item_subreddit))

    inner class ViewHolder(view: View) : BaseViewHolder<ThreadItem>(view) {

        @SuppressLint("SetTextI18n")
        override fun bind(item: ThreadItem) {
            val display = DateUtils.getRelativeTimeSpanString(item.timestamp * 1000)

            itemView.subredditTitle.text = item.title
            itemView.subredditUpvote.text = item.numUpvotes.toString()
            itemView.subredditComments.text = item.numComments.toString()
            itemView.subredditSubiitle.text = "${item.subreddit} • ${display} • ${item.user}"
        }
    }

}