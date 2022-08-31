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

class MainCarerDateOfBirthFormProvider @Inject()(clock: Clock) extends Mappings {

  def apply(mainCarerName: Name): Form[LocalDate] =
    Form(
      "value" -> localDate(
        invalidKey     = "mainCarerDateOfBirth.error.invalid",
        allRequiredKey = "mainCarerDateOfBirth.error.required.all",
        twoRequiredKey = "mainCarerDateOfBirth.error.required.two",
        requiredKey    = "mainCarerDateOfBirth.error.required",
        args           = Seq(mainCarerName.firstName)
      ).verifying(maxDate(LocalDate.now(clock), "mainCarerDateOfBirth.error.max"))
    )
}
