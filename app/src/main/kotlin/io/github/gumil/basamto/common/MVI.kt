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
import io.reactivex.Observable

internal interface MviView<I : MviIntent, in S : MviState> {
    fun S.render()
    fun intents(): Observable<I>
}

internal interface MviViewModel<S : MviState, in I : MviIntent> {
    val state: LiveData<S>

    fun processIntents(intent: Observable<out I>)
}


internal interface MviIntent
internal interface MviState
internal interface MviResult