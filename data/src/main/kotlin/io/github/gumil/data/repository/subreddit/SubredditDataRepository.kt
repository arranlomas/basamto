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

package io.github.gumil.data.repository.subreddit

import io.github.gumil.data.model.RedditThread
import io.github.gumil.data.remote.RedditApi
import io.github.gumil.data.util.applySchedulers
import io.reactivex.Observable

internal class SubredditDataRepository(
        private val redditApi: RedditApi
) : SubredditRepository {

    override fun getThreadsFrom(subreddit: String, after: String?, limit: Int): Observable<List<RedditThread>> {
        return redditApi.getSubreddit(subreddit, after, limit).map {
            it.data.children.map {
                it.data
            }
        }.toObservable().applySchedulers()
    }

}