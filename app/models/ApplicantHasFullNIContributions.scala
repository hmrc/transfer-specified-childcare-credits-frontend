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

package models

import play.api.i18n.Messages
import uk.gov.hmrc.govukfrontend.views.Aliases.Text
import uk.gov.hmrc.govukfrontend.views.viewmodels.hint.Hint
import uk.gov.hmrc.govukfrontend.views.viewmodels.radios.RadioItem

sealed trait ApplicantHasFullNIContributions

object ApplicantHasFullNIContributions extends Enumerable.Implicits {

  case object Yes extends WithName("yes") with ApplicantHasFullNIContributions
  case object No extends WithName("no") with ApplicantHasFullNIContributions
  case object DontKnow extends WithName("dontKnow") with ApplicantHasFullNIContributions

  val values: Seq[ApplicantHasFullNIContributions] = Seq(
    Yes, No, DontKnow
  )

  def options(implicit messages: Messages): Seq[RadioItem] = Seq(
    RadioItem(
      content = Text(messages("applicantHasFullNIContributions.yes")),
      value   = Some(Yes.toString),
      id      = Some(s"value_$Yes")
    ),
    RadioItem(
      content = Text(messages("applicantHasFullNIContributions.no")),
      value   = Some(No.toString),
      id      = Some(s"value_$No")
    ),
    RadioItem(
      content = Text(messages("applicantHasFullNIContributions.dontKnow")),
      value   = Some(DontKnow.toString),
      id      = Some(s"value_$DontKnow"),
      hint    = Some(Hint(content = Text(messages("applicantHasFullNIContributions.dontKnow.hint"))))
    )
  )

  implicit val enumerable: Enumerable[ApplicantHasFullNIContributions] =
    Enumerable(values.map(v => v.toString -> v): _*)
}
