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

package queries

import models.{ApplicantAndChildNames, Name, UserAnswers}
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers
import org.scalatest.{OptionValues, TryValues}
import pages.{ApplicantNamePage, ChildNamePage}

class ApplicantAndChildNamesQuerySpec extends AnyFreeSpec with Matchers with TryValues with OptionValues {

  "must read out the applicant and child names from user answers" in {

    val childName = Name("Foo", "Bar")
    val applicantName = Name("Bar", "Foo")

    val userAnswers = UserAnswers("id")
      .set(ChildNamePage, childName).success.value
      .set(ApplicantNamePage, applicantName).success.value

    userAnswers.get(ApplicantAndChildNamesQuery).value mustEqual ApplicantAndChildNames(applicantName = applicantName, childName = childName)
  }
}
