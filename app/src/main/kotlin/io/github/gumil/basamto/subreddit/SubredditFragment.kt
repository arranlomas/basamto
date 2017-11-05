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

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import io.github.gumil.basamto.R
import io.github.gumil.basamto.common.MviView
import io.github.gumil.core.ui.base.BaseFragment
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_subreddit.subredditList
import kotlinx.android.synthetic.main.fragment_subreddit.swipeRefreshLayout

internal class SubredditFragment : BaseFragment(), MviView<SubredditIntent, SubredditState> {

    override val layoutId: Int
        get() = R.layout.fragment_subreddit

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel = ViewModelProviders.of(this)[SubredditViewModel::class.java]

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