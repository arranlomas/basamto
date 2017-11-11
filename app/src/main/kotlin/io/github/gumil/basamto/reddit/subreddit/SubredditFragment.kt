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

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import io.github.gumil.basamto.R
import io.github.gumil.basamto.common.BaseFragment
import io.github.gumil.basamto.common.MviView
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_subreddit.subredditList
import kotlinx.android.synthetic.main.fragment_subreddit.swipeRefreshLayout
import javax.inject.Inject

internal class SubredditFragment : BaseFragment(), MviView<SubredditIntent, SubredditState> {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    override val layoutId: Int
        get() = R.layout.fragment_subreddit

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel = ViewModelProviders.of(this, viewModelFactory)[SubredditViewModel::class.java]

        viewModel.state.observe(this, Observer<SubredditState> {
            it?.render()
        })

        viewModel?.processIntents(intents())
    }

    private fun getLoadIntent(): Observable<SubredditIntent> {
        return rxLifecycle.filter {
            it == Lifecycle.Event.ON_START
        }.map {
            SubredditIntent.Load("androiddev", "asd")
        }
    }

    override fun intents(): Observable<SubredditIntent> = getLoadIntent()

    override fun SubredditState.render() {
        when (this) {
            is SubredditState.View -> {
                swipeRefreshLayout.isRefreshing = isLoading

                if (subredditList.adapter == null) {
                    subredditList.layoutManager = LinearLayoutManager(context)
                    subredditList.adapter = SubredditListAdapter()
                }

                (subredditList.adapter as? SubredditListAdapter)?.let {
                    it.list = threads
                }
            }
            is SubredditState.Error -> showSnackbarError(message)
        }
    }
}