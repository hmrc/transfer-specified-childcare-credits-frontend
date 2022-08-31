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
import models.Name
import org.scalacheck.Gen
import play.api.data.FormError

class MainCarerTelephoneNumberFormProviderSpec extends StringFieldBehaviours {

  val requiredKey = "mainCarerTelephoneNumber.error.required"
  val invalidKey = "mainCarerTelephoneNumber.error.invalid"

  val mainCarerName = Name("Foo", "Bar")
  val form = new MainCarerTelephoneNumberFormProvider()(mainCarerName)

  ".value" - {

    val fieldName = "value"

    behave like fieldThatBindsValidData(
      form,
      fieldName,
      Gen.oneOf("0777 777 7777", "+447777777777", "07777777777  ")
    )

    behave like mandatoryField(
      form,
      fieldName,
      requiredError = FormError(fieldName, requiredKey, Seq(mainCarerName.firstName))
    )

    "fail to bind an invalid phone number" in {
      form.bind(Map(fieldName -> "invalid")).error("value").value.message mustEqual invalidKey
    }
  }
}
