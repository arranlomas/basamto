///*
// * The MIT License (MIT)
// *
// * Copyright 2017 Miguel Panelo
// *
// * Permission is hereby granted, free of charge, to any person obtaining a copy
// * of this software and associated documentation files (the "Software"), to deal
// * in the Software without restriction, including without limitation the rights
// * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// * copies of the Software, and to permit persons to whom the Software is
// * furnished to do so, subject to the following conditions:
// *
// * The above copyright notice and this permission notice shall be included in all
// * copies or substantial portions of the Software.
// *
// * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// * SOFTWARE.
// */
//
//package io.github.gumil.data.model
//import com.squareup.moshi.Json
//
//
//data class Data(
//		@Json(name = "modhash") val modhash: String, //j0k1rrj67vc7a7389c06a85d87fd83ee34617d836b746f4c1d
//		@Json(name = "whitelist_status") val whitelistStatus: String, //all_ads
//		@Json(name = "children") val children: List<Children>,
//		@Json(name = "after") val after: Any, //null
//		@Json(name = "before") val before: Any //null
//)
//
//data class Children(
//		@Json(name = "kind") val kind: String, //t3
//		@Json(name = "data") val data: Data
//)
//
//data class Data(
//		@Json(name = "domain") val domain: String, //youtube.com
//		@Json(name = "approved_at_utc") val approvedAtUtc: Any, //null
//		@Json(name = "banned_by") val bannedBy: Any, //null
//		@Json(name = "media_embed") val mediaEmbed: MediaEmbed,
//		@Json(name = "thumbnail_width") val thumbnailWidth: Int, //140
//		@Json(name = "subreddit") val subreddit: String, //androiddev
//		@Json(name = "selftext_html") val selftextHtml: Any, //null
//		@Json(name = "selftext") val selftext: String,
//		@Json(name = "likes") val likes: Any, //null
//		@Json(name = "suggested_sort") val suggestedSort: Any, //null
//		@Json(name = "user_reports") val userReports: List<Any>,
//		@Json(name = "secure_media") val secureMedia: SecureMedia,
//		@Json(name = "is_reddit_media_domain") val isRedditMediaDomain: Boolean, //false
//		@Json(name = "link_flair_text") val linkFlairText: Any, //null
//		@Json(name = "id") val id: String, //7gwjyi
//		@Json(name = "banned_at_utc") val bannedAtUtc: Any, //null
//		@Json(name = "view_count") val viewCount: Any, //null
//		@Json(name = "archived") val archived: Boolean, //false
//		@Json(name = "clicked") val clicked: Boolean, //false
//		@Json(name = "report_reasons") val reportReasons: Any, //null
//		@Json(name = "title") val title: String, //Android - Building a layout from Scratch using ConstraintLayout - and Q&amp;A
//		@Json(name = "num_crossposts") val numCrossposts: Int, //0
//		@Json(name = "saved") val saved: Boolean, //false
//		@Json(name = "can_mod_post") val canModPost: Boolean, //false
//		@Json(name = "is_crosspostable") val isCrosspostable: Boolean, //true
//		@Json(name = "pinned") val pinned: Boolean, //false
//		@Json(name = "score") val score: Int, //68
//		@Json(name = "approved_by") val approvedBy: Any, //null
//		@Json(name = "over_18") val over18: Boolean, //false
//		@Json(name = "hidden") val hidden: Boolean, //false
//		@Json(name = "preview") val preview: Preview,
//		@Json(name = "num_comments") val numComments: Int, //1
//		@Json(name = "thumbnail") val thumbnail: String, //https://b.thumbs.redditmedia.com/p3MAgOq0BikhRD1nZZs5xZ1d6V4wESTmbcBybIdUwZw.jpg
//		@Json(name = "subreddit_id") val subredditId: String, //t5_2r26y
//		@Json(name = "hide_score") val hideScore: Boolean, //false
//		@Json(name = "edited") val edited: Boolean, //false
//		@Json(name = "link_flair_css_class") val linkFlairCssClass: Any, //null
//		@Json(name = "author_flair_css_class") val authorFlairCssClass: Any, //null
//		@Json(name = "contest_mode") val contestMode: Boolean, //false
//		@Json(name = "gilded") val gilded: Int, //0
//		@Json(name = "locked") val locked: Boolean, //false
//		@Json(name = "downs") val downs: Int, //0
//		@Json(name = "brand_safe") val brandSafe: Boolean, //true
//		@Json(name = "secure_media_embed") val secureMediaEmbed: SecureMediaEmbed,
//		@Json(name = "removal_reason") val removalReason: Any, //null
//		@Json(name = "post_hint") val postHint: String, //rich:video
//		@Json(name = "can_gild") val canGild: Boolean, //true
//		@Json(name = "thumbnail_height") val thumbnailHeight: Int, //105
//		@Json(name = "parent_whitelist_status") val parentWhitelistStatus: String, //all_ads
//		@Json(name = "name") val name: String, //t3_7gwjyi
//		@Json(name = "spoiler") val spoiler: Boolean, //false
//		@Json(name = "permalink") val permalink: String, ///r/androiddev/comments/7gwjyi/android_building_a_layout_from_scratch_using/
//		@Json(name = "num_reports") val numReports: Any, //null
//		@Json(name = "whitelist_status") val whitelistStatus: String, //all_ads
//		@Json(name = "stickied") val stickied: Boolean, //false
//		@Json(name = "created") val created: Double, //1512178364.0
//		@Json(name = "url") val url: String, //https://www.youtube.com/watch?v=h1LHzObflwo
//		@Json(name = "author_flair_text") val authorFlairText: Any, //null
//		@Json(name = "quarantine") val quarantine: Boolean, //false
//		@Json(name = "author") val author: String, //octarino
//		@Json(name = "created_utc") val createdUtc: Double, //1512149564.0
//		@Json(name = "subreddit_name_prefixed") val subredditNamePrefixed: String, //r/androiddev
//		@Json(name = "distinguished") val distinguished: Any, //null
//		@Json(name = "media") val media: Media,
//		@Json(name = "upvote_ratio") val upvoteRatio: Double, //0.93
//		@Json(name = "mod_reports") val modReports: List<Any>,
//		@Json(name = "is_self") val isSelf: Boolean, //false
//		@Json(name = "visited") val visited: Boolean, //false
//		@Json(name = "subreddit_type") val subredditType: String, //public
//		@Json(name = "is_video") val isVideo: Boolean, //false
//		@Json(name = "ups") val ups: Int //68
//)
//
//data class MediaEmbed(
//		@Json(name = "content") val content: String, //&lt;iframe width="459" height="344" src="https://www.youtube.com/embed/h1LHzObflwo?feature=oembed" frameborder="0" gesture="media" allow="encrypted-media" allowfullscreen&gt;&lt;/iframe&gt;
//		@Json(name = "width") val width: Int, //459
//		@Json(name = "scrolling") val scrolling: Boolean, //false
//		@Json(name = "height") val height: Int //344
//)
//
//data class Media(
//		@Json(name = "type") val type: String, //youtube.com
//		@Json(name = "oembed") val oembed: Oembed
//)
//
//data class Oembed(
//		@Json(name = "provider_url") val providerUrl: String, //https://www.youtube.com/
//		@Json(name = "version") val version: String, //1.0
//		@Json(name = "title") val title: String, //Android - Building a layout from Scratch using ConstraintLayout - and Q&amp;A #RiggarooLive
//		@Json(name = "thumbnail_width") val thumbnailWidth: Int, //480
//		@Json(name = "height") val height: Int, //344
//		@Json(name = "width") val width: Int, //459
//		@Json(name = "html") val html: String, //&lt;iframe width="459" height="344" src="https://www.youtube.com/embed/h1LHzObflwo?feature=oembed" frameborder="0" gesture="media" allow="encrypted-media" allowfullscreen&gt;&lt;/iframe&gt;
//		@Json(name = "author_name") val authorName: String, //Rebecca Franks
//		@Json(name = "thumbnail_height") val thumbnailHeight: Int, //360
//		@Json(name = "thumbnail_url") val thumbnailUrl: String, //https://i.ytimg.com/vi/h1LHzObflwo/hqdefault.jpg
//		@Json(name = "type") val type: String, //video
//		@Json(name = "provider_name") val providerName: String, //YouTube
//		@Json(name = "author_url") val authorUrl: String //https://www.youtube.com/user/TheRiggaroo
//)
//
//data class SecureMediaEmbed(
//		@Json(name = "content") val content: String, //&lt;iframe width="459" height="344" src="https://www.youtube.com/embed/h1LHzObflwo?feature=oembed" frameborder="0" gesture="media" allow="encrypted-media" allowfullscreen&gt;&lt;/iframe&gt;
//		@Json(name = "width") val width: Int, //459
//		@Json(name = "scrolling") val scrolling: Boolean, //false
//		@Json(name = "media_domain_url") val mediaDomainUrl: String, //https://www.redditmedia.com/mediaembed/7gwjyi
//		@Json(name = "height") val height: Int //344
//)
//
//data class SecureMedia(
//		@Json(name = "type") val type: String, //youtube.com
//		@Json(name = "oembed") val oembed: Oembed
//)
//
//data class Oembed(
//		@Json(name = "provider_url") val providerUrl: String, //https://www.youtube.com/
//		@Json(name = "version") val version: String, //1.0
//		@Json(name = "title") val title: String, //Android - Building a layout from Scratch using ConstraintLayout - and Q&amp;A #RiggarooLive
//		@Json(name = "thumbnail_width") val thumbnailWidth: Int, //480
//		@Json(name = "height") val height: Int, //344
//		@Json(name = "width") val width: Int, //459
//		@Json(name = "html") val html: String, //&lt;iframe width="459" height="344" src="https://www.youtube.com/embed/h1LHzObflwo?feature=oembed" frameborder="0" gesture="media" allow="encrypted-media" allowfullscreen&gt;&lt;/iframe&gt;
//		@Json(name = "author_name") val authorName: String, //Rebecca Franks
//		@Json(name = "thumbnail_height") val thumbnailHeight: Int, //360
//		@Json(name = "thumbnail_url") val thumbnailUrl: String, //https://i.ytimg.com/vi/h1LHzObflwo/hqdefault.jpg
//		@Json(name = "type") val type: String, //video
//		@Json(name = "provider_name") val providerName: String, //YouTube
//		@Json(name = "author_url") val authorUrl: String //https://www.youtube.com/user/TheRiggaroo
//)
//
//data class Preview(
//		@Json(name = "images") val images: List<Image>,
//		@Json(name = "enabled") val enabled: Boolean //false
//)
//
//data class Image(
//		@Json(name = "source") val source: Source,
//		@Json(name = "resolutions") val resolutions: List<Resolution>,
//		@Json(name = "variants") val variants: Variants,
//		@Json(name = "id") val id: String //eN5jxlfCXL4M8FSej7gP01L-fJXWAm-qxPxY9b96r08
//)
//
//data class Source(
//		@Json(name = "url") val url: String, //https://i.redditmedia.com/paOIDRxIj6-dZ5tIjzpBI7rsW-JqFowV54DsxVN_LN8.jpg?s=f2f9cf0103eac35f669a89f0c5a1c757
//		@Json(name = "width") val width: Int, //480
//		@Json(name = "height") val height: Int //360
//)
//
//data class Resolution(
//		@Json(name = "url") val url: String, //https://i.redditmedia.com/paOIDRxIj6-dZ5tIjzpBI7rsW-JqFowV54DsxVN_LN8.jpg?fit=crop&amp;crop=faces%2Centropy&amp;arh=2&amp;w=108&amp;s=5ac7713dabced4ce9c8ad476488e6af8
//		@Json(name = "width") val width: Int, //108
//		@Json(name = "height") val height: Int //81
//)
//
//data class Variants(
//)