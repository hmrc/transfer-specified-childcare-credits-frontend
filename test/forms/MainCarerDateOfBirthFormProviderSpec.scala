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

import java.time.{Clock, Instant, LocalDate, ZoneOffset}
import forms.behaviours.DateBehaviours
import models.Name
import play.api.data.FormError

class MainCarerDateOfBirthFormProviderSpec extends DateBehaviours {

  val clock = Clock.fixed(Instant.now, ZoneOffset.UTC)
  val max = LocalDate.now(clock)
  val mainCarerName = Name("Foo", "Bar")
  val form = new MainCarerDateOfBirthFormProvider(clock)(mainCarerName)

  ".value" - {

    val validData = datesBetween(
      min = LocalDate.of(2000, 1, 1),
      max = max
    )

    behave like dateField(form, "value", validData)
    behave like mandatoryDateField(form, "value", "mainCarerDateOfBirth.error.required.all", Seq(mainCarerName.firstName))
    behave like dateFieldWithMax(form, "value", max, FormError("value", "mainCarerDateOfBirth.error.max"))
  }
}
