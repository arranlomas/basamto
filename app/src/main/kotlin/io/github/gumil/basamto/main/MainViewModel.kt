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
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.jakewharton.rxrelay2.PublishRelay
import io.github.gumil.basamto.common.MviViewModel
import io.reactivex.Observable
import io.reactivex.functions.BiFunction

internal class MainViewModel : ViewModel(), MviViewModel<MainIntent, MainState, MainAction> {


    override val state: LiveData<MainState>
        get() = mutableState

    private val mutableState = MutableLiveData<MainState>()

    private val intents: PublishRelay<MainIntent> = PublishRelay.create()

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
    )).onErrorReturn {
        MainResult.Load(LceStatus.FAILURE)
    }.startWith(Observable.just(MainResult.Load(LceStatus.IN_FLIGHT)))

    private val reducer = BiFunction<MainState, MainResult, MainState> { _, result ->
        when (result) {
            is MainResult.Load -> MainState.Result(result.threads)
            is MainResult.Click -> MainState.Click()
        }
    }

    init {
        intents.map(this::actionFromIntent)
                .flatMap {
                    when (it) {
                        is MainAction.Load -> observable
                        is MainAction.Click -> Observable.just(MainResult.Click())
                    }
                }
                .scan(MainState.Empty(), reducer)
                .subscribe {
                    mutableState.value = it
                }
    }

    override fun sendIntent(intent: Observable<out MainIntent>) {
        intent.subscribe(intents)
    }

    override fun sendIntent(intent: MainIntent) {
        intents.accept(intent)
    }

    override fun actionFromIntent(intent: MainIntent): MainAction = when (intent) {
        is MainIntent.Load -> MainAction.Load()
        is MainIntent.ButtonClick -> MainAction.Click()
    }
}