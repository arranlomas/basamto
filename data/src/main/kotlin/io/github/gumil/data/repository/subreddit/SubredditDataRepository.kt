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

package io.github.gumil.data.repository.subreddit

import io.github.gumil.data.model.Comment
import io.github.gumil.data.model.Link
import io.github.gumil.data.rest.RedditApi
import io.github.gumil.data.util.applySchedulers
import io.reactivex.Observable

internal class SubredditDataRepository(
        private val redditApi: RedditApi
) : SubredditRepository {

    override fun getThreadsFrom(subreddit: String, after: String?, limit: Int): Observable<List<Link>> {
        return redditApi.getSubreddit(subreddit, after, limit).map {
            it.data.children.filterIsInstance(Link::class.java)
        }.toObservable().applySchedulers()
    }

    override fun getCommentsFrom(subreddit: String, id: String): Observable<Pair<Link, List<Comment>>> {
        return redditApi.getComments(subreddit, id).map {
            it[0].data.children.filterIsInstance<Link>().first() to
                    it[1].data.children.filterIsInstance<Comment>()
        }.toObservable().applySchedulers()
    }
}