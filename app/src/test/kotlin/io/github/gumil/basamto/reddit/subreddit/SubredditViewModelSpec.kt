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

import android.arch.lifecycle.Observer
import com.nhaarman.mockito_kotlin.MockitoKotlin
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.zhuinden.simplestack.Backstack
import io.github.gumil.basamto.R
import io.github.gumil.basamto.common.InstantTaskRule
import io.github.gumil.basamto.common.RxRule
import io.github.gumil.basamto.common.addRules
import io.github.gumil.basamto.navigation.BaseKey
import io.github.gumil.basamto.reddit.submission.SubmissionKey
import io.github.gumil.data.repository.subreddit.SubredditRepository
import io.github.gumil.data.util.error
import io.github.gumil.data.util.just
import org.amshove.kluent.any
import org.amshove.kluent.mock
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on

internal object SubredditViewModelSpec : Spek({
    addRules(InstantTaskRule(), RxRule())
    MockitoKotlin.registerInstanceCreator <BaseKey> { SubmissionKey() }
    given("a SubredditViewModel") {
        val subredditRepository = mock<SubredditRepository>()
        val backstack = mock<Backstack>()
        val observer = mock<Observer<SubredditState>>()
        val subredditViewModel = SubredditViewModel(subredditRepository, backstack)

        subredditViewModel.state.observeForever(observer)

        it("should emit initial state") {
            verify(observer).onChanged(SubredditState.Initial())
        }

        context("Load Intent") {

            on("success") {
                val redditThreads = SubredditObjects.createListOfRedditThread()
                val threadItems = listOf(SubredditObjects.createThreadItem())

                whenever(subredditRepository.getThreadsFrom("test", "qwer", 10))
                        .thenReturn(redditThreads.just())
                subredditViewModel.processIntents(SubredditIntent.Load("test", "qwer").just())

                it("should emit a View with list of threads") {
                    verify(observer, times(2)).onChanged(SubredditState.Initial(emptyList(), true))
                    verify(observer).onChanged(SubredditState.Initial(threadItems, false))
                }
            }

            on("error") {
                whenever(subredditRepository.getThreadsFrom("test", "qwer", 10))
                        .thenReturn(RuntimeException().error())
                subredditViewModel.processIntents(SubredditIntent.Load("test", "qwer").just())
                it("should emit a View with list of threads") {
                    verify(observer, times(3)).onChanged(SubredditState.Initial(emptyList(), true))
                    verify(observer).onChanged(SubredditState.Error(R.string.error_subreddit_list))
                }
            }
        }

        on("OnItemClick Intent") {
            subredditViewModel.processIntents(SubredditIntent.OnItemClick(
                    SubredditObjects.createThreadItem()
            ).just())

            it("should emit a Void state go to Submission Page") {
                verify(backstack).goTo(any())
                verify(observer).onChanged(SubredditState.Void)
            }
        }
    }

})