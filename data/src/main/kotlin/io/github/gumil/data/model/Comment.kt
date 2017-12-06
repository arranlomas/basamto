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

package io.github.gumil.data.model

import com.squareup.moshi.Json

data class Comment(
        override val id: String,

        override val name: String,

        override val subreddit: String,

        override val author: String,

        override val created: Long,

        @Json(name = "created_utc")
        override val createdUtc: Long,

        val body: String,

        @Json(name = "body_html")
        val bodyHtml: String,

        val controversiality: Int,

        val depth: Int,

        @Json(name = "link_id")
        val linkId: String,

        @Json(name = "parent_id")
        val parentId: String,

        val replies: Thing?,

        @Json(name = "subreddit_id")
        val subredditId: String,

        override val gilded: Int,
        override val score: Int,
        override val ups: Int
) : Submission