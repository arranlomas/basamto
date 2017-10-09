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
import io.github.gumil.data.model.base.Listing

data class Subreddit(

        @Json(name = "before")
        override val before: String?,

        @Json(name = "after")
        override val after: String?,

        @Json(name = "modhash")
        override val modHash: String?,

        @Json(name = "children")
        override val children: List<ThreadResponse>,

        @Json(name = "whitelist_status")
        val whiteListStatus: String = "all_ads"

) : Listing<ThreadResponse>