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

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import io.github.gumil.basamto.viewmodel.ViewModelFactory
import io.github.gumil.basamto.viewmodel.ViewModelKey
import javax.inject.Provider


@Module
internal abstract class MainActivityModule {

    @Module
    companion object {

        @JvmStatic
        @Provides
        @IntoMap
        @ViewModelKey(MainViewModel::class)
        fun provideMainViewModel(): ViewModel = MainViewModel()

        @Provides
        @JvmStatic
        fun provideViewModelFactory(
                creators: @JvmSuppressWildcards Map<Class<out ViewModel>, Provider<ViewModel>>
        ): ViewModelProvider.Factory = ViewModelFactory(creators)
    }
}