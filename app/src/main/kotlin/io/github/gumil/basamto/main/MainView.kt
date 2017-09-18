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

package io.github.gumil.basamto.main

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import io.github.gumil.basamto.common.BaseView
import io.github.gumil.basamto.common.ViewLayout
import org.jetbrains.anko.ctx
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.verticalLayout

internal class MainView(context: Context) : BaseView(context) {

    override val viewLayout: ViewLayout = Layout()

    private inner class Layout : ViewLayout {
        override fun createView(context: Context) = with(context) {
            verticalLayout {
                recyclerView {
                    setHasFixedSize(true)
                    layoutManager = LinearLayoutManager(ctx)
                    adapter = MainListAdapter().apply {
                        list = listOf(
                                "hello",
                                "world",
                                "list",
                                "first app",
                                "bookmarking",
                                "anti social"
                        )
                    }
                }.lparams(matchParent, matchParent)
            }
        }
    }
}