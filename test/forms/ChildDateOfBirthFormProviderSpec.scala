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

import forms.behaviours.DateBehaviours
import models.Name
import play.api.data.FormError

import java.time.format.DateTimeFormatter
import java.time.{Clock, Instant, LocalDate, ZoneId}

class ChildDateOfBirthFormProviderSpec extends DateBehaviours {

  ".value" - {

    val childName = Name("Foo", "Bar")
    val clock = Clock.fixed(Instant.now, ZoneId.systemDefault)
    val form = new ChildDateOfBirthFormProvider(clock)(childName)
    val dateFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy")
    val minDate = LocalDate.of(1999, 4, 6)
    val maxDate = LocalDate.now(clock)

    val validData = datesBetween(
      min = minDate,
      max = maxDate
    )

    behave like dateField(form, "value", validData)

    behave like mandatoryDateField(form, "value", "childDateOfBirth.error.required.all", Seq(childName.firstName))

    behave like dateFieldWithMax(
      form,
      "value",
      maxDate,
      FormError("value", "childDateOfBirth.error.max", Seq(maxDate.format(dateFormatter)))
    )

    behave like dateFieldWithMin(
      form,
      "value",
      minDate,
      FormError("value", "childDateOfBirth.error.min", Seq(minDate.format(dateFormatter)))
    )
  }
}
