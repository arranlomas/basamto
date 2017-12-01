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
import io.github.gumil.data.model.base.Created
import io.github.gumil.data.model.base.Distinguish
import io.github.gumil.data.model.base.Votable
import io.github.gumil.data.persistence.Converters

@Entity
@TypeConverters(Converters::class)
data class Submission(

        @PrimaryKey
        val id: String,

        val name: String,

        override val ups: Int,

        override val downs: Int,

        @Json(name = "likes")
        override val isLiked: Boolean?,

        override val created: Long,

        @Json(name = "created_utc")
        override val createdUtc: Long,

        val domain: String,

        val subreddit: String,

        @Json(name = "subreddit_id")
        val subredditId: String,

        @Json(name = "selftext_html")
        val selfTextHtml: String?,

        @Json(name = "selftext")
        val selfText: String?,

        @Json(name = "link_flair_text")
        val flair: String?,

        val title: String,

        val score: Int = 0,

        @Json(name = "over_18")
        val nsfw: Boolean = false,

        val thumbnail: String,

        @Json(name = "thumbnail_width")
        val thumbnailWidth: Int?,

        @Json(name = "thumbnail_height")
        val thumbnailHeight: Int?,

        val gilded: Int,

        @Json(name = "stickied")
        val isSticky: Boolean,

        @Json(name = "spoiler")
        val isSpoiler: Boolean = false,

        val permalink: String,

        @Json(name = "locked")
        val isLocked: Boolean = false,

        @Json(name = "hide_score")
        val hideScore: Boolean = false,

        val url: String,

        val author: String,

        @Json(name = "num_comments")
        val numComments: Int,

        @Json(name = "is_self")
        val isSelf: Boolean = false,

        val distinguished: Distinguish? = null

) : Votable, Created