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

package io.github.gumil.basamto.reddit.subreddit

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.zhuinden.simplestack.Backstack
import io.github.gumil.basamto.R
import io.github.gumil.basamto.common.MviStateMachine
import io.github.gumil.basamto.common.MviViewModel
import io.github.gumil.basamto.reddit.submission.SubmissionKey
import io.github.gumil.data.repository.subreddit.SubredditRepository
import io.github.gumil.data.util.just
import io.reactivex.Observable

internal class SubredditViewModel(
        private val subredditRepository: SubredditRepository,
        private val backstack: Backstack
): ViewModel(), MviViewModel<SubredditState, SubredditIntent> {

    override val state: LiveData<SubredditState> get() = stateMachine.state

    private val stateMachine =
            MviStateMachine<SubredditState, SubredditIntent, SubredditResult>(SubredditState.View(), {
                when (it) {
                    is SubredditIntent.Load -> subredditRepository.loadThreads(it.subreddit, it.after, LIMIT)
                    is SubredditIntent.OnItemClick -> SubredditResult.GoTo(SubmissionKey()).just()
                }
            }, { _, result ->
                when (result) {
                    is SubredditResult.Success -> SubredditState.View(result.threads, false)
                    is SubredditResult.Error -> SubredditState.Error(R.string.error_subreddit_list)
                    is SubredditResult.InProgress -> SubredditState.View()
                    is SubredditResult.GoTo -> {
                        backstack.goTo(result.key)
                        SubredditState.Void
                    }
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