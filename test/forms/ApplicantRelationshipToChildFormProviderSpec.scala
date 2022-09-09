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

package forms

import forms.behaviours.StringFieldBehaviours
import models.{ApplicantAndChildNames, Name}
import org.scalacheck.Shrink
import play.api.data.FormError

class ApplicantRelationshipToChildFormProviderSpec extends StringFieldBehaviours {

  implicit val noShrink: Shrink[Any] = Shrink.shrinkAny

  val applicantName = Name("Foo", "Bar")
  val childName = Name("Bar", "Foo")
  val names = ApplicantAndChildNames(applicantName, childName)
  val form = new ApplicantRelationshipToChildFormProvider()(names)

  ".value" - {

    val fieldName = "value"
    val requiredKey = "applicantRelationshipToChild.error.required"
    val lengthKey = "applicantRelationshipToChild.error.length"
    val maxLength = 150

    behave like fieldThatBindsValidData(
      form,
      fieldName,
      stringsWithMaxLength(maxLength)
    )

    behave like fieldWithMaxLength(
      form,
      fieldName,
      maxLength = maxLength,
      lengthError = FormError(fieldName, lengthKey, Seq(applicantName.firstName, childName.firstName, maxLength))
    )

    behave like mandatoryField(
      form,
      fieldName,
      requiredError = FormError(fieldName, requiredKey, Seq(applicantName.firstName, childName.firstName))
    )
  }
}
