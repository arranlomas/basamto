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

package io.github.gumil.data.model

import com.squareup.moshi.Json
import io.github.gumil.data.model.base.Created
import io.github.gumil.data.model.base.Distinguish
import io.github.gumil.data.model.base.Votable

data class RedditThread(

        @Json(name = "id")
        val id: String?,

        @Json(name = "name")
        val name: String?,

        @Json(name = "ups")
        override val ups: Int,

        @Json(name = "downs")
        override val downs: Int,

        @Json(name = "likes")
        override val isLiked: Boolean?,

        @Json(name = "created")
        override val created: Long,

        @Json(name = "created_utc")
        override val createdUtc: Long,

        @Json(name = "domain")
        val domain: String,

        @Json(name = "subreddit")
        val subreddit: String,

        @Json(name = "subreddit_id")
        val subredditId: String,

        @Json(name = "selftext_html")
        val selfTextHtml: String?,

        @Json(name = "selftext")
        val selfText: String?,

        @Json(name = "link_flair_text")
        val flair: String?,

        @Json(name = "title")
        val title: String,

        @Json(name = "score")
        val score: Int = 0,

        @Json(name = "over_18")
        val nsfw: Boolean = false,

        @Json(name = "thumbnail")
        val thumbnail: String,

        @Json(name = "thumbnail_width")
        val thumbnailWidth: Int?,

        @Json(name = "thumbnail_height")
        val thumbnailHeight: Int?,

        @Json(name = "gilded")
        val gilded: Int,

        @Json(name = "stickied")
        val isSticky: Boolean,

        @Json(name = "spoiler")
        val isSpoiler: Boolean = false,

        @Json(name = "permalink")
        val permalink: String,

        @Json(name = "locked")
        val isLocked: Boolean = false,

        @Json(name = "hide_score")
        val hideScore: Boolean = false,

        @Json(name = "url")
        val url: String,

        @Json(name = "author")
        val author: String,

        @Json(name = "num_comments")
        val numComments: Int,

        @Json(name = "is_self")
        val isSelf: Boolean = false,

        @Json(name = "distinguished")
        val distinguished: Distinguish? = null

) : Votable, Created