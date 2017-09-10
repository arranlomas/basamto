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

package io.github.gumil.basamto.common

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.view.View

internal abstract class ViewLayout {

    lateinit var view: View
    var toolbarTitle = ""

    open fun inflate(context: Context): View {
        view = createView(context)
        if (toolbarTitle.isNotBlank()) {
            setActionBarTitle(toolbarTitle)
        }
        return view
    }

    open fun setActionBarTitle(title: String) {
        val activity = view.context.findActivity()

        (activity as? AppCompatActivity)?.let {
            it.supportActionBar?.title = title
        }
    }

    protected abstract fun createView(context: Context): View
}