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

package views

import models.{ApplicantAndChildNames, Name}
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers
import play.api.i18n.{DefaultMessagesApi, Messages}
import play.api.test.FakeRequest

class ViewUtilsSpec extends AnyFreeSpec with Matchers {

  "messagesWithFallback" - {

    val testMessages = Map(
      "default" -> Map(
        "foo"          -> "Test {0}, {1}, {2}",
        "foo.fallback" -> "Test {0}"
      )
    )
    val messagesApi = new DefaultMessagesApi(testMessages)
    implicit val messages: Messages = messagesApi.preferred(FakeRequest("GET", "/"))

    "must return the regular message when names don't match" in {
      val applicantName = Name("Foo", "Quux")
      val childName = Name("Bar", "Quux")
      val names = ApplicantAndChildNames(applicantName, childName)
      ViewUtils.messageWithNameFallback("foo", names, "Baz") mustEqual "Test Foo, Bar, Baz"
    }

    "must return the fallback message when the names match" in {
      val childName = Name("Foo", "Quux")
      val applicantName = Name("Foo", "Quux")
      val names = ApplicantAndChildNames(applicantName, childName)
      ViewUtils.messageWithNameFallback("foo", names, "Baz") mustEqual "Test Baz"
    }
  }
}
