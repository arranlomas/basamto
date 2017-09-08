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

import android.content.Intent
import android.os.Parcelable
import android.support.annotation.MenuRes
import android.view.MenuItem
import com.zhuinden.simplestack.navigator.ViewChangeHandler

internal abstract class ViewKey {

    val layout by lazy {
        layout()
    }

    abstract protected fun layout(): ViewLayout

    abstract fun viewChangeHandler(): ViewChangeHandler

    /**
     * Lifecycle: When animation is started. By this time view is already attached.
     */
    open fun onChangeStarted() {}

    /**
     * Lifecycle: When animation is ended
     */
    open fun onChangeEnded() {}

    /**
     * Lifecycle: When view is removed
     */
    open fun onViewRemoved() {}

    open fun onBackPressed() = false

    /**
     * Lifecycle: Called from host activity, handling for activityResults
     */
    open fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {}

    /**
     * Lifecycle: Called when creating options menu
     * @return Menu Resource
     */
    @MenuRes
    open fun onCreateOptionsMenu(): Int = -1

    open fun onOptionsItemSelected(item: MenuItem) = false

    /**
     * @return flag to know if the Previous View should be removed from the container
     * Sometimes we need to make the previous view visible like we do with dialogs
     */
    open fun shouldPreviousViewBePersisted() = false

    /**
     * Lifecycle: Called when view was destroyed and readded immediately
     * Only called when shouldPreviousViewBePersisted() is set to true
     */
    open fun restoreState() {}
}