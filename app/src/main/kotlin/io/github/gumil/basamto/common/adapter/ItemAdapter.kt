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

import android.os.Handler
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import io.github.gumil.basamto.extensions.inflateLayout
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.MainThreadDisposable

internal class ItemAdapter<M>(
        private val defaultItem: ViewItem<M>,
        private val prefetchDistance: Int = 2
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var footerItem: ViewItem<*>? = null

    private var _footerItem: ViewItem<*>? = null

    var list: List<M> = emptyList()
        set(value) {
            field = value
            _footerItem = null
            notifyDataSetChanged()
        }

    private var currentListSize = 0

    var onPrefetch: (() -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            object : RecyclerView.ViewHolder(parent.inflateLayout(viewType)) {}

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position < list.size) {
            defaultItem.bind(holder.itemView, list[position])
        }

        if (list.size > currentListSize && position == (list.size - prefetchDistance)) {
            currentListSize = list.size
            Handler().post {
                onPrefetch?.invoke()
            }
        }
    }

    override fun getItemCount(): Int = list.size + (_footerItem?.let { 1 } ?: 0)

    override fun getItemViewType(position: Int): Int {
        return if (position == list.size) {
            _footerItem?.layout ?: 0
        } else {
            defaultItem.layout
        }
    }

    fun showFooter() {
        _footerItem = footerItem
        notifyItemInserted(currentListSize)
    }
}

internal class OnPrefetchObservable(
        private val adapter: ItemAdapter<*>
): Observable<Unit>() {

    override fun subscribeActual(observer: Observer<in Unit>?) {
        observer?.let {
            adapter.onPrefetch = Listener(adapter, it)
        }
    }

    internal class Listener(
            private val adapter: ItemAdapter<*>,
            private val observer: Observer<in Unit>
    ) : MainThreadDisposable(), Function0<Unit> {

        override fun invoke() {
            if (!isDisposed) {
                observer.onNext(Unit)
            }
        }

        override fun onDispose() {
            adapter.onPrefetch = null
        }
    }
}

internal fun <M> ItemAdapter<M>.prefetch() = OnPrefetchObservable(this)