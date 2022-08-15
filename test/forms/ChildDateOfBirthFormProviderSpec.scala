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

import java.time.{Clock, Instant, LocalDate, ZoneId, ZoneOffset}
import forms.behaviours.DateBehaviours
import play.api.data.FormError

import java.time.format.DateTimeFormatter

class ChildDateOfBirthFormProviderSpec extends DateBehaviours {

  ".value" - {

    val clock = Clock.fixed(Instant.now, ZoneId.systemDefault)
    val form = new ChildDateOfBirthFormProvider(clock)()
    val dateFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy")
    val maxDate = LocalDate.now(clock)

    val validData = datesBetween(
      min = LocalDate.of(2000, 1, 1),
      max = LocalDate.now(ZoneOffset.UTC)
    )

    behave like dateField(form, "value", validData)

    behave like mandatoryDateField(form, "value", "childDateOfBirth.error.required.all")

    behave like dateFieldWithMax(
      form,
      "value",
      maxDate,
      FormError("value", "childDateOfBirth.error.max", Array(maxDate.format(dateFormatter)))
    )
  }
}
