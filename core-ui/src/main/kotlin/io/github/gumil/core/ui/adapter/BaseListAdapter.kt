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

internal abstract class BaseListAdapter<M, out VH : BaseViewHolder<M>> : RecyclerView.Adapter<BaseViewHolder<M>>() {

    var list: List<M> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onBindViewHolder(holder: BaseViewHolder<M>, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size
}