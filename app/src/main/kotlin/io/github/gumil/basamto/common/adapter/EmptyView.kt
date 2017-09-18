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

package io.github.gumil.basamto.common.adapter

import android.content.Context
import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import android.view.Gravity
import android.view.ViewGroup
import io.github.gumil.basamto.R
import io.github.gumil.basamto.common.ViewLayout
import org.jetbrains.anko.*

internal class EmptyView(
        context: Context,
        @DrawableRes private val drawableRes: Int,
        @StringRes private val messageRes: Int
) : ListItemView<Unit>(context) {

    constructor(context: Context) : this(
            context,
            R.drawable.ic_sentiment_dissatisfied_black_24dp,
            R.string.empty_list
    )

    init {
        layoutParams = LayoutParams(matchParent, matchParent)
    }

    override val viewLayout: ViewLayout = Layout(drawableRes, messageRes)

    override fun bind(item: Unit) {}

    class Layout(
            @DrawableRes private val drawableRes: Int,
            @StringRes private val messageRes: Int
    ) : ViewLayout {
        override fun createView(context: Context) = with(context) {
            verticalLayout {
                gravity = Gravity.CENTER
                layoutParams = ViewGroup.LayoutParams(matchParent, matchParent)

                imageView {
                    gravity = Gravity.CENTER
                    setImageResource(drawableRes)
                }.lparams(dip(72), dip(72)) {
                    margin = dip(24)
                }

                textView(messageRes) {
                    gravity = Gravity.CENTER
                }
            }
        }

    }

    class ViewHolder(view: EmptyView) : BaseViewHolder(view)

    companion object {
        fun createViewHolder(context: Context) = ViewHolder(EmptyView(context))
    }
}