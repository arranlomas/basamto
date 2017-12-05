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

import io.github.gumil.data.model.Link
import io.github.gumil.data.repository.subreddit.SubredditRepository
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

internal fun SubredditRepository.loadThreads(
        subreddit: String,
        after: String?,
        limit: Int,
        mode: SubredditResult.Mode
): Observable<SubredditResult> {
    return getThreadsFrom(subreddit, after, limit)
            .map {
                SubredditResult.Success(it.map { it.map() }, mode)
            }
            .ofType(SubredditResult::class.java)
            .onErrorReturn { SubredditResult.Error }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .startWith(SubredditResult.InProgress(mode))
}


private fun Link.map(): SubmissionItem {
    return SubmissionItem(
            title,
            subreddit,
            createdUtc,
            author,
            score,
            commentsCount,
            name,
            preview?.images?.firstOrNull()?.source?.url
    )
}

internal data class SubmissionItem(
        val title: String,
        val subreddit: String,
        val timestamp: Long,
        val user: String,
        val numUpvotes: Int,
        val numComments: Int,
        val after: String,
        val preview: String? = null
)