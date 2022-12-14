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

import java.time.{Clock, LocalDate}
import forms.mappings.Mappings
import models.Name

import javax.inject.Inject
import play.api.data.Form

import java.time.format.DateTimeFormatter

class ChildDateOfBirthFormProvider @Inject()(clock: Clock) extends Mappings {

  private def min: LocalDate = LocalDate.of(1999, 4, 6)
  private def max: LocalDate = LocalDate.now(clock)
  private val dateFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy")

  def apply(childName: Name): Form[LocalDate] =
    Form(
      "value" -> localDate(
        invalidKey     = "childDateOfBirth.error.invalid",
        allRequiredKey = "childDateOfBirth.error.required.all",
        twoRequiredKey = "childDateOfBirth.error.required.two",
        requiredKey    = "childDateOfBirth.error.required",
        args           = Seq(childName.firstName)
      )
        .verifying(maxDate(max, "childDateOfBirth.error.max", max.format(dateFormatter)))
        .verifying(minDate(min, "childDateOfBirth.error.min", min.format(dateFormatter)))
    )
}
