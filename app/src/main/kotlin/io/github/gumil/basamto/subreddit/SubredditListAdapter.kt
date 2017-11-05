/*
 * Copyright 2017 Miguel Panelo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.gumil.basamto.subreddit

import android.annotation.SuppressLint
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

internal class SubredditListAdapter(
        list: List<ThreadItem>
) : BaseListAdapter<ThreadItem, SubredditListAdapter.ViewHolder>() {

    init {
        super.list = list
    }

    val after get() = list.lastOrNull()?.after

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<ThreadItem> =
            ViewHolder(parent.inflateLayout(R.layout.item_subreddit))

    inner class ViewHolder(view: View) : BaseViewHolder<ThreadItem>(view) {

        @SuppressLint("SetTextI18n")
        override fun bind(item: ThreadItem) {
            itemView.subredditTitle.text = item.title
            itemView.subredditUpvote.text = item.numUpvotes.toString()
            itemView.subredditComments.text = item.numComments.toString()
            itemView.subredditSubiitle.text = "${item.subreddit} • ${item.timeAgo} • ${item.user}"
        }
    }

}