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

import io.github.gumil.basamto.common.MviIntent
import io.github.gumil.basamto.common.MviResult
import io.github.gumil.basamto.common.MviState
import io.github.gumil.basamto.reddit.comments.CommentsKey

internal sealed class SubredditState: MviState {

    data class Initial(
            val threads: List<SubmissionItem> = emptyList(),
            val isLoading: Boolean = true
    ) : SubredditState()

    data class LoadMore(
            val threads: List<SubmissionItem> = emptyList(),
            val isLoading: Boolean = true
    ) : SubredditState()

    data class Error(
            val message: Int
    ) : SubredditState()

    object Void : SubredditState()
}

internal sealed class SubredditIntent : MviIntent {
    data class Initial(
            val subreddit: String
    ) : SubredditIntent()

    data class Load(
            val subreddit: String,
            val after: String
    ) : SubredditIntent()

    data class OnItemClick(
            val item: SubmissionItem
    ) : SubredditIntent()
}

internal sealed class SubredditResult : MviResult {

    data class Success(
            val threads: List<SubmissionItem> = emptyList(),
            val mode: Mode
    ) : SubredditResult()

    object Error : SubredditResult()

    data class InProgress(
            val mode: Mode
    ) : SubredditResult()

    data class GoTo(
            val key: CommentsKey
    ) : SubredditResult()

    enum class Mode {
        REFRESH, LOAD_MORE
    }
}