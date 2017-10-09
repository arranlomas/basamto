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

package io.github.gumil.data.network

import io.github.gumil.data.model.SubredditResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

internal interface RedditApi {

    companion object {
        private const val SUBREDDIT = "subreddit"
    }

    @GET("r/{$SUBREDDIT}/top.json")
    fun getSubreddit(
            @Path(SUBREDDIT) subreddit: String,
            @Query("limit") limit: Int = 10,
            @Query("after") after: String? = null): Single<SubredditResponse>
}