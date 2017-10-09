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

import okhttp3.mockwebserver.MockResponse
import java.io.ByteArrayOutputStream

internal fun Any.readFromFile(file: String): String {
    val inputStream = this.javaClass.classLoader.getResourceAsStream(file)
    val result = ByteArrayOutputStream()
    val buffer = ByteArray(1024)
    var length = inputStream.read(buffer)
    while (length != -1) {
        result.write(buffer, 0, length)
        length = inputStream.read(buffer)
    }
    return result.toString("UTF-8")
}

internal fun createMockResponse(body: String): MockResponse {
    return MockResponse().apply {
        setResponseCode(200)
        setBody(body)
        addHeader("Content-type: application/json")
    }
}