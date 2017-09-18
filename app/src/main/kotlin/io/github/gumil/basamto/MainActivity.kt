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

package io.github.gumil.basamto

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.zhuinden.simplestack.HistoryBuilder
import com.zhuinden.simplestack.StateChange
import com.zhuinden.simplestack.navigator.DefaultStateChanger
import com.zhuinden.simplestack.navigator.Navigator
import com.zhuinden.simplestack.navigator.changehandlers.NoOpViewChangeHandler
import de.l3s.boilerpipe.extractors.CommonExtractors
import io.github.gumil.basamto.common.ViewKey
import io.github.gumil.basamto.main.MainKey
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.frameLayout
import timber.log.Timber
import java.net.URL

internal class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.plant(Timber.DebugTree())
        super.onCreate(savedInstanceState)
        val root = frameLayout { }
        extract()

        Navigator.configure().setStateChanger(DefaultStateChanger.configure()
                .setLayoutInflationStrategy { _, key, context, _, callback ->
                    (key as? ViewKey)?.let {
                        callback.layoutInflationComplete(it.view(context))
                    }
                }
                .setGetViewChangeHandlerStrategy { _, _, previousKey, newKey, _, _, direction ->
                    when (direction) {
                        StateChange.FORWARD -> (newKey as ViewKey).viewChangeHandler()
                        StateChange.BACKWARD -> (previousKey as ViewKey).viewChangeHandler()
                        else -> NoOpViewChangeHandler()
                    }
                }
                .create(this, root)
        ).install(this, root, HistoryBuilder.single(MainKey()))
    }

    override fun onBackPressed() {
        if (!Navigator.getBackstack(this)
                .top<ViewKey>()
                .onBackPressed()) {
            if (!Navigator.onBackPressed(this)) {
                super.onBackPressed()
            }
        }
    }

    fun extract() {
        doAsync {
            val url = URL("https://medium.com/@fabioCollini/android-testing-using-dagger-2-mockito-and-a-custom-junit-rule-c8487ed01b56")

            // choose from a set of useful BoilerpipeExtractors...
            val extractor = CommonExtractors.ARTICLE_EXTRACTOR
            // final BoilerpipeExtractor extractor = CommonExtractors.DEFAULT_EXTRACTOR;
            // final BoilerpipeExtractor extractor = CommonExtractors.CANOLA_EXTRACTOR;
            // final BoilerpipeExtractor extractor = CommonExtractors.LARGEST_CONTENT_EXTRACTOR;

            // choose the operation mode (i.e., highlighting or extraction)
            //val hh = HTMLHighlighter.newHighlightingInstance()
            // final HTMLHighlighter hh = HTMLHighlighter.newExtractingInstance();


//        val out = PrintWriter("/tmp/highlighted.html", "UTF-8")
//        out.println("<base href=\"$url\" >")
//        out.println("<meta http-equiv=\"Content-Type\" content=\"text-html; charset=utf-8\" />")
//        out.println(hh.process(url, extractor))
//        out.close()

            Timber.d("url ${extractor.getText(url)}")
        }
    }
}
