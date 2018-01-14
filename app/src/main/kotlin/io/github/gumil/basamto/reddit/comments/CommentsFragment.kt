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

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import io.github.gumil.basamto.R
import io.github.gumil.basamto.common.BaseFragment
import io.github.gumil.basamto.common.MviView
import io.github.gumil.basamto.common.adapter.ItemAdapter
import io.github.gumil.basamto.extensions.load
import io.github.gumil.basamto.extensions.setVisible
import io.github.gumil.basamto.main.MainActivity
import io.github.gumil.basamto.reddit.subreddit.SubmissionItem
import io.github.gumil.basamto.widget.html.getBlocks
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_comments.body
import kotlinx.android.synthetic.main.fragment_comments.bodyContainer
import kotlinx.android.synthetic.main.fragment_comments.commentsList
import kotlinx.android.synthetic.main.fragment_comments.commentsPreview
import kotlinx.android.synthetic.main.fragment_comments.swipeRefreshLayout
import javax.inject.Inject

internal class CommentsFragment : BaseFragment(), MviView<CommentsIntent, CommentsState> {

    override val layoutId: Int
        get() = R.layout.fragment_comments

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    private val submission by lazy {
        arguments.getParcelable<SubmissionItem>(ARG_SUBMISSION)
    }

    private val adapter = ItemAdapter(CommentViewItem())

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        (activity as? MainActivity)?.showToolbar(false)

        val viewModel = ViewModelProviders.of(this, viewModelFactory)[CommentsViewModel::class.java]

        viewModel.state.observe(this, Observer<CommentsState> {
            it?.render()
        })

        viewModel.processIntents(intents())

        commentsList.layoutManager = LinearLayoutManager(context)
        commentsList.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
    }

    override fun CommentsState.render() = when (this) {
        is CommentsState.View -> {
            swipeRefreshLayout.isRefreshing = isLoading

            commentsList.adapter ?: let { commentsList.adapter = adapter }

            adapter.list = comments

            commentsPreview.setVisible(submissionItem?.preview != null)
            commentsPreview.load(submissionItem?.preview)
            submissionItem?.body?.getBlocks()?.let {
                body.setViews(it)
                bodyContainer.setVisible(true)
            }
        }
        is CommentsState.Error -> showSnackbarError(message)
    }

    override fun intents(): Observable<CommentsIntent> = rxLifecycle.filter {
        it == Lifecycle.Event.ON_START
    }.map {
        CommentsIntent.Load(submission.subreddit, submission.id)
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