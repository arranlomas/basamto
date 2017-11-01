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

package io.github.gumil.basamto.main

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.jakewharton.rxbinding2.view.RxView
import io.github.gumil.basamto.R
import io.github.gumil.basamto.common.MviView
import io.github.gumil.core.ui.adapter.EmptyViewAdapter
import kotlinx.android.synthetic.main.activity_main.subredditList
import kotlinx.android.synthetic.main.activity_main.swipeRefreshLayout
import kotlinx.android.synthetic.main.activity_main.testButton
import timber.log.Timber


internal class MainActivity : AppCompatActivity(), MviView<MainIntent, MainState> {

    private var mainViewModel: MainViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainViewModel = ViewModelProviders.of(this)[MainViewModel::class.java]

        mainViewModel?.state?.observe(this, Observer<MainState> {
            it?.render()
        })

        subredditList.setHasFixedSize(true)
        subredditList.layoutManager = LinearLayoutManager(this)

        mainViewModel?.sendIntent(MainIntent.Load())

        mainViewModel?.sendIntent(
                RxView.clicks(testButton).map {
                    Timber.tag("tantrums").d("click")
                    MainIntent.ButtonClick()
                }
        )
    }

    override fun MainState.render() {
        when (this) {
            is MainState.Empty -> {
                swipeRefreshLayout.isRefreshing = false
                subredditList.adapter = EmptyViewAdapter()
            }
            is MainState.Loading -> {
                swipeRefreshLayout.isRefreshing = true
            }
            is MainState.Result -> {
                swipeRefreshLayout.isRefreshing = false
                subredditList.adapter = SubredditListAdapter(threads)
            }
            is MainState.Click -> Toast.makeText(this@MainActivity, "Hello World", Toast.LENGTH_SHORT).show()
        }
    }
}