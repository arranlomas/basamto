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

package io.github.gumil.basamto.main

import com.zhuinden.simplestack.Backstack
import com.zhuinden.simplestack.navigator.Navigator
import dagger.Module
import dagger.Provides
import dagger.android.AndroidInjectionModule
import dagger.android.ContributesAndroidInjector
import io.github.gumil.basamto.reddit.submission.SubmissionBuilder
import io.github.gumil.basamto.reddit.subreddit.SubredditBuilder
import io.github.gumil.basamto.viewmodel.ViewModelBuilder

@Module(includes = arrayOf(AndroidInjectionModule::class))
internal abstract class ActivityBuilder {

    @ActivityScope
    @ContributesAndroidInjector(
            modules = arrayOf(
                    ViewModelBuilder::class,
                    SubredditBuilder::class,
                    SubmissionBuilder::class,
                    ActivityModule::class
            )
    )
    internal abstract fun bindMainActivity(): MainActivity

}

@Module
internal class ActivityModule {

    @Provides
    fun provideBackstack(activity: MainActivity): Backstack = Navigator.getBackstack(activity)
}