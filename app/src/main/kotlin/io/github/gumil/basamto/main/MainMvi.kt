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

import io.github.gumil.basamto.common.MviAction
import io.github.gumil.basamto.common.MviIntent
import io.github.gumil.basamto.common.MviResult
import io.github.gumil.basamto.common.MviState

internal sealed class MainState : MviState {

    class Empty : MainState()

    class Loading : MainState()

    data class Result(
            val threads: List<SimpleThread>
    ) : MainState()

    class Click: MainState()

}

internal sealed class MainIntent : MviIntent {

    class Load : MainIntent()

    class ButtonClick : MainIntent()
}

internal sealed class MainAction : MviAction {

    class Load : MainAction()

    class Click : MainAction()

}

internal sealed class MainResult : MviResult {

    class Load(
            val status: LceStatus,
            val threads: List<SimpleThread> = emptyList()
    ) : MainResult()

    class Click : MainResult()
}

enum class LceStatus {
    SUCCESS, FAILURE, IN_FLIGHT
}