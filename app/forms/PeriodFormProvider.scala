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

import forms.mappings.Mappings
import models.Period
import play.api.data.Form
import play.api.data.Forms._

import java.time.{Clock, LocalDate}
import javax.inject.Inject

class PeriodFormProvider @Inject()(clock: Clock) extends Mappings {

  private def max: LocalDate = LocalDate.now(clock)

  def apply(): Form[Period] = Form(
    mapping(
      "startDate" -> localDate(
        invalidKey = "period.startDate.error.invalid",
        allRequiredKey = "period.startDate.error.required.all",
        twoRequiredKey = "period.startDate.error.required.two",
        requiredKey = "period.startDate.error.required"
      ).verifying(maxDate(max, "period.startDate.error.max")),
      "endDate" -> localDate(
        invalidKey = "period.endDate.error.invalid",
        allRequiredKey = "period.endDate.error.required.all",
        twoRequiredKey = "period.endDate.error.required.two",
        requiredKey = "period.endDate.error.required"
      ).verifying(maxDate(max, "period.endDate.error.max"))
    )(Period.apply)(Period.unapply)
  )
}
