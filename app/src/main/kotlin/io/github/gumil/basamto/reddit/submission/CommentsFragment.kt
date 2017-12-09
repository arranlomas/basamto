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

package io.github.gumil.basamto.reddit.submission

import android.os.Bundle
import android.view.View
import io.github.gumil.basamto.R
import io.github.gumil.basamto.common.BaseFragment
import io.github.gumil.basamto.common.MviView
import io.github.gumil.basamto.extensions.load
import io.github.gumil.basamto.main.MainActivity
import io.github.gumil.basamto.reddit.subreddit.SubmissionItem
import io.github.gumil.basamto.reddit.subreddit.SubredditState
import io.github.gumil.data.repository.subreddit.SubredditRepository
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_comments.commentsPreview
import kotlinx.android.synthetic.main.fragment_comments.text
import javax.inject.Inject

internal class CommentsFragment : BaseFragment(), MviView<CommentsIntent, SubredditState> {

    override val layoutId: Int
        get() = R.layout.fragment_comments

    @Inject
    lateinit var repository: SubredditRepository

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        (activity as? MainActivity)?.showToolbar(false)

        val submission = arguments.getParcelable<SubmissionItem>(ARG_SUBMISSION)

        repository.getCommentsFrom(submission.subreddit, submission.id).subscribe({
            text.text = it.toString()
            commentsPreview.load(it.first.preview?.images?.get(0)?.source?.url)
        })
    }

    override fun SubredditState.render(): Any? {
        throw UnsupportedOperationException("not implemented")
    }

    override fun intents(): Observable<CommentsIntent> {
        throw UnsupportedOperationException("not implemented")
    }

    companion object {
        private const val ARG_SUBMISSION = "comments_submission"

        fun newInstance(submissionItem: SubmissionItem) = CommentsFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ARG_SUBMISSION, submissionItem)
            }
        }
    }
}