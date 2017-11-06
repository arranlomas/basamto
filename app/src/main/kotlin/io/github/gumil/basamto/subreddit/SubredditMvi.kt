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

package io.github.gumil.basamto.subreddit

import io.github.gumil.basamto.common.MviIntent
import io.github.gumil.basamto.common.MviResult
import io.github.gumil.basamto.common.MviState

internal sealed class SubredditState: MviState {

    data class View(
            val threads: List<ThreadItem> = emptyList(),
            val isLoading: Boolean = true
    ) : SubredditState()

    data class Error(
            val message: Int
    ) : SubredditState()
}

internal sealed class SubredditIntent : MviIntent {
    class Load(
            val subreddit: String,
            val after: String
    ) : SubredditIntent()
}

internal sealed class SubredditResult : MviResult {

    class Success(
            val threads: List<ThreadItem> = emptyList()
    ) : SubredditResult()

    class Error : SubredditResult()

    class InProgress : SubredditResult()
}

enum class RequestStatus {
    SUCCESS, FAILURE, IN_PROGRESS
}