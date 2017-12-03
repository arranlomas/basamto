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

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import io.github.gumil.data.rest.model.RedditType.LISTING
import io.github.gumil.data.rest.model.RedditType.MORE
import io.github.gumil.data.rest.model.RedditType.T1
import io.github.gumil.data.rest.model.RedditType.T3
import java.io.IOException
import java.lang.ref.WeakReference
import java.lang.reflect.Type

internal class RedditObjectFactory : JsonAdapter.Factory {

    override fun create(type: Type, annotations: Set<Annotation>, moshi: Moshi): JsonAdapter<*>? {
        val clazz = Types.getRawType(type)
        if (RedditThing::class.java != clazz) {
            return null
        }

        return object : JsonAdapter<Any>() {

            @Suppress("UNCHECKED_CAST")
            @Throws(IOException::class)
            override fun fromJson(reader: JsonReader): Any? {
                val jsonValue = reader.readJsonValue()
                return jsonValue as? String ?: (jsonValue as? Map<String, Any>)?.let { map ->
                    map["kind"]?.let {
                        moshi.adapter(RedditType.valueOf((it as String).toUpperCase()).derivedClass)
                                ?.fromJsonValue(map["data"]) ?: throw JsonDataException()
                    }
                }
            }

            @Throws(IOException::class)
            override fun toJson(writer: JsonWriter, value: Any?) {
                when (value) {
                    is RedditComment -> {
                        writer.name("kind")
                        moshi.adapter(RedditType::class.java).toJson(writer, T1)
                        writer.name("data")
                        moshi.adapter(RedditComment::class.java).toJson(writer, value)
                    }
                    is RedditLink -> {
                        writer.name("kind")
                        moshi.adapter(RedditType::class.java).toJson(writer, T3)
                        writer.name("data")
                        moshi.adapter(RedditLink::class.java).toJson(writer, value)
                    }
                    is RedditListing -> {
                        writer.name("kind")
                        moshi.adapter(RedditType::class.java).toJson(writer, LISTING)
                        writer.name("data")
                        moshi.adapter(RedditListing::class.java).toJson(writer, value)
                    }
                    is RedditMore -> {
                        writer.name("kind")
                        moshi.adapter(RedditType::class.java).toJson(writer, MORE)
                        writer.name("data")
                        moshi.adapter(RedditMore::class.java).toJson(writer, value)
                    }
                }
            }
        }
    }

    companion object {
        private var instance: WeakReference<RedditObjectFactory>? = null

        fun getInstance(): RedditObjectFactory =
                instance?.get() ?: RedditObjectFactory().also { instance = WeakReference(it) }
    }
}
