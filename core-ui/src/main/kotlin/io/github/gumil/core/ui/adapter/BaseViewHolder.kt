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

package io.github.gumil.core.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import io.github.gumil.core.ui.R
import kotlinx.android.synthetic.main.view_empty.view.emptyViewImage
import kotlinx.android.synthetic.main.view_empty.view.emptyViewText

internal abstract class BaseViewHolder<in M>(view: View) : RecyclerView.ViewHolder(view) {
    abstract fun bind(item: M)
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