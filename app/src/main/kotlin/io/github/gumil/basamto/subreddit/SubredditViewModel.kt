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

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import io.github.gumil.basamto.R
import io.github.gumil.basamto.common.MviStateMachine
import io.github.gumil.basamto.common.MviViewModel
import io.github.gumil.data.repository.subreddit.SubredditRepository
import io.reactivex.Observable

internal class SubredditViewModel(
        private val subredditRepository: SubredditRepository
): ViewModel(), MviViewModel<SubredditState, SubredditIntent> {

    override val state: LiveData<SubredditState> get() = stateMachine.state

    private val stateMachine =
            MviStateMachine<SubredditState, SubredditIntent, SubredditResult>(SubredditState.View(), {
                when (it) {
                    is SubredditIntent.Load -> subredditRepository.loadThreads(it.subreddit, it.after, LIMIT)
                }
            }, { _, result ->
                when (result) {
                    is SubredditResult.Success -> SubredditState.View(result.threads, false)
                    is SubredditResult.Error -> SubredditState.Error(R.string.error_subreddit_list)
                    is SubredditResult.InProgress -> SubredditState.View()
                }
            })

    override fun processIntents(intent: Observable<out SubredditIntent>) {
        stateMachine.processIntents(intent)
    }

    override fun onCleared() {
        super.onCleared()
        stateMachine.clearDisposables()
    }

    companion object {
        private const val LIMIT = 10
    }
}