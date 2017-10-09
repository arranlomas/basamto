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

package io.github.gumil.data

import io.github.gumil.data.model.RedditThread
import io.github.gumil.data.model.SubredditResponse
import io.github.gumil.data.model.base.Distinguish
import io.github.gumil.data.network.RedditApi
import io.reactivex.observers.TestObserver
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

internal class RedditApiTest {

    private val mockServer = MockWebServer()
    private lateinit var redditApi: RedditApi

    @Before
    fun setup() {
        mockServer.start()
        val url = mockServer.url("/").toString()
        redditApi = ApiFactory.createRedditApi(true, url)
    }

    @Test
    fun testGetSubreddit() {
        mockServer.enqueue(createMockResponse(readFromFile("subreddit.json")))

        val testSubscriber = TestObserver<SubredditResponse>()
        redditApi.getSubreddit("androiddev").subscribe(testSubscriber)

        testSubscriber.assertNoErrors()
        testSubscriber.assertNoTimeout()
        testSubscriber.assertComplete()
        assertEquals(3, testSubscriber.events.size)
        val subredditResponse = testSubscriber.events[0][0]
        assertTrue(subredditResponse is SubredditResponse)

        (subredditResponse as? SubredditResponse)?.let {
            assertEquals(10, it.data.children.size)
            assertEquals("74poqy", it.data.children.last().data.id)

            val thread = RedditThread(
                    "74ulnb", "t3_74ulnb",
                    1, 0, null,
                    1507408247, 1507379447,
                    "self.androiddev",
                    "androiddev", "t5_2r26y",
                    "&lt;!-- SC_OFF --&gt;&lt;div class=\"md\"&gt;&lt;p&gt;This thread is for getting feedback on your own apps.&lt;/p&gt;\n\n&lt;h4&gt;Developers:&lt;/h4&gt;\n\n&lt;ul&gt;\n&lt;li&gt;must &lt;strong&gt;provide feedback&lt;/strong&gt; for others&lt;/li&gt;\n&lt;li&gt;must include &lt;strong&gt;Play Store&lt;/strong&gt;, &lt;strong&gt;GitHub&lt;/strong&gt;, or &lt;strong&gt;BitBucket&lt;/strong&gt; link&lt;/li&gt;\n&lt;li&gt;must make top level comment&lt;/li&gt;\n&lt;li&gt;must make effort to respond to questions and feedback from commenters&lt;/li&gt;\n&lt;li&gt;may be open or closed source&lt;/li&gt;\n&lt;/ul&gt;\n\n&lt;h4&gt;Commenters:&lt;/h4&gt;\n\n&lt;ul&gt;\n&lt;li&gt;must give &lt;strong&gt;constructive feedback&lt;/strong&gt; in replies to top level comments&lt;/li&gt;\n&lt;li&gt;must not include links to other apps&lt;/li&gt;\n&lt;/ul&gt;\n\n&lt;p&gt;To cut down on spam, accounts who are too young or do not have enough karma to post will be removed. Please make an effort to contribute to the community before asking for feedback.&lt;/p&gt;\n\n&lt;p&gt;As always, the mod team is only a small group of people, and we rely on the readers to help us maintain this subreddit. Please report any rule breakers. Thank you.&lt;/p&gt;\n\n&lt;p&gt;- Da Mods    &lt;/p&gt;\n&lt;/div&gt;&lt;!-- SC_ON --&gt;",
                    "This thread is for getting feedback on your own apps.\n\n####Developers:\n\n- must **provide feedback** for others\n- must include **Play Store**, **GitHub**, or **BitBucket** link\n- must make top level comment\n- must make effort to respond to questions and feedback from commenters\n- may be open or closed source\n\n####Commenters:\n\n- must give **constructive feedback** in replies to top level comments\n- must not include links to other apps\n\nTo cut down on spam, accounts who are too young or do not have enough karma to post will be removed. Please make an effort to contribute to the community before asking for feedback.\n\nAs always, the mod team is only a small group of people, and we rely on the readers to help us maintain this subreddit. Please report any rule breakers. Thank you.\n\n\\- Da Mods    \n",
                    null,
                    "App Feedback Thread - October 07, 2017",
                    1,
                    false,
                    "self", null, null,
                    0,
                    true, false,
                    "/r/androiddev/comments/74ulnb/app_feedback_thread_october_07_2017/",
                    false, false,
                    "https://www.reddit.com/r/androiddev/comments/74ulnb/app_feedback_thread_october_07_2017/",
                    "AutoModerator",
                    0,
                    true,
                    Distinguish.moderator)

            assertEquals(thread, it.data.children.first().data)
        }

    }

    @After
    fun tearDown() {
        mockServer.shutdown()
    }
}