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

package io.github.gumil.basamto.reddit.comments

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import io.github.gumil.basamto.R
import io.github.gumil.basamto.common.adapter.ItemAdapter
import io.github.gumil.basamto.common.adapter.ViewItem
import io.github.gumil.basamto.extensions.getColorRes
import io.github.gumil.basamto.extensions.setVisible
import io.github.gumil.basamto.widget.html.getBlocks
import kotlinx.android.synthetic.main.item_comment.view.author
import kotlinx.android.synthetic.main.item_comment.view.body
import kotlinx.android.synthetic.main.item_comment.view.depth
import kotlinx.android.synthetic.main.item_comment.view.divider
import kotlinx.android.synthetic.main.item_comment.view.replies

internal class CommentViewItem : ViewItem<CommentItem> {

    private var depthColors = arrayOf(
            R.color.md_blue_500,
            R.color.md_green_500,
            R.color.md_yellow_500,
            R.color.md_orange_500,
            R.color.md_red_500
    )

    override var onItemClick: ((CommentItem) -> Unit)? = null

    override val layout: Int get() = R.layout.item_comment

    override fun bind(view: View, item: CommentItem) {
        val blocks = item.body.getBlocks()
        view.author.text = item.user
        view.body.setViews(blocks)
        view.replies.run {
            layoutManager = LinearLayoutManager(view.context)
            adapter = ItemAdapter(CommentViewItem()).apply {
                list = item.replies
            }
        }
        view.divider.setVisible(item.replies.isNotEmpty())

        if (item.depth > 0) {
            view.depth.run {
                setVisible(item.depth > 0)
                setBackgroundColor(context.getColorRes(depthColors[item.depth - 1]))
            }
        }
    }

}