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

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverters
import com.squareup.moshi.Json
import io.github.gumil.data.persistence.Converters

@Entity
@TypeConverters(Converters::class)
data class Link(

        @PrimaryKey
        override val id: String,
        override val name: String,
        override val subreddit: String,
        override val author: String,
        override val created: Long,

        @Json(name = "created_utc")
        override val createdUtc: Long,

        val title: String,
        val url: String,
        val domain: String,
        val permalink: String,
        val selftext: String?,

        @Json(name = "selftext_html")
        val selfTextHtml: String?,

        @Json(name = "is_self")
        val isSelf: Boolean,

        @Json(name = "link_flair_text")
        val linkFlairText: String?,

        @Json(name = "num_comments")
        val commentsCount: Int,
        override val gilded: Int,
        override val score: Int,
        override val ups: Int,
        val stickied: Boolean,
        val thumbnail: String
) : Submission, Thing