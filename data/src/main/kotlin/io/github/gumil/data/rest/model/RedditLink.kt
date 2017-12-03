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

package io.github.gumil.data.rest.model

import com.google.auto.value.AutoValue
import com.squareup.moshi.Json
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi

@AutoValue
internal abstract class RedditLink : RedditSubmission() {

    abstract fun clicked(): Boolean

    abstract fun domain(): String?

    abstract fun hidden(): Boolean

    @Json(name = "is_self")
    abstract val isSelf: Boolean

    @Json(name = "link_flair_text")
    abstract fun linkFlairText(): String?

    @Json(name = "num_comments")
    abstract fun commentsCount(): Int

    abstract fun permalink(): String

    abstract fun selftext(): String?

    @Json(name = "selftext_html")
    abstract fun selftextHtml(): String?

    abstract fun stickied(): Boolean

    abstract fun thumbnail(): String

    abstract fun title(): String

    abstract fun url(): String

    abstract fun visited(): Boolean

    companion object {
        @JvmStatic
        fun jsonAdapter(moshi: Moshi): JsonAdapter<RedditLink> =
                AutoValue_RedditLink.MoshiJsonAdapter(moshi)
    }
}
