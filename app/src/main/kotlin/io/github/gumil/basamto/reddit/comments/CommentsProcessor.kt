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

package io.github.gumil.basamto.reddit.comments

import io.github.gumil.basamto.reddit.subreddit.map
import io.github.gumil.data.model.Comment
import io.github.gumil.data.model.Listing
import io.github.gumil.data.repository.subreddit.SubredditRepository
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

internal fun SubredditRepository.loadComments(
        subreddit: String,
        submissionId: String
): Observable<CommentsResult> {
    return getCommentsFrom(subreddit, submissionId)
            .map {
                CommentsResult.Success(it.first.map(), it.second.map { it.map() })
            }
            .ofType(CommentsResult::class.java)
            .onErrorReturn { CommentsResult.Error }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .startWith(CommentsResult.InProgress)
}

private fun Comment.map(): CommentItem {
    return CommentItem(
            id,
            createdUtc,
            author,
            score,
            bodyHtml,
            (replies as? Listing)?.children?.filterIsInstance<Comment>()
                    ?.map { it.map() }?: emptyList()
    )
}

internal data class CommentItem(
        val id: String,
        val timestamp: Long,
        val user: String,
        val score: Int,
        val body: String,
        val replies: List<CommentItem>
)