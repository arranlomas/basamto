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

import io.github.gumil.data.util.just

internal sealed class TestState : MviState {
    object State1 : TestState()
    object State2 : TestState()
    object State3 : TestState()
}

internal sealed class TestIntent : MviIntent {
    object Intent1 : TestIntent()
    object Intent2 : TestIntent()
    object Intent3 : TestIntent()
}

internal sealed class TestResult : MviResult {
    object Result1 : TestResult()
    object Result2 : TestResult()
    object Result3 : TestResult()
}

internal fun createTestStateMachine() = MviStateMachine<TestState, TestIntent, TestResult>(
        TestState.State1,
        {
            when (it) {
                TestIntent.Intent1 -> TestResult.Result1
                TestIntent.Intent2 -> TestResult.Result2
                TestIntent.Intent3 -> TestResult.Result3
            }.just()
        },
        { _, result ->
            when (result) {
                TestResult.Result1 -> TestState.State1
                TestResult.Result2 -> TestState.State2
                TestResult.Result3 -> TestState.State3
            }
        }
)