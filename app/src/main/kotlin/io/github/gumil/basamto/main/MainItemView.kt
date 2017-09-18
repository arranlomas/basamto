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
import android.view.Gravity
import android.widget.TextView
import io.github.gumil.basamto.common.ViewLayout
import io.github.gumil.basamto.common.adapter.ListItemView
import org.jetbrains.anko.frameLayout
import org.jetbrains.anko.textView

internal class MainItemView(context: Context): ListItemView<String>(context) {

    override val viewLayout = Layout()

    override fun bind(item: String) {
        viewLayout.textView.text = item
    }

    inner class Layout : ViewLayout {
        lateinit var textView: TextView

        override fun createView(context: Context) = with(context) {
            frameLayout {
                textView = textView {
                    gravity = Gravity.CENTER
                }
            }
        }

    }
}