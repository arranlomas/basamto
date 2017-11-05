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

import android.os.Bundle
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.android.support.DaggerFragment
import io.github.gumil.core.ui.base.RxLifecycle

abstract class BaseFragment: DaggerFragment() {

    abstract val layoutId: Int
    protected val rxLifecycle = RxLifecycle()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycle.addObserver(rxLifecycle)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(layoutId, container, false)

    fun showSnackbarError(@StringRes stringRes: Int) {
        view?.let {
            Snackbar.make(it, stringRes, Snackbar.LENGTH_SHORT)
        }
    }
}