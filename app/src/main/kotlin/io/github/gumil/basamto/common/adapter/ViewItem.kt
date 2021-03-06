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
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.MainThreadDisposable

internal interface ViewItem<M> {

    var onItemClick: ((M) -> Unit)?

    val layout: Int

    fun bind(view: View, item: M)
}

internal data class FooterItem(
        override val layout: Int
) : ViewItem<Nothing> {

    override var onItemClick: ((Nothing) -> Unit)? = null

    override fun bind(view: View, item: Nothing) {
        //no bindings
    }
}

internal class OnItemClickObservable<T>(
        private val viewItem: ViewItem<T>
): Observable<T>() {

    override fun subscribeActual(observer: Observer<in T>?) {
        observer?.let {
            viewItem.onItemClick = Listener(viewItem, it)
        }

    }

    internal class Listener<in T>(
            private val viewItem: ViewItem<T>,
            private val observer: Observer<in T>
    ) : MainThreadDisposable(), Function1<T, Unit> {

        override fun invoke(p1: T) {
            if (!isDisposed) {
                observer.onNext(p1)
            }
        }

        override fun onDispose() {
            viewItem.onItemClick = null
        }
    }
}

internal fun <M> ViewItem<M>.itemClick() = OnItemClickObservable(this)