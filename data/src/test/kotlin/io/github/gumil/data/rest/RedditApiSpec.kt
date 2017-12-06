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

package io.github.gumil.data.rest

import io.github.gumil.data.ApiFactory
import io.github.gumil.data.createMockResponse
import io.github.gumil.data.model.Comment
import io.github.gumil.data.model.Link
import io.github.gumil.data.model.Listing
import io.github.gumil.data.model.RedditResponse
import io.github.gumil.data.readFromFile
import io.reactivex.observers.TestObserver
import okhttp3.mockwebserver.MockWebServer
import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldEqualTo
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on

internal object RedditApiSpec : Spek({
    val mockServer by memoized { MockWebServer() }
    val url by memoized { mockServer.url("/").toString() }
    val redditApi by memoized { ApiFactory.createRedditApi(true, url) }

    context("subreddit") {
        on("getSubreddit") {
            val subscriber by memoized { TestObserver<RedditResponse>() }
            mockServer.enqueue(createMockResponse(readFromFile("subreddit.json")))
            redditApi.getSubreddit("androiddev").subscribe(subscriber)

            it("should return a list of Link") {
                subscriber.assertNoErrors()
                subscriber.assertNoTimeout()
                subscriber.assertComplete()
                subscriber.events.size shouldEqualTo 3

                (subscriber.events[0][0] as? RedditResponse)?.let {
                    it.data.children.size shouldEqualTo 10
                    (it.data as? Listing)?.let {
                        (it.children.last() as? Link)?.let {
                            it.id shouldEqual "74poqy"
                        }

                        it.children.first() shouldEqual TestObjects.link()
                    }
                }
            }
        }

        on("getComment") {
            val subscriber by memoized { TestObserver<List<RedditResponse>>() }
            mockServer.enqueue(createMockResponse(readFromFile("comment.json")))
            redditApi.getComments("androiddev", "74ulnb").subscribe(subscriber)

            it("should return a the Link and list of Comment") {
                subscriber.assertNoErrors()
                subscriber.assertNoTimeout()
                subscriber.assertComplete()
                subscriber.events.size shouldEqualTo 3
                subscriber.events[0][0] shouldBeInstanceOf List::class

                (subscriber.events[0][0] as? List<*>)?.let {
                    (it[0] as? RedditResponse)?.let {
                        it.data.children.first() shouldBeInstanceOf Link::class
                    }

                    (it[1] as? RedditResponse)?.let {
                        it.data.children.first() shouldBeInstanceOf Comment::class
                    }
                }
            }
        }
    }

    afterEachTest {
        mockServer.shutdown()
    }
})