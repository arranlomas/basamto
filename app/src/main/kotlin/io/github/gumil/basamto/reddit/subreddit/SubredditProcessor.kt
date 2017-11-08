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

package io.github.gumil.basamto.reddit.subreddit

import io.github.gumil.data.model.RedditThread
import io.github.gumil.data.repository.subreddit.SubredditRepository
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

internal fun SubredditRepository.loadThreads(
        subreddit: String,
        after: String,
        limit: Int
): Observable<SubredditResult> {
    return getThreadsFrom(subreddit, after, limit)
            .map {
                SubredditResult.Success(it.map { it.map() })
            }
            .ofType(SubredditResult::class.java)
            .onErrorReturn { SubredditResult.Error() }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .startWith(SubredditResult.InProgress())
}


private fun RedditThread.map(): ThreadItem {
    return ThreadItem(
            title,
            subreddit,
            createdUtc,
            author,
            ups - downs,
            numComments
    )
}

internal data class ThreadItem(
        val title: String,
        val subreddit: String,
        val timestamp: Long,
        val user: String,
        val numUpvotes: Int,
        val numComments: Int,
        val after: String? = null
)