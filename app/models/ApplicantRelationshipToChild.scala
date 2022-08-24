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
import play.api.libs.json.{JsString, Json, Reads, Writes, __}
import uk.gov.hmrc.govukfrontend.views.Aliases.Text
import uk.gov.hmrc.govukfrontend.views.viewmodels.hint.Hint
import uk.gov.hmrc.govukfrontend.views.viewmodels.radios.RadioItem

sealed trait ApplicantRelationshipToChild

object ApplicantRelationshipToChild extends Enumerable.Implicits {

  case object Grandparent extends WithName("grandparent") with ApplicantRelationshipToChild
  case object AuntOrUncle extends WithName("auntOrUncle") with ApplicantRelationshipToChild
  case object BrotherOrSister extends WithName("brotherOrSister") with ApplicantRelationshipToChild
  case object GreatAuntOrGreatUncle extends WithName("greatAuntOrGreatUncle") with ApplicantRelationshipToChild
  case object NonResidentParent extends WithName("nonResidentParent") with ApplicantRelationshipToChild
  final case class Other(value: String) extends ApplicantRelationshipToChild

  implicit lazy val reads: Reads[ApplicantRelationshipToChild] =
    (__ \ "type").read[String].flatMap {
      case "grandparent"           => Reads.pure(Grandparent)
      case "auntOrUncle"           => Reads.pure(AuntOrUncle)
      case "brotherOrSister"       => Reads.pure(BrotherOrSister)
      case "greatAuntOrGreatUncle" => Reads.pure(GreatAuntOrGreatUncle)
      case "nonResidentParent"     => Reads.pure(NonResidentParent)
      case "other"                 => (__ \ "value").read[String].map(Other)
      case _                       => Reads.failed("error.invalid")
    }

  implicit lazy val writes: Writes[ApplicantRelationshipToChild] =
    Writes {
      case Grandparent           => Json.obj("type" -> "grandparent")
      case AuntOrUncle           => Json.obj("type" -> "auntOrUncle")
      case BrotherOrSister       => Json.obj("type" -> "brotherOrSister")
      case GreatAuntOrGreatUncle => Json.obj("type" -> "greatAuntOrGreatUncle")
      case NonResidentParent     => Json.obj("type" -> "nonResidentParent")
      case Other(value)          => Json.obj("type" -> "other", "value" -> value)
    }
}
