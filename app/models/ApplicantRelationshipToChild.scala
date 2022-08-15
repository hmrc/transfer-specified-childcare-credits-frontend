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

sealed trait ApplicantRelationshipToChild

object ApplicantRelationshipToChild extends Enumerable.Implicits {

  case object Grandparent extends WithName("grandparent") with ApplicantRelationshipToChild
  case object NonResidentParent extends WithName("nonResidentParent") with ApplicantRelationshipToChild
  case object AuntOrUncle extends WithName("auntOrUncle") with ApplicantRelationshipToChild
  case object BrotherOrSister extends WithName("brotherOrSister") with ApplicantRelationshipToChild
  case object Other extends WithName("other") with ApplicantRelationshipToChild

  val values: Seq[ApplicantRelationshipToChild] = Seq(
    Grandparent, NonResidentParent, AuntOrUncle, BrotherOrSister, Other
  )

  def options(implicit messages: Messages): Seq[RadioItem] = Seq(
    RadioItem(
      content = Text(messages("applicantRelationshipToChild.grandparent")),
      hint = Some(Hint(
        content = Text(messages("applicantRelationshipToChild.grandparent.hint"))
      )),
      value = Some(Grandparent.toString),
      id = Some(s"value_$Grandparent")
    ),
    RadioItem(
      content = Text(messages("applicantRelationshipToChild.nonResidentParent")),
      hint = Some(Hint(
        content = Text(messages("applicantRelationshipToChild.nonResidentParent.hint"))
      )),
      value = Some(NonResidentParent.toString),
      id = Some(s"value_$NonResidentParent")
    ),
    RadioItem(
      content = Text(messages("applicantRelationshipToChild.auntOrUncle")),
      hint = Some(Hint(
        content = Text(messages("applicantRelationshipToChild.auntOrUncle.hint"))
      )),
      value = Some(AuntOrUncle.toString),
      id = Some(s"value_$AuntOrUncle")
    ),
    RadioItem(
      content = Text(messages("applicantRelationshipToChild.brotherOrSister")),
      hint = Some(Hint(
        content = Text(messages("applicantRelationshipToChild.brotherOrSister.hint"))
      )),
      value = Some(BrotherOrSister.toString),
      id = Some(s"value_$BrotherOrSister")
    ),
    RadioItem(divider = Some(messages("site.or"))),
    RadioItem(
      content = Text(messages("applicantRelationshipToChild.other")),
      value = Some(Other.toString),
      id = Some(s"value_$Other")
    )
  )

  implicit val enumerable: Enumerable[ApplicantRelationshipToChild] =
    Enumerable(values.map(v => v.toString -> v): _*)
}
