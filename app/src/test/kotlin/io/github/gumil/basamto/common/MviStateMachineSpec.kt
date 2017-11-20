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

package io.github.gumil.basamto.common

import android.arch.lifecycle.Observer
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import io.github.gumil.data.util.just
import org.amshove.kluent.mock
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on

internal object MviStateMachineSpec : Spek({
    addRule(InstantTaskRule())
    given("a TestStateMachine") {

        val stateMachine = createTestStateMachine()
        val observer = mock<Observer<TestState>>()

        stateMachine.state.observeForever(observer)
        //stateMachine.processIntents(intentObservable)

        it("should emit initial state") {
            verify(observer).onChanged(TestState.State1)
        }

        on("Intent1") {
            stateMachine.processIntents(TestIntent.Intent1.just())

            it("should emit state1") {
                // we have 2 emissions here since the emission from initial state is considered
                verify(observer, times(2)).onChanged(TestState.State1)
            }
        }

        on("Intent2") {
            stateMachine.processIntents(TestIntent.Intent2.just())

            it("should emit state2") {
                verify(observer).onChanged(TestState.State2)
            }
        }

        on("Intent3") {
            stateMachine.processIntents(TestIntent.Intent3.just())

            it("should emit state3") {
                verify(observer).onChanged(TestState.State3)
            }
        }
    }
})