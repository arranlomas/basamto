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

package io.github.gumil.data.persistence.dao

import android.support.test.runner.AndroidJUnit4
import io.github.gumil.data.Creators
import io.github.gumil.data.model.RedditThread
import io.reactivex.observers.TestObserver
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
internal class RedditThreadDaoTest : DbTest() {

    @Test
    fun insertAndLoad() {
        val thread = Creators.createRedditThread("1", 123,"test")
        db.redditThreadDao().insert(thread)

        val allSubscriber = TestObserver<List<RedditThread>>()
        db.redditThreadDao().getAll().subscribe(allSubscriber)

        allSubscriber.assertNoErrors()
        allSubscriber.assertNoTimeout()
        allSubscriber.assertComplete()
        allSubscriber.assertValue(listOf(thread))
    }

    @Test
    fun getFirstTenFromTwentySameSubreddit() {
        val subreddit = "test"
        val list = (1..20).map {
            Creators.createRedditThread(it.toString(), 20 - it.toLong(), subreddit)
        }
        val expected = list.subList(0, 10)

        testInsertGet(list, subreddit, expected)
    }

    @Test
    fun getTenFromTwoSubreddits() {
        val subreddit = "test"
        val subreddit2 = "test2"

        val list = mutableListOf<RedditThread>()
        (1..10).forEach {
            list.add(Creators.createRedditThread(it.toString(), it.toLong(), subreddit))
            list.add(Creators.createRedditThread((10 + it).toString(), it.toLong(), subreddit2))
        }

        val expected = list.filter { it.subreddit == subreddit }.reversed()

        testInsertGet(list, subreddit, expected)
    }

    private fun testInsertGet(list: List<RedditThread>, subreddit: String, expected: List<RedditThread>) {
        db.redditThreadDao().insert(*list.toTypedArray())

        val subscriber = TestObserver<List<RedditThread>>()
        db.redditThreadDao().getThreadsFrom(subreddit).subscribe(subscriber)

        subscriber.assertNoErrors()
        subscriber.assertNoTimeout()
        subscriber.assertComplete()

        subscriber.assertValue(expected)
    }

}