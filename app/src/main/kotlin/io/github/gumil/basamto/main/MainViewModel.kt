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

package io.github.gumil.basamto.main

import android.arch.lifecycle.LiveData
import io.github.gumil.basamto.common.BaseViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers

internal class MainViewModel : BaseViewModel<MainState, MainIntent, MainAction, MainResult>() {

    override val initialState: MainState
        get() = MainState.Empty()

    override val state: LiveData<MainState>
        get() = mutableState

    private val observable = Observable.just(MainResult.Load(
            LceStatus.SUCCESS,
            listOf(
                    SimpleThread(
                            "Title Super",
                            "/r/androiddev",
                            "8h",
                            "gumil",
                            12345,
                            10
                    )
            )
    )).onErrorReturn { MainResult.Load(LceStatus.FAILURE) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .startWith(Observable.just(MainResult.Load(LceStatus.IN_FLIGHT)))


    override val reducer = BiFunction<MainState, MainResult, MainState> { _, result ->
        when (result) {
            is MainResult.Load -> MainState.Result(result.threads)
            is MainResult.Click -> MainState.Click()
        }
    }

    override fun actionFromIntent(intent: MainIntent): MainAction = when (intent) {
        is MainIntent.Load -> MainAction.Load()
        is MainIntent.ButtonClick -> MainAction.Click()
    }

    override fun resultFromAction(action: MainAction): Observable<out MainResult> = when (action) {
        is MainAction.Load -> observable
        is MainAction.Click -> Observable.just(MainResult.Click())
    }
}