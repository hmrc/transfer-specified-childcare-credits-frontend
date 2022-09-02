/*
 * Copyright 2022 HM Revenue & Customs
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

package controllers

import base.SpecBase
import com.dmanchester.playfop.sapi.PlayFop
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import org.scalatestplus.mockito.MockitoSugar
import play.api.inject.bind
import play.api.test.FakeRequest
import play.api.test.Helpers._

import java.nio.charset.Charset

class PrintControllerSpec extends SpecBase with MockitoSugar {

  "Print Controller" - {

    "must return OK and the correct view for a GET" in {

      val mockFop = mock[PlayFop]
      when(mockFop.processTwirlXml(any(), any(), any(), any())) thenReturn "hello".getBytes

      val application = applicationBuilder(userAnswers = Some(emptyUserAnswers))
        .overrides(
          bind[PlayFop].toInstance(mockFop)
        )
        .build()

      running(application) {
        val request = FakeRequest(GET, routes.PrintController.onDownload.url)
        val result = route(application, request).value

        status(result) mustEqual OK
        contentAsBytes(result).decodeString(Charset.defaultCharset()) mustEqual "hello"
      }
    }
  }
}
