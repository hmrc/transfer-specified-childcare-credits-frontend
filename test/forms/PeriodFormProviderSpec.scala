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
import play.api.data.FormError

import java.time.{Clock, Instant, LocalDate, ZoneOffset}

class PeriodFormProviderSpec extends DateBehaviours {

  val clock = Clock.fixed(Instant.now, ZoneOffset.UTC)
  val form = new PeriodFormProvider(clock)()

  val validData = datesBetween(
    min = LocalDate.of(2000, 1, 1),
    max = LocalDate.now(ZoneOffset.UTC)
  )

  ".startDate" - {

    behave like dateField(form, "startDate", validData)
    behave like mandatoryDateField(form, "startDate", "period.startDate.error.required.all")
    behave like dateFieldWithMax(form, "startDate", LocalDate.now(clock), FormError("startDate", "period.startDate.error.max"))
  }

  ".endDate" - {

    behave like dateField(form, "endDate", validData)
    behave like mandatoryDateField(form, "endDate", "period.endDate.error.required.all")
    behave like dateFieldWithMax(form, "endDate", LocalDate.now(clock), FormError("endDate", "period.endDate.error.max"))
  }
}
