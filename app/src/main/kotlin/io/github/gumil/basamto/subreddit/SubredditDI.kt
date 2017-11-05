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

package io.github.gumil.basamto.subreddit

import android.arch.lifecycle.ViewModel
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import io.github.gumil.basamto.viewmodel.ViewModelKey

@Module
internal abstract class SubredditBuilder {

    @ContributesAndroidInjector(
            modules = arrayOf(
                    SubredditModule::class
            )
    )
    internal abstract fun subredditFragment(): SubredditFragment

}

@Module
internal abstract class SubredditModule {

    @Module
    companion object {

        @JvmStatic
        @Provides
        @IntoMap
        @ViewModelKey(SubredditViewModel::class)
        fun provideSubredditViewModel(): ViewModel = SubredditViewModel()
    }
}