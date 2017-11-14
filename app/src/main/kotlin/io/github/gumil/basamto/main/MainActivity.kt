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

package io.github.gumil.basamto.main

import android.os.Bundle
import com.zhuinden.simplestack.HistoryBuilder
import com.zhuinden.simplestack.navigator.Navigator
import dagger.android.support.DaggerAppCompatActivity
import io.github.gumil.basamto.R
import io.github.gumil.basamto.navigation.FragmentStateChanger
import io.github.gumil.basamto.reddit.subreddit.SubredditKey
import kotlinx.android.synthetic.main.activity_main.fragmentContainer


internal class MainActivity : DaggerAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Navigator.configure()
                .setStateChanger(FragmentStateChanger(supportFragmentManager, R.id.fragmentContainer))
                .setShouldPersistContainerChild(false)
                .install(this, fragmentContainer, HistoryBuilder.single(SubredditKey()))

    }

    override fun onBackPressed() {
        if (!Navigator.getBackstack(this).goBack()) {
            super.onBackPressed()
        }
    }
}