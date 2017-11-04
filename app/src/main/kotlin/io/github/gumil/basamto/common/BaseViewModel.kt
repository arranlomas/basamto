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

package io.github.gumil.basamto.common

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.jakewharton.rxrelay2.BehaviorRelay
import com.jakewharton.rxrelay2.Relay
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

internal abstract class BaseViewModel<S : MviState, in I : MviIntent, A : MviAction, R : MviResult>
    : ViewModel(), MviViewModel<S, I, A, R> {

    private val disposables = CompositeDisposable()

    protected val mutableState = MutableLiveData<S>()

    private val intents: Relay<I> = BehaviorRelay.create()

    fun initialize() {
        intents.map(this::actionFromIntent)
                .flatMap(this::resultFromAction)
                .scan(initialState, reducer)
                .subscribe { mutableState.postValue(it) }
                .disposeOnCleared()

    }

    override fun processIntents(intent: Observable<out I>) {
        intent.subscribe(intents).disposeOnCleared()
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

    private fun Disposable.disposeOnCleared() {
        disposables.add(this)
    }
}