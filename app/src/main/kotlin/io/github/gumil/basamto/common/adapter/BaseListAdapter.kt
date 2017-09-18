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
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import io.github.gumil.basamto.common.BaseView

internal abstract class BaseListAdapter<M, out VH: BaseViewHolder> : RecyclerView.Adapter<BaseViewHolder>() {

    var list: List<M> = emptyList()

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        if (list.isNotEmpty()) {
            holder.bind(list[position])
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return if (viewType == TYPE_EMPTY) {
            onCreateEmptyViewHolder(parent.context, viewType)
        } else {
            onCreateViewHolder(parent.context, viewType).apply {
                (itemView as? BaseView)?.inflateView()
            }
        }
    }

    abstract fun onCreateViewHolder(context: Context, viewType: Int): VH

    open fun onCreateEmptyViewHolder(context: Context, viewType: Int): BaseViewHolder =
            EmptyView.createViewHolder(context)

    override fun getItemCount(): Int {
        return if (list.isEmpty()) {
            1
        } else {
            list.size
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (list.isEmpty()) {
            TYPE_EMPTY
        } else {
            getItemType(position)
        }
    }

    open fun getItemType(position: Int) = 0

    companion object {
        protected const val TYPE_EMPTY = -1
    }
}