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
import de.l3s.boilerpipe.extractors.CommonExtractors
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.frameLayout
import timber.log.Timber
import java.net.URL

internal class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val root = frameLayout { }
        extract()
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
