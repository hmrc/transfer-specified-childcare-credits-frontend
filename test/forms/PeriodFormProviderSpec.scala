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
import models.{ApplicantAndChild, Child, Name}
import play.api.data.FormError

import java.time.{Clock, Instant, LocalDate, ZoneOffset}

class PeriodFormProviderSpec extends DateBehaviours {

  val clock = Clock.fixed(Instant.now, ZoneOffset.UTC)

  val cutOffDate = LocalDate.of(2011, 4, 6)
  val childName = Name("Foo", "Bar")
  val childDob = LocalDate.now(clock).minusYears(13)
  val applicantName = Name("Bar", "Foo")

  val form = new PeriodFormProvider(clock)(ApplicantAndChild(applicantName, Child(childName, childDob)))

  val validData = datesBetween(
    min = cutOffDate,
    max = childDob.plusYears(12),
  )

  ".startDate" - {

    behave like dateField(form, "startDate", validData)
    behave like mandatoryDateField(form, "startDate", "period.startDate.error.required.all", Seq(applicantName.firstName, childName.firstName))
    behave like dateFieldWithMax(form, "startDate", LocalDate.now(clock), FormError("startDate", "period.startDate.error.max"))
    behave like dateFieldWithMax(form, "startDate", childDob.plusYears(12), LocalDate.now(clock), FormError("startDate", "period.startDate.error.childOver12", Seq(childName.firstName)))
    behave like dateFieldWithMin(form, "startDate", cutOffDate, FormError("startDate", "period.startDate.error.min"))
  }

  ".endDate" - {

    behave like dateField(form, "endDate", validData)
    behave like mandatoryDateField(form, "endDate", "period.endDate.error.required.all", Seq(applicantName.firstName, childName.firstName))
    behave like dateFieldWithMax(form, "endDate", LocalDate.now(clock), FormError("endDate", "period.endDate.error.max"))
    behave like dateFieldWithMin(form, "endDate", cutOffDate, FormError("endDate", "period.endDate.error.min"))
  }
}
