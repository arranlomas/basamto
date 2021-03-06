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

package io.github.gumil.basamto.common.adapter

import android.view.View
import android.view.ViewGroup
import io.github.gumil.basamto.R
import io.github.gumil.basamto.extensions.inflateLayout
import kotlinx.android.synthetic.main.view_empty.view.emptyViewImage
import kotlinx.android.synthetic.main.view_empty.view.emptyViewText

internal open class EmptyViewAdapter: BaseListAdapter<Unit, EmptyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Unit> =
            EmptyViewHolder(parent.inflateLayout(R.layout.view_empty))

}

internal class EmptyViewHolder(
        view: View,
        drawableRes: Int = R.drawable.ic_sentiment_dissatisfied_black_24dp,
        messageRes: Int = R.string.empty_list
) : BaseViewHolder<Unit>(view) {

    init {
        itemView.emptyViewImage.setImageResource(drawableRes)
        itemView.emptyViewText.setText(messageRes)
    }

    override fun bind(item: Unit) {
        //do nothing
    }
}