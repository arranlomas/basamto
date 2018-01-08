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

package io.github.gumil.basamto.navigation

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import com.zhuinden.simplestack.StateChange
import com.zhuinden.simplestack.StateChanger
import io.github.gumil.basamto.R
import io.github.gumil.basamto.extensions.getColorRes

internal class FragmentStateChanger(
        private val fragmentManager: FragmentManager,
        private val containerId: Int
) : StateChanger {


    override fun handleStateChange(stateChange: StateChange, completionCallback: StateChanger.Callback) {
        val fragmentTransaction = fragmentManager.beginTransaction().disallowAddToBackStack()

        when (stateChange.direction) {
            StateChange.FORWARD -> fragmentTransaction.setCustomAnimations(
                    R.anim.slide_in_from_right,
                    R.anim.slide_out_to_left,
                    R.anim.slide_in_from_right,
                    R.anim.slide_out_to_left
            )
            StateChange.BACKWARD -> fragmentTransaction.setCustomAnimations(
                    R.anim.slide_in_from_left,
                    R.anim.slide_out_to_right,
                    R.anim.slide_in_from_left,
                    R.anim.slide_out_to_right
            )
        }

        val previousState = stateChange.getPreviousState<BaseKey>()
        val newState = stateChange.getNewState<BaseKey>()

        previousState.forEach { key ->
            fragmentManager.findFragmentByTag(key.getFragmentTag())?.also {
                if (!newState.contains(key)) {
                    fragmentTransaction.remove(it)
                } else if (!it.isDetached) {
                    fragmentTransaction.detach(it)
                }
            }
        }

        newState.forEach { key ->
            var fragment: Fragment? = fragmentManager.findFragmentByTag(key.getFragmentTag())
            fragment?.view?.setBackgroundColor(fragment.context.getColorRes(R.color.white))
            if (key == stateChange.topNewState()) {
                fragment?.also {
                    if (it.isDetached) {
                        fragmentTransaction.attach(fragment)
                    }
                } ?: also {
                    fragment = key.newFragment()
                    fragmentTransaction.add(containerId, fragment, key.getFragmentTag())
                }
            } else {
                fragment?.also {
                    if (it.isDetached) {
                        fragmentTransaction.attach(fragment)
                    }
                }
            }
        }

        fragmentTransaction.commitNow()
        completionCallback.stateChangeComplete()
    }
}