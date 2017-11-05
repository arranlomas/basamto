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

package io.github.gumil.basamto.dagger

import dagger.Module
import dagger.android.AndroidInjectionModule
import dagger.android.ContributesAndroidInjector
import io.github.gumil.basamto.main.MainActivity
import io.github.gumil.basamto.subreddit.SubredditBuilder

@Module(includes = arrayOf(AndroidInjectionModule::class))
internal abstract class ActivityBuilder {

    @ActivityScope
    @ContributesAndroidInjector(
            modules = arrayOf(
                    ViewModelBuilder::class,
                    SubredditBuilder::class
            )
    )

    internal abstract fun bindMainActivity(): MainActivity

}