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

package io.github.gumil.data.persistence.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import io.github.gumil.data.model.Link
import io.reactivex.Single

@Dao
internal interface LinkDao {

    @Query("SELECT * FROM Link" +
            " WHERE subreddit = :subreddit" +
            " ORDER BY createdUtc DESC"+
            " LIMIT :limit" +
            " OFFSET :offset")
    fun getFrom(subreddit: String, offset: Int = 0, limit: Int = 10): Single<List<Link>>

    @Query("SELECT * FROM Link" +
            " ORDER BY createdUtc DESC"+
            " LIMIT :limit" +
            " OFFSET :offset")
    fun getAll(offset: Int = 0, limit: Int = 10): Single<List<Link>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg link: Link)
}