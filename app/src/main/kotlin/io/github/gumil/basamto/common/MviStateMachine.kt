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

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.jakewharton.rxrelay2.BehaviorRelay
import com.jakewharton.rxrelay2.Relay
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Function as RxFunction

internal class MviStateMachine<S : MviState, in I : MviIntent, R : MviResult>(
        initialState: S,
        resultFromIntent: (I) -> Observable<R>,
        reducer: (state: S, result: R) -> S

) {

    val state: LiveData<S> get() = mutableState

    private val disposables = CompositeDisposable()

    private val mutableState = MutableLiveData<S>()

    private val intents: Relay<I> = BehaviorRelay.create()

    init {
        intents.flatMap(resultFromIntent)
                .scan(initialState, reducer)
                .subscribe { mutableState.postValue(it) }
                .disposeOnCleared()
    }


    fun processIntents(intent: Observable<out I>) {
        intent.subscribe(intents).disposeOnCleared()
    }

    fun clearDisposables() {
        disposables.clear()
    }

    private fun Disposable.disposeOnCleared() {
        disposables.add(this)
    }
}