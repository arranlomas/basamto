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

import io.github.gumil.data.model.RedditThread
import io.github.gumil.data.repository.subreddit.SubredditRepository
import io.github.gumil.data.util.just
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

internal fun SubredditRepository.loadThreads(
        subreddit: String,
        after: String,
        limit: Int
): Observable<SubredditResult> {
    return getThreadsFrom(subreddit, after, limit)
            .map { SubredditResult.Success(
                    listOf(
                            ThreadItem(
                                    "Title Super",
                                    "/r/androiddev",
                                    "8h",
                                    "gumil",
                                    12345,
                                    10
                            )
                    )
            ) as SubredditResult }
            .onErrorReturn { SubredditResult.Error() }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .startWith(SubredditResult.InProgress())
}

internal class DummyRepository: SubredditRepository {
    override fun getThreadsFrom(subreddit: String, after: String?, limit: Int): Observable<List<RedditThread>> =
            listOf<RedditThread>().just()

}